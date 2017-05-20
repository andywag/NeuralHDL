
# testSigmoid


This module is a block test wrapper for the sigmoid block. The block tests the output of this block
against a linear ramp over a range of -9.0 to 9.0. The output of this is matched
against a reference model.

## Test Results

The results for this test are shown below :
![Ref vs RTL](discrim.jpg)
The error for this relative to the output is shown below :
![RTL](discrime.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/testSigmoid.v)
* [Test Wrapper (C++)](../test/testSigmoid.cpp)
* [Code Generator](../)

    

        