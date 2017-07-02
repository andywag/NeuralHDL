package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct, NewMemory}
import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.NeuralMemory.{Dimensions, Internal}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntityInstance
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.sv.SignalInterface
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  * Created by andy on 5/22/17.
  */
case class NeuralMemory(override val name:String,
                        dimensions:Dimensions,
                        info:NeuralStageInfo
                       )
                       (implicit clk:ClockControl) extends EntityParser {


  override def createBody() {}

  //signal(clk.allSignals(INPUT))
  //signal()

  val memoryStruct = MemoryStruct(appendName("int"),Array(dimensions.memWidth),Array(1))
  val memoryBase = NewMemory(appendName("mem_base"),memoryStruct)
  val memoryBaseEnt = new SegmentEntity(memoryBase, memoryBase.name)


  val tapWidth  = Array(dimensions.memWidth,dimensions.neuronDepth)
  val tapDim    = Array(dimensions.tapDim._1,dimensions.tapDim._2/dimensions.neuronDepth)

  val tapStructW  = MemoryStruct("tap_int",tapWidth,tapDim)
  val biasStructW = MemoryStruct("bias_int",Array(dimensions.memWidth,1),Array(dimensions.tapDim._2))
  val dataStructW = MemoryStruct("data_int",Array(dimensions.memWidth,1),Array(dimensions.tapDim._1,dimensions.dataDepth))

  val tapBank = MemoryBank(appendName("tap"),tapStructW, info.tapLocation)
  val biasBank = MemoryBank(appendName("bias"),biasStructW)
  val dataBank = MemoryBank(appendName("data"),dataStructW)

  instance(tapBank)
  instance(biasBank)
  instance(dataBank)





}

object NeuralMemory {
  import com.simplifide.generate.newparser.typ.SegmentParser._

  case class Dimensions(tapDim:(Int,Int), neuronDepth:Int, dataDepth:Int, memWidth:Int=32)
  case class Internal(val name:String,
                      tap:MemoryStruct,
                      bias:MemoryStruct,
                      data:MemoryStruct) extends SignalInterface {
    override val interfaces = List(tap,bias,data)
  }

  /*
  case class Controller(override val name:String,
                        dimensions: Dimensions,
                        internal:Internal
                       )(implicit clk:ClockControl) extends EntityParser {
    /** Defines the body in the block */
    override def createBody: Unit = {}

    val tapWidth  = Array(dimensions.memWidth,dimensions.neuronDepth)
    val tapDim    = Array(dimensions.tapDim._1,dimensions.tapDim._2/dimensions.neuronDepth)


    signal(clk.allSignals(INPUT))
    val select     = signal("select",INPUT)
    signal(internal.reverse)



    val tapIn  = Seq.tabulate(2){x => MemoryStruct(s"tap_int_$x",tapWidth,tapDim)}
    val biasIn = Seq.tabulate(2){x => MemoryStruct(s"bias_int_$x",Array(dimensions.memWidth,1),Array(dimensions.tapDim._1))}
    val dataIn = Seq.tabulate(2){x => MemoryStruct(s"data_int_$x",Array(dimensions.memWidth,1),Array(dimensions.tapDim._1,dimensions.dataDepth))}

    signal(tapIn.flatMap(_.signals).toList);
    signal(biasIn.flatMap(_.signals).toList);
    signal(dataIn.flatMap(_.signals).toList);

    $always_star(
      $iff (select) $then (
        internal.tap  !::= tapIn(0),
        internal.data !::= dataIn(0),
        internal.bias !::= biasIn(0)
      ) $else (
        internal.tap  !::= tapIn(1),
        internal.data !::= dataIn(1),
        internal.bias !::= biasIn(1)
      )
    )

  }

*/

}