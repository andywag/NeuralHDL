package com.simplifide.generate.blocks.test

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 6/2/12
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */

trait NewClockGenerator extends ComplexSegment {

  val clk:ClockControl
  
  val count = signal(clk.clock.signal.name,REG)

  $initial {
    clk.clockSignal()  ::= 0
    if (clk.resetSignal() != None)   clk.resetSignal().get ::= 0
    if (clk.enableSignal() != None)  clk.enableSignal().get ::= 1
    count ::= 0
  }
  
  count := count + 1 $at  clk

  def createBody() {}

}

object NewClockGenerator {

  def apply(clk:ClockControl) = new Impl(clk)

  class Impl(val clk:ClockControl) extends NewClockGenerator {

  }
}
