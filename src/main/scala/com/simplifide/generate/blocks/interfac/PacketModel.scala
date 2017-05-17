package com.simplifide.generate.blocks.interfac

import com.simplifide.generate.blocks.interfac.PacketModel.Impl

/**
 * Class which describes the content of a packet
 */

trait PacketModel {
  val fields:Map[Int, PacketField]
  
  def convertToRegister:PacketModel = PacketModel(fields.map(x => (x._1,x._2.convertToReg)).toMap)

}

object PacketModel {

  def apply(fields:Map[Int, PacketField]) = new Impl(fields)
  class Impl(val fields:Map[Int, PacketField]) extends PacketModel
}