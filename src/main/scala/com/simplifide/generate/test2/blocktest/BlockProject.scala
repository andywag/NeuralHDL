package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.project.{Project, ProjectStructure}
import com.simplifide.generate.test2.{Test, TestEntityParser}

/**
  * Created by andy on 5/3/17.
  */
class BlockProject(test:BlockTestParser)(implicit val clk:ClockControl) extends Project{
  /** Base Location of the ProjectGenerator */
  override val location: String = s"tests/${test.blockName}"


  override val newRoot  = null //test.dutParser.createEntity

  lazy val rootEntity1 = test.dutParser.createEntity
  override def rootEntity        = rootEntity1

  val testCase          = test
  lazy val rootTests1    = List(Test(testCase.createEntity))
  override def rootTests = rootTests1

}

object BlockProject {
  def getStructure(testName:String) = {
    ProjectStructure.project(s"tests/${testName}")
  }

}
