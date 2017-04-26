package com.simplifide.generate.project

import com.simplifide.generate.parser.SignalHolder
import com.simplifide.generate.test2.SimInterface
import com.simplifide.generate.test2.Test
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 8/12/11
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */

/** ProjectGenerator which contains a set of modules in the design*/
trait Project extends SignalHolder{

  /** Base Location of the ProjectGenerator */
  val location:String
  /** Structure Defining where the project outputs are written */
  val projectStructure = ProjectStructure(this)
  /** Base Entity for the ProjectGenerator */

  /** List of Tests for the project */
  val tests:List[Test] = List()
  /** Top Level Entity for this project */
  val newRoot:NewEntity = null
  
  def allEntities = newRoot.children ::: List(newRoot)

  def designFiles:List[java.io.File] =
    allEntities.map(x => x.fileLocation(projectStructure.designDirectory))//new File(projectStructure.designDirectory,x.name + ".v"))

  val extraFiles:List[String] = List()


  def createProject = {

    projectStructure.create
    // Expand the modules and connect the signals -- Emacs Auto Type Stuff
    val expanded = newRoot.connect
    // Create a total list of entities in the design
    val total = List(expanded) ::: expanded.children
    // Write out all of the verilog modules
    total.foreach(x => x.writeModule(projectStructure.design))
    // Create the tests for this project
    tests.foreach(x => x.createTest(this))

  }

  def runTests(testType:SimInterface) {
    //def createFiles = 
    tests.foreach(_.runTest(this,testType))
  }


}

object Project {


}