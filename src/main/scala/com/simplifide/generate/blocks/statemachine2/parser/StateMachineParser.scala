package com.simplifide.generate.blocks.statemachine2.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.statemachine2.{Transition, State}
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.SignalTrait


/**
 * Builder Class to create a statemachine description
 */
trait StateMachineParser {

  val currentState:Option[SignalTrait] = None
  val nextState:Option[SignalTrait]    = None

  val states      = new ListBuffer[State]()
  val transitions = new ListBuffer[Transition]()
  
  /** Create a state and append it to the list of states */
  def state(name:String, index:Int):State  = {
    val state = State(name,index,this)
    states.append(state)
    state
  }

  def stateWidth = {
    val max = states.map(_.index).foldLeft(0)((a,b) => if (a > b) a else b)
    val width = math.ceil(math.log(max)/math.log(2.0)).toInt
    width + 1
  }


  /** Calculate the final states by combining the transitions as well as the outputs */
  def finalStates:List[State] = {
    val transitionMap = transitions.toList.groupBy(_.source)
    states.toList.map(x =>
      transitionMap.get(x) match {
        case Some(y) => x.copy(transitions = y)
        case None    => x
      }
    )
  }
  
  def transition(transitionBuilder:TransitionBuilder*) =
    transitions.appendAll(transitionBuilder.flatMap(_.totalTransitions))

  
  def matchState(state:Expression,states:List[State]):Expression = {
    val intermediate = states.map(x => (state == x.parameter) )
    intermediate.reduceLeft(_ | _)
  }
  
  /** Implicit conversion to function for defining transitions */
  implicit def state2TransitionBuilder(state:State) = TransitionBuilder(state)
  
 
  
  
}