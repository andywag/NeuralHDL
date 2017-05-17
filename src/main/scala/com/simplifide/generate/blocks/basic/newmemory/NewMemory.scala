package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment

/**
  * Created by andy on 5/9/17.
  */
case class NewMemory(override val name:String,
                     val input:MemoryStruct)(implicit clk:ClockControl) extends ComplexSegment{
  /** Defines the body in the block */

  val memory     = signal(name + "_memory",REG,input.dataWidth,input.memoryDepth)
  val readDelay  = signal(name + "_read_address",REG, input.rdAddress.fixed)

  override def createBody: Unit = {
    readDelay := input.rdAddress $at(clk)
    memory(input.wrAddress) := $iff(input.wrVld) $then input.wrData $at(clk.withOutReset)
    input.rdData            := memory(readDelay) $at(clk)
  }
}
