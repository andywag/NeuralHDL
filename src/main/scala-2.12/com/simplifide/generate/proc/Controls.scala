package com.simplifide.generate.proc

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.project.Connection

/**
 * Trait which defines a control signal
 */
trait Controls {
  /** Control Signal */
  val signal:SignalTrait = null
  /** Index of the control signal */
  //val index:Int
  /** Value of the control signal */
  //val value:Int

  def connect(connection:Connection) = Controls(connection.connect(signal))




}

/**
 * Factory method to create controls
 */
object Controls {

  def apply(signal:SignalTrait) = new Impl(signal)
  /** Default implementation of controls */
  class Impl(override val signal:SignalTrait) extends Controls {
    override def toString = "Controls -> " + signal
  }

  /**
   * Class which contains values which occur for the control at a given time
   **/
  class Value(val control:Controls,val index:Int, val value:Int) {
    override def toString = control.signal + "(" + index + ")" + " = " + value
    def connect(connection:Connection) = new Value(control.connect(connection),index,value)

  }
  object Value {
    def apply(control:Controls,index:Int, value:Int) = new Value(control,index,value)
  }

  class Composite(override val signal:SignalTrait,val controls:List[Controls]) extends Controls {

  }

}