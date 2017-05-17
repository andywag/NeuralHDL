package com.simplifide.generate.blocks.basic.typ

import com.simplifide.generate.blocks
import com.simplifide.generate.blocks.basic
import com.simplifide.generate.blocks.basic.typ
import com.simplifide.generate.blocks.basic.typ.ConditionType.Open
import com.simplifide.generate.blocks.basic.typ.NumberType.NumberLike.{NumberLikeFloatSignal, NumberLikeSignal}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{FloatSignal, RegisterTrait, SignalTrait}

/**
  * Created by andy on 5/11/17.
  */
object SegmentParser {
  // Expression Conversions
  //implicit def expressionToSeq(input:Expression) = new Assignable.AssignableSeqExpression(Seq(input))
  //implicit def expressionToSeq(input:Seq[Expression]) = new Assignable.AssignableSeqExpression(input)

  implicit def seqToExpressable(input:Expression) = new typ.Expressable.SeqExpressable(Seq(input))
  implicit def seqToExpressable(input:Seq[Expression]) = new typ.Expressable.SeqExpressable(input)
  implicit def seqToAssignable(input:Expression) = new basic.typ.Assignable.AssignableSeq(Seq(input))
  implicit def seqToAssignable(input:Seq[Expression]) = new basic.typ.Assignable.AssignableSeq(input)

  implicit def flopToOutputAssignable[T](input:OutputAssignable.AtFlop[T]) =
    new OutputAssignable.AtFlopAssignable(input)

  implicit def conditionToOutputAssignable[T](input:ConditionType.Close[T]) =
    new OutputAssignable.ConditionAssignable[T](input)

  implicit def outputAssignableToExpressable[T](input:OutputAssignable[T]) =
    new blocks.basic.typ.Expressable.OutAssExpressable[T](input)





  // Numeric Conversions
  implicit def SignalToNumberLike(signal:SignalTrait) = {
    signal match {
      case x:FloatSignal => new NumberLikeFloatSignal(x)
      case _             => new NumberLikeSignal(signal)
    }
  }
  // Conditional Conversions
  def $if[T] (input:Expressable[T]) = Open(List(),Some(input))

  // Assignment Conversinons

  // Condition Expressions

}
