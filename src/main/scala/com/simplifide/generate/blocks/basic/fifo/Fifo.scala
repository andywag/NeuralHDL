package com.simplifide.generate.blocks.basic.fifo

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.ClockControl

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 12/9/11
 * Time: 9:52 AM
 * To change this template use File | Settings | File Templates.
 */

class Fifo(val depth:Int,
           val data:SignalTrait,
           val wrValid:SignalTrait,
           val rdValid:SignalTrait)(implicit clk:ClockControl) extends ComplexSegment {

  val logDepth = (math.log(depth)/math.log(2.0)).toInt

  val rdCount  = signal("read_count", REG, U(logDepth,0))
  val wrCount  = signal("write_count",REG, U(logDepth,0))

  def createBody {
    //rdCount := (rdCount + C(1,1)) @@ clk.createEnable(wrValid)
    //wrCount := (wrCount + C(1,1)) @@ clk.createEnable(rdValid)


  }

}