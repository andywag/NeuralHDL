package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator._
import com.simplifide.generate.parser.{ConditionParser, SignalParser}
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  * Created by andy on 5/8/17.
  */
case class Neuron(dataOut:SignalTrait,
                  dataIn:SignalTrait,
                  taps:SignalTrait,
                  bias:SignalTrait)(implicit val clk:ClockControl) extends ComplexSegment  {

  def createBody = {}

  val internalSignal = signal(dataOut.newSignal(name = dataOut.appendName("tap_data_out")))
  internalSignal := dataIn * taps
  dataOut        := internalSignal + bias

  val result = dataOut.newSignal(name = dataOut.appendName("result"))

  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals = List(dataIn,taps,bias)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(dataOut)


}
