# Language

The blocks for this design are written in an embedded DSL inside scala. The purpose of this language is to
allow fine grained control of the hardware generation while allowing the power of a high level language to
allow parameterization and simulation control. The examples shown in this section will detail some the syntax
of the language used. This is is an internal portion of the design which is not meant to be exposed to the end
user unless they want to extend the generated cores.

## Examples

The examples shown below are not full level examples of neural networks but show the method and structure which is used for creating and testing the building blocks. This is not the planned API for the end user but give an example of the underlying structures which will be built upon to create that API.

Higher level functionallity will be added as the building block development progresses so less hardare savvy designers can create architectures by specifying parameters.

As a starting point most of the operations are done using floating point. The reason for this is avoid quantization and numerical issues in the development. Floating point operations were added for this specific purpose as the initial language design was completely fixed point. The designs will be generalized to any number system as a second step. The language chooses operations internally based on the signal type so it should be a simple exercise.

### Sigmoid Function

The sigmoid function is a standard nonlinearity used for neurons. A hardware approximation of this function is shown below as an example. The approach uses 7 stage piecewise linear approximation.

[Sigmoid](https://github.com/andywag/NeuralHDL/blob/master/tests/sigmoid/doc/sigmoid_proj.md)

### Neuron

The example below is a basic building block for the design. It is actually a MAC rather than a neuron as it only contains the multiply accumulate operation without a nonlinearity and external control. This is required to allow a more general architecture.

[Neuron](https://github.com/andywag/NeuralHDL/blob/master/tests/neuron/doc/neuron_proj.md)

### Neural Stage

The example below is a fully connected neural network stage. This is the most basic example where the number of MAC units available is the same as the input and output but the block itself is programmable to handle generic configurations.

[Neural Stage](https://github.com/andywag/NeuralHDL/blob/master/tests/neural_stage/doc/neural_stage_proj.md)

### Neural Stage with Memory and Control

The example below is a fully connected neural stage which contains the memory and control for dealing with both I/O and interfacing with an external block. It is fully programmable like the other designs.

[Neural Complete Stage](https://github.com/andywag/NeuralHDL/blob/master/tests/stage/doc/stage_proj.md)

