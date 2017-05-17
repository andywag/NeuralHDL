package com.simplifide.generate.blocks.basic.fifo

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.bus.{ReadyValidBase, ReadyValid, CommonBuses}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/22/12
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */

class RdyVldFifo(val signalIn:ReadyValidBase[_],
  val signalOut:ReadyValidBase[_],
  val depth:Int,
  val baseName:Option[String] = None)(implicit val clk:ClockControl) extends ComplexSegment {

  val counterWidth = math.ceil(math.log(depth)/math.log(2.0)).toInt

  val vWrite   = signal(baseName.getOrElse("fifo_") + "write_address",REG,U(counterWidth,0))
  val vReadE    = signal(baseName.getOrElse("fifo_") + "read_address_e",WIRE,U(counterWidth,0))
  val vRead    = signal(baseName.getOrElse("fifo_") + "read_address",REG,U(counterWidth,0))
  val vDepth    = signal(baseName.getOrElse("fifo_") + "depth",REG,U(counterWidth+1,0))
  val vDepthE    = signal(baseName.getOrElse("fifo_") + "e_depth",REG,U(counterWidth+1,0))

  val vInternal = array(baseName.getOrElse("fifo_") + "internal",REG,signalIn.vData.fixed)(depth)

  vDepthE         := $iff (signalIn.enable & signalOut.enable) $then vDepth $else_if (signalIn.enable) $then vDepth + 1 $else_if (signalOut.enable) $then vDepth - 1 $else vDepth
  vDepth          := vDepthE $at clk

  vWrite          := $iff (signalIn.enable)  $then vWrite + 1 $at clk
  //vReadE         := $iff (signalOut.enable) $then vRead  + 1 $else vRead
  //vRead          := vReadE $at clk
  vReadE          := vRead - 1
  vRead           := $flop(clk.createEnable(signalOut.enable)) $reset (1) $enable (vRead + 1)

  signalOut.vVld     := (vDepthE > 0)     $at clk
  signalIn.vRdy      := (vDepthE < (depth-1)) $at clk

  List.tabulate(depth)(x => vInternal(x) := $iff (vWrite === x) $then  signalIn.vData $at clk.createEnable(signalIn.enable.create))
  signalOut.vData := vReadE $match (List.tabulate(depth)(x => $cases(x) $then vInternal(x))) //$at clk.createEnable(signalOut.enable.create)


  def createBody {}

}

object RdyVldFifo {

  class Entity[T <: SignalTrait](override val name:String,
    val signalIn:ReadyValidBase[T],
    val signalOut:ReadyValidBase[T],
    val depth:Int
  )(implicit clk:ClockControl) extends EntityParser {

    signal(clk.allSignals(INPUT))
    bus(signalIn,INPUT)
    bus(signalOut,REGOUT)
    
    -> (new RdyVldFifo(signalIn,signalOut,depth))

  }
}

