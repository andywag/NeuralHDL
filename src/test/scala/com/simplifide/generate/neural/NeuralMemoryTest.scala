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
