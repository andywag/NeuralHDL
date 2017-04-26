package com.simplifide.generate.proc

/**
 * Map which contains the instructions
 */
trait Instruction {
  val controls:List[Controls]

  def createInstruction(newControl:List[Controls]):Instruction = {
    //controls.find()
    null
  }

}

object Instruction {
  def apply(controls:List[Controls]) = new Impl(controls)

  class Impl(override val controls:List[Controls]) extends Instruction

  class Value {

  }


}