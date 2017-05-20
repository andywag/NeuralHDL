package com.simplifide.generate.doc

import java.io.File

import com.simplifide.generate.doc.MdGenerator.MdTest
import com.simplifide.generate.project.Project
import com.simplifide.generate.test2.blocktest.BlockProject
import com.simplifide.generate.util.FileOps

/**
  * Created by andy on 5/19/17.
  */
class MdProject(val project:Project) extends MdGenerator[Project] {

  val name = project.rootEntity.name
  def blockDocument =  s"""
# ${project.rootEntity.name}

This document details the block design of $name. It starts out with a description of the design followed
by the test descriptions and results.

${project.rootEntity.base.get.document}
${project.rootTests.map(x => x.document).mkString("\n")}


"""

  def blankDocument = ""

  override def document: String = {
    project match {
      case x:BlockProject => blockDocument
      case _              => blankDocument
    }
  }


  override def create(file:String) = {
    super.create(s"${file}/${project.rootEntity.name}_proj.md")
    project.totalEntities.foreach(x => new MdEntity(x).create(s"${file}/${x.name}.md"))
    project.rootTests.foreach(x => new MdTest(x).create(s"${file}/${x.testBench.name}.md"))
  }

}
