package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.blocks.test.FileLoad
import com.simplifide.generate.blocks.test.FileLoad.ReadMemH
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/9/17.
  */
case class NewMemory(override val name:String,
                  input:MemoryStruct,
                  file:Option[String]=None)(implicit clk:ClockControl) extends ComplexSegment{
  /** Defines the body in the block */

  val memory     = signal(name + "_memory",REG,input.dataWidth,input.memoryDepth)
  val readDelay  = signal(name + "_read_address",REG, input.rdAddress.fixed)

  override def createBody: Unit = {}

  /- ("Read Address Input Data")
  readDelay := input.rdAddress $at(clk)
  /- ("Write Data")
  memory(input.wrAddress) := $iff(input.wrVld) $then input.wrData $at(clk.withOutReset)
  /- ("Read Operation")
  input.rdData            := memory(readDelay) $at(clk)


  file.map(x => {
    /-("Optional Memory Load for Testing")
    $initial(new ReadMemH(memory, x))
  }
  )

  override def inputs: Seq[SignalTrait] = input :: input.wrData :: clk.allSignals(INPUT)
  override def outputs:List[SignalTrait] = List(input.rdData)
}

object NewMemory {




}
