package com.simplifide.generate.blocks.statemachine2

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.statemachine2.Transition.Impl
import com.simplifide.generate.parser.model.Expression

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 1/19/12
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */

trait Transition {

  /** Initial StateMachine */
  val source:State
  /** Final StateMachine */
  val destination:State
  /** Condition to Transition */
  val condition:Option[Expression]

  
  def copy(source:State = this.source,
    destination:State = this.destination, 
    condition:Option[Expression] = this.condition):Transition
}

object Transition {
  
  def apply(source:State,  destination:State = null, condition:Option[Expression] = null) =
    new Impl(source,destination,condition)
  
  class Impl(override val source:State, override val destination:State, override val condition:Option[Expression]) extends Transition{
    def copy(source:State = this.source,
             destination:State = this.destination,
             condition:Option[Expression] = this.condition):Transition = new Impl(source,destination,condition)
  }
  

  
                  
}