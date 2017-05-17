package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.func.HardwareFunction

/**
 *
 */

trait FunctionHolder {

  val functions = new ListBuffer[FunctionParser]

  def createFunctions:List[HardwareFunction] = functions.toList.map(_.createFunction)
  
  def function(newFunction:FunctionParser) {
    functions.append(newFunction)
  }


}