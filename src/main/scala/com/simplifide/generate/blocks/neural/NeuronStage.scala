package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.newparser.typ.OutputAssignable
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{OpType, SignalTrait}
import com.simplifide.generate.util.PathUtilities
import com.simplifide.generate.newparser.typ.SegmentParser._
import com.simplifide.generate.doc.MdGenerator._
import scala.concurrent.Future

/**
  * Created by andy on 5/8/17.
  */
// FIXME : nonlinearity needs generalization
case class NeuronStage(override val name:String,
                       valid:SignalTrait,
                       dataIn:Seq[SignalTrait],
                       tapIn:Seq[SignalTrait],
                       biasIn:Seq[SignalTrait],    // FIXME : Should probably be a scalar

                       dataOut:Seq[SignalTrait],   // FIXME : Should probably be a scalar as well
                       dataOutPre:Seq[SignalTrait],   // FIXME : Should probably be a scalar as well

                       numberOfNeurons:Int,
                       neuron:SegmentEntity[Neuron],
                       nonlinearity:SegmentEntity[Sigmoid])(implicit clk:ClockControl) extends  EntityParser{

  override def document =

    s"""
This block is a generic fully connected parameterized stage of a neural network. The parameterization is controlled
by the inputs and the number of available neurons for the operation. Operation ordering is a key aspect in terms
of efficiency of the hardware and memory access. This block is currently architected to work with the following
ordering but as can be seen by the simplicity of the code. This ordering can be changed trivially. The ordering for this
operation is as follows.

1. The input data is brought in serially at the input and spread to the neurons
1. The bias is brought in serially ahead of the data and put into a delay line so it can be placed in parallel to the neurons
   at the start time. This allows for pipelining of the system
1. The taps are brought in continually
1. The output data is brought out in parallel but fed to the non-linearity in a serial fashion to minize interface timing


## Input/Output
* output ${dataOut(0).document} : Output of the block following the equal to dataIn*taps + bias

* input ${dataIn(0).document}   : Data Input of the Block
* input ${tapIn(0).document}     : Neural Tap input of the Block
* input ${biasIn(0).document}     : Bias Input of the Block (Needs to be delayed by 1 sample relative to other inputs

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


  val share  = dataIn.length
  val depth  = tapIn.length

  def inputs = valid :: clk.allSignals(INPUT) ::: dataIn.toList ::: tapIn.toList ::: biasIn.toList

  sigs(inputs.map(_.changeType(OpType.Input)))
  sigs(dataOut.map(_.changeType(OpType.Output)))
  sigs(dataOutPre.map(_.changeType(OpType.Output)))


  val biasInputDelay = register(biasIn(0))(depth)(clk).allSignalChildren.toSeq
  val neuronOut      = seq(dataOut(0).newSignal(name = "wireOut"))(numberOfNeurons)
  val neuronAccumIn  = seq(biasIn(0))(numberOfNeurons)

  val dataOutDelay    = seq(dataOut(0).newSignal(name = "outLine"),REG)(numberOfNeurons)
  val validD          = Seq.tabulate(2)(x => signal(valid.newSignal(name = s"valid$x",REG)))

  import com.simplifide.generate.newparser.typ.SegmentParser._

  //val a = valid := valid ? valid :: valid
  /- ("Delay the Input Valid")
  Seq(validD(0),validD(1)) !:= Seq(valid,validD(0)) at (clk)

  /- ("Select the inputs to the Neuron\n")
  for (i <- 0 until depth) {
    val biasInput = if (i == depth-1) biasIn(0) else biasInputDelay(depth-2-i)
    neuronAccumIn(i) !:= validD(0) ? biasInput :: neuronOut(i)

  }



  /- ("Create the output Delay Line\n")
  dataOutDelay !:= ($if (validD(0)) $then neuronOut $else dataOutDelay.slice(1,dataOutDelay.length)) at (clk)

  /- ("Assign the outputs")
  dataOutPre(0) !:= dataOutDelay(0)


  val neuronInput = biasIn.map(x => signal(x.newSignal(name = x.appendName("_input"),opType=OpType.Logic)))
  //List.tabulate(numberOfNeurons)(x => neuronInput(x) := valid ? biasIn(x) :: dataOut(x))

  val neuronEntity = neuron.createEntity
  for (i <- 0 until numberOfNeurons) {
    val connection = Map(
      neuron.segment.dataIn  -> dataIn(0),
      neuron.segment.taps    -> tapIn(i),
      neuron.segment.bias    -> neuronAccumIn(i),
      neuron.segment.dataOut -> neuronOut(i)
    )
    instance(neuronEntity,s"neuron$i",connection)
  }

  instance(nonlinearity.createEntity,s"sigmoid",Map(nonlinearity.segment.dataIn -> dataOutDelay(0)))


}
