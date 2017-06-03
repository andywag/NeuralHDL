package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct}
import com.simplifide.generate.blocks.neural.NeuralError.Ctrl
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 6/1/17.
  */
case class NeuralError[T](override val name:String,
                          info:NeuralStageInfo,
                          outputIn:ReadyValidInterface[T],
                          dataIn:ReadyValidInterface[T],
                          errorOut:ReadyValidInterface[T]
                           )(implicit clk:ClockControl) extends EntityParser {

  override def createBody() {}

  val outputInt = MemoryStruct("error_int",Array(info.memoryWidth,1),Array(info.tapDimension._2,info.outputFill))
  val outputBank   = MemoryBank(appendName("output_mem"),outputInt, None)

  instance(outputBank)
  instance(new Ctrl(appendName("ctrl"),info,this))

}

object NeuralError {

  case class Ctrl[T](override val name:String, info:NeuralStageInfo, parent:NeuralError[T])
                    (implicit clk:ClockControl)extends EntityParser {

    import com.simplifide.generate.newparser.typ.SegmentParser._

    signal(clk.allSignals(INPUT))

    signal(parent.outputIn.signals)
    signal(parent.dataIn.signals)
    signal(parent.errorOut.reverse)
    signal(parent.outputInt.reverse)

    val vldDely = register(parent.dataIn.vld)(3)

    // Tie the Ready signal to high (ignore error conditions)
    parent.outputIn.rdy := 1
    parent.dataIn.rdy   := 1

    val inCount = signal("input_counter",REG,U(info.outputFifoAddressWidth,0))
    inCount := (inCount + 1).$at(clk.createEnable(parent.outputInt.ctrl.wrVld))
    parent.outputInt.ctrl.wrVld     := parent.outputIn.vld
    parent.outputInt.ctrl.wrAddress := inCount
    parent.outputInt.wrData         := parent.outputIn.value.signals(0)

    val outCount = signal("output_counter",REG,U(info.outputFifoAddressWidth,0))
    outCount := (outCount + 1).$at(clk.createEnable(parent.dataIn.vld))
    parent.outputInt.ctrl.rdVld     := parent.dataIn.vld
    parent.outputInt.ctrl.rdAddress := outCount


    parent.errorOut.vld        := vldDely(3)

    /- ("Actual Error Calculation")
    val delayIn = register(parent.dataIn.value.signals(0))(2)
    val calcIn = signal(FloatSignal("calc_in",WIRE))
    calcIn := parent.outputInt.rdData // FIXME : Bad Hack to force the signal to the right type for the operation

    //parent.errorOut.value.signals(0) !:= parent.dataIn.value.signals(0) minus calcIn
    parent.errorOut.value.signals(0) !:= calcIn minus delayIn(2)

  }

}
