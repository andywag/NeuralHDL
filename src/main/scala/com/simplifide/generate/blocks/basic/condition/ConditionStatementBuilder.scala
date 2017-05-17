package com.simplifide.generate.blocks.basic.condition

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.generator._
import com.simplifide.generate.parser.model.Expression
import collection.mutable.ListBuffer
import com.simplifide.generate.signal.SignalTrait

/**
 * Condition ParserStatement -- If Else Clause
 *
 * @constructor
 * @parameters conditions : List of Condition Statements
 */
class ConditionStatementBuilder() extends SimpleSegment.Combo {

  val conditions = new ListBuffer[SimpleSegment]()
  /** Output of this code segment */
  override def outputs:List[SignalTrait] = conditions.toList.flatMap(_.outputs)

  /** Add an Extra Elseif condition to the statement */
  def elseIf(condition:Expression)(states:List[Expression])  {
    val newCondition = new ConditionStatement.Middle(condition.asInstanceOf[SimpleSegment],
      BasicSegments.List(states.map(_.asInstanceOf[SimpleSegment])));
    conditions.append(newCondition)
  }
  /** Add the final else statement */
  def els(states:List[Expression])  {
    val newCondition = new ConditionStatement.Last(BasicSegments.List(states.map(_.asInstanceOf[SimpleSegment])))
    conditions.append(newCondition)
  }

  /*
   override def split:List[SimpleSegment] = {
    val lis =  conditions.toList.flatMap(_.split).map(_.asInstanceOf[SimpleSegment])
    return  lis
   }
   */


}

/** Factory methods and classes to aid in creation of the condition statement */
object ConditionStatementBuilder {

  def apply(cond:SimpleSegment,statements:List[SimpleSegment]) = {
    val build = new ConditionStatementBuilder()
    val first = new ConditionStatement.First(cond,BasicSegments.List(statements))
    build.conditions.append(first)
    build
  }

}


