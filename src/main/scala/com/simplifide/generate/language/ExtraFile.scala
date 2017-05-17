package com.simplifide.generate.language

import com.simplifide.generate.util.FileOps


/**
 * Extra file which is generated with the rest of the project
 */
trait ExtraFile {
  /** Contents of the generated file */
  val contents:String
  /** Name of the File to Generate */
  val filename:String

  /** Method which writes this file to the fileLocation fileLocation */
  def createFile(location:String) = {
    FileOps.createFile(location,filename,contents)
  }
}

object ExtraFile {

  def apply(filename:String,contents:String) = new Impl(filename,contents)
  
  class Impl(override val contents:String, override val filename:String) extends ExtraFile
  
}