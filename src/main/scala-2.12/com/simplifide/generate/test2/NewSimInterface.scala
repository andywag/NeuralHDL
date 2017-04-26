package com.simplifide.generate.test2

import com.simplifide.generate.project.NewEntity
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */

trait NewSimInterface {
  /** Method to create simulation files */
  def createSimFiles(files:List[String], testLocation:String)
  /** Create Waveform Files */
  def createWaveFile(entity:NewEntity,path:String,file:File):String
  /** Compile the simulation files*/
  def compile(rootModule:String, testLocation:String):Boolean
  /** Run the simulation */
  def run(rootModule:String, testLocation:String):Boolean
}
