package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.fifo
import com.simplifide.generate.blocks.basic.fifo.NewRdyVldFifo
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Counter
import com.simplifide.generate.blocks.basic.newmemory.{MemoryBank, MemoryStruct}
import com.simplifide.generate.blocks.neural.NeuralError.Ctrl
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 6/1/17.
  */
case class NeuralError[T](override val name:String,
                          info:NeuralStageInfo,
                          expected:ReadyValidInterface[_],
                          dataIn:ReadyValidInterface[_],
                          ctrlOut:ReadyValidInterface[_],
                          errorOut:ReadyValidInterface[_]
                           )(implicit clk:ClockControl) extends EntityParser {

  override def createBody() {}

  signal(errorOut.rdy) // FIXME : Not sure why this is needed

  // Create the memory interface for the expected data
  val expectedInt    = MemoryStruct("expected_int",Array(info.memoryWidth,1),Array(info.tapDimension._2,info.expectedFill))
  val expectedBank   = MemoryBank(appendName("expect"),expectedInt, None)
  instance(expectedBank)

  // Create the Control for this block
  instance(new Ctrl(appendName("ctrl"),info,this))

  // FIXME : Need to remove Hardocded Depths
  val fifo = new NewRdyVldFifo(appendName("fifo"),ctrlOut,errorOut,16)
  instance(fifo)

}

object NeuralError {

  case class Ctrl[T](override val name:String,
                     info:NeuralStageInfo, parent:NeuralError[T])
                    (implicit clk:ClockControl)extends EntityParser {

    import com.simplifide.generate.newparser.typ.SegmentParser._

    signal(clk.allSignals(INPUT))

    signal(parent.expected.signals)
    signal(parent.dataIn.signals)

    signal(parent.ctrlOut.reverse)
    signal(parent.expectedInt.reverse)

    parent.dataIn.rdy   := 1


    /- ("Create the controls for the fifo")
    val fifoDepth = signal("fifo_depth",REG,U(info.expectedFifoAddressWidth+1,0))
    parent.expected.rdy := fifoDepth < info.expectedFifoDepth-1
    fifoDepth := ($iff (parent.expected.enable & parent.dataIn.vld) $then
      fifoDepth $else_if (parent.dataIn.vld) $then
      fifoDepth - 1 $else_if (parent.expected.enable) $then
      fifoDepth + 1) $at clk

    /-("Data Input Counter and Control")
    val inCount = signal("input_counter",REG,U(info.expectedFifoAddressWidth,0))
    ->(Counter.Length(inCount,info.expectedFifoDepth-1,Some(parent.expected.enable)))
    parent.expectedInt.ctrl.wrVld     := parent.expected.enable
    parent.expectedInt.ctrl.wrAddress := inCount
    parent.expectedInt.wrData         := parent.expected.value.signals(0)

    /-("Data Output Counter and Control")
    val outCount = signal("output_counter",REG,U(info.expectedFifoAddressWidth,0))
    ->(Counter.Length(outCount,info.expectedFifoDepth-1,Some(parent.dataIn.vld)))
    parent.expectedInt.ctrl.rdVld     := parent.dataIn.vld
    parent.expectedInt.ctrl.rdAddress := outCount

    val vldDely = register(parent.dataIn.vld)(3)
    parent.ctrlOut.vld        := vldDely(3)

    /- ("Actual Error Calculation")
    val delayIn = register(parent.dataIn.value.signals(0))(2)
    val calcIn = signal(FloatSignal("calc_in",WIRE))
    calcIn := parent.expectedInt.rdData // FIXME : Bad Hack to force the signal to the right type for the operation

    parent.ctrlOut.value.signals(0) !:= calcIn minus delayIn(2)

  }

}
