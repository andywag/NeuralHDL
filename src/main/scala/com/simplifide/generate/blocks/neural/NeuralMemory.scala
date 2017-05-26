package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct, NewMemory}
import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.NeuralMemory.Dimensions
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.project.NewEntityInstance
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  * Created by andy on 5/22/17.
  */
case class NeuralMemory(override val name:String,
                        dimensions:Dimensions
                       )
                       (implicit clk:ClockControl) extends ComplexSegment {


  override def createBody() {}

  val memoryStruct = MemoryStruct(appendName("int"),INPUT,Array(dimensions.memWidth),Array(1))
  val memoryBase = NewMemory(appendName("mem_base"),memoryStruct)
  val memoryBaseEnt = new SegmentEntity(memoryBase, memoryBase.name)


  val tapWidth  = Array(dimensions.memWidth,dimensions.neuronDepth)
  val tapDim    = Array(dimensions.tapDim._1,dimensions.tapDim._2/dimensions.neuronDepth)

  val tapStruct  = Seq.tabulate(2){x => MemoryStruct(s"tap_int_$x",INPUT,tapWidth,tapDim)}
  val biasStruct = Seq.tabulate(2){x => MemoryStruct(s"bias_int_$x",INPUT,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1))}
  val dataStruct = Seq.tabulate(2){x => MemoryStruct(s"data_int_$x",INPUT,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1,dimensions.dataDepth))}

  val tapStructW  = MemoryStruct("tap_int",WIRE,tapWidth,tapDim)
  val biasStructW = MemoryStruct("bias_int",WIRE,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1))
  val dataStructW = MemoryStruct("data_int",WIRE,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1,dimensions.dataDepth))


  val tapBank = MemoryBank(tapStruct(0))
  val biasBank = MemoryBank(biasStruct(0))
  val dataBank = MemoryBank(dataStruct(0))


  val tapEntity = new SegmentEntity(tapBank,"tapMem").createEntity
  instances.append(NewEntityInstance(tapEntity,"tapMem"))

  val biasEntity = new SegmentEntity(biasBank,"biasMem").createEntity
  instances.append(NewEntityInstance(biasEntity,"biasMem"))

  val dataEntity = new SegmentEntity(dataBank,"dataMem").createEntity
  instances.append(NewEntityInstance(dataEntity,"dataMem"))


  val select     = signal("select")
  val controller = new NeuralMemory.Controller("memControl",select,
    tapStructW,biasStructW,dataStructW, tapStruct,biasStruct,dataStruct)
  val controllerSegment = new SegmentEntity(controller,"control").createEntity
  instances.append(NewEntityInstance(controllerSegment,"control"))

  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals:List[SignalTrait] = List(tapStruct(0), biasStruct(0), dataStruct(0), tapStruct(0).wrData,
     dataStruct(0).wrData,  biasStruct(0).wrData)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(tapStruct(0).rdData,dataStruct(0).rdData,biasStruct(0).rdData)

  //instances.append(NewEntityInstance(entity,instanceName,con))


}

object NeuralMemory {
  import com.simplifide.generate.newparser.typ.SegmentParser._

  case class Dimensions(tapDim:(Int,Int), neuronDepth:Int, dataDepth:Int, memWidth:Int=32)

  case class Controller(override val name:String,
    select:SignalTrait,
    tapOut:MemoryStruct,biasOut:MemoryStruct,dataOut:MemoryStruct,
    tapIn:Seq[MemoryStruct], biasIn:Seq[MemoryStruct], dataIn:Seq[MemoryStruct]
  )(implicit clk:ClockControl) extends ComplexSegment{
    /** Defines the body in the block */
    override def createBody: Unit = {}

    override def inputs = {
      val clkSignals   = clk.allSignals(INPUT)
      val readSignals  = List(select,tapOut.rdData,biasOut.rdData,dataOut.rdData)
      val inputSignals      = tapIn.toList ::: biasIn.toList ::: dataIn.toList
      val inputWriteSignals = tapIn.map(_.wrData).toList ::: biasIn.map(_.wrData).toList ::: dataIn.map(_.wrData).toList
      clkSignals ::: readSignals ::: inputSignals ::: inputWriteSignals
    }


    override def outputs = {
      val rdSignals = tapIn.map(_.rdData).toList ::: biasIn.map(_.rdData).toList ::: dataIn.map(_.rdData).toList
      val wrSignals = List(tapOut.wrData,dataOut.wrData,biasOut.wrData)
      val outputSignals = List(tapOut,biasOut,dataOut)

      rdSignals ::: outputSignals ::: wrSignals

    }

    $always_star(
      $iff (select) $then (
        tapOut  !::= tapIn(0),
        dataOut !::= dataIn(0),
        biasOut !::= biasIn(0)
      ) $else (
        tapOut  !::= tapIn(1),
        dataOut !::= dataIn(1),
        biasOut !::= biasIn(1)
      )
    )

  }



}