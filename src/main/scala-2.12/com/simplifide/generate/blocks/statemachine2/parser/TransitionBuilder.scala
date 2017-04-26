package com.simplifide.generate.blocks.statemachine2.parser

import com.simplifide.generate.blocks.statemachine2.parser.TransitionBuilder.Impl
import com.simplifide.generate.blocks.statemachine2.{Transition, State}
import com.simplifide.generate.generator.SimpleSegment
import collection.mutable.ListBuffer
import com.simplifide.generate.parser.model.Expression

/**
 * Builder class to create transitions for the state machien
 */

trait TransitionBuilder {
  
  val transitions:List[Transition]
  val current:Transition
  
  
  val totalTransitions = transitions ::: List(current)
  
  def copy(current:Transition = this.current,  transitions:List[Transition] = this.transitions):TransitionBuilder
  
  /** Append a destination state to the current transition */
  def to(state:State) = {
    current.destination match {
      case null     => this.copy(current = current.copy(destination = state))
      case _        => this.copy(current = Transition(current.source,state),transitions = this.transitions ::: List(current))
    }
  }
  /** Append a transition condition state to the current transition */
  def when(condition:Expression) =
    this.copy(current = current.copy(condition = Some(condition)))

}

object TransitionBuilder {

  def apply(state:State) = new Impl(Transition(state),List())
  def apply(current:Transition, transitions:List[Transition] = List()) = new Impl(current,transitions)

  class Impl(override val current:Transition,override val transitions:List[Transition]) extends TransitionBuilder {
    def copy(current:Transition = this.current,  transitions:List[Transition] = this.transitions):TransitionBuilder
      = new Impl(current,transitions)
  }
  
}