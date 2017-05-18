package com.simplifide.generate.project

import com.simplifide.generate.util.FileOps
import java.io.File


/**
 * Structure for the project which contains
 */
trait ProjectStructure {

  val separator = "/"

  //val project:Project
  val baseLocation:String
  /** Location where the design sources are stored */
  val designLocation:String
  /**Location where the documents are stored */
  val docLocation:String
  /** Location where test cases are stored */
  val testLocation:String
  /** */
  val dataLocation:String

  def design = baseLocation + separator + designLocation
  def doc    = baseLocation + separator + docLocation
  def test    = baseLocation + separator + testLocation
  def data    = baseLocation  + separator + dataLocation

  def designDirectory:File = new File(baseLocation,designLocation)
  def docDirectory:File    = new File(baseLocation,docLocation)
  def testDirectory:File   = new File(baseLocation,testLocation)
  def dataDirectory:File   = new File(baseLocation, dataLocation)



  def create = {
    FileOps.createDirectory(baseLocation)
    FileOps.createDirectory(design)
    FileOps.createDirectory(doc)
    FileOps.createDirectory(test)
    FileOps.createDirectory(data)
  }

  def clean = {
    FileOps.removeDirectory(baseLocation)
    //FileOps.createDirectory(baseLocation)
    //FileOps.createDirectory(design)
    //FileOps.createDirectory(doc)
    //FileOps.createDirectory(test)
    //FileOps.createDirectory(data)
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
            dataLocation:String = DATA) = new User(project.location,designLocation,docLocation,testLocation, dataLocation)

  def project(location:String,
            designLocation:String = DESIGN,
            docLocation:String = DOC,
            testLocation:String = TEST,
            dataLocation:String = DATA) = new User(location,designLocation,docLocation,testLocation, dataLocation)


  class User(override val baseLocation:String,
    override val designLocation:String = DESIGN,
    override val docLocation:String    = DOC,
    override val testLocation:String   = TEST,
    override val dataLocation:String = DATA) extends ProjectStructure


}