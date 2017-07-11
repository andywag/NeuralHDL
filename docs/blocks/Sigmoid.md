# Sigmoid Function

The sigmoid function is a standard nonlinearity used for neurons. A hardware approximation of this function is shown below as an example. The approach uses 7 stage piecewise linear approximation.


This block contains a sigmoid nonlinear operation based on "Myers and Hutchinson" piecewise linear approximation.
There are a few slight differences in the operation to simplify things due to the floating point aspect but
there output error has similar properties with a maximum error of ~4.8%. This block is more naturally done using
fixed point which inherently is internally done in the internal shifters of this block


## Generator Code

The code used to generate this code is relatively complex and can be found below. Most of this complexity is related to the use of floating point and the effects of this operation around the zero crossing around an input of .5. This could be much simpler for a fixed point operation and given the magnitude of the error it would be correct to use fixed point.

* [Code Generator](https://github.com/andywag/NeuralHDL/tree/master/src/main/scala/com/simplifide/generate/blocks/neural//Sigmoid.scala)
* [Verilog Output](https://github.com/andywag/NeuralHDL/tree/master/tests/sigmoid/design/sigmoid.v)

## Testbench Code

* [Testbench (Verilog)](https://github.com/andywag/NeuralHDL/tree/master/tests/sigmoid/test/testSigmoid.v)
* [Test Wrapper (C++)](https://github.com/andywag/NeuralHDL/tree/master/tests/sigmoid/test/testSigmoid.cpp)
* [Test Generator](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural//SigmoidTest.scala)


