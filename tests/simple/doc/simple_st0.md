
# simple_st0


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
* output stage_0_data_out    : Output of the block following the equal to dataIn*taps + bias
* output stage_0_data_out_pre : Output of the block before the non-linearity (for testing)

* input ReadyValidInterface(com.simplifide.generate.signal.sv.ReadyValid$ReadyValidSignal@7ea4d397)   : Data Input of the Block
* input ReadyValidInterface(com.simplifide.generate.signal.sv.ReadyValid$ReadyValidSignal@49298ce7)     : Neural Tap input of the Block
* Various other input controls are input to this block to configure the lengths used for controlling the
* MAC units

## Subblocks

This block contains 3 major subblocks

* A control section which addresses the memories and drives the Neurons
* A memory unit which contains memory for the data, tap and bias
* A set of neuron units (MAC units) along with a nonlinearity (currently sigmoid)

## Reference Code

* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//NeuralStageTop.scala)
* [Top Verilog Output](../design/simple_st0.v)
* [Verilog Folder](../design)





