package com.simplifide.generate.float

import com.simplifide.generate.blocks.float.{FloatAddition, FloatMult}
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

/**
  * Created by andy on 5/4/17.
  */
class FloatAdditionRandomTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "floatAddRandom"

  val dutParser = new FloatAddition.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  //override val dut = dutParser.createEntity
  /** Design Under Test */
  val in1 = dutParser.in1 <-- RANDOM
  val in2 = dutParser.in2 <-- RANDOM

  dutParser.out1 --> (in1 + in2, "Adder")



}