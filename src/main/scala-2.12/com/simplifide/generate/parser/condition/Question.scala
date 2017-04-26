package com.simplifide.generate.parser.condition

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.ObjectFactory
import com.simplifide.generate.blocks.basic.condition.QuestionSegment
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Model which represents a question
 * 
 * condition ? tru : fal
 */

trait Question extends Expression {
  
  /** Condition for the question statement */
  val condition:Expression
  /** True Result of the Condition Statement */
  val tru:Expression
  /** False Result of the Condition Statement */
  val fal:Expression

  override def create(implicit creator:CreationFactory) =
    QuestionSegment(condition.create,tru.create,fal.create)
  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) =
    QuestionSegment(condition.create,tru.createOutput(output),fal.createOutput(output))

  override def toString = condition  + " ? " + tru + " : " + fal

}

object Question {
  def apply(condition:Expression, tru:Expression,  fal:Expression) = new Impl(condition,tru,fal)
  
  class Impl(override val condition:Expression, 
    override val tru:Expression, 
    override val fal:Expression) extends Question
  
  def Item(tru:Expression, fal:Expression):Expression = {
    fal match {
      case x:Question.Open    => Question(x.condition,x.tru,tru)//return ObjectFactory.Question(x,y,tru)
      case _                  => return new Question.Item(tru,fal)
    }
  }
  
  def Open(condition:Expression, tru:Expression) = new Open(condition,tru)
  
  class Open(val condition:Expression, val tru:Expression) extends Expression {
    override def :: (rhs:Expression):Question  = Question(this.condition, this.tru, rhs)
    override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = null
    override def create(implicit creator:CreationFactory) = null
  }
  
  class Item(val tru:Expression, val fal:Expression) extends Expression {
    override def toString =  tru + " : " + fal
    override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = null
    override def create(implicit creator:CreationFactory) = null
  }
  
}