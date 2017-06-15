package com.simplifide.generate.blocks.basic.flop

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import scala.collection.mutable.ListBuffer
import com.simplifide.generate.generator.{CodeWriter, SimpleSegment}
import com.simplifide.generate.signal._
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.blocks.basic.flop.ClockControl.Impl
import com.simplifide.generate.parser.factory.CreationFactory.Hardware

/**
 * Class which defines how a register will operate.
 *
 * @constructor
 * @parameter name Name of Clock Control
 * @parameter clock Clock
 * @parameter reset Optional Clock Reset
 * @parameter enable Optional Clock Enable
 * @parameter index Optional Index for the Clock - Used for Sharing
 */
trait ClockControl extends SimpleSegment with com.simplifide.generate.parser.model.Clock{


  val clock:Clocks.Clock
  val reset:Option[Clocks.Reset]   = None
  val enable:Option[Clocks.Enable] = None
  val index:Option[Clocks.Index]   = None
  val period:Int                   = 10



  override val delay = 0
  override def createCode(implicit writer:CodeWriter) = null

  def copy(clock:Clocks.Clock = this.clock,
    reset:Option[Clocks.Reset] = this.reset,
    enable:Option[Clocks.Enable] = this.enable,
    index:Option[Clocks.Index]   = this.index,
    period:Int                   = this.period) = new ClockControl.Impl(clock,reset,enable,index,period)
  
  
  def withOutReset = this.copy(reset = None)
  /** Create a new version of the clock control with an enable */
  def createEnable(enable:Expression)(implicit creator:CreationFactory=Hardware) = this.copy(enable = Some(new Clocks.Enable(enable.create)))
  def createEnable(enable:SimpleSegment) = this.copy(enable = Some(new Clocks.Enable(enable)))


  /** Returns the appendSignal associated with the clock */
  def clockSignal(optype:OpType = OpType.Input):SignalTrait = clock.signal.copy(optype = optype)

  /** Returns the appendSignal associated with the reset */
  def resetSignal(optype:OpType = OpType.Input):Option[SignalTrait] = reset.map(_.signal).map(x => x.copy(optype = optype))

  /** Returns the appendSignal associated with the reset */
  def enableSignal(optype:OpType = OpType.Input):Option[SignalTrait] =
    if (enable != None && enable.get.signal.isInstanceOf[SignalTrait]) Some(enable.get.signal.asInstanceOf[SignalTrait].copy(optype = optype)) else None


  /** All signals included with this class */
  def allSignals(optype:OpType):List[SignalTrait] = {
    val buffer = new ListBuffer[SignalTrait]()
    buffer.append(clockSignal(optype))
    resetSignal(optype).map(x => buffer.append(x))
    enableSignal(optype).map(x => buffer.append(x))
    return buffer.toList
    
  }

  def input = allSignals(OpType.Input)

  /** Returns the sensitivity list for this */
  def createSensitivityList():List[SimpleSegment] =
    clock.sensitivityList() ::: (if (reset.isDefined) reset.get.sensitivityList else List())

}

object ClockControl {

  class CLOCK_TYPE 
  object POSEDGE_SYNC  extends CLOCK_TYPE
  object NEGEDGE_SYNC  extends CLOCK_TYPE
  object POSEDGE_ASYNC extends CLOCK_TYPE
  object NEGEDGE_ASYNC extends CLOCK_TYPE


  def create(clk:SignalTrait,
    reset:Option[SignalTrait] = None,
    enable:Option[SignalTrait] = None,
    typ:CLOCK_TYPE = POSEDGE_SYNC,
    period:Int = 10)  = {

    val clockT = typ match {
      case POSEDGE_SYNC | POSEDGE_ASYNC => new Clocks.Clock(clk,true)
      case _                            => new Clocks.Clock(clk,false)
    }
    
    val resetT = typ match {
      case POSEDGE_SYNC | NEGEDGE_SYNC  => reset.map(new Clocks.Reset(_,false,true))
      case _                            => reset.map(new Clocks.Reset(_,true,false))
    }
    
    val enableT = enable.map(new Clocks.Enable(_))
    
    new Impl(clockT,resetT,enableT,None,period)
    
  }
  
  class Impl(override val clock:Clocks.Clock,
    override val reset:Option[Clocks.Reset]   = None,
    override val enable:Option[Clocks.Enable] = None,
    override val index:Option[Clocks.Index]   = None,
    override val period:Int                   = 10) extends ClockControl


  /** @deprecated */
  def apply(clock:String = "clk",
    reset:String = "reset",
    enable:String = "",
    posedge:Boolean = true,
    reset_sync:Boolean = false,
    period:Int = 10):ClockControl = {

    new ClockControl.Impl(new Clocks.Clock(SignalTrait(clock),posedge),
      if (reset.equalsIgnoreCase(""))  None else Some(new Clocks.Reset(SignalTrait(reset),reset_sync, reset_sync)),
      if (enable.equalsIgnoreCase("")) None else Some(new Clocks.Enable(SignalTrait(enable))),
      None,
      period
    )
  }



}
