package com.simplifide.generate.parser

import items._
import model.{Clock, Expression}
import com.simplifide.generate.blocks.basic.flop.{SimpleFlop, ClockControl}
import com.simplifide.generate.language.FlopFactory
import collection.mutable.Stack
import com.simplifide.generate.blocks.basic.condition.{ConditionStatementBuilder, ConditionStatement, NewCaseStatement}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.signal.{SignalTrait, Constant}
import com.simplifide.generate.blocks.basic.Statement


/**
 * Parser section which handles the parsing of condition statements and case statements as well as always
 * blocks
 **/
trait ConditionParser extends BaseParser with SingleConditionParser with SingleCaseParser with ExpressionGroupParser
  with FlopParser with ConstantParser with InitialParser {



  /*
  private def removeExpressions(values:List[Expression]) {
    val indexes = values.map(x => this.statements.findIndexOf(x == _)).sortBy(-_)
    indexes.foreach(x => if (x >= 0) this.statements.remove(x))
  }
  */


  /** Creation of AlwaysBlock Block containing the expressions given by expressions */
  def $always_star(expressions:Expression*):Expression =  {
    val expr = expressions.toList.filter(_ != null).map(_.create)
    //removeExpressions(expr)
    val always = ObjectFactory.AlwaysStar(expr)
    scope.assign(always)
    always
  }

  /** Creation of AlwaysBlock Block with a sensitivity list containing the expressions given by expressions */
  /*
  def $always(sensitivity:Expression*)(expressions:Expression*) {
    val expr = expressions.toList.filter(_ != null).flatMap(_.create)
    removeExpressions(expr)
    val always = ObjectFactory.AlwaysBlock(sensitivity.toList)(expr)
    scope.assign(always)
  }
  */
  /*
  def $always(clk:ClockControl)(body:Expression*) {
    removeExpressions(body.toList)
    val lis = body.filter(_ != null).flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    scope.assign(Always.Sensitivity(None,BasicSegments.List(lis),clk.createSensitivityList().toList))
  }
  */

  /** Create a flop without a reset
   *  TODO Need to clean up the condition
   * */
  def flop(expressions:Expression*)(implicit clk:ClockControl):SimpleSegment = {
    val statements = expressions.map(_.asInstanceOf[Statement])
    val flo =  FlopFactory.simpleFlopList(statements.toList)
    this.assign(flo)
    flo
  }
  /** Create a flop without a reset
   *  TODO Need to clean up the condition
   **/

  /*
  def $flopR(expressions:Expression*)(implicit clk:ClockControl):SimpleSegment = {
    removeExpressions(expressions.toList)
    val outputs:List[SignalTrait] = expressions.filter(_ != null).flatMap(_.split).flatMap(_.outputs).toList
    val resets = SignalTrait.uniqueSignals(outputs).map(x => new Statement.Reg(x,Constant(0,x.fixed)))
    val segments = expressions.filter(_ != null).flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    val flo = SimpleFlop(resets,segments)(clk)
    this.assign(flo)
    flo
  }

  def $flop(resets:Expression*)(expressions:Expression*)(implicit clk:ClockControl):SimpleSegment = {
    removeExpressions(resets.toList ::: expressions.toList)
    val resets2  = resets.filter(_ != null).flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    val segments = expressions.filter(_ != null).flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    val flo = SimpleFlop(resets2,segments)(clk)
    this.assign(flo)
    flo
  }
  */


  /*
  def $case(condition:Expression)(statements:Expression*):Expression = {
    NewCaseStatement(condition.asInstanceOf[SimpleSegment],
      statements.toList.map(NewCaseStatement.Item(_)))

    //$case(condition,statements.toList)
  }
  /** Creates a case statement.  */
  def $caseL(condition:Expression)(statements:List[Expression]):Expression = {
    NewCaseStatement(condition.asInstanceOf[SimpleSegment],
      statements.map(NewCaseStatement.Item(_)))

  }

  def $ifL(conditions:List[ConditionStatement.Prototype]):SimpleSegment =
    BasicSegments.List(conditions.zipWithIndex.map(x => x._1.create(x._2)))


  def $ifc(statements:SimpleSegment*):ConditionStatement.Prototype =
    new ConditionStatement.Prototype(None,statements.toList)

  def $ifc(condition:SimpleSegment)(statements:SimpleSegment*):ConditionStatement.Prototype =
    new ConditionStatement.Prototype(Some(condition),statements.toList)


  def $if(statements:Expression)(values:Expression*):Expression = {
    baseCondition = ConditionStatementBuilder(statements,values.toList.map(_.asInstanceOf[SimpleSegment]))
    this.statements.append(baseCondition)
    baseCondition

  }
  /** Else Clause for the Condition ParserStatement */
  def $else_if(condition:Expression)(values:Expression*):Expression = {
    removeExpressions(values.toList)
    baseCondition.elseIf(condition)(values.toList)
    null
  }
  /** Default Else clause for the condition statement */
  def $else(values:Expression*):Expression = {
    removeExpressions(values.toList)
    baseCondition.els(values.toList)
    null
  }
  */
}

object ConditionParser {

}