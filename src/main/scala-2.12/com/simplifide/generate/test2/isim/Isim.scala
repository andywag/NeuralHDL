package com.simplifide.generate.test2.isim

import com.simplifide.generate.TestConstants
import com.simplifide.generate.project.{NewEntity, Project}
import com.simplifide.generate.test2.SimInterface
import com.simplifide.generate.util.{Logger, ProcessOps, FileOps}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 9/26/11
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */


class Isim(val project: Project) extends SimInterface {

  val separator = "/"

  val PROJECTFILE = "files.prj"
  val TCLFILE = "test.tcl"
  val LOGFILE = "test.log"

  val testLocation = project.projectStructure.test

  def projectFile = project.projectStructure.test + separator + PROJECTFILE

  def createSimFiles(designFiles: List[String]) = {
    val totalFiles = project.extraFiles ::: designFiles
    def contents = totalFiles.map(x => "verilog work " + x + "\n").reduceLeft(_ + _)
    FileOps.createFile(testLocation + separator, PROJECTFILE, contents)
    FileOps.createFile(testLocation + separator, TCLFILE, "run all;\nexit;\n")
  }

  def compile(entity: NewEntity) = {
    def command = TestConstants.fuseLocation + " work." + entity.name + " -prj " + projectFile + " -o " + testLocation + separator + entity.name
    ProcessOps.exec(command, Some(testLocation))(ln => Logger.booleanMessage(ln))
  }

  def run(entity: NewEntity) = {
    val command = testLocation + separator + entity.name + ".exe" + " -tclbatch " + TCLFILE + " -log " + LOGFILE
    ProcessOps.exec(command, Some(testLocation))(ln => Logger.booleanMessage(ln))
  }


}

object Isim


