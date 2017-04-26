package com.simplifide.generate.signal

import com.simplifide.generate.generator.SimpleSegment

/**
 * A group of signals defined by the busType
 */

class Bus[T <: BusType](override val name:String,val busType:T) extends SignalTrait {

  import busType._

  override val opType            = OpType.Signal
  override val fixed:FixedType   = FixedType.Simple


  override def changeTestType:SignalTrait = new Bus(this.name,busType.changeTestType)
  override def changeType(typ:OpType):SignalTrait = new Bus(this.name,busType.changeType(typ))
  override def reverseType:SignalTrait = new Bus(this.name,busType.reverseType)

  def newSignal(name:String = this.name,
    opType:OpType   = this.opType,
    fixed:FixedType = this.fixed):SignalTrait = new Bus(name,this.busType)

  override def numberOfChildren:Int = busType.length

  override def children:List[SignalTrait] = busType.createSignals(this.name)
  override def createSlice(index:Int,prefix:String=""):SignalTrait = new Bus(this.name + "_" + index,this.busType)

  override def child(index:Int):SignalTrait = this.children(index)
  override def slice(index:Int):SignalTrait   = this.children(index)


}

object Bus {

  def apply[T <: BusType](name:String, busType:T) = new Bus[T](name,busType)


}