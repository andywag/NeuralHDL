package com.simplifide.generate.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.blocks.neural.Neuron
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

class NeuronTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "neuron"

  val dutParser = new NeuronTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  //override val dut = dutParser.createEntity
  /** Design Under Test */
  val in1    = dutParser.dataIn <-- RANDOM
  val in2    = dutParser.tapIn <-- RANDOM
  val biasIn = dutParser.biasIn <-- RANDOM


  dutParser.dataOut --> ( (in1 * in2)+ biasIn, "Neuron")

}

object NeuronTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {
    val dataIn    = signal(FloatSignal("data",INPUT))
    val tapIn     = signal(FloatSignal("tap",INPUT))
    val biasIn    = signal(FloatSignal("bias",INPUT))

    val dataOut   = signal(FloatSignal("out",OUTPUT))

    ->(new Neuron(dataOut, dataIn, tapIn, biasIn))

  }
}