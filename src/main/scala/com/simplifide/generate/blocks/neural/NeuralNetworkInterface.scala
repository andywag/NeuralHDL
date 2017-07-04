package com.simplifide.generate.blocks.neural

import com.simplifide.generate.signal.{FloatSignal, OpType}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * Created by andy on 7/4/17.
  */
class NeuralNetworkInterface[T](override val name:String, val stage:NeuralStageInterface[_]) extends SignalInterface{

  val expected         = FloatSignal("expected",OpType.Input)
  val expectedRdy          = new ReadyValidInterface(expected)

  override val interfaces       = expectedRdy :: stage.interfaces
  override val outputInterfaces = stage.outputInterfaces
}



