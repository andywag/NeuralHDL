package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.newparser.typ.NumberType
import com.simplifide.generate.generator._
import com.simplifide.generate.parser.{ConditionParser, SignalParser}
import com.simplifide.generate.signal.{OpType, SignalTrait}
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/8/17.
  */
case class Neuron(proto:SignalTrait)(implicit val clk:ClockControl) extends ComplexSegment  {

  import com.simplifide.generate.newparser.typ.SegmentParser._
  import com.simplifide.generate.doc.MdGenerator._

  def createBody = {}

  override def document =s""""""

  // Input/Output Declarations
  val dataOut:SignalTrait = proto.newSignal(name = "data_out",opType = OUTPUT)
  val dataIn:SignalTrait = proto.newSignal(name = "data_in",opType = INPUT)
  val taps:SignalTrait = proto.newSignal(name = "taps",opType = INPUT)
  val bias:SignalTrait = proto.newSignal(name = "bias",opType = INPUT)

  val internalSignal = signal(dataOut.newSignal(name = dataOut.appendName("tap_data_out"),OpType.Signal))

  // MAC Operation
  internalSignal := dataIn times taps
  dataOut        := internalSignal plus bias

  val result = dataOut.newSignal(name = dataOut.appendName("result"))

  // Convenience information to allow automatic block testing
  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals = List(dataIn,taps,bias)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(dataOut)


}
