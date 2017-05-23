package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.test.FileLoad.{ReadMemH, ReadMemHName}
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.project.{Connection, NewEntityInstance}
import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/22/17.
  */
case class MemoryBank(struct:SegmentEntity[NewMemory],
                      input:MemoryStruct,
                      file:Option[String]=None)(implicit clk:ClockControl) extends ComplexSegment{

  override def createBody() {}
  //override val instances = struct.createEntity
  // FIXME : Currently only supports single dimension of Repeat
  val width = input.memoryRepeat(0)
  val number = input.memoryRepeat(1)

  override val name = struct.name

  val entity = struct.createEntity
  val insts    = for (i <- 0 until number) {
    // Signals
    val rd = signal(appendName(s"read_$i"),SIGNAL,U(width,0))
    val wr = signal(appendName(s"write_$i"),SIGNAL,U(width,0))
    // Slice values for signal
    val slice = (width*(i+1)-1,width*i)
    // Attaching signals to slices
    wr := input.wrData(slice)
    input.rdData(slice) := rd
    // Connection Creation
    val connection:Map[SignalTrait,SimpleSegment] = Map(struct.segment.input.rdData -> rd,
      struct.segment.input.wrData -> wr)
    val con = Connection.MapSignalConnection.name(connection)
    val instanceName = s"${struct.name}_${i}"
    // Attaching Instance
    instances.append(NewEntityInstance(entity,instanceName,con))

    file.map(x => {
      /-(s"Optional Memory Load for Memory $x")
      $initial(new ReadMemHName(s"${instanceName}.${struct.segment.memory.name}", s"$x.hex"))
    })

  }


  override def inputs: Seq[SignalTrait] = input :: clk.allSignals(INPUT)
  override def outputs:List[SignalTrait] = List(input.rdData)


}
