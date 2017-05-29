package com.simplifide.generate.newparser.typ

import com.simplifide.generate
import com.simplifide.generate.{blocks, newparser}
import com.simplifide.generate.blocks.basic
import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct
import com.simplifide.generate.newparser.typ
import com.simplifide.generate.newparser.typ.Assignable.{AssignableInterface, AssignableSingle}
import com.simplifide.generate.newparser.typ.ConditionType.Open
import com.simplifide.generate.newparser.typ.Expressable.ExpressableInterface
import com.simplifide.generate.newparser.typ.NumberType.NumberLike.{NumberLikeFloatSignal, NumberLikeSignal}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.sv.SignalInterface
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

  implicit def seqToAssignable(input:Expression) = input match {
    case _              => new AssignableSingle(input)
  }
  implicit def interfaceToAssignable(input:SignalInterface) = new AssignableInterface(input)
  implicit def interfaceToExpressable(input:SignalInterface) = new ExpressableInterface(input)


  implicit def seqToAssignable(input:Seq[Expression]) = new com.simplifide.generate.newparser.typ.Assignable.AssignableSeq(input)

  implicit def flopToOutputAssignable[T](input:OutputAssignable.AtFlop[T]) =
    new OutputAssignable.AtFlopAssignable(input)

  implicit def conditionToOutputAssignable[T](input:ConditionType.Close[T]) =
    new OutputAssignable.ConditionAssignable[T](input)

  implicit def outputAssignableToExpressable[T](input:OutputAssignable[T]) =
    new com.simplifide.generate.newparser.typ.Expressable.OutAssExpressable[T](input)





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
