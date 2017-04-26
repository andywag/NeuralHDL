package com.simplifide.generate.parser.model

import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.condition.{Question}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser._
import factory.{CreationFactory}
import items.{SingleCaseParser,RegisterAtParser}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.generator.{SimpleSegment}
import com.simplifide.generate.blocks.basic.flop.{ClockControl}
import com.simplifide.generate.signal.FixedType
import com.simplifide.generate.blocks.basic.fixed.{MultiplySegment, AdditionSegment}
import operator.{BinaryParserOperator, UnaryParserOperator}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.signal.complex.ComplexSignal

/**
 * Basic Parser Class
 */

trait Expression {


  /** Create the simple segment */
  def create(implicit creator:CreationFactory):SimpleSegment
  /** Create Expression as a function of the output */
  def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment
  /** Creates an Assignment based on the output*/
  def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
    creator match {
      case CreationFactory.Function        => new Statement.FunctionBody(output,this.createOutput(output))
      case _                               => new Statement.Reg(output,this.createOutput(output))
    }


  // Main Class for Handling an assignment statement to another condition
  def := (rhs:Expression)(implicit scope:SegmentHolder):Expression = {
    val state = ParserStatement(this,rhs)
      if (scope != null) scope.assign(state)
      state
  }


  def ::= (rhs:Expression):Expression = {
    ParserStatement(this,rhs)
  }

  def :::= (rhs:Expression)(implicit scope:SegmentHolder):Expression = {
    val state = new ParserStatement.AlwaysReg(this,rhs)
    if (scope != null) scope.assign(state)
    state
  }

    // Case ParserStatement
    def $match(result:SingleCaseParser.Close)        = new SingleCaseParser.Top(this,result)
    def $match(result:List[SingleCaseParser.Close])  = new SingleCaseParser.Top(this,result.reduceLeft(_+_))


    // Unary Operators
    def unary_! : Expression = new UnaryParserOperator.NotLogical(this)
    def unary_~ : Expression = new UnaryParserOperator.Not(this)
    def unary_+ : Expression = new UnaryParserOperator.Positive(this)
    def unary_- : Expression = new UnaryParserOperator.Negative(this)
    // Binary Operators
    def - (rhs:Expression)(implicit creator:CreationFactory):Expression = new AdditionSegment.Truncate("",this.create,rhs.create,true,FixedType.Simple)
    def + (rhs:Expression)(implicit creator:CreationFactory):Expression = new AdditionSegment.Truncate("",this.create,rhs.create,false,FixedType.Simple)

    /** Handle Multiplication */
    def * (rhs:Expression)(implicit creator:CreationFactory):Expression = MultiplySegment(this.create,rhs.create)

    def / (rhs:Expression)(implicit creator:CreationFactory):Expression = ObjectFactory.Div(this.create,rhs.create)
    // Comparison Operators
    def < (rhs:Expression):Expression      = new BinaryParserOperator.LT(this,rhs)
    def > (rhs:Expression):Expression      = new BinaryParserOperator.GT(this,rhs)
    def <= (rhs:Expression):Expression     = new BinaryParserOperator.LTE(this,rhs)
    def >= (rhs:Expression):Expression     = new BinaryParserOperator.GTE(this,rhs)
    def == (rhs:Expression):Expression     = new BinaryParserOperator.EQ(this,rhs)
    def != (rhs:Expression):Expression     = new BinaryParserOperator.NEQ(this,rhs)
    def === (rhs:Expression):Expression    = new BinaryParserOperator.EQ(this,rhs)
    def !== (rhs:Expression):Expression    = new BinaryParserOperator.NEQ3(this,rhs)
    // Logical and Reduction Operators       new
    def ~ (rhs:Expression):Expression      = new BinaryParserOperator.NOT(this,rhs)
    def & (rhs:Expression):Expression      = new BinaryParserOperator.And(this,rhs)
    def ~& (rhs:Expression):Expression     = new BinaryParserOperator.Nand(this,rhs)
    def | (rhs:Expression):Expression      = new BinaryParserOperator.Or(this,rhs)
    def ~| (rhs:Expression):Expression     = new BinaryParserOperator.Nor(this,rhs)
    def ^ (rhs:Expression):Expression      = new BinaryParserOperator.Xor(this,rhs)
    def ^~ (rhs:Expression):Expression     = new BinaryParserOperator.Nxor(this,rhs)
    def ~^ (rhs:Expression):Expression     = new BinaryParserOperator.Nxor(this,rhs)
    // Shift Operators                       new
    def >> (rhs:Expression):Expression     = new BinaryParserOperator.Sl(this,rhs)
    def << (rhs:Expression):Expression     = new BinaryParserOperator.Sr(this,rhs)


    // Conditional Operators
    def ?  (rhs:Expression)    = Question.Open(this,rhs)
    def :: (rhs:Expression)    = Question.Item(this, rhs)

    def $at(clk:ClockControl)  = new RegisterAtParser.Flop(this,clk)
    def $delay(delay:Int)      = new ParserStatement.Delay(this,delay)

    /** Splits an individual operation */
    def split(output:Expression,index:Int):ExpressionReturn = 
      new ExpressionReturn(this,List())
    

}

object Expression {


  /*
  class Flop(val register:Expression, val clk:ClockControl) extends Expression {

    override def create(output:Expression):SimpleSegment = {
      val internal = register.create(output)
      BasicSegments.List (new SimpleFlopSegment(clk,internal).split)
    }
    override def create:SimpleSegment = {
      null
    }
    override def createOutput(output:SimpleSegment):SimpleSegment = {
      null
    }

  } */
}