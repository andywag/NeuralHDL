# Getting Started

The code generator is writtin in Scala and uses SBT as a build tool.  

* [SBT](http://www.scala-sbt.org/) : Build tool to create cores and tests
* [Jetbrains IDEA](https://www.jetbrains.com/idea/) : Integrated Development Environment for editting

The resulting designs are simulated and verified using open source tools. 

1. [Verilator] (https://www.veripool.org/wiki/verilator) : Free Verilog simulator
1. Python with Numpy and Scipy

## Running Basic Test

Both the generation and testing of the resulting cores is handled by scalaTest. 

* sbt test-only `test-name` --- `sbt test-only com.simplifide.generate.neural.DoubleStageTest`

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



