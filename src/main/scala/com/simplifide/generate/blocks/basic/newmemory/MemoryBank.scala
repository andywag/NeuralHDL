package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Counter
import com.simplifide.generate.blocks.test.FileLoad.{ReadMemH, ReadMemHName}
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{Connection, NewEntityInstance}
import com.simplifide.generate.signal.{Constant, FixedType, SignalTrait}

/**
  * Created by andy on 5/22/17.
  */
case class MemoryBank(name:String,
                      input:MemoryStruct,
                      file:Option[String]=None)(implicit clk:ClockControl) extends EntityParser{

  import com.simplifide.generate.newparser.typ.SegmentParser._


  def createInter() = {
    val interCount0     = signal("inter_count_0",REG,input.ctrl.rdAddress.fixed)
    val interCount1     = signal("inter_count_1",REG,input.ctrl.rdAddress.fixed)
    val base            = signal("inter_base",REG,input.ctrl.rdAddress.fixed)

    val res = input.ctrl.interFirst | (interCount0 === number -1)
    ->(Counter.Simple(interCount0,Some(res),Some(input.ctrl.inter)))
    ->(Counter.Increment(interCount1,number,Some(input.ctrl.interFirst),Some(res)))

    val rdOffset = seq(signal(s"rd_offset", WIRE, input.ctrl.rdAddress.fixed))(number)

    val insts    = for (i <- 0 until number) {
      val rd_offset1 = signal(s"rd_offset1_$i", WIRE, input.ctrl.rdAddress.fixed)
      val rd_offset0 = signal(s"rd_offset0_$i", WIRE, input.ctrl.rdAddress.fixed)
      //val rd_offset = signal(s"rd_offset_$i", WIRE, input.ctrl.rdAddress.fixed)

      rd_offset1  := interCount0 + i
      rd_offset0  := (rd_offset1 < number) ? rd_offset1 :: rd_offset1 - number
      rdOffset(i)   := rd_offset0 + interCount1
    }
    rdOffset
  }

  override def createBody() {}
  //override val instances = struct.createEntity

  signal(clk.allSignals(INPUT))
  signal(input.signals)

  // FIXME : Currently only supports single dimension of Repeat
  val width = input.memoryRepeat(0)
  val number = input.memoryRepeat(1)

  val entityParser = input.createEntity
  val entity = entityParser.createEntity

  val writeArray = signal("write_sub",WIRE,FixedType.unsigned(number,1))

  val memCtrl1     = List.tabulate(number)(x => signal(input.ctrl.copyStruct(s"mem_int_$x",WIRE)))
  val memCtrl      = memCtrl1.map(_.asInstanceOf[MemoryStruct.Ctrl])

  // Create the interleaving control for the tap memory.
  // Should not be included in this block but is here for convenience now
  val rdOffset = if (number > 1) createInter() else Seq()

  val insts    = for (i <- 0 until number) {
    // Signals
    val rd = signal(s"read_$i",SIGNAL,U(width,0))
    val wr = signal(s"write_$i",SIGNAL,U(width,0))

    // Slice values for signal
    val slice = (width*(i+1)-1,width*i)
    if (number > 1) {
      //val rd_offset1 = signal(s"rd_offset1_$i", WIRE, input.ctrl.rdAddress.fixed)
      //val rd_offset = signal(s"rd_offset_$i", WIRE, input.ctrl.rdAddress.fixed)
      //rd_offset1 := input.ctrl.rdAddress + i
      //rd_offset  := (rd_offset1 < number) ? rd_offset1 :: rd_offset1 - number
      writeArray((i,i)) := (input.ctrl.subAddress === i) & input.ctrl.subVld
      wr := writeArray((i,i)) ? input.ctrl.subData :: input.wrData(slice) // Select either the single line or the array
      memCtrl(i).rdAddress := (input.ctrl.inter & ~ input.ctrl.interFirst) ? rdOffset(i) :: input.ctrl.rdAddress
      memCtrl(i).rdVld     := input.ctrl.rdVld
      memCtrl(i).wrAddress := input.ctrl.wrAddress
      memCtrl(i).wrVld     := writeArray((i,i)) | (input.ctrl.wrVld & ~input.ctrl.subVld)
    }
    else  {
      wr := input.wrData(slice)
      memCtrl(i) !:= input.ctrl
    }
    // Attaching signals to slices



    input.rdData(slice) := rd
    // Connection Creation
    val connection:Map[SignalTrait,SimpleSegment] = Map(
      entityParser.segment.input.ctrl   -> memCtrl(i),
      entityParser.segment.input.rdData -> rd,
      entityParser.segment.input.wrData -> wr)

    val con = Connection.MapSignalConnection.name(connection)
    val instanceName = s"${name}_${i}"
    // Attaching Instance
    instance(entity,instanceName,con)
    //instances.append(NewEntityInstance(entity,instanceName,con))

    file.map(x => {
      /-(s"Optional Memory Load for Memory $x")
      $initial(new ReadMemHName(s"${instanceName}.${entityParser.segment.memory.name}", s"${x}_${i}.hex"))
    })

  }


}
