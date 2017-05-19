package com.simplifide.generate.doc

import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity

/**
  * Created by andy on 5/18/17.
  */
class MdEntity(val entity:NewEntity) extends MdGenerator[EntityParser] {

  val root = entity.base.get

  def document:String =
s"""
# ${entity.name}

${root.document}



"""



}
