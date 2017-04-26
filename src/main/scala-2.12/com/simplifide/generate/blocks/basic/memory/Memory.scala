package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.memory.Memory.MemoryBus
import com.simplifide.generate.proc.{Controls, ControlHolder}
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.proc.parser.ProcessorSegment
import old.BusType

/**
 * MemoryModel of a memory
 */

trait Memory extends ControlHolder {

  def apply(address:Int) = new Memory.BusHolder(this,address)
  def apply(address:Int,index:Int) = new Memory.BusHolder(this,address,index)

  val model:MemoryModel
  /** Write Buses for this memory */
  val writeBuses:List[MemoryBus]
  /** Read Buses for this memory */
  val readBuses:List[MemoryBus]
  /** Create an address bus for this memory */
  def createBus(name:String,opType:OpType) = new MemoryBus(name,model.dataWidth,model.addressWidth,opType)

  override lazy val controls = writeBuses.map(x => Controls(x.address)) :::
    writeBuses.map(x => Controls(x.enable)):::
    readBuses.map(x => Controls(x.address))

  def readDataSignal(index:Int):SignalTrait = readBuses(index).data
  def writeDataSignal(index:Int):SignalTrait = writeBuses(index).data


}

object Memory {


  class MemoryBus(name:String, val dataWidth:Int, val addressWidth:Int, val typ:OpType)
    extends BusDirect(name,typ) {

    private val inputType = if (typ.isSignal) OpType.Signal else OpType.Input
    private val dataType  = if (typ.isSignal) OpType.Signal else typ

    val address = SignalTrait(name + "_address",inputType,FixedType.unsigned(addressWidth,0))
    val enable  = SignalTrait(name + "_enable" ,inputType)
    val data    = SignalTrait(name + "_data"   ,dataType,FixedType.unsigned(dataWidth,0))

    override val signals = List(address,enable,data)

    override def newSignal(name:String = this.name,
      opType:OpType = this.opType,
      fixed:FixedType = this.fixed):SignalTrait = new MemoryBus(name,this.dataWidth,this.addressWidth,opType)
  }

  class MemoryBusType(val dataWidth:Int, val addressWidth:Int, val typ:OpType) extends BusType {

    val inputType = if (typ.isSignal) OpType.Signal else OpType.Input
    val dataType  = if (typ.isSignal) OpType.Signal else typ

    val address = SignalTrait("address",inputType,FixedType.unsigned(addressWidth,0))
    val enable  = SignalTrait("enable" ,inputType)
    val data    = SignalTrait("data"   ,dataType,FixedType.unsigned(dataWidth,0))

    override val signals = List(data,address,enable)
  }

  class BusHolder(val memory:Memory, val address:Int, val index:Int = 0) extends SimpleSegment.Combo with ProcessorSegment {

    val writeAddressControl =  memory.writeBuses(index).address
    val writeEnableControl  =  memory.writeBuses(index).enable

    val readAddressControl  =  memory.readBuses(index).address
    val readEnableControl   =  memory.readBuses(index).enable

    override lazy val controls:List[Controls] = List(writeAddressControl,writeEnableControl,readAddressControl,readEnableControl)

    override def createControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = {
      List( Controls.Value(readAddressControl,index,address),Controls.Value(readEnableControl,index,1))
    }

    override def createOutputControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = {
      List( Controls.Value(writeAddressControl,index,address),Controls.Value(writeEnableControl,index,1))
    }

  }

}