package com.simplifide.generate.blocks.neural

import com.simplifide.generate.signal.{FloatSignal, OpType}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * FIXME : Really need to make data types generic
  */
case class NeuralStageInterface[T](name:String, proto:T) extends SignalInterface {

  val dataIn         = FloatSignal(appendName("data"),OpType.Input)
  val inRdy          = new ReadyValidInterface(dataIn)

  val errorIn        = FloatSignal(appendName("error"),OpType.Input)
  val errorRdy       = new ReadyValidInterface(errorIn)

  val dataOut        = FloatSignal(appendName("data_out"),OpType.Output)
  val outRdy         = new ReadyValidInterface(dataOut)

  val dataOutPre     = FloatSignal(appendName("data_out_pre"),OpType.Output)
  val outPreRdy      = new ReadyValidInterface(dataOutPre)

  override val interfaces       = List(inRdy,errorRdy)
  override val outputInterfaces = List(outRdy, outPreRdy)

  // Tap Load Interface - Not currently implemented : Direct load of memory for now
  val tapIn       = FloatSignal("tap_in",OpType.Input)
  val tapRdy      = new ReadyValidInterface(tapIn)
}
