package com.simplifide.generate.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.{NeuralMemory, Neuron}
import com.simplifide.generate.memory.MemoryTest
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.util.PathUtilities

/**
  * Created by andy on 5/23/17.
  */
class NeuronMemoryTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "neuralMemory"

  val dutParser = new NeuronMemoryTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity



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

object NeuronMemoryTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {
    val dims = NeuralMemory.Dimensions((128,128),32,16)
    val nueralMemory = NeuralMemory(name,dims)

    val sig = ->(nueralMemory)
    internalInstances.appendAll(sig.instances)
    signals.appendAll(sig.signals)
  }

}
