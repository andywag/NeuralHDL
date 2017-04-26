package com.simplifide.generate.blocks.basic.memory

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.{SignalMethods, EntityParser}
import com.simplifide.generate.signal.{OpType, BusDirect, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 3/20/12
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */

class XilinxMemory(override val name:String,
  val wrAddress:SignalTrait,
  val wrData:SignalTrait,
  val wrVld:SignalTrait,
  val rdAddress:SignalTrait,
  val rdData:SignalTrait,
  val depth:Option[Int])(implicit clk:ClockControl) extends ComplexSegment {

  val dataWidth = wrData.fixed
  val realDepth = depth.getOrElse(math.pow(2.0,wrAddress.width).toInt-1)
 
  override def createBody {
    // Internal Memory Generation
    val memory     = signal(name + "_memory",REG,dataWidth,realDepth)
    val readDelay  = signal(name + "_read_address",REG,rdAddress.fixed)
    
    readDelay := rdAddress $at(clk)
    memory(wrAddress) := $iff(wrVld) $then wrData $at(clk.withOutReset)
    rdData            := memory(readDelay) $at(clk)

  }
  
}

object XilinxMemory extends SignalMethods {


  class Bus(override val name:String,
    val dataWidth:Int,
    val addressWidth:Int,
    val inputType:OpType = INPUT,
    val outputType:OpType = REGOUT) extends BusDirect(name,INPUT) {

    def attach(prefix:String,postfix:String):String =
      if (name.equalsIgnoreCase("")) postfix else name + "_" + postfix

    val wrAddress = SignalTrait(attach(name,"wr_address"),inputType,U(addressWidth,0))
    val wrData    = SignalTrait(attach(name,"wr_data"),inputType,U(dataWidth,0))
    val wrVld     = SignalTrait(attach(name,"wr_vld"),inputType)
    val rdAddress = SignalTrait(attach(name,"rd_address"),inputType,U(addressWidth,0))
    val rdData    = SignalTrait(attach(name,"rd_data"),outputType,U(dataWidth,0))

    override val signals = List(wrAddress,wrData,wrVld,rdAddress,rdData)
  }
  
  class Entity(override val name:String,val width:Int,val depth:Int)(implicit clk:ClockControl) extends EntityParser  {
    val addressWidth = math.ceil(math.log(depth)/math.log(2.0)).toInt

    signal(clk.allSignals(INPUT))
    val bus       = new XilinxMemory.Bus("",width,addressWidth)
    signal(bus)

    val memory = new XilinxMemory(name,bus.wrAddress,bus.wrData,bus.wrVld,bus.rdAddress,bus.rdData,Some(depth))
    this.assign(memory)
  }
  /*
  class Entity(memory:XilinxMemory)(implicit clk:ClockControl) extends EntityParser {
    signal(clk.allSignals(INPUT))
    signal(memory.wrAddress.asInput)
    signal(memory.wrData.asInput)
    signal(memory.wrVld.asInput)
    signal(memory.rdAddress.asInput)
    signal(memory.rdData.asOutput)

    this.assign(memory)
    
  }

  */
}