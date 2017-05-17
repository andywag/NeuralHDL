package com.simplifide.generate.signal

import com.simplifide.generate.generator.SimpleSegment

/**
 * A group of signals
 */
class BusDirect(override val name:String,
  override val opType:OpType) extends SignalTrait {

  override val fixed:FixedType   = FixedType.Simple

  
  val signals = List[SignalTrait]()


  /** Convert the input data for the test bench */
  override def changeTestType:SignalTrait = new BusDirect.Impl(this.name,opType.testType,signals.map(_.changeTestType))
  /** Convert the input to the type typ */
  override def changeType(typ:OpType):SignalTrait = new BusDirect.Impl(this.name,typ,signals.map(_.changeType(typ)))
    /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  override def reverseType:SignalTrait = new BusDirect.Impl(this.name,this.opType,signals.map(_.reverseType))

  def newSignal(name:String=this.name,
    opType:OpType   = this.opType,
    fixed:FixedType = this.fixed):SignalTrait = new BusDirect.Impl(name,opType,signals)

  override def connect(inputSignal:SignalTrait):Map[SignalTrait,SignalTrait] = {
    if (inputSignal.isInstanceOf[BusDirect])
      (this.signals zip inputSignal.asInstanceOf[BusDirect].signals).map(x => (x._1,x._2)).toMap
    else
      Map(this.signals(0) -> inputSignal)

  }




  override def numberOfChildren:Int = signals.length // Number of Signals is the length of the list of signals
  override def children:List[SignalTrait] = signals  // Signals are the total amount of children
  override def allChildren:List[SimpleSegment] = signals.flatMap(_.allChildren)

  override def createSlice(index:Int,prefix:String=""):SignalTrait = new BusDirect(this.name + "_" + index,this.opType)

  override def child(index:Int):SignalTrait = this.children(index)
  override def slice(index:Int):SignalTrait   = this.children(index)


}

object BusDirect {

  def apply(name:String, opType:OpType, signals:List[SignalTrait] = List()) = new BusDirect.Impl(name,opType,signals)

  class Impl(name:String,opType:OpType,override val signals:List[SignalTrait]) extends BusDirect(name,opType) {


  }


}