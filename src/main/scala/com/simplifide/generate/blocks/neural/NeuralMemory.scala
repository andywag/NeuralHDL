package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct, NewMemory}
import com.simplifide.generate.blocks.neural.NeuralMemory.Dimensions
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.project.NewEntityInstance
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  * Created by andy on 5/22/17.
  */
case class NeuralMemory(override val name:String,
                        dimensions:Dimensions)
                       (implicit clk:ClockControl) extends ComplexSegment {


  override def createBody() {}

  val memoryStruct = MemoryStruct(appendName("int"),INPUT,Array(dimensions.memWidth),Array(1))
  val memoryBase = NewMemory(appendName("mem_base"),memoryStruct)
  val memoryBaseEnt = new SegmentEntity(memoryBase, memoryBase.name)


  val tapWidth  = Array(dimensions.memWidth,dimensions.neuronDepth)
  val tapDim    = Array(dimensions.tapDim._1,dimensions.tapDim._2/dimensions.neuronDepth)

  val tapStruct = (MemoryStruct("tap_int",INPUT,tapWidth,tapDim))
  val tapBank = MemoryBank(tapStruct)

  val biasStruct = (MemoryStruct("bias_int",INPUT,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1)))
  val biasBank = MemoryBank(biasStruct)

  val dataStruct = (MemoryStruct("data_int",INPUT,Array(dimensions.memWidth,1),Array(dimensions.tapDim._1,dimensions.dataDepth)))
  val dataBank = MemoryBank(dataStruct)


  val tapEntity = new SegmentEntity(tapBank,"tapMem").createEntity
  instances.append(NewEntityInstance(tapEntity,"tapMem"))

  val biasEntity = new SegmentEntity(biasBank,"biasMem").createEntity
  instances.append(NewEntityInstance(biasEntity,"biasMem"))

  val dataEntity = new SegmentEntity(dataBank,"dataMem").createEntity
  instances.append(NewEntityInstance(dataEntity,"dataMem"))


  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals = List(tapStruct, biasStruct, dataStruct, tapStruct.wrData,
     dataStruct.wrData,  biasStruct.wrData)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(tapStruct.rdData,dataStruct.rdData,biasStruct.rdData)

  //instances.append(NewEntityInstance(entity,instanceName,con))


}

object NeuralMemory {
  case class Dimensions(tapDim:(Int,Int), neuronDepth:Int, dataDepth:Int, memWidth:Int=32)


}