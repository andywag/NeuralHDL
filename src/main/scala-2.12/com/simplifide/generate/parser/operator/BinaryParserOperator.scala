package com.simplifide.generate.parser.operator

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.parser.factory.CreationFactory


trait BinaryParserOperator extends Expression {
  /** First Input for the operatoration */
  val in1:Expression
  /** Second Input for the operation */
  val in2:Expression
 
  
  private def createTotal(realIn1:SimpleSegment, realIn2:SimpleSegment) = {
    this match {
      case x:BinaryParserOperator.And          => new BinaryOperator.And(realIn1,realIn2)
      case x:BinaryParserOperator.Or           => new BinaryOperator.Or(realIn1,realIn2)
      case x:BinaryParserOperator.Nand         => new BinaryOperator.Nand(realIn1,realIn2)
      case x:BinaryParserOperator.Nor          => new BinaryOperator.Nor(realIn1,realIn2)
      case x:BinaryParserOperator.Xor          => new BinaryOperator.Xor(realIn1,realIn2)
      case x:BinaryParserOperator.Nxor         => new BinaryOperator.Nxor(realIn1,realIn2)
      case x:BinaryParserOperator.Sl           => new BinaryOperator.Sl(realIn1,realIn2)
      case x:BinaryParserOperator.Sr           => new BinaryOperator.Sr(realIn1,realIn2)
      case x:BinaryParserOperator.LogicalAnd   => new BinaryOperator.LogicalAnd(realIn1,realIn2)
      case x:BinaryParserOperator.GT           => new BinaryOperator.GT(realIn1,realIn2)
      case x:BinaryParserOperator.GTE          => new BinaryOperator.GTE(realIn1,realIn2)
      case x:BinaryParserOperator.LT           => new BinaryOperator.LT(realIn1,realIn2)
      case x:BinaryParserOperator.LTE          => new BinaryOperator.LTE(realIn1,realIn2)
      case x:BinaryParserOperator.EQ           => new BinaryOperator.EQ(realIn1,realIn2)
      case x:BinaryParserOperator.NEQ          => new BinaryOperator.NEQ(realIn1,realIn2)
      case x:BinaryParserOperator.EQ3          => new BinaryOperator.EQ3(realIn1,realIn2)
      case x:BinaryParserOperator.NEQ3         => new BinaryOperator.NEQ3(realIn1,realIn2)
      case x:BinaryParserOperator.NOT          => new BinaryOperator.NOT(realIn1,realIn2)
      case x:BinaryParserOperator.Plus         => new BinaryOperator.Plus(realIn1,realIn2)
      case x:BinaryParserOperator.Minus        => new BinaryOperator.Minus(realIn1,realIn2)
      case x:BinaryParserOperator.Multiply     => new BinaryOperator.Multiply(realIn1,realIn2)
    } 
  }
  
  override def create(implicit creator:CreationFactory):SimpleSegment =
    createTotal(in1.create, in2.create)
  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
    createTotal(in1.createOutput(output), in2.createOutput(output))

  
}

object BinaryParserOperator {
  
  class Base(override val in1:Expression, override val in2:Expression) extends BinaryParserOperator
  
  class And(in1:Expression,in2:Expression) extends Base(in1,in2) 
  class Or(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Nand(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Nor(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Xor(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Nxor(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Sl(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Sr(in1:Expression,in2:Expression) extends Base(in1,in2)
  class LogicalAnd(in1:Expression,in2:Expression) extends Base(in1,in2)
  class GT(in1:Expression,in2:Expression) extends Base(in1,in2)
  class GTE(in1:Expression,in2:Expression) extends Base(in1,in2)
  class LT(in1:Expression,in2:Expression) extends Base(in1,in2)
  class LTE(in1:Expression,in2:Expression) extends Base(in1,in2)
  class EQ(in1:Expression,in2:Expression) extends Base(in1,in2)
  class NEQ(in1:Expression,in2:Expression) extends Base(in1,in2)
  class EQ3(in1:Expression,in2:Expression) extends Base(in1,in2)
  class NEQ3(in1:Expression,in2:Expression) extends Base(in1,in2)
  class NOT(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Plus(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Minus(in1:Expression,in2:Expression) extends Base(in1,in2)
  class Multiply(in1:Expression,in2:Expression) extends Base(in1,in2)
}