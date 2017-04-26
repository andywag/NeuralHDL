package com.simplifide.generate.blocks.test

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.generator.{BasicSegments, SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.operator.UnaryOperator
import com.simplifide.generate.signal.Constant
import com.simplifide.generate.parser.factory.CreationFactory

/**
 *  Method used to generate a clock for the test bench
 */



trait ClockGenerator extends SimpleSegment.Combo {

  val clk:ClockControl

  override def create(implicit creator:CreationFactory) = {
    BasicSegments.List(new ClockGenerator.Clock(clk))
  }



}

object ClockGenerator  {

  def apply(clk:ClockControl) = new Impl(clk)
  class Impl(val clk:ClockControl) extends ClockGenerator

  /*
  class Reset(val clk:ClockControl) extends SimpleSegment.Combo {
    override def create = {
      clk.reset match {
        case None => BasicSegments.List()
        case Some(x) => {
          if (x.activeLow) new Statement.Delay(x,Constant(1,0),10*clk.period)
          else             new Statement.Delay(x,Constant(1,1),10*clk.period)
        }
      }
    }
  }
  */

  class Clock(val clk:ClockControl) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      SegmentReturn("always # " + clk.period + " ") + clk.clock.name + " <= ~" + clk.clock.name + ";\n\n"
  }
  
}