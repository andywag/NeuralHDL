# Neuron

The example below is a basic building block for the design. It is actually a MAC rather than a neuron as it only contains the multiply accumulate operation without a nonlinearity and external control. This is required to allow a more general architecture.

## Generator Code

The code to generate this block is relatively straightforward. It simply contains a multiplier and an adder. The
appropriate adder is selected by the type of the input. *The use of times and use of plus rather than */+ is due
to a recent change in the way these operations are handled and is temporary.*

```scala

internalSignal := dataIn times taps
dataOut        := internalSignal plus bias

```

## Reference Code

* [Code Generator](https://github.com/andywag/NeuralHDL/tree/master/src/main/scala/com/simplifide/generate/blocks/neural//Neuron.scala)
* [Verilog Output](https://github.com/andywag/NeuralHDL/blob/master/tests/neuron/design/neuron.v)


## Reference Code for Test
* [Testbench (Verilog)](https://github.com/andywag/NeuralHDL/tree/master/tests/neuron/test/testNeuron.v)
* [Test Wrapper (C++)](https://github.com/andywag/NeuralHDL/tree/master/tests/neuron//test/testNeuron.cpp)
* [Test Generator](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural//NeuronTest.scala)
* [Code Generator](https://github.com/andywag/NeuralHDL/tree/master/src/main/scala/com/simplifide/generate/blocks/neural//Neuron.scala)