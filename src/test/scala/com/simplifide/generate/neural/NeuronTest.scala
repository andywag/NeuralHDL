package com.simplifide.generate.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.blocks.neural.Neuron
import com.simplifide.generate.model.{DataFileGenerator, NdUtils}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.factory.Nd4j

class NeuronTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "neuron"

  val dutParser = new NeuronTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  import org.nd4s.Implicits._
  import com.simplifide.generate.model.NdArrayWrap._



  val data = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data",DataFileGenerator.RANDOM)
  val tap  = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/tap",DataFileGenerator.RANDOM)
  val bias = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/bias",DataFileGenerator.RANDOM)


  val result = ((tap.data*data.data).d(1) + bias.data.a(1)).d(1)

  val out  = DataFileGenerator.createFlatten(s"$dataLocation/out",result)

  /- ("Delay the bias to line up the data")
  delayIndex := index - 1;

  val dataInD   = dutParser.dataIn <-- data
  val tapInD    = dutParser.tapIn  <-- tap
  val biasInD   = dutParser.biasIn <-- bias

  val rout = dutParser.dataOut ---> (s"$dataLocation/rout", Some(out), "Neuron Output")



  override def postRun = {
    val output = rout.load()
    val error = PlotUtility.plotError(output.data().asDouble(),
      result.data().asDouble(),Some(s"$docLocation/results"))
    assert(error.max < .05)
  }


  override def document =
    s"""
This module is a block test wrapper for the a floating point neuron block. This is a random test using a Gaussian input
over the data, taps and bias. All the inputs and outputs are floating point.

## Test Results

### Plot of RTL vs Reference Data

![Ref vs RTL](results.jpg)

### Difference between Rtl and Reference Data
![RTL](resultse.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/${name}.v)
* [Test Wrapper (C++)](../test/${name}.cpp)
* [Test Generator](${PathUtilities.nueralTestPath}/NeuronTest.scala)
* [Code Generator](${PathUtilities.nueralPath}/Neuron.scala)

"""


}

object NeuronTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {
    signal(clk.allSignals(INPUT))
    val dataIn    = signal(FloatSignal("data",INPUT))
    val tapIn     = signal(FloatSignal("tap",INPUT))
    val biasIn    = signal(FloatSignal("bias",INPUT))

    val dataOut   = signal(FloatSignal("out",OUTPUT))

    val neuron = ->(new Neuron(dataOut, dataIn, tapIn, biasIn))

    override val document = neuron.document

  }
}