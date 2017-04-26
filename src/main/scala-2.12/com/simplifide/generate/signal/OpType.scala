package com.simplifide.generate.signal

/**
 * Class defining the type of a signal
 */

/** Class which contains the type of appendSignal related to it's operation
*/
class OpType  {

  /** Returns a list of appendSignal declarations associated with this type */
  def reverseType:OpType = {
    if (this.isOutput) OpType.Input
    else if (this.isInput) OpType.Output
    else this
  }

  /** Returns a list of appendSignal declarations associated with this type */
  def reverseTypeWithReg:OpType = {
    if (this.isOutput) OpType.Input
    else if (this.isInput) OpType.RegOutput
    else this
  }

  /** Converts the type of the signal to append to a testbench */
  def testType:OpType = {
    if (this.isInput) OpType.Register
    else if (this.isOutput) OpType.Signal
    else this
  }


  def isInput:Boolean  = this match {
    case OpType.Input      => true
    case _                 => false
  }

  def isOutput:Boolean  = this match {
    case OpType.Output            => true
    case OpType.RegOutput   => true
    case _                          => false
  }

  def isReg:Boolean    = this match {
    case OpType.Register        => true
    case OpType.RegOutput => true
    case _                 => false
  }

  def isWire:Boolean    = this match {
    case OpType.Signal => true
    case _                 => false
  }

  def isParameter:Boolean    = this match {
    case OpType.Param => true
    case _                 => false
  }

  def isSignal:Boolean = !isOutput && !isInput && (this != OpType.InOut)


  override def toString = {
    this match {
      case OpType.Input     => "Input"
      case OpType.Output    => "Output"
      case OpType.Signal    => "Wire"
      case OpType.Register  => "Reg"
      case _                => "Other"
    }
  }

}

object OpType {
  object Input extends OpType
  object Output extends OpType
  object InOut  extends OpType
  object Constant extends OpType
  object Signal extends OpType
  object Register extends OpType
  object Param    extends OpType
  object RegOutput extends OpType

  
}
