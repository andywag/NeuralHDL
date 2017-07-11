# Neural Stage
The example below is a fully connected neural network stage. This is the most basic example where the number of MAC units available is the same as the input and output but the block itself is programmable to handle generic configurations.

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


## Generator Code

* [Code Generator](https://github.com/andywag/NeuralHDL/tree/master/src/main/scala/com/simplifide/generate/blocks/neural/NeuralStage.scala)
* [Verilog Output](https://github.com/andywag/NeuralHDL/tree/master/tests/neural_stage/design/neural_stage.v)

## Testbench Code

* [Test Generator](https://github.com/andywag/NeuralHDL/tree/master/src/test/scala/com/simplifide/generate/neural/NeuralStageTest.scala)

