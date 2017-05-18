package com.simplifide.generate.project

import com.simplifide.generate.parser.{EntityParser, SignalHolder}
import com.simplifide.generate.test2.SimInterface
import com.simplifide.generate.test2.Test
import java.io.File

import com.simplifide.generate.generator.CodeWriter.Verilog
import com.simplifide.generate.generator.SegmentReturn
import com.simplifide.generate.project.Project.TypeModule
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.Struct
import com.simplifide.generate.signal.sv.Struct.StructDeclaration
import com.simplifide.generate.util.FileOps

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
  lazy val projectStructure = ProjectStructure(this)
  /** Base Entity for the ProjectGenerator */

  /** List of Tests for the project */
  val tests:List[Test] = List()
  /** Top Level Entity for this project */
  val newRoot:NewEntity = null
  
  def allEntities = newRoot.children ::: List(newRoot)

  def designFiles:List[java.io.File] =
    allEntities.map(x => x.fileLocation(projectStructure.designDirectory))//new File(projectStructure.designDirectory,x.name + ".v"))

  val extraFiles:List[String] = List()


  def handleTypes(entities:List[NewEntity]) = {
    def getType(input:SignalTrait):Option[Struct] = {
      input match {
        case x:Struct => Some(x)
        case _        => None
      }
    }
    val signals = entities.flatMap(x => x.signals)
    val types1 = signals.flatMap(getType(_)).map(x => (x.typeName,x)).toMap
    val types  = types1.values.toSeq
    types

  }


  def cleanProject = {

    projectStructure.clean

  }

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
    // Create
    val types = handleTypes(total)
    Project.createTypeModule(projectStructure.design,types)

  }

  def runTests(testType:SimInterface) {
    //def createFiles = 
    tests.foreach(_.runTest(this,testType))
  }


}

object Project {

  def createTypeModule(location:String, types:Seq[Struct]) = {
    val code = types.map(x => new StructDeclaration(x)).map(x => Verilog.createCode(x))
    val total = code.foldLeft(SegmentReturn(""))(_+_)
    FileOps.createFile(location,"types.v",total.code)
  }

  case class TypeModule(types:Seq[Struct]) extends EntityParser {

    override val name: String = "types"
    types.foreach(x => ->(new StructDeclaration(x)))


  }

}