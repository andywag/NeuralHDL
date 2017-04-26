package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.memory.Memory.MemoryBus
import com.simplifide.generate.signal.{ArrayTrait, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/14/11
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */

/*
class RegisterFile(override val model:MemoryModel,
                   override val writeBuses:List[MemoryBus],
                   override val readBuses:List[MemoryBus])(implicit clk:ClockControl) extends ComplexSegment with Memory {

  val memory:ArrayTrait[SignalTrait] = array("memory",REG,U(model.dataWidth,0))(model.depth)


  def createBody {
    /- ("Write Muxes")
    assign(new WriteMux(writeBuses,memory))
    /- ("Read Muxes")
    readBuses.foreach(x => assign(new ReadMux(x,memory,model.delay)))

  }

}

object RegisterFile {

  def apply(model:MemoryModel,rdPorts:Int,wrPorts:Int)(implicit clk:ClockControl):RegisterFile = {
    val reads  = List.tabulate(rdPorts)(x => model.rdPort(model.name,x))
    val writes = List.tabulate(rdPorts)(x => model.wrPort(model.name,x))
    new RegisterFile(model,writes,reads)
  }

  class ReadMux(val bus:MemoryBus, val memory:ArrayTrait[SignalTrait], delay:Int)(implicit clk:ClockControl) extends ComplexSegment {
    // TODO Requires Proper Use of Delay. Currently zero delay
    def createBody {
      val clkEnable = clk.createEnable(bus.enable)
      /*$always_star(
        $caseL(bus.address) (
         memory.children.zipWithIndex.map(x => C(bus.addressWidth,x._2) ~> (bus.data ::= memory(x._2)) )
        )
      )*/
      
      bus.data := bus.address $match (
        memory.children.zipWithIndex.map(x => $cases(C(bus.addressWidth,x._2)) $then (memory(x._2)))
      )
      
     }
  }

  class WriteMux(val bus:List[MemoryBus], val memory:SignalTrait)(implicit clk:ClockControl) extends ComplexSegment {
    def createBody {
      val clkEnable = clk.createEnable(bus(0).enable)

      // Not Done a bit complicated
      /*
      bus.address $match (
        memory.children.zipWithIndex.map(x => $cases(C(x._1.addressWidth,x._2),memory(x._2) := bus.))
      ) $at(clkEnable)
      */
    }
  }
}
*/