package com.simplifide.generate.blocks.interfac

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.{BasicSegments, ComplexSegment}
import com.simplifide.generate.signal.{NewConstant, SignalTrait}
import com.simplifide.generate.parser.factory.CreationFactory

/**
  * Trait which is used to Decode the contents of a packet
  */

trait PacketDecoder extends ComplexSegment {
  
  implicit val clk:ClockControl
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
    def statement(x:Int,y:Int) = {
      val value = field.singleValue(bytes*x + y)
      value match {
        case x:NewConstant => None
        case _ => Some((field.singleValue(bytes*x + y) ::= bus((8*(bytes-y)-1,8*(bytes-y-1)))).create(CreationFactory.Hardware))
      }
    }

    val fieldNumber = math.ceil(field.length.toDouble/bytes.toDouble).toInt
    index $match (   // Case Statement based on the index of the packet
      List.tabulate(fieldNumber) (
        x => $cases(x) $then( BasicSegments.BeginEnd(List.tabulate(bytes)(y => statement(x,y)).flatten))
      )
    )
  }

  def createBody = {

    $always_clk(clk) (
      typ $match (  // Case Statement based on the type of packet to encode
        model.fields.map(x => $cases(x._1) $then  createField(x._2)).toList
      )
    )
   
  }

}

object PacketDecoder {
  def apply(bus:SignalTrait,index:SignalTrait,typ:SignalTrait,model:PacketModel)(implicit clk:ClockControl) =
    new Impl(bus,index,typ,model)

  class Impl(val bus:SignalTrait,
             val index:SignalTrait,
             val typ:SignalTrait,
             val model:PacketModel)(implicit val clk:ClockControl) extends PacketDecoder {

  }
}