package com.simplifide.generate.project

import com.simplifide.generate.util.FileOps
import java.io.File


/**
 * Structure for the project which contains
 */
trait ProjectStructure {

  val separator = "/"

  val project:Project
  /** Location where the design sources are stored */
  val designLocation:String
  /**Location where the documents are stored */
  val docLocation:String
  /** Location where test cases are stored */
  val testLocation:String
  /** */
  val dataLocation:String

  def design = project.location + separator + designLocation
  def doc    = project.location + separator + docLocation
  def test    = project.location + separator + testLocation
  def data    = project.location  + separator + dataLocation

  def designDirectory:File = new File(project.location,designLocation)
  def docDirectory:File    = new File(project.location,docLocation)
  def testDirectory:File   = new File(project.location,testLocation)
  def dataDirectory:File   = new File(project.location, dataLocation)



  def create = {
    FileOps.createDirectory(project.location)
    FileOps.createDirectory(design)
    FileOps.createDirectory(doc)
    FileOps.createDirectory(test)
    FileOps.createDirectory(data)

  }

}

object ProjectStructure {
  val DESIGN = "design"
  val DOC    = "doc"
  val TEST   = "test"
  val DATA   = "data"

  def apply(project:Project,
            designLocation:String = DESIGN,
            docLocation:String = DOC,
            testLocation:String = TEST,
            dataLocation:String = DATA) = new User(project,designLocation,docLocation,testLocation, dataLocation)

  class User(override val project:Project,
    override val designLocation:String = DESIGN,
    override val docLocation:String    = DOC,
    override val testLocation:String   = TEST,
    override val dataLocation:String = DATA) extends ProjectStructure


}