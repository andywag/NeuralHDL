package com.simplifide.generate.blocks.statemachine2

/**
 * Class containing information used to create state machine
 */

trait StateModel {
  /** States used by this state machine */
  val states:List[State]
  /** List of transitions contained in this state machine */
  val transitions:List[Transition]

  /** Parameters defined by the state definitions */
  val parameters = states.map(_.parameter)



}