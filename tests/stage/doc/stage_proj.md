
# stage

This document details the block design of stage. It starts out with a description of the design followed
by the test descriptions and results.


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
* output data_out    : Output of the block following the equal to dataIn*taps + bias
* output data_out_pre : Output of the block before the non-linearity (for testing)

* input ReadyValidInterface(com.simplifide.generate.signal.sv.ReadyValid$ReadyValidSignal@2c383e33)   : Data Input of the Block
* input ReadyValidInterface(com.simplifide.generate.signal.sv.ReadyValid$ReadyValidSignal@74a195a4)     : Neural Tap input of the Block
* Various other input controls are input to this block to configure the lengths used for controlling the
* MAC units

## Subblocks

This block contains 3 major subblocks

* A control section which addresses the memories and drives the Neurons
* A memory unit which contains memory for the data, tap and bias
* A set of neuron units (MAC units) along with a nonlinearity (currently sigmoid)

## Reference Code

* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//NeuralStageTop.scala)
* [Top Verilog Output](../design/stage.v)
* [Verilog Folder](../design)



# testStage


This module is a block test wrapper for the a fully connected neuron stage with control and memories. The block
is currently configured to operate with

* 6  inputs
* 12 outputs
* 6  neurons

This configuration was selected for a simple test case with a 6 input, 6 output test case. The plan was to put another
12x12 hidden layer and a 12x6 output layer to complete the test. This test might be simplified to a more simple test
to verify the convergence of a single stage with a simple transfer function.

##

This test matches the output stage versus a model of the system defined in this module using Nd4j. The match is not perfect
due to quantization effects especially due to the sigmoid approximation with a maximum error of .048. Given this error the block
should definitely be done in fixed point.

```scala

val result      = tapData dot dataData._1
val finalResult = Transforms.sigmoid(result)

```

## Reference Code for Test
* [Testbench (Verilog)](../test/testStage.v)
* [Test Wrapper (C++)](../test/testStage.cpp)
* [Test Generator](../../../src/test/scala/com/simplifide/generate/neural//NeuralTopTest.scala)
* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//NeuralStageTop.scala)
* [Test Directory](../test/)
* [Design Directory](../design/)
* [Docs Directory](../doc/)



        


