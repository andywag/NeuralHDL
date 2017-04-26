package com.simplifide.generate.parser.test

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.SimpleSegment

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 6/2/12
 * Time: 7:09 AM
 * To change this template use File | Settings | File Templates.
 */

/*
trait TestSignalParser {

  implicit def signalToWrapper(signal:SignalTrait) = new TestSignalParser.SignalWrapper(signal)
  
  def $at(time:SimpleSegment)           = new TestSignalParser.TimeAssignment(time,null)
  
}

object TestSignalParser {
  
  class SignalWrapper(val signal:SignalTrait) {
    def $at(segment:SimpleSegment)
    def $times(transitions:OperationBuilder)

  }
  
  class OperationBuilder {

  }
  
  class Initial(val segment:SimpleSegment)
  class TimeAssignment(val time:SimpleSegment, val value:SimpleSegment) {
    def $then(value:SimpleSegment)
  }
  
}
*/
