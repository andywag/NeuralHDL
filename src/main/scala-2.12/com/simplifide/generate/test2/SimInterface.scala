package com.simplifide.generate.test2

import com.simplifide.generate.project.NewEntity


/**
 * Simulation Interface
 */

trait SimInterface {
  /** Method to create simulation files */
  def createSimFiles(files:List[String])
  /** Compile the simulation files*/
  def compile(entity:NewEntity)
  /** Run the simulation */
  def run(entity:NewEntity)
}
