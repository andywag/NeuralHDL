package com.simplifide.generate.blocks.interfac

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.basic.operator.Operators.Concat
import com.simplifide.generate.signal.NewConstant


/**
 */

trait PacketField {
  /** Map of items contained in this field */
  val items:Map[Int, PacketItem]
  /** Size of this field in bytes */
  val length:Int

  def convertToReg:PacketField = new PacketField.Impl(items.map(x => (x._1,x._2.convertToReg)).toMap,length)

  /** Returns a full list of packet items contained */
  // TODO Support cases where the packet isn't packed
  val partials:List[PacketItem] = {
    def makePartial(item:PacketItem):List[PacketItem] = {
      if (item.size == 0) List(item) else List.tabulate(item.size)(x => new PacketItem.Partial(x,item))
    }
    val sortedItems = items.map(x => (x._1,x._2)).toList.sortBy(_._1)
    sortedItems.flatMap(x => makePartial(x._2))
  }

  def partial(index:Int) = if (index < partials.length) partials(index).value() else NewConstant.Hex(0,8)
  /** Value of this field at a given time */
  def value(index:Int, width:Int):SimpleSegment = {
    if (width == 0) partials(index).value()
    else new Concat(List.tabulate(width)(x => partial(index*width + x)))
  }
  /** Single Value */
  def singleValue(index:Int):SimpleSegment = partial(index)
}

object PacketField {  
  
  def apply(segments:List[SimpleSegment]) = {
    val widths     = segments.map(x => math.ceil(x.fixed.width/8).toInt)    // Create a map of (width,x)
    val addresses  = widths.scanLeft(-widths(0))(_+_)                       // Create the Internal Widths
    val scaledAddresses      = addresses.slice(1,addresses.length)          // Return the initial value
    val items = segments.map(new PacketItem.Segment(_))

    new Impl((scaledAddresses zip items).toMap,addresses(addresses.length-1)+widths(0))
  }
  
  class Impl(override val items:Map[Int, PacketItem], override val length:Int ) extends PacketField
  
}