package com.simplifide.generate.language

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlopList}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.parser.model.{ Expression, Model, Clock}
import com.simplifide.generate.signal.{ArrayTrait, OpType, SignalTrait, FixedType}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.blocks.basic.condition.{QuestionSegment}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.blocks.basic.fixed.{RoundSegment, MultiplySegment}
import com.simplifide.generate.signal.complex.ComplexSignal
import com.simplifide.generate.blocks.basic.Statement

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 7/16/11
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */

class LanguageFactory {

}



object LanguageFactory {

  /*
  def Statement(output:Expression, input:Expression):SimpleSegment    = {
    input.create(output)
  }
  */
  //def StatementReg(output:Expression, input:Expression):SimpleSegment = new Statement.Reg(output,input)

  /*
  def Flop(clk:Clock,output:Expression,input:Expression):SimpleSegment = {

    FlopFactory(clk,output,input)
  }
  */

  /*
  def Flop(clk:Clock,output:Expression,reset:Expression,input:Expression) = {
    val res =  List(new SimpleFlopList.Segment(output,Some(reset)))
    val en  =  List(new SimpleFlopList.Segment(output,Some(input)))
    new SimpleFlopList(None,clk,res,en)
  }
  */

  // Condition Statements
  //def Question(condition:Expression, tru:Expression, fal:Expression) = QuestionSegment(condition,tru,fal)
  // Math Functions
  // Additions
  /*
  def Adder(lhs:Expression,rhs:Expression,negative:Boolean = false) =
   */

  /* new AdditionSegment2("",lhs,rhs,negative,FixedType.Simple,FixedType.Simple)

  def AdderTrunc(lhs:Expression,rhs:Expression,negative:Boolean = false,fixed:Model.Fixed,internal:Model.Fixed)     =
    new AdditionSegment2.Truncate("",lhs,rhs,negative,fixed,internal)

  def AdderTruncClip(lhs:Expression,rhs:Expression,negative:Boolean = false,fixed:Model.Fixed,internal:Model.Fixed)  =
    new AdditionSegment2.TruncateClip("",lhs,rhs,negative,fixed,internal)

  def AdderRound(lhs:Expression,rhs:Expression,negative:Boolean = false,fixed:Model.Fixed,internal:Model.Fixed)      =
    new AdditionSegment2.Round("",lhs,rhs,negative,fixed,internal)

  def AdderRoundClip(lhs:Expression,rhs:Expression,negative:Boolean = false,fixed:Model.Fixed,internal:Model.Fixed)  =
    new AdditionSegment2.RoundClip("",lhs,rhs,negative,fixed,internal)

  // Multiplier
  def Mult(lhs:Expression,rhs:Expression,fixed:FixedType,internal:FixedType)(implicit clk:ClockControl) = {
    (lhs,rhs) match {
      case (x:ComplexSignal,y:ComplexSignal) =>
        new ComplexMultiplySegment("",clk,null,x,y,internal)
      case _ =>
        new MultiplySegment("",lhs,rhs,fixed,internal)

    }

  }
  def MultTrunc(lhs:Expression,rhs:Expression,fixed:Model.Fixed,internal:Model.Fixed)     =
    new MultiplySegment.Truncate("",lhs,rhs,fixed,internal)
  def MultTruncClip(lhs:Expression,rhs:Expression,fixed:Model.Fixed,internal:Model.Fixed) =
    new MultiplySegment.TruncateClip("",lhs,rhs,fixed,internal)
  def MultRound(lhs:Expression,rhs:Expression,fixed:Model.Fixed,internal:Model.Fixed)     =
    new MultiplySegment.Round("",lhs,rhs,fixed,internal)
  def MultRoundClip(lhs:Expression,rhs:Expression,fixed:Model.Fixed,internal:Model.Fixed) =
    new MultiplySegment.RoundClip("",lhs,rhs,fixed,internal)
  // Division
  def Div(lhs:Expression,rhs:Expression)                            = null//factory.Mult(lhs,rhs)
  def DivTrunc(lhs:Expression,rhs:Expression,fixed:Model.Fixed)     = null//factory.MultTrunc(lhs,rhs,fixed)
  def DivTruncClip(lhs:Expression,rhs:Expression,fixed:Model.Fixed) = null//factory.MultTruncClip(lhs,rhs,fixed)
  def DivRound(lhs:Expression,rhs:Expression,fixed:Model.Fixed)     = null//factory.MultRound(lhs,rhs,fixed)
  def DivRoundClip(lhs:Expression,rhs:Expression,fixed:Model.Fixed) = null//factory.MultRoundClip(lhs,rhs,fixed)
  // Rounding
  def Truncate(expression:Expression, fixed:Model.Fixed,internal:Model.Fixed)       =
    new RoundSegment.Truncate("",expression,fixed,internal)
  def TruncateClip(expression:Expression, fixed:Model.Fixed,internal:Model.Fixed)  =
    new RoundSegment.TruncateClip("",expression,fixed,internal)
  def RoundInt(expression:Expression, fixed:Model.Fixed,internal:Model.Fixed)     =
    new RoundSegment.Round("",expression,fixed,internal)
  def RoundClip(expression:Expression, fixed:Model.Fixed,internal:Model.Fixed)    =
    new RoundSegment.RoundClip("",expression,fixed,internal)
  */

