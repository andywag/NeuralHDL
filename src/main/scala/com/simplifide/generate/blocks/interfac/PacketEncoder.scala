package com.simplifide.generate.blocks.interfac

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.interfac.PacketEncoder.Impl

/**
 * Class which generations a packet encoder
 */

trait PacketEncoder extends ComplexSegment {

  /** Input Bus */
  val bus:SignalTrait
  /** Index of the packet */
  val index:SignalTrait
  /** Type of Packet */
  val typ:SignalTrait
  /** Module of the Packet Encoder */
  val model:PacketModel
  
  /** Amount of Bytes per packet */
  val bytes = bus.width/8


  def createField(field:PacketField) = {
    val fieldNumber = math.ceil(field.length.toDouble/bytes.toDouble).toInt
    index $match (   // Case Statement based on the index of the packet
      List.tabulate(fieldNumber)(x => $cases(x) $then field.value(x,bytes))
    )  
  }
  
  def createBody = {
    
    bus := typ $match (  // Case Statement based on the type of packet to encode
      model.fields.map(x => $cases(x._1) $then  createField(x._2)).toList
    )
  }

}

object PacketEncoder {
  
  def apply(bus:SignalTrait,index:SignalTrait,typ:SignalTrait,model:PacketModel) = 
    new Impl(bus,index,typ,model)
  
  class Impl(val bus:SignalTrait,
    val index:SignalTrait,
    val typ:SignalTrait,
    val model:PacketModel) extends PacketEncoder {
    
  }
}