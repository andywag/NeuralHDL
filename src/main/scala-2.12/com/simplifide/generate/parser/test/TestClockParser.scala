package com.simplifide.generate.parser.test

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.test.TestClockParser.ClockWrapper

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 6/2/12
 * Time: 7:21 AM
 * To change this template use File | Settings | File Templates.
 */

trait TestClockParser {

  self:NewTestParser =>

  implicit def ClockControl2Wrapper(clk:ClockControl) = new ClockWrapper(clk)
}

object TestClockParser {

  class ClockWrapper(clk:ClockControl) {
    def generate(period:Int) = {

    }
  }
  
}
