package com.simplifide.generate.blocks.basic.typ

import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop, SimpleFlopSegment}
import com.simplifide.generate.blocks.basic.typ.ConditionType.Close
import com.simplifide.generate.generator
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.{SegmentHolder, block}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.condition.Question
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.items.RegisterAtParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.operator.BinaryParserOperator
import com.simplifide.generate.signal.{RegisterTrait, SignalTrait}

/**
  * Created by andy on 5/10/17.
  */

/*
object AssignType {

  val And: (Expression, Expression) => Expression = (x, y) => new BinaryParserOperator.And(x, y)
  val ColonColon: (Expression, Expression) => Expression = (x, y) => Question.Item(x, y)
  val Quest: (Expression, Expression) => Expression = (x, y) => Question.Open(x, y)
  val AssignReg: (Expression, Expression) => Expression = (x, y) => new block.ParserStatement.AlwaysReg(x, y)
  val At: (Expression, ClockControl) => Expression = (x, y) => new RegisterAtParser.Flop(x, y)

  trait Assignable[T] {
    val value: T

    def :=[S](input: Assignable[S])(implicit scope: SegmentHolder): SimpleSegment

    def ::=[S](input: Assignable[S]): SimpleSegment

    def :::=(input: Assignable[T]): Assignable[T] = op(input, AssignReg)

    def &(input: Assignable[T]): Assignable[T] = op(input, And)

    def ::(input: Assignable[T]): Assignable[T] = op(input, ColonColon)

    def ?(input: Assignable[T]): Assignable[T] = op(input, Quest)

    //def at(clk: ClockControl) = new OutputAssignable.AtFlop[T](this, clk)


    def op(input: Assignable[T], f: (Expression, Expression) => Expression): Assignable[T]


    def expression: Expression

    def create(implicit creationFactory: CreationFactory): SimpleSegment

    //def createStatement(output:Assignable[T],input:Assignable[T]):Assignable[T]


  }

  object Assignable {
    implicit val creator = CreationFactory.Hardware
    type R = Seq[Expression]


    class AssignableOutputAssignable[T](override val value: OutputAssignable[T]) extends Assignable[OutputAssignable[T]] {

      override def :=[S](input: Assignable[S])(implicit scope: SegmentHolder) = ???

      override def ::=[S](input: Assignable[S])  = ???

      override def op(input: Assignable[OutputAssignable[T]], f: (Expression, Expression) => Expression): Assignable[OutputAssignable[T]] = ???

      override def expression: Expression = ???

      override def create(implicit creationFactory: CreationFactory): SimpleSegment = ???

    }

    class AssignableSeqExpression(override val value: Seq[Expression]) extends Assignable[Seq[Expression]] {

      def createStatement(output: Assignable[Seq[Expression]], input: Assignable[Seq[Expression]]): Assignable[Seq[Expression]] = {
        def parseStatement(in: Seq[Expression]) = (output.value zip in).map(x => ParserStatement(x._1, x._2))

        val mat = input.value(0)
        val result = mat match {
          case _ => parseStatement(input.value)
        }
        new AssignableSeqExpression(result)
      }


      def :=[S](input: Assignable[S])(implicit scope: SegmentHolder) = {
        def createStatement(input:Assignable[S]) = {
          val result = input match {
            case x:AssignableSeqExpression => (value zip x.value).map(x => ParserStatement(x._1, x._2))
            //case _                         => (value zip  value).map(x => ParserStatement(x._1, x._2))
          }

          BasicSegments.List(result.toList.map(_.create))
          //ParserStatement(this.create,input.create)
        }
        val result = input.value match {
          case x:OutputAssignable[_] => x.createStatement(this)
          case _                     => createStatement(input)
        }
        val out = result.create
        scope.assign(out)
        out
        //val states = createStatement(this, input)
        //scope.assign(states.value.toList)
        //states
      }

      def ::=[S](input: Assignable[S]) = {
        input match {
          case x:AssignableSeqExpression => {
            val states = (this.value zip x.value).map(x => ParserStatement(x._1, x._2))
            BasicSegments.List(states.toList.map(_.create))
          }
          case _ => BasicSegments.List()
        }

      }


      override def op(input: Assignable[Seq[Expression]], f: (Expression, Expression) => Expression): Assignable[Seq[Expression]] = {
        val result = (value zip input.value).map(x => f(x._1, x._2))
        new AssignableSeqExpression(result)
      }


      override def expression: Expression = if (value.length == 1) value(0) else BasicSegments.ListExpression(value.toList)

      override def create(implicit creationFactory: CreationFactory): SimpleSegment =
        if (value.length == 1) value(0).create else {
          val result = value.map(_.create).toList
          BasicSegments.List(result)
        }

    }

  }

}


  //implicit def expressionToSeq(input:Expression) = new AssignableSeqExpression(Seq(input))
  //implicit def registerToSeq(input:RegisterTrait[_]) = new AssignableSeqExpression(input.children)
*/

