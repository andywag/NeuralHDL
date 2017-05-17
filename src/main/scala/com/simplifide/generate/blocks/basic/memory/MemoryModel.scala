package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.blocks.basic.memory.Memory.MemoryBus
import com.simplifide.generate.signal.OpType


/**
 * MemoryModel used to help define the memory parameter
 *
 * @constructor
 * @parameter name Name of Memory
 * @parameter dataWidth Width of the Data Bus
 * @parameter depth Depth of the memory
 * @parameter delay Delay associated with reading from the memory
 *
 */
class MemoryModel(val name:String,
            val dataWidth:Int,
            val depth:Int,
            val delay:Int) {

  /** Width of the address port */
  val addressWidth = (math.log(depth)/math.log(2.0)).ceil.toInt
  /** Method to create a port for the memory */
  def port(name:String, index:Int,typ:OpType) = new MemoryBus(name + "_" + index,dataWidth,addressWidth,typ)
  /** Method to create a read port for the memory */
  def rdPort(name:String, index:Int, typ:OpType = OpType.RegOutput) = port("rd_" + name,index,typ)
  /** Method to create a write port for the memory */
  def wrPort(name:String, index:Int, typ:OpType = OpType.Input) = port("wr_" + name,index,typ)


}

object MemoryModel {

}