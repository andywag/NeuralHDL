package com.simplifide.generate.newparser.typ

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.{SegmentHolder, block}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.condition.Question
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.items.RegisterAtParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.operator.BinaryParserOperator
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * Created by andy on 5/12/17.
  */
trait Expressable[T] {

  val And: (Expression, Expression) => Expression = (x, y) => new BinaryParserOperator.And(x, y)
  val ColonColon: (Expression, Expression) => Expression = (x, y) => Question.Item(x, y)
  val Quest: (Expression, Expression) => Expression = (x, y) => Question.Open(x, y)
  val AssignReg: (Expression, Expression) => Expression = (x, y) => new block.ParserStatement.AlwaysReg(x, y)
  val At: (Expression, ClockControl) => Expression = (x, y) => new RegisterAtParser.Flop(x, y)


  val value:T
  def expression: Expression
  def &(input: Expressable[T]): Expressable[T] = op(input, And)
  def ::(input: Expressable[T]): Expressable[T] = op(input, ColonColon)
  def ?(input: Expressable[T]): Expressable[T] = op(input, Quest)
  def at(clk: ClockControl) = new OutputAssignable.AtFlop[T](this, clk)
  //def at(clk: ClockControl) = new OutputAssignable.AtFlop[T](this, clk)
  def op(input: Expressable[T], f: (Expression, Expression) => Expression): Expressable[T]
}

object Expressable {
  implicit val creator = CreationFactory.Hardware


  case class SeqExpressable(override val value: Seq[Expression]) extends Expressable[Seq[Expression]] {
    override def op(input: Expressable[Seq[Expression]], f: (Expression, Expression) => Expression) = {
      val result = (value zip input.value).map(x => f(x._1, x._2))
      new SeqExpressable(result)
    }
    def expression = if (value.length == 1) value(0) else BasicSegments.ListExpression(value.toList)
  }


  class OutAssExpressable[T](override val value:OutputAssignable[T]) extends Expressable[OutputAssignable[T]] {
    override def expression: Expression = ???
    override def op(input: Expressable[OutputAssignable[T]], f: (Expression, Expression) => Expression): Expressable[OutputAssignable[T]] = ???
  }

  case class ExpressableInterface(override val value: SignalInterface) extends Expressable[SignalInterface] {
    override def op(input: Expressable[SignalInterface], f: (Expression, Expression) => Expression) = {
      ???
    }
    def expression = BasicSegments.ListExpression(value.signals)
  }

}
