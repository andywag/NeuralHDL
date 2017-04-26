package com.simplifide.generate.test2.util

import java.io.File
import io.Source
import com.simplifide.generate.util.Logger

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/15/12
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */

class FileMatch() {


  



}

object FileMatch {

  private def lineCompare(line1:String, line2:String, line:Int,comparator:(String, String)=>Boolean):Option[FileMatch.LineError] = {
    def valueCompare(column:Int, value1:String,  value2:String, comparator:(String, String)=>Boolean):Option[FileMatch.Error] =
      if (!comparator(value1,value2)) Some(new FileMatch.Error(column,value1,value2)) else None

    val l1 = line1.trim.split(" ").filter(!_.equalsIgnoreCase(""))
    val l2 = line2.trim.split(" ").filter(!_.equalsIgnoreCase(""))
    val errors = (l1 zip l2).zipWithIndex.flatMap(x => valueCompare(x._2,x._1._1,x._1._2,comparator)).toList
    if (errors.size > 0) Some(LineError(line,errors)) else None
  }

  def printErrors(errors:List[FileMatch.LineError]):String = {
    errors.map(x => x.printError).foldLeft("")(_+_)
  }
  
  
  def defaultComparator(desired:String, actual:String) = actual.equals(desired)
  
  def compare(matchFile:File, hardFile:File, comparator:(String, String)=>Boolean = defaultComparator(_,_)):Boolean = {

    val mat      = Source.fromFile(matchFile)
    val hard     = Source.fromFile(hardFile)
    var result = false

    val matLines  = mat.getLines()
    val hardLines = hard.getLines()

    val matLength = matLines.size
    val hardLength = hardLines.size

    if (matLength == 0) Logger.message("ERROR - Stimulus File Empty");
    else if (hardLength == 0) Logger.message("ERROR - Output File Empty")
    else if (matLength != hardLength) Logger.message("ERROR - File Sizes don't match")
    else {
      val data = ((matLines) zip (hardLines))
      val errors = data.zipWithIndex.flatMap(x => lineCompare(x._1._1,x._1._2,x._2,comparator)).toList



      if (errors.size > 0) {
        Logger.message("Matching Failed Between " + matchFile + " --- " + hardFile)
        Logger.message(this.printErrors(errors))

      }
      else {
        Logger.message("Matching Passed between " + matchFile + " --- " + hardFile)
        result = true
      }
    }
    mat.close()
    hard.close()
    result
  }



  def LineError(line:Int, errors:List[Error]) = new LineError(line,errors)  
  
  class Error(val column:Int, val desired:String, val result:String) {
    def printError = "      Column " + column + " Desired " + desired + " Actual " + result + "\n"
  }
  
  class LineError(val line:Int, val errors:List[Error]) {
    def printError = "   Mismatch at Line " + line + "\n" + errors.map(_.printError).foldLeft("")(_+_)
  }
}
