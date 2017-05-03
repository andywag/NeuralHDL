package com.simplifide.generate.signal

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.signal
import com.simplifide.generate.signal.sv.Struct

/**
  * Created by andy on 4/27/17.
  */
trait FloatSignal extends Struct {


  val signalWidth:Int = FloatSignal.SIGNAL_WIDTH
  val expWidth:Int    = FloatSignal.EXP_WIDTH

  val typeName = s"float_${signalWidth+1}_${expWidth}"
  val packed   = true

  val sgn  = SignalTrait(createName("sgn"),OpType.Signal,FixedType.unsigned(1,0))
  val exp  = SignalTrait(createName("exp"),OpType.Signal,FixedType.unsigned(expWidth,0))
  val man  = SignalTrait(createName("man"),OpType.Signal,FixedType.signed(signalWidth,0))
  val signals = List(sgn, man, exp)

  /*
  override def slice(index:Int):SimpleSegment  =
    signals(index)


  override def child(index:Int):SimpleSegment = slice(index)
  override def numberOfChildren:Int = signals.length // Number of Signals is the length of the list of signals
  override def children:List[SignalTrait] = signals  // Signals are the total amount of children
  override def allChildren:List[SimpleSegment] = signals.flatMap(_.allChildren)
*/
  /** Creates a New Signal (Virtual Constructor) */
  override def newSignal(name: String, opType: OpType, fix: FixedType): SignalTrait =
    new FloatSignal.Impl(name,opType,this.signalWidth,this.expWidth)

  def copyStruct(n:String, o:OpType) =
    new FloatSignal.Impl(n,o,this.signalWidth,this.expWidth)


}

object FloatSignal {

  val SIGNAL_WIDTH = 23
  val EXP_WIDTH    = 8

  def apply(name:String, opType:OpType, signalWidth:Int = SIGNAL_WIDTH, expWidth:Int = EXP_WIDTH) =
    new Impl(name,opType,signalWidth,expWidth)
  class Impl(override val name:String,
             override val opType:OpType,
             override val signalWidth:Int,
             override val expWidth:Int) extends FloatSignal
}
