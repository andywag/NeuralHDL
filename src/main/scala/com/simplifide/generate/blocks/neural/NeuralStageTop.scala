package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/26/17.
  */
class NeuralStageTop[T](val name:String,
                       info:NeuralStageTop.Info,
                     val dataIn:ReadyValidInterface[T],
                     val tapIn:ReadyValidInterface[T],
                     val dataOut:ReadyValidInterface[T],
                     val dataPreOut:ReadyValidInterface[T])(implicit clk:ClockControl) extends EntityParser {


  override def document =

    s"""
This block is a parameterizable fully connected neural network stage. The structure is really a matrix multiply
which is driven from memories. The ordering of the operations has been design so the same structure can be used for
all of the main operations by varying the inputs to block.

* Calculating the neural outputs (data, taps, bias)
* Updating the taps and biases   (data, error, taps)
* Back propagating the errors    (error, transpose(taps))

`Detailed Description of the Ordering and Operation of this Block is TBD`

## Parameters

This stage is configurable and the control parameters are specified in `info`. These values will control
the size and shape of this stage.

## Input/Output
* output ${dataOut.value.signals(0)}    : Output of the block following the equal to dataIn*taps + bias
* output ${dataPreOut.value.signals(0)} : Output of the block before the non-linearity (for testing)

* input ${dataIn}   : Data Input of the Block
* input ${tapIn}     : Neural Tap input of the Block
* Various other input controls are input to this block to configure the lengths used for controlling the
* MAC units

## Subblocks

This block contains 3 major subblocks

* A control section which addresses the memories and drives the Neurons
* A memory unit which contains memory for the data, tap and bias
* A set of neuron units (MAC units) along with a nonlinearity (currently sigmoid)

## Reference Code

* [Code Generator](${PathUtilities.nueralPath}/NeuralStageTop.scala)
* [Top Verilog Output](../design/${name}.v)
* [Verilog Folder](../design)

"""

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(dataOut.reverse)
  signal(dataPreOut.reverse)

  val stage = new NeuralStage(appendName("st"),info.numberNeurons)
  instance(stage)

  val memorySize = NeuralMemory.Dimensions(info.tapDimension,info.numberNeurons,info.dataFill)
  val memory = new NeuralMemory(appendName("mem"),memorySize, info)
  instance(memory)

  val control    = new NeuronControl[T](appendName("ctrl"),info, dataIn, tapIn, dataOut,dataPreOut, this)
  instance(control)


  //this.createNeuronStage

}

object NeuralStageTop {

  /**
    *
    * @param tapDimension    : Size of the Matrix Multiplication
    * @param dataLength      : Length of the Input Data (Column of Matrix)
    * @param dataFill        : Amount of memory for Data storage
    * @param errorFill       : Amount of extra tap storage used to contain the errors
    * @param numberNeurons   : Number of MAC Units for this Stage
    * @param dataLocation    : Location where the initial taps are stored for test
    */
  case class Info(tapDimension:(Int,Int),
                  dataLength:Int,
                  dataFill:Int,
                  numberNeurons:Int,
                  errorFill:Int,
                  dataLocation:String
                 ) {

    def logWidth(input:Int) = {
      math.ceil(math.log(input)/math.log(2)).toInt
    }


    def logWidthPlus1(input:Int) = {
      val c = math.ceil(math.log(input)/math.log(2)).toInt
      val f = math.floor(math.log(input)/math.log(2)).toInt
      if (f == c) (c + 1) else c
    }

    // Size of the data memory address
    val dataSingleWidth  = logWidth(dataLength)
    // Depth of the memory address
    val dataFillWidth    = logWidthPlus1(dataFill)
    // Width of Data Address (NxK)
    val dataAddressWidth =  dataSingleWidth + dataFillWidth

    // Number of passes through the input data required for each stage
    val stateLength      = tapDimension._2/numberNeurons
    val stateWidth       = logWidth(stateLength)
    // Width of tap address
    val tapAddressLength = (tapDimension._1*tapDimension._2)/numberNeurons
    val tapAddressWidth   = logWidth(tapAddressLength)
    // Width of Bias
    val biasLength       = numberNeurons
    val biasAddressWidth  = dataSingleWidth

    // FIXME : Need generic
    val memoryWidth = 32;

  }
}
