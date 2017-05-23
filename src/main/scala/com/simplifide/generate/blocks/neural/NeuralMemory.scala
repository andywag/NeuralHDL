package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct
import com.simplifide.generate.generator.ComplexSegment

/**
  * Created by andy on 5/22/17.
  */
case class NeuralMemory(input:MemoryStruct, shape:Array[Int])(implicit clk:ClockControl) extends ComplexSegment {


  val memory     = signal(name + "_memory",REG,input.dataWidth,input.memoryDepth)
  val readDelay  = signal(name + "_read_address",REG, input.rdAddress.fixed)

  override def createBody: Unit = {
    readDelay := input.rdAddress $at(clk)
    memory(input.wrAddress) := $iff(input.wrVld) $then input.wrData $at(clk.withOutReset)
    input.rdData            := memory(readDelay) $at(clk)
  }

}
