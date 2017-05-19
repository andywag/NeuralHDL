package com.simplifide.generate.doc

import java.io.File

import com.simplifide.generate.project.Project
import com.simplifide.generate.util.FileOps

/**
  * Created by andy on 5/19/17.
  */
class MdProject(val project:Project) extends MdGenerator[Project] {
  override def document: String =
    s"""

    """.stripMargin

  override def create(file:String) = {
    project.totalEntities.foreach(x => new MdEntity(x).create(s"${file}/${x.name}.md"))
  }

}
