package com.simplifide.generate.newparser.typ

import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlopSegment}
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.model.Expression

/**
  * Created by andy on 5/12/17.
  */
  trait OutputAssignable[T] extends Expressable[T]{
    def createStatement[S](out:Assignable[S]):SimpleSegment
    override def expression: Expression = ???
    override def op(input: Expressable[T], f: (Expression, Expression) => Expression): Expressable[T] = ???

    //override def create(implicit creator: CreationFactory): SimpleSegment = ???
    //override def createOutput(output: SimpleSegment)(implicit creator: CreationFactory): SimpleSegment = ???
  }
  object OutputAssignable {
    implicit val creator = CreationFactory.Hardware

    case class AtFlopAssignable[T](override val value:AtFlop[T]) extends OutputAssignable[AtFlop[T]] {
      override def createStatement[S](out: Assignable[S]): SimpleSegment = {
        val s = (out !::= value.input)
        new SimpleFlopSegment(value.clk,s)
      }
    }
    case class ConditionAssignable[T](override val value:ConditionType.Close[T]) extends OutputAssignable[ConditionType.Close[T]] {
      override def createStatement[S](out: Assignable[S]): SimpleSegment = {
        value.createStatement(out)
      }
    }
    //def parseStatement(in:Seq[Expression]) = (output.value zip in).map(x => ParserStatement(x._1, x._2))
    case class AtFlop[T](input:Expressable[T],clk:ClockControl)



  }

