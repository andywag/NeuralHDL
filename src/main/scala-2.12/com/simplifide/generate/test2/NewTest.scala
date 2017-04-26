package com.simplifide.generate.test2

import com.simplifide.generate.project.{Project, NewEntity}
import java.io.File
import com.simplifide.generate.blocks.basic.flop.ClockControl

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */

trait NewTest {

  /** Name of the Test */
  val testName:String

  /** Test Entity */
  val testBench:NewEntity

  /** Base Project for Testing */
  val project:Project

  /** Location where the test will be run */
  val location:File

  /** Post Run Operations */
  def postRun:Boolean = false



  /** Location of the Source Directory */
  def sourceDirectory = new File(location,"src")
  /** Location of the Waveform Directory */
  def waveDirectory   = new File(location,"wave")
  /** Test Bench Location */
  def testBenchLocation = new File(sourceDirectory,testBench.name + ".v")
  /** List of all files in the project */
  def allFiles =
    project.designFiles.map(_.getAbsolutePath) ::: project.extraFiles ::: List(testBenchLocation.getAbsolutePath)
  
  private def createWaveFiles(interface:NewSimInterface) = {
    def createSingleFile(entity:NewEntity,path:String,name:String)  {
      def pathName(path:String, name:String) = path + name
      def fileName(path:String, name:String) = name//pathName(path,name).substring(1).replace("/","-")
      interface.createWaveFile(entity,path,new File(waveDirectory,name + ".tcl"))
      entity.instances.foreach(x => createSingleFile(x.entity.asInstanceOf[NewEntity],pathName(path,x.name) + "/",fileName(path,x.name)))
    }
    createSingleFile(testBench,"/"+testBench.name + "/",testBench.name)

  }
  /** Create this test case */
  def createTest(interface:NewSimInterface) = {
    location.mkdir(); sourceDirectory.mkdir(); waveDirectory.mkdir()
    testBench.writeModule(sourceDirectory.getAbsolutePath) // Create the Test Bench for this Test
    interface.createSimFiles(allFiles, location.getAbsolutePath)
    createWaveFiles(interface)
  }

  def compile(interface:NewSimInterface):Boolean =
    interface.compile(testBench.name,location.getAbsolutePath)


  def run(interface:NewSimInterface):Boolean = {
    interface.run(testBench.name, location.getAbsolutePath)
    this.postRun
  }

  def runAll(interface:NewSimInterface):Boolean = {
    var error = false
    createTest(interface)
    error = compile(interface)
    if (!error) error = run(interface)
    error
  }
  /*
  def runTest(project:Project, testType:SimInterface) = {
    def allFiles =  project.designFiles ::: List(new File(sourceDirectory,testBench.name + ".v"))
    testType.createSimFiles(allFiles.map(_.getAbsolutePath))
    testType.compile(testBench)
    testType.run(testBench)
    this.postRun
  }
  */


}

object NewTest {
  def apply(testBench:NewEntity,project:Project)(implicit clk:ClockControl) = new Impl(testBench,project)
  def apply(testBench:NewEntity,project:Project,location:File)(implicit clk:ClockControl) = new WithLocation(testBench,project,location)

  
  class Impl(override val testBench:NewEntity, override val project:Project) extends NewTest {
    override val location = new File(project.location,"test")
    override val testName = testBench.name
  }
  class WithLocation(testBench:NewEntity, project:Project, override val location:File) extends Impl(testBench,project)

}

