# Getting Started

The tool is written in Scala and uses SBT as a build tool as well as Scalatest for testing. 

The minimal requirements for creating the cores is : 

* [SBT](http://www.scala-sbt.org/)  Scala build tool which will automatically download test configuration
* [Scala](https://www.scala-lang.org/) Scala is also required but might be automatically downloaded through SBT

Optionally an IDE is useful and the distribution contains a project for IDEA. 

* [Jetbrains IDEA](https://www.jetbrains.com/idea/) : Integrated Development Environment for editting

The tool assumes uses the following tools for simulation and verification. 

* [Verilator] (https://www.veripool.org/wiki/verilator) : Free Verilog simulator
* Python with Numpy and Scipy : Used for analysis and plotting of results

Other simulators should work as well but will require setup a bit of setup, test bench conversion and potentially some generator fixes. The target RTL was used for a production FPGA but new constructs have been added and the cores have not been taken through a synthesis flow. 

## Running Basic Test

Running tests can be done either on the command line using SBT or inside IDEA. The command line usage is shown below.

* sbt test-only `test-name`

A couple of examples are shown below which run basic tests. 


* sbt test-only `com.simplifide.generate.neural.Two6x6x6`
* sbt test-only `com.simplifide.generate.neural.Four12`

An example section of the test code is shown below. 

```scala

class Two6x6x6 extends TwoStageTest {
  override def blockName: String = "two6x6"
  override lazy val dimensions = Seq((6,6),(6,6))
  override def waveformEnable = true
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val plot:Boolean = true
  override val failThreshold = Some(0.1)
}
```

An example test for a two stage network is shown above. There are multiple options for the operation of the test including plotting of the results when complete as well as waveform generation. 



