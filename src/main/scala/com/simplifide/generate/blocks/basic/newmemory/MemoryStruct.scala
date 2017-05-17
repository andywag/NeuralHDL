package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.signal.sv.Struct
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/9/17.
  */
case class MemoryStruct(override val name:String, memoryWidth:Int, memoryDepth:Int) extends Struct {


  val addressSize = math.ceil(math.log(memoryDepth)/math.log(2.0)).toInt

  val dataWidth    = FixedType.unsigned(memoryWidth,0)
  val addressWidth = FixedType.unsigned(addressSize,0)

  val wrVld      = SignalTrait(createName("wr_vld")    ,OpType.Signal ,FixedType.unsigned(1,0))
  val wrAddress  = SignalTrait(createName("wr_address"),OpType.Signal ,addressWidth)
  val wrData     = SignalTrait(createName("wr_data")   ,OpType.Signal ,dataWidth)
  val rdAddress  = SignalTrait(createName("rd_address"),OpType.Signal ,addressWidth)
  val rdData     = SignalTrait(createName("rd_data")   ,OpType.Signal ,dataWidth)


  override val packed: Boolean = true
  override val typeName: String = s"${name}_${memoryWidth}_${memoryDepth}"
  override val signals: List[SignalTrait] = List(rdAddress, rdData, wrAddress, wrData)

  /** Kind of a kludge need a better way to copy objects (shapeless maybe) */
  override def copyStruct(n: String, o: OpType): SignalTrait =
    new MemoryStruct(n,memoryWidth, memoryDepth)

  /** Creates a New Signal (Virtual Constructor) */
  override def newSignal(name: String, opType: OpType, fix: FixedType): SignalTrait =
    new MemoryStruct(name,memoryWidth, memoryDepth)
}
