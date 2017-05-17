package com.simplifide.generate.blocks.communication

import com.simplifide.generate.generator.{SimpleSegment, ComplexSegment}
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.signal.Constant._
import com.simplifide.generate.signal.{Constant, FixedType, SignalTrait}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.statemachine2.parser.StateMachineParser
import com.simplifide.generate.blocks.statemachine2.StateMachine
import com.simplifide.generate.signal.bus.ReadyValid

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 3/12/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */

class CrcReadyValid(
  val dataIn:ReadyValid[SignalTrait],
  val dataOut:ReadyValid[SignalTrait],
  val signalLength:SignalTrait,
  val fail:SignalTrait,
  val poly:List[Int],
  val length:Int)(implicit clk:ClockControl) extends ComplexSegment {

  val state = new CrcReadyValid.CrcState(this)

  val vCrcInput  = signal("crc_result_input")
  val vCompare   = signal("compare")
  val vCompareR  = signal("compareR",REG)
  val vDone      = signal("done")

  val vResult    = signal("data_result",WIRE,U(length,0))
  val vResultR   = signal("data_resultR",REG,U(length,0))

  val vValid    = signal("crc_enable") // Enable signal for this block


  def createBody {
    this.assign(state)

    /- ("CRC Enable Generation")
    vValid   := (state.current == state.DATA) ? dataIn.vVld & dataOut.vRdy :: dataIn.vVld
    /- ("CRC Reset Generation")
    vCrcInput := (state.current == state.MATCH) ? 0 :: dataIn.data ^ vResultR(this.length-1)

    vResultR := vResult $at(clk.createEnable(vValid))
    for (x <- 0 until length) {
      if (x == 0)                vResult(x) := vCrcInput
      else if (poly.contains(x)) vResult(x) := vResultR(x-1) ^ vCrcInput
      else                       vResult(x) := vResultR(x-1)
    }

    vCompare     := (vResultR(length-1) != dataIn.data) & (state.current === state.MATCH)
    vCompareR    := (vCompareR | vCompare) $at (clk)
    vDone        := (state.current == state.DONE)
    fail         := vCompareR $at clk.createEnable(vDone)
    /- ("Output Interface Latching")
    dataOut.vVld := (dataIn.vVld & dataOut.vRdy) $at clk
    dataOut.data :=  dataIn.data                 $at clk.createEnable(dataIn.vVld & dataOut.vRdy)
    dataIn.vRdy  :=  dataOut.vRdy                $at clk

  }



}

object CrcReadyValid {

  class CrcState(crc:CrcReadyValid)(implicit clk:ClockControl) extends ComplexSegment with StateMachineParser {
    val current  = signal("current",REG,unsigned(3,0))
    val count    = signal("counter",REG,unsigned(13,0))

    val IDLE   = state("IDLE",0x0)
    val DATA   = state("DATA",0x1)
    val MATCH  = state("MATCH",0x2)
    val DONE   = state("DONE",0x4)

    transition (
      IDLE  to DATA  when (crc.vValid),
      DATA  to MATCH when (count == crc.signalLength - crc.length-1),
      MATCH to DONE  when (count == (crc.signalLength-2)) ,
      DONE  to IDLE  when (1)
    )

    def createBody = {
      /- ("StateMachine Machine Definition")
      this.assign(StateMachine(this.finalStates,current))
      /- ("Counter Control")
      count := current $match (
        $cases(DATA)  $then count + 1
          $cases(MATCH) $then count + 1
          $cases(DONE)  $then 0
        ) $at (clk)
    }

  }







}