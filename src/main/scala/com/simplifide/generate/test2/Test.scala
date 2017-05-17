package com.simplifide.generate.test2

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{NewEntity, Project}
import java.io.File

import com.simplifide.generate.TestConstants
import com.simplifide.generate.test2.verilator.TestCWrapper

/**
 *  Test Cases
 */

trait Test {

  /** Test Entity */
  val testBench:NewEntity

  def postRun = {}



  def createTest(project:Project) = {
    testBench.writeModule(project.projectStructure.test)
    val wrap = new TestCWrapper(testBench.name)
    wrap.writeCode(s"${project.projectStructure.test}/${testBench.name}.cpp")
  }





  def runTest(project:Project, testType:SimInterface) = {
    def allFiles =  project.designFiles ::: List(new File(project.projectStructure.testDirectory,testBench.name + ".v"))
    testType.createSimFiles(allFiles.map(_.getAbsolutePath))
    testType.compile(testBench)
    testType.run(testBench)
    this.postRun
  }

}

object Test {
  def apply(dut:NewEntity)(implicit clk:ClockControl) = new Impl(dut)

  class Impl(override val testBench:NewEntity) extends Test

}

