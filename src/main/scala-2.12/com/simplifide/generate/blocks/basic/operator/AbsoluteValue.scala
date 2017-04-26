package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.condition.QuestionSegment
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Absolute Value ParserStatement. Converts a twos complement number to an absolute value
 **/
class AbsoluteValue(val signal:SignalTrait) extends SimpleSegment.Combo {

  val mux = QuestionSegment(signal.sign,UnaryOperator.Negative(signal),signal)

  override def create(implicit creator:CreationFactory) = mux.create
  /*
  override def split:List[Expression] = {
    mux.split
  }
  */
}