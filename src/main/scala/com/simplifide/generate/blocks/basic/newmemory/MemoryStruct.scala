package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory
import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct.Ctrl
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.items.ConstantParser
import com.simplifide.generate.signal.sv.{SignalInterface, Struct}
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/9/17.
  */
case class MemoryStruct(val name:String,
                        memoryRepeat:Array[Int],
                        dimensions:Array[Int]) extends SignalInterface  {

  val opType = OpType.Input

  val memoryWidth = memoryRepeat.foldLeft(1)(_*_)
  val memoryDepth1  = dimensions.foldLeft(1)(_*_)
  val addressSize  = math.ceil(math.log(memoryDepth1)/math.log(2.0)).toInt
  val memoryDepth  = math.pow(2.0,addressSize).toInt

  val dataWidth    = FixedType.unsigned(memoryWidth,0)
  val addressWidth = FixedType.unsigned(addressSize,0)

  val wrData     = SignalTrait(appendName("wr_data")   ,opType ,dataWidth)
  val rdData     = SignalTrait(appendName("rd_data")   ,opType.reverseType ,dataWidth)

  val ctrl = Ctrl(name,memoryRepeat,dimensions)
  override val inputs  = List(ctrl,wrData)
  override val outputs = List(rdData)



  def createEntity(implicit clk:ClockControl):SegmentEntity[NewMemory] = {
    val name = s"memory_${this.memoryRepeat(0)}_${addressSize}"
    val struct = MemoryStruct("m",Array(this.memoryRepeat(0)),Array(this.memoryDepth))
    new SegmentEntity(NewMemory(name,struct),name)
  }


}

object MemoryStruct {
  case class Ctrl(override val name:String, memoryRepeat:Array[Int],
                  dimensions:Array[Int], override val opType:OpType = OpType.Input) extends Struct {


    val memoryWidth = memoryRepeat.foldLeft(1)(_*_)
    val memoryDepth  = dimensions.foldLeft(1)(_*_)
    val addressSize     = math.ceil(math.log(memoryDepth)/math.log(2.0)).toInt


    val dataWidth    = FixedType.unsigned(memoryWidth,0)
    val addressWidth = FixedType.unsigned(addressSize,0)


    val wrVld      = SignalTrait(createName("wr_vld")    ,opType ,FixedType.unsigned(1,0))
    val wrAddress  = SignalTrait(createName("wr_address"),opType ,addressWidth)
    val rdAddress  = SignalTrait(createName("rd_address"),opType ,addressWidth)
    val rdVld      = SignalTrait(createName("rd_vld")    ,opType ,FixedType.unsigned(1,0))


    val subVld     = SignalTrait(createName("sub_vld"),opType)

    val addressSubSize  = if (memoryRepeat.length > 1) math.ceil(math.log(memoryRepeat(1)/math.log(2.0))).toInt else 1
    val subAddress = SignalTrait(createName("sub_addr"),opType,FixedType.unsigned(addressSubSize,0))
    val subData    = SignalTrait(createName("sub_data"),opType,FixedType.unsigned(memoryRepeat(0),0))


    //override val signals: List[SignalTrait] = if (memoryDepth <= 1) List(rdAddress, wrVld, wrAddress, rdVld)
    //else List(rdAddress, wrVld, wrAddress, rdVld, subVld, subAddress, subData)

    override val signals: List[SignalTrait] =List(subVld, subAddress, subData, rdAddress, wrVld, wrAddress, rdVld)

    override val packed: Boolean = true
    override val typeName: String = s"${name}_${memoryWidth}_${addressSize}"

    //override val signals: List[SignalTrait] = List(rdAddress, wrVld, wrAddress, rdVld)


    /** Kind of a kludge need a better way to copy objects (shapeless maybe) */
    override def copyStruct(n: String, o: OpType): SignalTrait = {
      new newmemory.MemoryStruct.Ctrl(n,memoryRepeat,dimensions,o)
    }

    /** Creates a New Signal (Virtual Constructor) */
    override def newSignal(name: String, opType: OpType, fix: FixedType): SignalTrait = {
      new newmemory.MemoryStruct.Ctrl(name,memoryRepeat,dimensions,opType)
    }
  }
}
