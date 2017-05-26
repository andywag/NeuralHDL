package com.simplifide.generate.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.NeuronControl
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}

/**
  * Created by andy on 5/26/17.
  */
class NeuralControlTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  //override implicit val clk: ClockControl = ClockControl("clk","reset")

  override def blockName: String = "nueronControl"

  val dimension = NeuronControl.Dimension(10,6,7)

  override val dutParser = NeuronControl(blockName,dimension)
  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  dutParser.start       := (index === 5) ? 1 :: 0;
  dutParser.dataLength  := 1023
  dutParser.biasLength  := 127
  dutParser.stateLength := 63
}

object NeuralControlTest {

}
