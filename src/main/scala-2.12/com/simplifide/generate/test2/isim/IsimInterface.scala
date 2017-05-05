package com.simplifide.generate.test2.isim

import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.TestConstants
import com.simplifide.generate.test2.{NewSimInterface, SimInterface}
import java.io.File
import com.simplifide.generate.util.{InternalLogger, ProcessOps, FileOps}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */

object IsimInterface extends NewSimInterface {

  val PROJECTFILE = "files.prj"
  val TCLFILE = "test.tcl"
  val LOGFILE = "test.log"

  val separator = "/"

  def projectFile(testLocation:String) = testLocation + separator + PROJECTFILE


  def createSimFiles(totalFiles: List[String], testLocation:String) = {
    //val totalFiles = project.extraFiles ::: designFiles
    def contents = totalFiles.map(x => "verilog work " + x + "\n").reduceLeft(_ + _)
    FileOps.createFile(testLocation + separator, PROJECTFILE, contents)
    FileOps.createFile(testLocation + separator, TCLFILE, "run all;\nexit;\n")
  }

  def createWaveFile(entity:NewEntity,path:String,file:File):String = {
    new IsimWaveformCreator(entity,path).createFile(file)
    ""
  }


  def compile(rootModule:String, testLocation:String) = {
    def handleExpression(input:String):Boolean = {
      InternalLogger.message(input)
      if (input.startsWith("ERROR")) true else false
    }
    
    def command = TestConstants.fuseLocation + " work." + rootModule + " work.glbl -L unisims_ver -prj " + projectFile(testLocation) + " -o " + testLocation + separator + rootModule
    ProcessOps.exec(command, Some(testLocation))(ln => handleExpression(ln))
  }

  def run(rootModule:String, testLocation:String) = {
    val command = testLocation + separator + rootModule + ".exe" + " -tclbatch " + TCLFILE + " -log " + LOGFILE
    ProcessOps.exec(command, Some(testLocation))(ln => InternalLogger.booleanMessage(ln))
  }
}
