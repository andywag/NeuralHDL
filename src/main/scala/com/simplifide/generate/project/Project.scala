package com.simplifide.generate.project

import com.simplifide.generate.parser.{EntityParser, SignalHolder}
import com.simplifide.generate.test2.SimInterface
import com.simplifide.generate.test2.Test
import java.io.File

import com.simplifide.generate.doc.{MdEntity, MdGenerator, MdProject}
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

  /** @deprecated : List of Tests for the project */
  val tests:List[Test] = List()
  def rootTests        = tests

  /** @deprecated : Top Level Entity for this project*/
  val newRoot:NewEntity = null
  def rootEntity = newRoot

  def allEntities = rootEntity.children ::: List(rootEntity)

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


  def cleanProject = projectStructure.clean
  def createProjectStructure = projectStructure.create


  lazy val totalEntities = {
    // Expand the modules and connect the signals -- Emacs Auto Type Stuff
    val expanded = rootEntity.connect
    // Create a total list of entities in the design
    val total = List(expanded) ::: expanded.children
    total
  }

  def createProject = {

    projectStructure.create
    // Write out all of the verilog modules
    totalEntities.foreach(x => x.writeModule(projectStructure.design))
    // Write out the documentation for all the verilog modules
    new MdProject(this).create(projectStructure.doc)
    //totalEntities.foreach(x => new MdEntity(x).create(s"${projectStructure.doc}/${x.name}.md"))
    // Create the tests for this project
    rootTests.foreach(x => x.createTest(this))

    // Create extra verilog file which contains the structures used in this project
    val types = handleTypes(totalEntities)
    Project.createTypeModule(projectStructure.design,types)

  }

  def runTests(testType:SimInterface) {
    //def createFiles = 
    rootTests.foreach(_.runTest(this,testType))
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