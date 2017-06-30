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

  val errorMode        = signal("stage_error_mode",INPUT)
  val errorFirst       = signal("stage_error_first",INPUT)
  val stateErrorBack   = signal("stage_error_back",INPUT)
  val updateErrorFirst = signal("update_error_first",INPUT)

  val dataIn = Seq(signal(FloatSignal(appendName("data"),INPUT))) // FIXME : Generalize to Generic Number Type
  // FIXME : Add support for arrays :
  val tapIn    = signal(SignalArray.Arr("taps",FloatSignal(appendName(s"tap"),INPUT),numberOfNeurons))
  // Latch the tap input data used for error updates
  val tapConv   = signal(SignalArray.Arr("taps_conv",FloatSignal(appendName(s"tap_lat"),REG),numberOfNeurons))
  val tapLat   = signal(SignalArray.Arr("taps_lat",FloatSignal(appendName(s"tap_lat"),REG),numberOfNeurons))
  val tapSel   = signal(SignalArray.Arr("taps_select",FloatSignal(appendName(s"tap_lat"),WIRE),numberOfNeurons))

  val biasIn  = signal(FloatSignal(appendName("bias"),INPUT)) // FIXME : Generalize to Generic Number Type
  // FIXME : The use of this is broken on a few levels. There are 2 purposes for this variable
  // 1. Delay for Bias Addition : Delay needs to be the accumulator depth + 2 for adder delay
  //                              This should be fixed to be the accumulator input but harder to debug
  // 2. The second use is for the bias addition update which has a reasonable delay
  val biasInR = register(biasIn)(numberOfNeurons+2) // FIXME : Generalize to Generic Number Type

  val dataOutPre   = signal(FloatSignal(appendName("data_out_pre"),OUTPUT))
  val dataOutBias  = signal(FloatSignal(appendName("data_out_bias"),OUTPUT))
  val dataOut      = signal(FloatSignal(appendName("data_out"),OUTPUT))

  val fullOut1      = signal(appendName("tap_out_int"),WIRE,U(numberOfNeurons*32))
  val fullOut      = signal(appendName("tap_out"),REGOUT,U(numberOfNeurons*32))


  val interface = new Interface(this.name, this)

  val neuronBlock    = new Neuron(dataOut)

  val sigmoidBlock   = new Sigmoid.AlawFloat2("sigmoid", dataOut)


  val neuron       = new ComplexSegment.SegmentEntity(neuronBlock, "neuron")
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
* input ${biasIn.document}     : Bias Input of the Block (Needs to be delayed by 1 sample relative to other inputs

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

  val tapGain = signal("tap_gain",WIRE,U(8))
  val biasGain = signal("bias_gain",WIRE,U(8))

  tapGain := 3
  biasGain := 6

  val share  = dataIn.length
  val depth  = tapIn.length

  def inputs = biasIn :: tapIn :: first :: clk.allSignals(INPUT) ::: dataIn.toList

  sigs(inputs.map(_.changeType(OpType.Input)))
  signal(dataOut.changeType(OpType.Output))
  signal(dataOutBias.changeType(OpType.Output))
  signal(dataOutPre.changeType(OpType.Output))


  // Input Delay Line for Backpropagation : Error is stored and rotated around
  val neuronIn    = seq(dataOut.newSignal(name = "in_line"),REG)(numberOfNeurons)

  val neuronOut      = seq(dataOut.newSignal(name = "wireOut"))(numberOfNeurons)
  val neuronAccumIn  = seq(biasIn)(numberOfNeurons)

  val dataOutDelay    = seq(dataOut.newSignal(name = "out_line"),REG)(numberOfNeurons)
  val firstD          = Seq.tabulate(2)(x => signal(first.newSignal(name = s"first$x",REG)))
  val errorD          = register(errorMode)(1)
  val tapInD          = register(tapIn)(1)

  // Delay the control signal for backpropagation
  // FIXME : Need to properly control the length
  val stageErrorBackD = register(stateErrorBack)(9)

  import com.simplifide.generate.newparser.typ.SegmentParser._

  /- ("Delay the Input Valid")
  Seq(firstD(0),firstD(1)) !:= Seq(first,firstD(0)) at (clk)

  // Latch the input error for backpropagation
  neuronIn !:= ($if (first) $then tapIn.toSeq $else (neuronIn.slice(1,neuronIn.length).toList ::: List(neuronIn(0))) ) at (clk)

  // Negate and shift down the error input which is input through the tap
  for (i <- 0 until numberOfNeurons) {
    tapConv.s(i).sgn :=  tapIn.s(i).sgn
    tapConv.s(i).exp :=  (tapIn.s(i).exp > tapGain) ? (tapIn.s(i).exp - tapGain) :: tapIn.s(i).exp // FIXME : Programmable Gain
    tapConv.s(i).man :=  tapIn.s(i).man
  }
  // Latch the error input into a storage register for the first error
  tapLat !:= tapConv $at(clk.createEnable(errorFirst))

  // During Error mode use the Error otherwise use the taps
  // During Back propagation and data the taps are used regularly
  tapSel !:= (errorMode & ~stageErrorBackD(2)) ? tapLat :: tapIn

  // Input Selection :
  // For error use the delated error signal stored in tapInD
  // For Data use either previous output or 0 for first data sample
  /- ("Select the inputs to the Neuron")
  for (i <- 0 until depth) {
    neuronAccumIn(i) !:= (errorD(1) & ~stageErrorBackD(2))  ? tapInD(1).s(i) :: ((firstD(0) | updateErrorFirst) ? 0 :: neuronOut(i))
  }

  // Put the Neuron outputs into a delay line to share both the adder and the non-linearity
  // For error Mode : Use Error Output to be added to the bias
  // For data  Mode : Use outputs
  /- ("Create the output Delay Line\n")
  val neuronTemp      = seq(dataOut.newSignal(name = "neuron_temp"))(numberOfNeurons)
  for (i <- 0 until numberOfNeurons) {
    neuronTemp(i)   !:=  neuronOut(i)
  }
  dataOutDelay !:= ($if (firstD(0) | updateErrorFirst) $then neuronTemp $else dataOutDelay.slice(1,dataOutDelay.length)) at (clk)

  /- ("Create the bias update code")
  val tapLat1   = signal(SignalArray.Arr("taps_lat1",FloatSignal(appendName(s"tap_lat"),REG),numberOfNeurons))
  val biasAddInput = seq(dataOut.newSignal(name = "bias_add_input"),REG)(numberOfNeurons)

  for (i <- 0 until numberOfNeurons) {
    tapLat1.s(i).sgn !:= tapLat.s(i).sgn
    tapLat1.s(i).man !:= tapLat.s(i).man
    tapLat1.s(i).exp !:= (tapLat.s(i).exp > biasGain) ? (tapLat.s(i).exp - biasGain) :: tapLat.s(i).exp
    biasAddInput(i)     !:= tapLat1.s(i)
  }


  val biasAddDelay    = seq(dataOut.newSignal(name = "bias_add_delay"),REG)(numberOfNeurons)
  biasAddDelay !:= ($if (firstD(0)) $then biasAddInput $else biasAddDelay.slice(1,biasAddDelay.length)) at (clk)


  val nIn = seq(neuron.segment.dataIn.newSignal(name = "input_data"))(numberOfNeurons)
  List.tabulate(numberOfNeurons){x => nIn(x) !:= stageErrorBackD(2) ? neuronIn(x) :: dataIn(0)}


  // Creation of neuron instance
  val neuronEntity = neuron.createEntity
  for (i <- 0 until numberOfNeurons) {
    val connection = Map(
      neuron.segment.dataIn  -> nIn(i),//dataIn(0),
      neuron.segment.taps    -> tapSel.s(i),
      neuron.segment.bias    -> neuronAccumIn(i),
      neuron.segment.dataOut -> neuronOut(i)
    )
    instance(neuronEntity,s"neuron$i",connection)
  }

  // Final Adder Stage Used before Sigmoing
  // For Data Stage uses  : Output + Bias
  // For Error Stage uses : Bias + Error
  val adderOut           = neuronOut(0).newSignal(appendName("adder"))
  val adderBlock         = new StageAdder(appendName("_add"),adderOut,dataOutDelay(0),biasInR(numberOfNeurons+2))
  instance(adderBlock)

  val biasAdderOut       = neuronOut(0).newSignal(appendName("bias_adder"))
  val biasAdderBlock     = new StageAdder(appendName("bias_add"),biasAdderOut,biasAddDelay(0),biasInR(1))
  instance(biasAdderBlock)

  /- ("Assign the outputs")
  dataOutPre  !:= dataOutDelay(0)

  /- ("Assign the bias output")
  dataOutBias !:= biasAdderOut

  val connect = Map(nonlinearity.segment.dataIn.asInstanceOf[SignalTrait] -> adderOut,
    nonlinearity.segment.dataOut.asInstanceOf[SignalTrait] -> dataOut)
  instance(nonlinearity.createEntity,s"sigmoid",connect)

  // Parallel output of the blocks containing all the taps

  /- ("Assign the tap outputs")
  for (i <- 0 until depth) {
    val sl = (32*(i+1)-1,32*i)
    fullOut1(sl) := neuronOut(i)
  }
  fullOut := fullOut1 $at(clk)



}

object NeuralStage {
  class Interface(override val name:String, stage:NeuralStage) extends SignalInterface {
    override val inputs = List(stage.first, stage.dataIn(0), stage.biasIn,stage.tapIn,
      stage.errorMode,
      stage.errorFirst,
      stage.stateErrorBack,
      stage.updateErrorFirst)
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
