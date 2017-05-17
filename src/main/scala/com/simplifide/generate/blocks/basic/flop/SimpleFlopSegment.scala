package com.simplifide.generate.blocks.basic.flop

import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{NewConstant, SignalTrait, Constant}
import com.simplifide.generate.parser.factory.CreationFactory


/*
 * Flop Segment
 */

class SimpleFlopSegment(val clk:ClockControl,
                        val internal:SimpleSegment) extends SimpleSegment.Combo {

  val flop = {
    val resets = SignalTrait.uniqueSignals(internal.outputs).map(x => new Statement.Reg(x,NewConstant(0,x.fixed)))
    new SimpleFlop(None,clk,BasicSegments.List(resets),internal)
  }

  //override def split = flop.split
  override def create(implicit creator:CreationFactory) = flop.create

}
