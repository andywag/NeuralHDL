package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/2/12
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */

class BinaryShifter(val signal:SignalTrait, val input:SignalTrait)(implicit clk:ClockControl) extends ComplexSegment {

  signal(0)                     := $flop(clk) $reset(0) $enable(input)
  signal((signal.width-1) ~> 1) := $flop(clk) $reset(0) $enable(signal((signal.width-2) ~> 0))

  override def createBody {}
}
