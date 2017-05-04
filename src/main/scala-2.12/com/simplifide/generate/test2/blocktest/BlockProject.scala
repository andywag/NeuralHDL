package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.test2.{Test, TestEntityParser}

/**
  * Created by andy on 5/3/17.
  */
class BlockProject(test:BlockTestParser)(implicit val clk:ClockControl) extends com.simplifide.generate.project.Project{
  /** Base Location of the ProjectGenerator */
  override val location: String = s"tests/${test.blockName}"

  override val newRoot  = test.dutParser.createEntity
  val testCase          = test
  override val tests    = List(Test(testCase.createEntity))


}
