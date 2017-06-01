
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



        