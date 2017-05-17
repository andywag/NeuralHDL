package com.simplifide.generate.project

/**
 * Trait which provides a path in the design hierarchy
 */

trait PathProvider {

  val name:String
  val parent:PathProvider = PathProvider.Empty
  def path = if (parent == PathProvider.Empty) name else parent.name + "." + name
  
}

object PathProvider {
  object Empty extends PathProvider {
    override val name = "none"
  }
}
