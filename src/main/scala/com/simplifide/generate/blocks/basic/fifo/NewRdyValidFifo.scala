package com.simplifide.generate.blocks.basic.fifo

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.bus.ReadyValidBase
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalArray

/**
  * Created by andy on 6/7/17.
  */
case class NewRdyVldFifo(val name:String,
                 val signalIn:ReadyValidInterface[_],
                 val signalOut:ReadyValidInterface[_],
                 val depth:Int,
                 val baseName:Option[String] = None)(implicit val clk:ClockControl) extends EntityParser {

  signal(clk.allSignals(INPUT))
  signal(signalIn.signals)
  signal(signalOut.reverse)

  val counterWidth = math.ceil(math.log(depth)/math.log(2.0)).toInt

  val vWrite   = signal(baseName.getOrElse("fifo_") + "write_address",REG,U(counterWidth,0))
  val vReadE    = signal(baseName.getOrElse("fifo_") + "read_address_e",WIRE,U(counterWidth,0))
  val vRead    = signal(baseName.getOrElse("fifo_") + "read_address",REG,U(counterWidth,0))
  val vDepth    = signal(baseName.getOrElse("fifo_") + "depth",REG,U(counterWidth+1,0))
  val vDepthE    = signal(baseName.getOrElse("fifo_") + "e_depth",REG,U(counterWidth+1,0))

  val vInternal = signal(SignalArray.Arr("internal",signalIn.value.signal.changeType(WIRE),depth))


  vDepthE         := $iff (signalIn.enable & signalOut.enable) $then vDepth $else_if (signalIn.enable) $then vDepth + 1 $else_if (signalOut.enable) $then vDepth - 1 $else vDepth
  vDepth          := vDepthE $at clk

  vWrite          := $iff (signalIn.enable)  $then vWrite + 1 $at clk
  vReadE          := vRead - 1
  vRead           := $flop(clk.createEnable(signalOut.enable)) $reset (1) $enable (vRead + 1)

  signalOut.vld     := (vDepthE > 0)     $at clk
  signalIn.rdy      := (vDepthE < (depth-1)) $at clk

  List.tabulate(depth)(x => vInternal.s(x) := $iff (vWrite === x) $then  signalIn.value.signal $at clk.createEnable(signalIn.enable.create))
  signalOut.value.signal := vReadE $match (List.tabulate(depth)(x => $cases(x) $then vInternal.s(x))) //$at clk.createEnable(signalOut.enable.create)


  //def createBody {}
}

object NewRdyVldFifo {


}


