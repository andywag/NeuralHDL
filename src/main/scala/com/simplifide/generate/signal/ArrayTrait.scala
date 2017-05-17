package com.simplifide.generate.signal

import com.simplifide.generate.generator.SimpleSegment


/**
 *  An array of signals
 */

trait ArrayTrait[T <: SignalTrait] extends SignalTrait {

  /** Length of Array */
  val length:Int
  /** Prototype signal */
  val prototype:T

  override val name:String = prototype.name
  override val opType:OpType = prototype.opType
  override val fixed:FixedType = prototype.fixed

  override def apply(index:Int):T = this.slice(index)

  override def toString = this.prototype.toString + "[" + length + "]"

  override val numberOfChildren:Int = length

  override def child(index:Int):SignalTrait = slice(index)


  override  def changeTestType:SignalTrait = ArrayTrait(prototype.changeTestType,this.length)
  /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  override def changeType(typ:OpType):SignalTrait = ArrayTrait(prototype.changeType(typ),this.length)


  override def children:List[SignalTrait] =
    List.tabulate(length)(this.slice(_))

  override def allChildren:List[SimpleSegment] =
    children.flatMap(x => x.allChildren)

  /** Slice is created by getting the slice variable from the prototype which creates the new variable */
  override def slice(index:Int):T = prototype.createSlice(index).asInstanceOf[T]


}

object ArrayTrait {
  def apply[T <: SignalTrait](prototype:T,length:Int):ArrayTrait[T]       = new Array(length,prototype)

  class Array[T <: SignalTrait](val length:Int,val prototype:T) extends ArrayTrait[T] {
      override def newSignal(name:String = this.name,
        opType:OpType = this.opType,
        fixed:FixedType = this.fixed):SignalTrait = new Array(this.length,prototype.newSignal(name))
  }
}