# Getting Started

Scala is the only requirement for generation of the blocks but other tools listed below are used
in the testing of the resulting cores. Please install these tools before getting started.

1. [SBT](http://www.scala-sbt.org/) : Build tool to create cores and tests
1. [Jetbrains IDEA](https://www.jetbrains.com/idea/) : Integrated Development Environment for editting
1. [Verilator] (https://www.veripool.org/wiki/verilator) : Free Verilog simulator
1. Python with Numpy and Scipy


## Running Basic Test

Scalatest is used for running generating the cores and running basic tests. To run a test use the
following command or run the test inside the IDEA IDE. 

* sbt test-only `test-name`
* `sbt test-only com.simplifide.generate.neural.DoubleStageTest`

To validate there are python scripts to check the results. For the case shown above.  

* `python python/test/CheckDoubleNetwork.py`

## Future Directions

The initial goal of this project was to have the design/test be completely self contained in a single
environment (Scala). Scala has signal processing and math libraries but are not near the level of numpy. 
Because of this the testing is now a hybrid between scala and python but is long term going to move 
to python. 

* `The current framework is in an alpha state and not fully productized so there is the potential for some
issues with setup.` 

