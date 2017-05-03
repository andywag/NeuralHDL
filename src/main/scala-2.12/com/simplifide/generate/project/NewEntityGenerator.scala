package com.simplifide.generate.project

import com.simplifide.generate.signal.{SignalDeclaration, SignalTrait}
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter}
import com.simplifide.generate.generator.SegmentReturn._
import com.simplifide.generate.util.{FileOps, StringOps}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 2/5/12
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */

trait NewEntityGenerator {

  val entity:NewEntity


  protected def comment:String = {
    "//-----------------------------------------------------------------------------\n" +
      "// Company: 			                                                              \n" +
      "// Author:				Andy                                                          \n" +
      "// Date:                                                                       \n" +
      "// Module Name:       " + entity.name +                                       "\n" +
      "// Description:                                                                \n" +
      "//                                                                             \n" +
      "//-----------------------------------------------------------------------------\n"
  }

  /** Create the code for the head of the module */
  def createHead(writer:CodeWriter):String = {
    def singleDec(index:Int,segment:String):String = {
      if (index != 0) return ",\n" + segment.replaceAll("\\s+$", "")
      else            return segment.replaceAll("\\s+$", "")
    }
    // Return all of the signals contained in this entity
    val tot:List[SignalTrait] = SignalTrait.uniqueSignals( (entity.signals ::: entity.extraSignals).flatMap(_.allSignalChildren))
    // Filter to only input/output types and sort by type then name
    val fil = tot.filter(x => !x.opType.isSignal).sortBy(x => (x.opType.toString, x.name))
    // Create the Declaration
    val dec:List[SignalDeclaration] = fil.flatMap(SignalDeclaration.head(_))
    "(\n" + StringOps.accumulate(dec.zipWithIndex.map(x => singleDec(x._2,writer.createCode(x._1).code))) +");\n\n"

  }

  def functions(implicit writer:CodeWriter):SegmentReturn =
    entity.functions.map(_.createCode).foldLeft(SegmentReturn(""))(_+_)

   def head(implicit writer:CodeWriter) = entity.head.map(_.createCode).foldLeft(SegmentReturn(""))(_+_)

  val includeString =
    """
`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    """.stripMargin

  def createCode(writer:CodeWriter):SegmentReturn = {
    implicit val writ:CodeWriter = writer
    val mod = entity.createModule
    return SegmentReturn(comment) +
      head +
      SegmentReturn(includeString) +
      SegmentReturn("module ") +
      entity.name +
      this.createHead(writer) +
      mod +
      functions +
      "endmodule\n\n"
  }

  def writeModule(location:String):SegmentReturn     = {
    val writer = CodeWriter.Verilog
    val ret = createCode(writer)
    this.entity.files.foreach(_.createFile(location))
    FileOps.createFile(location, entity.name + ".v",ret.code)
    ret
  }


}

object NewEntityGenerator {

  def apply(entity:NewEntity) = new Impl(entity)
  class Impl(val entity:NewEntity) extends NewEntityGenerator


}