package com.simplifide.generate.signal.bus

import com.simplifide.generate.signal.{SignalTrait, BusDirect, OpType}
import com.simplifide.generate.parser.SignalMethods
import com.simplifide.generate.parser.items.BusParser


/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/18/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */

object CommonBuses extends SignalMethods {

  /*
  class DataValid(name:String, val busWidth:Int, opType:OpType) extends BusDirect(name,opType) {
    val data = SignalTrait(name + "_" + "dat",this.opType,U(busWidth,0))
    val vld  = SignalTrait(name + "_" + "vld",this.opType)
    override val signals = List(data,vld)
  }
  */

  class DataValid(val name:String, val busWidth:Int, opType:OpType) extends BusParser {
    val data = signal(name + "_" + "dat",opType,U(busWidth,0))
    val vld  = signal(name + "_" + "vld",opType)
  }


  /*
  class ReadyValid(name:String, val width:Int, override val opType:OpType) extends BusParser(name) {
    val data = signal(name + "_data" ,opType,U(width,0))
    val vld  = signal(name + "_vld"  ,opType)
    val rdy  = signal(name + "_rdy"  ,opType.reverseTypeWithReg)

    def enable = rdy & vld

    override def createSignal(opType:OpType) = new ReadyValid(name,width,opType).createSignal
  }
    */
  
}