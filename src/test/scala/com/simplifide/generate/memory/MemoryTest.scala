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

  val struct2  = new MemoryStruct("mem_int", Array(width,32), dims)

  val dutParser = MemoryBank("memory",struct2,Some(fileLocation))
  override val dut: NewEntity = dutParser.createEntity



  // FIXME : Need to support Integers with the data generation
  val typ = DataFileGenerator.Ramp(0,len)
  val data = DataFileGenerator.createData(Array(len,1),fileLocation,
    typ)

  dutParser.input.ctrl.rdAddress := index
  dutParser.input.ctrl.wrAddress := index
  dutParser.input.wrData         := index
  dutParser.input.ctrl.wrVld     := H(1)

  override def createBody = {

  }
}

object MemoryTest {
  /*
  class Dut(val name:String,
            dataLocation:String,
           width:Int,
           dims:Array[Int])(implicit val clk:ClockControl) extends EntityParser {

    signal(clk.allSignals(INPUT))

    val struct   = new MemoryStruct("mem_int",INPUT, Array(width), dims)
    val struct2  = signal(new MemoryStruct("mem_int",INPUT, Array(width,32), dims))
    signal(struct2.rdData)
    signal(struct2.wrData)



    val sig = ->(MemoryBank(struct2,Some(dataLocation)))

    //internalInstances.appendAll(sig.instances)
    signals.appendAll(sig.signals)

    override def document = sig.document

  }
  */
}
