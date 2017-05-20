package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.typ.NumberType
import com.simplifide.generate.generator._
import com.simplifide.generate.parser.{ConditionParser, SignalParser}
import com.simplifide.generate.signal.{OpType, SignalTrait}
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/8/17.
  */
case class Neuron(dataOut:SignalTrait,
                  dataIn:SignalTrait,
                  taps:SignalTrait,
                  bias:SignalTrait)(implicit val clk:ClockControl) extends ComplexSegment  {

  import com.simplifide.generate.blocks.basic.typ.SegmentParser._
  import com.simplifide.generate.doc.MdGenerator._

  def createBody = {}

  override def document =

s"""
This block contains a simple neuron (actually a MAC) which contains a multiplier and an adder. The
true nueron operation is handled externally by appropriately driving the MAC unit. The block has 2 delays.
There is one delay for the multiplier output and one delay for the addition block. The bias delay is expected
to be taken care of outside the block to simplify the input interface to this block.

## Input/Output
* output ${dataOut.document} : Output of the block following the equal to dataIn*taps + bias

* input ${dataIn.document}   : Data Input of the Block
* input ${taps.document}     : Neural Tap input of the Block
* input ${bias.document}     : Bias Input of the Block (Needs to be delayed by 1 sample relative to other inputs

## Generator Code

The code to generate this block is relatively straightforward. It simply contains a multiplier and an adder. The
appropriate adder is selected by the type of the input. *The use of times and use of plus rather than */+ is due
to a recent change in the way these operations are handled and is temporary.*

```scala

internalSignal := dataIn times taps
dataOut        := internalSignal plus bias

```

## Reference Code

* [Code Generator](${PathUtilities.nueralPath}/Neuron.scala)
* [Verilog Output](../design/${name}.v)


"""

  val internalSignal = signal(dataOut.newSignal(name = dataOut.appendName("tap_data_out"),OpType.Signal))
  //val biasDelay      = signal(bias.newSignal(name = bias.appendName("d"),OpType.Register))

  //biasDelay      := bias $at (clk)
  internalSignal := dataIn times taps
  dataOut        := internalSignal plus bias

  val result = dataOut.newSignal(name = dataOut.appendName("result"))

  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals = List(dataIn,taps,bias)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(dataOut)


}
