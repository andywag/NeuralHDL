package com.simplifide.generate.float

import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

class FloatMultRandomTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "floatMultRandom"

  val dutParser = new FloatMult.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  //override val dut = dutParser.createEntity
  /** Design Under Test */
  val in1 = dutParser.in1 <-- RANDOM
  val in2 = dutParser.in2 <-- RANDOM

  dutParser.out1 --> (in1 * in2, "Multiplier")

}