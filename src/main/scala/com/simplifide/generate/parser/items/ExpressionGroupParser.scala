package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.generator.{BasicSegments, SegmentMap, SegmentGroup, SimpleSegment}
import com.simplifide.generate.blocks.basic.flop.{SimpleFlopSegment, ClockControl}
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Group of Expressions
 */

trait ExpressionGroupParser {

  implicit val creator:CreationFactory

  def G(expressions:Expression*) = new SegmentGroup(expressions.map(_.asInstanceOf[SimpleSegment]).toList)
  def H(expressions:(SimpleSegment, SimpleSegment)*) = new SegmentMap(expressions.toMap)

  def $always_clk(clk:ClockControl)(expression:Expression)(implicit scope:SegmentHolder) = {
    val flop = BasicSegments.List (new SimpleFlopSegment(clk,expression.create))
    scope.assign(flop)
  }

  def $always_body(expression:Expression)(implicit scope:SegmentHolder) = {
    scope.assign(Always.Star(expression.create))
  }
}

object ExpressionGroupParser {



  
}

