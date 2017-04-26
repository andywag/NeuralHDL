package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.memory.Memory.MemoryBus
import com.simplifide.generate.signal.SignalTrait

/**
 * Behavioral model of a memory
 */

/*

class Behavioral(override val model:MemoryModel,
                 override val writeBuses:List[MemoryBus],
                 override val readBuses:List[MemoryBus])(implicit clk:ClockControl) extends ComplexSegment with Memory {

  /** Internal Memory */
  val memory = internal_array("memory",REG,U(model.dataWidth,0),model.depth)
  /** Complete List of Buses */
  val buses = writeBuses ::: readBuses

  def createBody {
    /- ("Write Muxes")
    writeBuses.foreach(x => assign(new WriteMux(x,memory)))
    /- ("Read Muxes")
    readBuses.foreach(x => assign(new ReadMux(x,memory,model.delay)))

  }

}

object Behavioral {

  def apply(model:MemoryModel,rdPorts:Int,wrPorts:Int)(implicit clk:ClockControl):Behavioral = {
    val reads  = List.tabulate(rdPorts)(x => model.rdPort(model.name,x))
    val writes = List.tabulate(rdPorts)(x => model.wrPort(model.name,x))
    Behavioral(model,writes,reads)
  }

  def apply(model:MemoryModel, rdPorts:List[MemoryBus], wrPorts:List[MemoryBus])(implicit clk:ClockControl):Behavioral = {
     new Behavioral(model,rdPorts,wrPorts)
  }

  class ReadMux(val bus:MemoryBus, val memory:SignalTrait, delay:Int)(implicit clk:ClockControl) extends ComplexSegment {
    // TODO Requires Proper Use of Delay
    def createBody {
      val clkEnable = clk.createEnable(bus.enable)
      //bus.data := memory(bus.address) @@ clkEnable
    }
  }

  class WriteMux(val bus:MemoryBus, val memory:SignalTrait)(implicit clk:ClockControl) extends ComplexSegment {
    def createBody {
      val clkEnable = clk.createEnable(bus.enable)
      $always_clk(clk) (
        $iff (bus.enable) $then (
         memory(bus.address) ::= bus.data
        )
      )
    }
  }


}
*/