# Basic Examples

The purpose of the examples in this section are to prove out the hardware design created by generated and not delve into the signal processings of the network. All of the networks created use the same basic infrastructure which are generalized to test network configurations. The examples also contain building block tests which are used to verify the functionallity of the network by testing each stage individually. The examples shown below starts with a single stage network and then is expanded to larger networks. 

The first test created was based on a Braille example shown below so many of the tests use configurations based on these sizes. This example was chosen since it was not based on a power of 2 and had unity outputs which made debugging of the design more straightforward. 

[Braille Example](http://neuroph.sourceforge.net/tutorials/Braille/RecognitionOfBrailleAlphabetUsingNeuralNetworks.html)

## Example Configuration

The framework for all designs is common so each of the examples below are based on using the same design and setting. Each example will contain links to the 

* The test results which is a jupyter notebook results which describes the converging of the network
* The results directory which contains the generated rtl
* The scala code used to generate the network

Given the nature of the tests and number of configurations, the scala code for the tests is being refactored down to only the smallest changes for the tests. The basic building blocks for the network configurations can be found in the following locations. 

* [Test Generator Location](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural)
* [Main Generator Test Block](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural/BasicNetworkTest.scala)

The generator code is based on an existing generic DSL for RTL generation and is a bit more complicated. The basic building blocks for the neural network blocks ban be found in the following location : 

* [Generator Location](https://github.com/andywag/NeuralHDL/tree/master/src/main/scala/com/simplifide/generate/blocks/neural)

## Basic Example Testing

The testing of the blocks is done using : 

* Scala Framework : Generates the hardware and the test framework
* Verilator       : Runs a verilog simulator when the test is complete
* Numpy           : Analyzes the results by looking at the output, taps, and error. Declares an error when the MSE of the error is greater than a user defined threshold

## Single Stage Neural Network

This is a simple test case which contains a network with a single stage with 6 inputs, 12 outputs utilizing 6 MAC blocks.

```mermaid
graph LR
    Stage0[Stage - 6x12]
    Input-->Stage0
    Stage0-->Error
    Stage0-->Output
    Error-->Stage0
```

* [Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/SingleStage.ipynb)
* [Output Directory - Generated Source and Results](https://github.com/andywag/NeuralHDL/tree/master/tests/simple)
* [Block Generator](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural/SingleStageTest.scala)

## Two Stage Neural Network

This is a simple test case which contains a network with 2 fully connected stages.

1. The first stage has 6 inputs and 12 outputs and uses 6 MAC units
1. The second stage has 12 inputs and 6 outputs and uses 6 MAC units


```mermaid
graph LR
    Stage0[Stage - 6x12]
    Stage1[Stage - 12x6]
    Input-->Stage0
    Stage0-->Stage1
    Stage1-->Stage0
    Stage1-->Error
    Stage1-->Output
    Error-->Stage1
```


* [Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/TwoStage.ipynb)
* [Output Directory - Generated Source and Results](https://github.com/andywag/NeuralHDL/tree/master/tests/full)
* [Block Generator](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural/DoubleStageTest.scala)

## Three Stage Neural Network

This is the first realistic test case which contains a 3 stage network with 12 hidden neurons.


1. The first stage has 6 inputs and 12 outputs and uses 6 MAC units
1. The second stage contains 12 inputs and 12 outputs and uses 6 MAC units
1. The third stage has 12 inputs and 6 outputs and uses 6 MAC units


```mermaid
graph LR
    Stage0[Stage - 6x12]
    Stage1[Stage - 12x12]
    Stage2[Stage - 12x6]
    Input-->Stage0
    Stage0-->Stage1
    Stage1-->Stage2
    Stage2-->Stage0
    Stage2-->Error
    Stage2-->Output
    Error-->Stage2
```


* [Results](results/DoubleStage.jpynb)
* [Output Directory - Generated Source and Results](../../tests/hidden)
* [Block Generator](../../src/test/scala/com/simplifide/generate/neural/HiddenStageTest.scala)
