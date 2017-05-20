
# sigmoid


This block contains a sigmoid nonlinear operation based on "Myers and Hutchinson" piecewise linear approximation.
There are a few slight differences in the operation to simplify things due to the floating point aspect but
there output error has similar properties with a maximum error of ~4.8%. This block is more naturally done using
fixed point which inherently is internally done in the internal shifters of this block


## Input/Output
* output out      - float_24_8   : Output of the block
* input  data      - float_24_8    : Input of the block

## Generator Code

The code used to generate this code is relatively complex

* [Code Generator](../../../src/main/scala/com/simplifide/generate/blocks/neural//Sigmoid.scala)





