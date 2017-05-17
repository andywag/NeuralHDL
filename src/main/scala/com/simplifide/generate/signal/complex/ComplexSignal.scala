package com.simplifide.generate.signal.complex

import com.simplifide.generate.signal._
import com.simplifide.generate.signal.SignalTrait._
import com.simplifide.generate.generator.SimpleSegment


/** Complex Number which contains a real and imaginary section
    For the most part this is treated like a vector where
    the first part is the real and the second part is the imaginary
 */

class ComplexSignal(val prototype:SignalTrait) extends SignalTrait with Complex {

  override type T = SignalTrait

  override val name:String       = prototype.name
  override val opType:OpType     = prototype.opType
  override val fixed:FixedType   = prototype.fixed

  override def newSignal(name:String = this.name,
    opType:OpType = this.opType,
    fixed:FixedType = this.fixed):SignalTrait = new ComplexSignal(this.prototype.newSignal(name,opType,fixed))

  // Complex Operations
  val real      =  prototype.newSignal(this.name + "_re")
  val imag      =  prototype.newSignal(this.name + "_im")

  override val isConjugate = this match {
    case x:ComplexSignal.Conjugate => true
    case _                         => false
  }

  override def changeTestType:SignalTrait = new ComplexSignal(prototype.changeTestType)
  /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  override def changeType(typ:OpType):SignalTrait = new ComplexSignal(prototype.changeType(typ))
  /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  override def reverseType:SignalTrait = new ComplexSignal(prototype.reverseType)





  override def numberOfChildren = 2

  override def child(index:Int):SignalTrait = if (index == 0) this.real else if (index ==1) this.imag else this
  override def children:List[SignalTrait] = List(real,imag)
  override def allChildren:List[SignalTrait] = children
  override def slice(index:Int):SignalTrait  = this

  override def createSlice(index:Int,prefix:String=""):SignalTrait = //if (index == 0) this.real else if (index ==1) this.imag else this
    new ComplexSignal(prototype.createSlice(index,prefix))


}

object ComplexSignal {


  def apply(name:String,fixed:FixedType = FixedType.Simple) =
    new ComplexSignal(SignalTrait(name,OpType.Signal,fixed))

  def apply(name:String,optype:OpType,fixed:FixedType) =
    new ComplexSignal(SignalTrait(name,optype,fixed))



  class Conjugate(prototype:SignalTrait) extends ComplexSignal(prototype) {
      override val isConjugate = true
  }

  object Conjugate {
    def apply(signal:SignalTrait) = new ComplexSignal.Conjugate(signal)
  }


  
}


