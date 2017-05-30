package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.NeuralStage.{Interface, StageAdder}
import com.simplifide.generate.doc.MdGenerator._
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.{ReadyValid, SignalArray, SignalInterface}
import com.simplifide.generate.signal.{FloatSignal, OpType, SignalTrait, sv}
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/8/17.
  */
// FIXME : nonlinearity needs generalization
case class NeuralStage(override val name:String,
                       numberOfNeurons:Int)(implicit clk:ClockControl) extends  EntityParser{

  import SignalArray._

  // Create the I/O signals for the block
  val first  = signal("first",INPUT)

  val dataIn = Seq(signal(FloatSignal(appendName("data"),INPUT))) // FIXME : Generalize to Generic Number Type
  //val tapIn  = Seq.tabulate(numberOfNeurons)(x => signal(FloatSignal(appendName(s"tap_${x}"),INPUT))) // FIXME : Generalize to Generic Number Type
  // FIXME : Add support for arrays :
  val tapIn   = signal(SignalArray.Arr("taps",FloatSignal(appendName(s"tap"),INPUT),numberOfNeurons))

  val biasIn = Seq(signal(FloatSignal(appendName("bias"),INPUT))) // FIXME : Generalize to Generic Number Type

  val dataOutPre   = signal(FloatSignal(appendName("data_out_pre"),OUTPUT))
  val dataOutBias  = signal(FloatSignal(appendName("data_out_bias"),OUTPUT))
  val dataOut      = signal(FloatSignal(appendName("data_out"),OUTPUT))

  val interface = new Interface(this.name, this)

  val neuronBlock    = new Neuron(dataOut,dataIn(0),tapIn.input.value,biasIn(0))
  val sigmoidBlock   = new Sigmoid.AlawFloat2("sigmoid", dataOut, dataOutPre)


  val neuron    = new ComplexSegment.SegmentEntity(neuronBlock, "neuron")
  val nonlinearity = new ComplexSegment.SegmentEntity(sigmoidBlock, "sigmoid")


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
* output ${dataOut.document} : Output of the block following the equal to dataIn*taps + bias

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

  def inputs = tapIn :: first :: clk.allSignals(INPUT) ::: dataIn.toList  ::: biasIn.toList

  sigs(inputs.map(_.changeType(OpType.Input)))
  signal(dataOut.changeType(OpType.Output))
  signal(dataOutBias.changeType(OpType.Output))
  signal(dataOutPre.changeType(OpType.Output))


  //val biasInputDelay = register(biasIn(0))(depth)(clk).allSignalChildren.toSeq
  val neuronOut      = seq(dataOut.newSignal(name = "wireOut"))(numberOfNeurons)
  val neuronAccumIn  = seq(biasIn(0))(numberOfNeurons)

  val dataOutDelay    = seq(dataOut.newSignal(name = "outLine"),REG)(numberOfNeurons)
  val firstD          = Seq.tabulate(2)(x => signal(first.newSignal(name = s"first$x",REG)))

  import com.simplifide.generate.newparser.typ.SegmentParser._

  //val a = valid := valid ? valid :: valid
  /- ("Delay the Input Valid")
  Seq(firstD(0),firstD(1)) !:= Seq(first,firstD(0)) at (clk)

  /- ("Select the inputs to the Neuron\n")
  for (i <- 0 until depth) {
    //val biasInput = if (i == depth-1) biasIn(0) else biasInputDelay(depth-2-i)
    neuronAccumIn(i) !:= firstD(0) ? 0 :: neuronOut(i)
  }

  /- ("Create the output Delay Line\n")
  dataOutDelay !:= ($if (firstD(0)) $then neuronOut $else dataOutDelay.slice(1,dataOutDelay.length)) at (clk)




  val neuronInput = biasIn.map(x => signal(x.newSignal(name = x.appendName("_input"),opType=OpType.Logic)))
  //List.tabulate(numberOfNeurons)(x => neuronInput(x) := valid ? biasIn(x) :: dataOut(x))

  val neuronEntity = neuron.createEntity
  for (i <- 0 until numberOfNeurons) {
    val connection = Map(
      neuron.segment.dataIn  -> dataIn(0),
      neuron.segment.taps    -> tapIn.s(i),
      neuron.segment.bias    -> neuronAccumIn(i),
      neuron.segment.dataOut -> neuronOut(i)
    )
    instance(neuronEntity,s"neuron$i",connection)
  }

  val adderOut       = neuronOut(0).newSignal(appendName("adder"))
  val adderBlock     = new StageAdder(appendName("_add"),adderOut,dataOutDelay(0),biasIn(0))
  instance(adderBlock)

  /- ("Assign the outputs")
  dataOutPre  !:= dataOutDelay(0)
  dataOutBias !:= adderOut

  val connect = Map(nonlinearity.segment.dataIn.asInstanceOf[SignalTrait] -> adderOut)
  instance(nonlinearity.createEntity,s"sigmoid",connect)


}

object NeuralStage {
  class Interface(override val name:String, stage:NeuralStage) extends SignalInterface {
    override val inputs = List(stage.first, stage.dataIn(0), stage.biasIn(0),stage.tapIn)
    override val outputs = List(stage.dataOutPre, stage.dataOut)
  }

  class StageAdder(val name:String, val out:SignalTrait, val in1:SignalTrait, in2:SignalTrait)(implicit clk:ClockControl) extends EntityParser {
    import com.simplifide.generate.newparser.typ.SegmentParser._
    signal(clk.allSignals(INPUT))
    signal(in1.asInput)
    signal(in2.asInput)
    signal(out.asOutput)
    out        := (in1 plus in2)
  }

}
