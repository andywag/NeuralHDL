package com.simplifide.generate.test2

import com.simplifide.generate.project.Project
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */

trait TestSuite {

  /** Base Location for the tests */
  val location:File
  /** Project which is tested by this suite */
  //val project:Project
  /** List of Tests associated with this project */
  val tests:List[NewTest]

  /** Create the test cases associated with this project */
  def createTests(simInterface:NewSimInterface) = 
    tests.foreach(_.createTest(simInterface))
  
  /** Compile the Test Cases */
  def compile(simInterface:NewSimInterface) =
    tests.foreach(_.compile(simInterface))

  def runTests(simInterface:NewSimInterface) =
    tests.foreach(_.run(simInterface))


  def runAll(simInterface:NewSimInterface) = {
    tests.foreach(_.runAll(simInterface))
  }


}

object TestSuite {
  
  def apply(location:File, project:Project, tests:List[NewTest]) = new TestSuite.Impl(location,project,tests)
  class Impl(val location:File,  val project:Project,  val tests:List[NewTest]) extends TestSuite
  
}
