package com.simplifide.generate.signal.bus

import com.simplifide.generate.parser.items.BusParser
import com.simplifide.generate.signal.{SignalTrait, SignalCreator, OpType}
import com.simplifide.generate.parser.SignalMethods

/**
 * Method which handles a ready valid
 */

class ReadyValid[T <: SignalCreator](
  val name:String,
  val creator:T, 
  override val opType:OpType) extends ReadyValidBase[T] {

  val data = creator

  def nameData  = name
  def nameValid = name + "_vld"
  def nameReady = name + "_rdy"
  
  override val vData      = signal(data.createSignal(nameData,opType))
  val vVld       = signal(nameValid ,opType)
  val vRdy       = signal(nameReady ,opType.reverseTypeWithReg)

  //def enable = vRdy & vVld

  override def createSignal(opType:OpType) = new ReadyValid.Impl(name,creator.createSignal(opType),opType)//ReadyValid.Signal(name,width,opType).createSignal
}

object ReadyValid extends SignalMethods {
  def Signal(name:String, width:Int,  opType:OpType) = new ReadyValid.Impl[SignalTrait](name,SignalTrait("data",opType,U(width,0)),opType)
  
  class Impl[T <: SignalCreator](name:String, creator:T, opType:OpType) extends ReadyValid[T](name,creator,opType)

  /** Basic Class with Diffent Signal Name */

  /*
  class SignalName(creator:
    val dataName:String,
    val validName:String,
    val readyName:String) extends ReadyValid(name,SignalTrait("",opType,U(width,0))) {

    override def nameData  = name + dataName
    override def nameValid = name + validName
    override def nameReady = name + readyName
  }
  */

}
