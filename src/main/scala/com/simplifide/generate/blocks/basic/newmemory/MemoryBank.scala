package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.test.FileLoad.{ReadMemH, ReadMemHName}
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{Connection, NewEntityInstance}
import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/22/17.
  */
case class MemoryBank(name:String,
                      input:MemoryStruct,
                      file:Option[String]=None)(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}
  //override val instances = struct.createEntity

  signal(clk.allSignals(INPUT))
  signal(input.signals)

  // FIXME : Currently only supports single dimension of Repeat
  val width = input.memoryRepeat(0)
  val number = input.memoryRepeat(1)


  val entityParser = input.createEntity
  val entity = entityParser.createEntity

  val insts    = for (i <- 0 until number) {
    // Signals
    val rd = signal(s"read_$i",SIGNAL,U(width,0))
    val wr = signal(s"write_$i",SIGNAL,U(width,0))
    // Slice values for signal
    val slice = (width*(i+1)-1,width*i)
    // Attaching signals to slices
    wr := input.wrData(slice)
    input.rdData(slice) := rd
    // Connection Creation
    val connection:Map[SignalTrait,SimpleSegment] = Map(
      entityParser.segment.input.ctrl   -> input.ctrl,
      entityParser.segment.input.rdData -> rd,
      entityParser.segment.input.wrData -> wr)

    val con = Connection.MapSignalConnection.name(connection)
    val instanceName = s"${name}_${i}"
    // Attaching Instance
    instance(entity,instanceName,con)
    //instances.append(NewEntityInstance(entity,instanceName,con))

    file.map(x => {
      /-(s"Optional Memory Load for Memory $x")
      $initial(new ReadMemHName(s"${instanceName}.${entityParser.segment.memory.name}", s"$x.hex"))
    })

  }


}
