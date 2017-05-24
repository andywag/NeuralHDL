package com.simplifide.generate.memory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct, NewMemory}
import com.simplifide.generate.blocks.neural.Sigmoid
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.neural.SigmoidTest
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/22/17.
  */
class MemoryTest extends BlockScalaTest with BlockTestParser {
  def blockName: String = "memory"


  val fileLocation = s"$dataLocation/data"

  val dims = Array(16,8)
  val len  = dims.foldLeft(1)(_*_)
  val width = 32

  val dutParser = new MemoryTest.Dut(blockName,fileLocation,width,dims)
  override val dut: NewEntity = dutParser.createEntity



  // FIXME : Need to support Integers with the data generation
  val typ = DataFileGenerator.Ramp(0,len)
  val data = DataFileGenerator.createData(Array(len,1),fileLocation,
    typ)

  dutParser.struct.rdAddress := index
  dutParser.struct.wrAddress := index
  dutParser.struct.wrData    := index
  dutParser.struct.wrVld     := H(1)

  override def createBody = {

  }
}

object MemoryTest {
  class Dut(val name:String,
            dataLocation:String,
           width:Int,
           dims:Array[Int])(implicit val clk:ClockControl) extends EntityParser {

    signal(clk.allSignals(INPUT))

    val struct   = new MemoryStruct("mem_int",INPUT, Array(width), dims)
    val struct2  = signal(new MemoryStruct("mem_int",INPUT, Array(width,32), dims))
    signal(struct2.rdData)
    signal(struct2.wrData)


    val template = NewMemory("memory",struct)
    //val base = new SegmentEntity[NewMemory](template,s"${name}Base")


    val sig = ->(MemoryBank(struct2,Some(dataLocation)))

    //internalInstances.appendAll(sig.instances)
    signals.appendAll(sig.signals)

    override def document = sig.document

  }
}
