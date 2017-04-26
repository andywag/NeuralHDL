package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.project.NewEntityInstance._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{NewEntityInstance, ModuleProvider}
import com.simplifide.generate.proc.Controls._
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}


/*
class MemoryEntity(val memory:Memory with SimpleSegment)(implicit clk:ClockControl) extends Entity.Leaf(memory.model.name) {

  def apply(address:Int) = memory(address)
  def apply(address:Int,index:Int) = memory(address,index)

  override val entitySignals =  clk.allSignals(INPUT) ::: memory.writeBuses ::: memory.readBuses
  //override lazy val controls = memory.controls

  this.assign(memory)


}

object MemoryEntity {

}
*/