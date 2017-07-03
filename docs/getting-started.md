## Getting Started

All that is required for this project is scala but other tools are useful for testing of the blocks. The
current tools used for this project are :

1. [SBT](http://www.scala-sbt.org/) : Build tool to create cores and tests
1. [Jetbrains IDEA](https://www.jetbrains.com/idea/) : Integrated Development Environment for editting
1. [Verilator] (https://www.veripool.org/wiki/verilator) : Free Verilog simulator
1. [Python with Numpy]

<br>
The tools above are optional for generating the RTL and test benches. The examples currently
are using them for both simulation and testing so are required for that purpose. The examples can
be run either on the command line or though the idea. The command line syntax is shown below.
<br>

sbt test-only `test-name>`
<br>
Example : `sbt test-only com.simplifide.generate.neural.DoubleStageTest`

## Infrastructure Limitations

The current framework is in an alpha state so there is the potential for issues with setup. There
are also some run issues which are currently being addressed. The main one is the tests were initially
designed to be completley handled in scala. Scala analysis tools are not at the level of numpy so the future
top level will probably be run through python. Currently running is a 2 step process which requires generating
and runnign the code in scala and testing in python.

<br>
To run the full code and test for this example follow the steps below

1. `sbt test-only com.simplifide.generate.neural.DoubleStageTest`
2. `python python/test/CheckDoubleNetwork.py`