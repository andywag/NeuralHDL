package com.simplifide.generate.blocks.basic.newmemory

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory
import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct.Ctrl
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.model.NdDataSet
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.parser.items.ConstantParser
import com.simplifide.generate.signal
import com.simplifide.generate.signal.sv.{SignalInterface, Struct}
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}
import com.simplifide.generate.test2.data.DataGenParser.DisplayDump2

/**
  * Created by andy on 5/9/17.
  */

case class MemoryStruct(val name:String,
                        memoryRepeat:Array[Int],
                        dimensions:Array[Int],
                        packed:Boolean = false) extends SignalInterface  {

  val opType = OpType.Input

  // FIXME : The memory size has issues with the current non-packet usage
  //         Need to set a different option when the memory is packed
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

  /** Should move to a dump type class */
  def ---->(name:String,path:Option[String]=None)(implicit clk:ClockControl,scope:EntityParser) = {
    val dataSet = NdDataSet.empty(name)
    val newVld = path.map(x => new SignalTrait.SignalPath(x,ctrl.wrVld)).getOrElse(ctrl.wrVld)
    val newClk = clk.createEnable(newVld)
    val newSignal = path.map(x => new SignalTrait.SignalPath(x,wrData)).getOrElse(wrData)
    scope->(DisplayDump2(s"${name}.hex",newSignal)(newClk))
    dataSet
  }

  def sread(name:String,path:Option[String]=None)(implicit clk:ClockControl,scope:EntityParser) = {
    val dataSet = NdDataSet.empty(name)
    val newVld = path.map(x => new SignalTrait.SignalPath(x,ctrl.rdVld)).getOrElse(ctrl.rdVld)
    val newClk = clk.createEnable(newVld)
    val newSignal = path.map(x => new SignalTrait.SignalPath(x,rdData)).getOrElse(rdData)
    scope->(DisplayDump2(s"${name}.hex",newSignal)(newClk))
    dataSet
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


    val wrVld       = SignalTrait(createName("wr_vld")    ,opType ,FixedType.unsigned(1,0))
    val wrAddress   = SignalTrait(createName("wr_address"),opType ,addressWidth)
    val rdAddress   = SignalTrait(createName("rd_address"),opType ,addressWidth)
    val rdVld       = SignalTrait(createName("rd_vld")    ,opType ,FixedType.unsigned(1,0))
    val subVld      = SignalTrait(createName("sub_vld"),opType)
    val inter       = SignalTrait(createName("inter"),opType)
    val interFirst  = SignalTrait(createName("inter_first"),opType)

    val addressSubSize  = if (memoryRepeat.length > 1) math.ceil(math.log(memoryRepeat(1)/math.log(2.0))).toInt else 1
    val subAddress = SignalTrait(createName("sub_addr"),opType,FixedType.unsigned(addressSubSize,0))
    val subData    = SignalTrait(createName("sub_data"),opType,FixedType.unsigned(memoryRepeat(0),0))


    //override val signals: List[SignalTrait] = if (memoryDepth <= 1) List(rdAddress, wrVld, wrAddress, rdVld)
    //else List(rdAddress, wrVld, wrAddress, rdVld, subVld, subAddress, subData)

    override val signals: List[SignalTrait] =List(subVld, subAddress, subData, rdAddress,
      wrVld, wrAddress, rdVld, inter, interFirst)

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
