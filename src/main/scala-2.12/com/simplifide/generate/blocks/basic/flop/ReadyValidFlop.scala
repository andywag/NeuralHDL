package com.simplifide.generate.blocks.basic.flop

import com.simplifide.generate.generator.{SimpleSegment, ComplexSegment}
import com.simplifide.generate.signal.bus.{ReadyValidBase, ReadyValid, CommonBuses}
import com.simplifide.generate.signal.{SignalCreator, SignalTrait}
import com.simplifide.generate.parser.model.Expression

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/28/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */

class ReadyValidFlop[T <: SignalCreator](val input:ReadyValidBase[T],
  val output:ReadyValidBase[T],
  val enable:SimpleSegment)(implicit clk:ClockControl) extends ComplexSegment {

  val en = ((output.vRdy & output.vVld) | (input.vVld & ~output.vVld))
  
  output.vData := input.vData $at clk.createEnable( (input.vRdy & input.vVld) & enable)
  //output.vVld  := input.vVld $at clk.createEnable(input.vRdy & input.vVld & enable)
  output.vVld  := $iff ((output.vRdy | input.vRdy)) $then (enable & input.vVld) $at clk
  input.vRdy   := $iff (1) $then (~output.vVld & enable)

  def createBody =  {}
  
}

object ReadyValidFlop {
  /*
  class Valid[T <: SignalCreator](val input:ReadyValidBase[T],
    val output:ReadyValidBase[T],
    val enable:SimpleSegment,
    val validValue:Expression,
    val readyValue:Expression)(implicit clk:ClockControl) extends ComplexSegment {

    val en = ((output.vRdy & output.vVld) | (input.vVld & ~output.vVld))

    output.vData := input.vData $at clk.createEnable( (input.vRdy & input.vVld) & enable)
    //output.vVld  := input.vVld $at clk.createEnable(input.vRdy & input.vVld & enable)
    output.vVld  := validValue $at clk
    input.vRdy   := $iff (1) $then rdyValue.create

    def createBody =  {}

  }
  */

  
}
