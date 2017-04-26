package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.blocks.basic.operator.{UnaryOperator, BinaryOperator, Select}
import com.simplifide.generate.blocks.basic.condition.QuestionSegment
import com.simplifide.generate.generator._
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{NewConstant, SignalTrait, FixedType, Constant}

/**
 * Class which defines a clip operation
 *
 * @constructor
 * @parameter input Input signal for the clipping operation
 * @parameter fixed Internal fixed point value for the clipping operation
 */

trait ClipSegment extends ComplexSingleSegment {
  /** Input Segment to Clip */
  val input:SignalTrait

  def createSegment = {
    val diff = input.fixed.integer - fixed.integer 
    if (diff == 0) {
      input
    }
    else {
      val negative:List[Expression] = List(input.sign)  ::: List.tabulate(diff)(x => ~input(input.width-x-2))
      val positive:List[Expression] = List(~input.sign) ::: List.tabulate(diff)(x => input(input.width-x-2))
      negative.reduceLeft(_&_) ? NewConstant.min(fixed) :: positive.reduceLeft(_&_) ? NewConstant.max(fixed) :: input(fixed)
    }
  }

}

object ClipSegment {
  /** Factory constructor to create clip segment */
  def apply(input:SignalTrait,fixed:FixedType) = new Impl(input,fixed)
  class Impl(override val input:SignalTrait, override val fixed:FixedType) extends ClipSegment
}