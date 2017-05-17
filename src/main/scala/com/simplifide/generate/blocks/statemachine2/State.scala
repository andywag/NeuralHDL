package com.simplifide.generate.blocks.statemachine2

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.statemachine2.State.Impl
import com.simplifide.generate.signal.{FixedType, SignalTrait, Constant, ParameterTrait}
import parser.StateMachineParser

/**
 *  Class Describing a state for the state matchine
 */

trait State extends SimpleSegment{
  /** Name of StateMachine */
  val name:String 
  /** Index of the StateMachine */
  val index:Int
  /** List of Possible Transitions from this state */
  val transitions:List[Transition]
  /** List of Operations which occur during this state */
  val operations:List[SimpleSegment]
  
  val state:StateMachineParser
  
  def current = state.currentState match {
    case Some(x) => (x == parameter)
    case None    => parameter
  }

  def next = state.nextState match {
    case Some(x) => (x == parameter)
    case None    => parameter
  }

  /** Copy this state */
  def copy(name:String = this.name,
    index:Int = this.index,
    transitions:List[Transition] = this.transitions,
    operations:List[SimpleSegment] = this.operations,
    state:StateMachineParser = this.state):State

  def parameter = new ParameterTrait.Hex(name,index.toHexString,FixedType.unsigned(state.stateWidth,0))

  
}

object State {
  
  def apply(name:String, index:Int, state:StateMachineParser) = new Impl(name,index,List(),List(),state)

  
  class Impl(override val name:String,
    override val index:Int,
    override val transitions:List[Transition],
    override val operations:List[SimpleSegment],
    override val state:StateMachineParser) extends State with SimpleSegment {

    def createCode(implicit writer:CodeWriter):SegmentReturn =
      Constant(index).createCode


    def copy(name:String = this.name,
             index:Int = this.index,
             transitions:List[Transition] = this.transitions,
             operations:List[SimpleSegment] = this.operations,
             state:StateMachineParser = this.state):State = new Impl(name,index,transitions,operations,state)

  }
}