
# testSigmoid


This module is a block test wrapper for the sigmoid block. The block tests the output of this block
against a linear ramp over a range of -9.0 to 9.0. The output of this is matched
against a reference model which give a maximum error of around 4.8%.

## Test Results

### Plot of RTL vs Reference Data

![Ref vs RTL](discrim.jpg)

### Actual Difference between Rtl and Reference Data
![RTL](discrime.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/testSigmoid.v)
* [Test Wrapper (C++)](../test/testSigmoid.cpp)
* [Test Generator](../../../src/test/scala/com/simplifide/generate/neural//SigmoidTest.scala)
* [Code Generator](../../../src/main/scala/com/simplifide/generate/neural//Sigmoid.scala)



        