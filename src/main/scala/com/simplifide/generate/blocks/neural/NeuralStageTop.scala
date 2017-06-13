package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.simple.NeuronControl
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface
import com.simplifide.generate.signal.{FloatSignal, OpType}
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/26/17.
  */

// FIXME : Convert the I/O to a single structure
class NeuralStageTop[T](val name:String,
                        info:NeuralStageInfo,
                        val interface:NeuralStageInterface[T]
                       )(implicit clk:ClockControl) extends EntityParser {


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
* output ${interface.outRdy.value.signals(0)}    : Output of the block following the equal to dataIn*taps + bias
* output ${interface.outPreRdy.value.signals(0)} : Output of the block before the non-linearity (for testing)

* input ${interface.inRdy}   : Data Input of the Block
* input ${interface.tapRdy}     : Neural Tap input of the Block
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
  signal(interface.inRdy.signals)
  signal(interface.outRdy.reverse)
  signal(interface.outPreRdy.reverse)

  val stage = new NeuralStage(appendName("st"),info.numberNeurons)
  instance(stage)

  val memorySize = NeuralMemory.Dimensions(info.tapDimension,info.numberNeurons,info.dataFill)
  val memory = new NeuralMemory(appendName("mem"),memorySize, info)
  instance(memory)

  val control    = new NeuronControl[T](appendName("ctrl"),info, interface, this)
  instance(control)

  //memory.tapStructW.wrData := stage.fullOut

  //this.createNeuronStage

}

object NeuralStageTop {





}
