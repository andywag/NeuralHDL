package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.signal.sv.Struct
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/9/17.
  */
case class MemoryStruct(override val name:String, override val opType:OpType, memoryRepeat:Array[Int],
                        dimensions:Array[Int]) extends Struct {

  val memoryWidth = memoryRepeat.foldLeft(1)(_*_)
  val memoryDepth  = dimensions.foldLeft(1)(_*_)
  val addressSize  = math.ceil(math.log(memoryDepth)/math.log(2.0)).toInt

  val dataWidth    = FixedType.unsigned(memoryWidth,0)
  val addressWidth = FixedType.unsigned(addressSize,0)

  val wrVld      = SignalTrait(createName("wr_vld")    ,opType ,FixedType.unsigned(1,0))
  val wrAddress  = SignalTrait(createName("wr_address"),opType ,addressWidth)
  val rdAddress  = SignalTrait(createName("rd_address"),opType ,addressWidth)
  val rdVld      = SignalTrait(createName("rd_vld")    ,opType ,FixedType.unsigned(1,0))

  val wrData     = SignalTrait(appendName("wr_data")   ,opType ,dataWidth)
  val rdData     = SignalTrait(appendName("rd_data")   ,opType.reverseType ,dataWidth)


  override val packed: Boolean = true
  override val typeName: String = s"${name}_${memoryWidth}_${memoryDepth}"
  override val signals: List[SignalTrait] = List(rdAddress, wrVld, wrAddress, rdVld)

  /** Kind of a kludge need a better way to copy objects (shapeless maybe) */
  override def copyStruct(n: String, o: OpType): SignalTrait =
    new MemoryStruct(n,o,memoryRepeat,dimensions)

  /** Creates a New Signal (Virtual Constructor) */
  override def newSignal(name: String, o: OpType, fix: FixedType): SignalTrait =
    new MemoryStruct(name,o,memoryRepeat,dimensions)
}
