package com.simplifide.generate.parser.operator

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.operator.{UnaryOperator, BinaryOperator}
import com.simplifide.generate.parser.factory.CreationFactory


trait UnaryParserOperator extends Expression {
  /** First Input for the operatoration */
  val in1:Expression

 
  
  private def createTotal(realIn1:SimpleSegment) = {
    this match {
      case x:UnaryParserOperator.Positive      => new UnaryOperator.Positive(realIn1)
      case x:UnaryParserOperator.Negative      => new UnaryOperator.Negative(realIn1)
      case x:UnaryParserOperator.NotLogical    => new UnaryOperator.NotLogical(realIn1)
      case x:UnaryParserOperator.Not           => new UnaryOperator.Not(realIn1)
      case x:UnaryParserOperator.And           => new UnaryOperator.And(realIn1)
      case x:UnaryParserOperator.NotAnd        => new UnaryOperator.NotAnd(realIn1)
      case x:UnaryParserOperator.Or            => new UnaryOperator.Or(realIn1)
      case x:UnaryParserOperator.Bang          => new UnaryOperator.Bang(realIn1)
      case x:UnaryParserOperator.Tilda         => new UnaryOperator.Tilda(realIn1)
    } 
  }
  
  override def create(implicit creator:CreationFactory):SimpleSegment = createTotal(in1.create)
  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = createTotal(in1.createOutput(output))


  
}

object UnaryParserOperator {
  
  class Base(override val in1:Expression) extends UnaryParserOperator
  
  class Positive(in1:Expression) extends Base(in1)
  class Negative(in1:Expression) extends Base(in1)
  class NotLogical(in1:Expression) extends Base(in1)
  class Not(in1:Expression) extends Base(in1)
  class And(in1:Expression) extends Base(in1)
  class NotAnd(in1:Expression) extends Base(in1)
  class Or(in1:Expression) extends Base(in1)
  class Bang(in1:Expression) extends Base(in1)
  class Tilda(in1:Expression) extends Base(in1)
  
}
