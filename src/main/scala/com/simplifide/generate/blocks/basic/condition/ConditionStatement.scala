package com.simplifide.generate.blocks.basic.condition

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.generator._

/**
 * Condition ParserStatement -- If Else Clause
 *
 * @constructor
 * @parameters conditions : List of Condition Statements
 */
class ConditionStatement(val conditions:List[SimpleSegment]) extends SimpleSegment {

  override def outputs = conditions.flatMap(_.outputs)

  override def createVector:List[SimpleSegment] = List(new ConditionStatement(conditions.flatMap(_.createVector)))
  


  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val st = this.conditions.toList.map(x => x.asInstanceOf[SimpleSegment])
    st.map(writer.createCode(_)).reduceLeft(_ + _)
  }



}

/** Factory methods and classes to aid in creation of the condition statement */
object ConditionStatement {

  def First(condition:SimpleSegment,result:List[SimpleSegment]):SimpleSegment =
    new First(condition,BasicSegments.List(result))

  def Middle(condition:SimpleSegment,result:List[SimpleSegment]):SimpleSegment =
    new Middle(condition,BasicSegments.List(result))

  def Last (result:List[SimpleSegment]) =
    new Last(BasicSegments.List(result))


  
  // TODO Need simpler method for creation of this statement

  /** Method for creating the condition statement based on a list of expressions.
   *
   *  The input contains a list of conditions
   **/
  def apply(conditions:List[(Option[SimpleSegment],List[SimpleSegment])]):SimpleSegment = {
    def condition(index:Int,cond:(Option[SimpleSegment],List[SimpleSegment])):SimpleSegment = {
      if (index == 0) return new First(cond._1.get,BasicSegments.ListExpression(cond._2))
      else {
        cond._1 match {
          case Some(x) => return new Middle(x,BasicSegments.ListExpression(cond._2))
          case None    => return new Last(BasicSegments.ListExpression(cond._2))
        }
      }
    }
    if (!conditions(0)._1.isDefined) BasicSegments.ListExpression(conditions(0)._2)       // No Condition ParserStatement Condition (Occurs with Flop)
    else new ConditionStatement(conditions.zipWithIndex.map(x => condition(x._2,x._1)))
  }

  def apply(cond:SimpleSegment,statements:List[SimpleSegment]) = {
    val first = new First(cond,BasicSegments.List(statements))
    new ConditionStatement(List(first))

  }

  class Prototype(val condition:Option[SimpleSegment],val body:List[SimpleSegment]) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = null

    def create(index:Int) = {
      if (index == 0) new First(condition.get,BasicSegments.List(body))
      else if (condition.isDefined) new Middle(condition.get,BasicSegments.List(body))
      else new Last(BasicSegments.List(body))
    }
  }

  /** Class describing the first condition  */
  class First(condition:SimpleSegment,body:SimpleSegment) extends SimpleSegment {

   override def outputs = body.outputs
   override def toString = "if (" + condition + ")" + body



   override def createVector:List[SimpleSegment] =
    return List(new First(condition,body.createVectorSingle))


   override def createCode(implicit writer:CodeWriter):SegmentReturn = {
     return SegmentReturn("if (") + writer.createCode(condition) + ") begin\n" ++ writer.createCode(body) + "end\n"
   }

  }

  /** Class Defining Middle Condition */
  class Middle(condition:SimpleSegment,body:SimpleSegment) extends SimpleSegment {
    override def outputs = body.outputs

    override def createVector:List[SimpleSegment] = {
      return List(new Middle(condition,body.createVectorSingle))
    }

    override def createCode(implicit writer:CodeWriter):SegmentReturn =  {
      return SegmentReturn("else if (") + writer.createCode(condition) + ") begin \n" ++ writer.createCode(body) + "end\n"
    }

  }
  /** Class Defining Last Condition */
  class Last(body:SimpleSegment) extends SimpleSegment {
    override def outputs = body.outputs

    override def createVector:List[SimpleSegment] = {
      return List(new Last(body.createVectorSingle))
    }

     override def createCode(implicit writer:CodeWriter):SegmentReturn =
      return SegmentReturn("else begin\n") ++ writer.createCode(body) + "end\n"


  }

}


