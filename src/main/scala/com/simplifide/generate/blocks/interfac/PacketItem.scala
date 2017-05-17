package com.simplifide.generate.blocks.interfac

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  *
  */

trait PacketItem {
 
  /** Size of the packet item in bytes */
  val size:Int

  //def value:SimpleSegment
  def value(index:Int=0):SimpleSegment

  def convertToReg = value() match {
    case x:SignalTrait => new PacketItem.Segment(x.copy(optype = OpType.Register))
    case _ => this
  }

  val itemWidth = 8
  
}

object PacketItem {
  
  //def apply(name:String, size:Int) = new Segment(name,size)
  
  class Segment(val segment:SimpleSegment) extends PacketItem {

    override val size = segment.fixed.width/itemWidth

    //override def value            = segment
    // MSB First
    override def value(index:Int = 0) =
      if (index == 0) segment else segment((itemWidth*(size-index)-1,itemWidth*(size-index-1)))
  }
  
  /** Main Working Utility for creating the packets */
  class Partial(val offset:Int, val item:PacketItem) extends PacketItem {
    override val size = 1

    //override def value            = item.value(offset)
    override def value(index:Int = 0) = item.value(offset + index)

  }

  

}