/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simplifide.generate.util

import scala.collection.mutable.Buffer
import com.simplifide.generate.generator.{CodeWriter, SimpleSegment}
import java.lang.StringBuilder

class StringOps {}


/** Utilities for operating on strings */
object StringOps {

  val INDENT = 20

  /**
   * Format the line to have a minimum indent corresponding to the indent defined in the list
   */
  def formatLine(items:List[String],indents:List[Int]):String =  {
    formatLine(items zip indents)
  }
  
  def formatLine(items:List[(String, Int)]):String = {
    var total = 0
    val builder = new StringBuilder()
    items.foreach {
      x => {
        val result = if (total < x._2) List.fill(x._2 - total)(" ").reduceLeft(_+_) + x._1
                     else x._1
        builder.append(result)
        total += result.length
      }
    }
    builder.toString
  }


  
  def writeIndent(value:String,indent:Int):String =
    List.fill(2*indent)(" ").reduceLeft(_+_) + value


   def indentLines(value:String,indent:Int):String = {
    //val build:StringBuilder = new StringBuilder();
    val lines = value.split("\n")
    lines.map(x => writeIndent(x,indent) + "\n").reduceLeft(_+_)

  }

  /** Convenience class to handel the error condition when the length of the List is 0 */
  def accumulate(data:List[String]):String = {
    if (data.length == 0) "" else data.reduceLeft(_+_)
  }





  
  

}
