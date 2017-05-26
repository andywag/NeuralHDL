package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/26/17.
  */
case class NeuronControl(override val name:String,
                          dimension:NeuronControl.Dimension

                         )(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}

  signal(clk.allSignals(INPUT))
  val start      = signal("start",INPUT)

  val dataLength  = signal("dataLength",INPUT,U(dimension.dataAddWidth,0))
  val stateLength = signal("stateLength",INPUT,U(dimension.stateAddWidth,0))
  val biasLength  = signal("biasLength",INPUT,U(dimension.biasAddWidth,0))

  val dataAddress  = signal("dataAddress",OUTPUT,U(dimension.dataAddWidth,0))
  val tapAddress   = signal("tapAddress",OUTPUT,U(dimension.stateAddWidth,0))
  val biasAddress  = signal("biasAddress",OUTPUT,U(dimension.biasAddWidth,0))

  val dataDone      = signal("data_finish",  WIRE, U(1,0))
  val stateDone     = signal("state_finish", WIRE, U(1,0))
  val biasStart     = signal("bias_start",   WIRE, U(1,0))
  val biasEnable    = signal("bias_enable",  WIRE, U(1,0))

  val stateAddress = signal("stateAddress",WIRE,stateLength.fixed)

  dataDone     := (dataAddress === dataLength)
  stateDone    := (stateAddress === stateLength)
  biasStart    := dataDone & (stateAddress === 0)
  biasEnable   := (dataAddress <= biasLength) & (dataAddress > 0)


  /- ("Internal Counter for which state the operation is in")
  stateAddress := $iff (start) $then 0 $else_if (dataDone) $then stateAddress + 1
  /- ("Data Address")
  dataAddress  := $iff (start | dataDone) $then 0 $else  dataAddress + 1 $at clk
  /- ("Tap Address")
  tapAddress   := $iff (start) $then 0 $else tapAddress + 1 $at clk
  /- ("Bias Address")
  biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk

}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int)
}