  /*
  def GT (lhs:Expression,rhs:Expression):Expression   = BinaryOperator.GT(lhs,rhs)
  def LT (lhs:Expression,rhs:Expression):Expression   = BinaryOperator.LT(lhs,rhs)
  def LTE (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.LTE(lhs,rhs)  // See <= Assign ParserStatement
  def GTE (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.GTE(lhs,rhs)
  def EQ (lhs:Expression,rhs:Expression):Expression   = BinaryOperator.EQ(lhs,rhs)
  def NEQ (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.NEQ(lhs,rhs)
  def EQ3 (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.EQ3(lhs,rhs)
  def NEQ3 (lhs:Expression,rhs:Expression):Expression = BinaryOperator.NEQ3(lhs,rhs)
    // Logical and Reduction Operators
  def NOT (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.NOT(lhs,rhs)
  def AND (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.AND(lhs,rhs)
  def NAND (lhs:Expression,rhs:Expression):Expression = BinaryOperator.NAND(lhs,rhs)
  def OR (lhs:Expression,rhs:Expression):Expression   = BinaryOperator.OR(lhs,rhs)
  def NOR (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.NOR(lhs,rhs)
  def XOR (lhs:Expression,rhs:Expression):Expression  = BinaryOperator.XOR(lhs,rhs)
  def NXOR (lhs:Expression,rhs:Expression):Expression = BinaryOperator.NXOR(lhs,rhs)
    // Shift Operators
  def SL (lhs:Expression,rhs:Expression):Expression =   BinaryOperator.SL(lhs,rhs)
  def SR (lhs:Expression,rhs:Expression):Expression =   BinaryOperator.SR(lhs,rhs)
  */
  //
  //def ConditionIf(statements:Expression)(values:List[Expression])      = ConditionStatementBuilder(statements,values.toList.map(_.asInstanceOf[SimpleSegment]))
  //def Case(condition:Expression)(statements:List[Expression]) = NewCaseStatement(condition,statements)
  //def CaseStatement(statement:Expression) = NewCaseStatement.Item(statement)
  //def CaseStatement(condition:Expression, statement:Expression) = NewCaseStatement.Item(condition,statement)

  def AlwaysBlock(values:List[Expression])(states:List[Expression]) =
    Always.Sensitivity(states.map(_.asInstanceOf[SimpleSegment]),values.map(_.asInstanceOf[SimpleSegment]))

  def AlwaysStar(values:List[Expression]) = Always.Star(values.map(_.asInstanceOf[SimpleSegment]))
  // Signal Creation
  // TODO Doesn't Support multidimensional arrays
  /*
  def Signal(name:String, typ:SignalType = OpType.Signal,fixed:Model.Fixed = Model.Fixed(1,0))(arr:List[Int]):SignalTrait = {
    val sig = SignalTrait(name,typ,fixed)
    if (arr.size > 0) ArrayTrait(sig,arr(0))
    else sig
  }
  */
  /*
  def Constant(name:String = "",value:Double,fixed:Model.Fixed = Model.NoFixed) = {
     com.simplifide.generate.signal.Constant(value,fixed)
  }*/


  class ExpressionConversion(expression:Expression) extends SimpleSegment {
     def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn("Not Defined")
  }



}