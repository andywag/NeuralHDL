package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.{NeuralError, NeuralStageInfo, NeuralStageInterface, NeuralStageTop}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 6/3/17.
  */
class NeuralNetwork[T](val name:String,
                        neuralInfo:Seq[NeuralStageInfo],
                        interface:NeuralStageInterface[T],
                        expected:ReadyValidInterface[_]
                       )(implicit clk:ClockControl) extends EntityParser {

  val info = neuralInfo(0)
  import com.simplifide.generate.newparser.typ.SegmentParser._

  // Attach the output signals
  signal(interface.outRdy.reverse)
  signal(interface.outPreRdy.reverse)

  // Convenience Assignment to handle hierarchy creation : output goes both to error and out of block
  val outInternalRdy = new ReadyValidInterface(FloatSignal("internal_out",WIRE))
  signal(outInternalRdy.signals.map(_.changeType(WIRE)))
  outInternalRdy.vld !:= interface.outRdy.vld
  outInternalRdy.value.value !:= interface.outRdy.value.value

  val ctrlRdy   = new ReadyValidInterface(FloatSignal("zctrl",INPUT))

  val mStage = new NeuralStageTop(appendName("st1"),info,interface)
  val iStage  = instance(mStage)

  val mError = new NeuralError(appendName("err"),info, expected,outInternalRdy,ctrlRdy,mStage.interface.errorRdy)
  val iError = instance(mError)


}
