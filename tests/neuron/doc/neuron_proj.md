
# neuron

This document details the block design of neuron. It starts out with a description of the design followed
by the test descriptions and results.


This block contains a simple neuron (actually a MAC) which contains a multiplier and an adder. The
true nueron operation is handled externally by appropriately driving the MAC unit. The block has 2 delays.
There is one delay for the multiplier output and one delay for the addition block. The bias delay is expected
to be taken care of outside the block to simplify the input interface to this block.

## Input/Output
* output out      - float_24_8  : Output of the block following the equal to dataIn*taps + bias

* input data      - float_24_8    : Data Input of the Block
* input tap      - float_24_8      : Neural Tap input of the Block
* input bias      - float_24_8      : Bias Input of the Block (Needs to be delayed by 1 sample relative to other inputs

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
* [Verilog Output](../design/.v)




# testNeuron


This module is a block test wrapper for the a floating point neuron block. This is a random test using a Gaussian input
over the data, taps and bias. All the inputs and outputs are floating point.

## Test Results

### Plot of RTL vs Reference Data

![Ref vs RTL](results.jpg)

### Difference between Rtl and Reference Data
![RTL](resultse.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/testNeuron.v)
* [Test Wrapper (C++)](../test/testNeuron.cpp)
* [Test Generator](../../../src/test/scala/com/simplifide/generate/neural//NeuronTest.scala)
* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//Neuron.scala)



        


