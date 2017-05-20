
# neuronStage

This document details the block design of neuronStage. It starts out with a description of the design followed
by the test descriptions and results.


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
* output dataOut_0      - float_24_8  : Output of the block following the equal to dataIn*taps + bias

* input dataIn_0      - float_24_8    : Data Input of the Block
* input tapIn_0      - float_24_8      : Neural Tap input of the Block
* input biasIn_0      - float_24_8      : Bias Input of the Block (Needs to be delayed by 1 sample relative to other inputs

## Generator Code

The code to generate this block is relatively straightforward. It simply contains a multiplier and an adder. The
appropriate adder is selected by the type of the input. *The use of times and use of plus rather than */+ is due
to a recent change in the way these operations are handled and is temporary.*

```scala

internalSignal := dataIn times taps
dataOut        := internalSignal plus bias

```

## Reference Code

* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//Neuron.scala)
* [Verilog Output](../design/neuronStage.v)




# testNeuronStage


This module is a block test wrapper for the a fully connected neuron stage. This is the most basic test which
has the same number of MAC units as neurons and an equal number of inputs and outputs.

## Test Results

### Plot of RTL vs Reference Data for output before non-linearity

![Ref vs RTL](results.jpg)

### Error between Rtl and Reference Data before non-linearity

![RTL](resultse.jpg)

### Plot of RTL vs Reference Data for output before non-linearity
![Ref vs RTL](results2.jpg)
       |
### Error between Rtl and Reference Data before non-linearity
       |
![RTL](results2e.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/testNeuronStage.v)
* [Test Wrapper (C++)](../test/testNeuronStage.cpp)
* [Test Generator](../../../src/test/scala/com/simplifide/generate/neural//NeuronStateTest.scala)
* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//NeuronStage.scala)



        


