package com.simplifide.generate.parser.test

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.test.TimeValueParser.{OperationBuilder, IntWrapper}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 6/3/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */

trait TimeValueParser {

  implicit def Int2Wrapper(int:Int) = new IntWrapper(int)

  def $time_value(index:SimpleSegment)(operations:OperationBuilder*) = new TimeValueParser.TimeValue(index,operations.toList)
  def $time(time:SimpleSegment) = new OperationBuilder(time,null)
  
}

object TimeValueParser {
  class IntWrapper(int:Int)
  
  class TimeValue(index:SimpleSegment,builder:List[OperationBuilder]) extends Expression {
    /** Create the simple segment */
    def create(implicit creator:CreationFactory):SimpleSegment = null
    /** Create Expression as a function of the output */
    def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = null
  }
  
  class OperationBuilder(val time:SimpleSegment, val value:SimpleSegment) {
    def $value(value:SimpleSegment) = new OperationBuilder(this.time, this.value)
  }
  
}
