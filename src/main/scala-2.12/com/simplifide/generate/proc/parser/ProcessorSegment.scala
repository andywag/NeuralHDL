package com.simplifide.generate.proc.parser

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.proc.{ProcProgram, Controls}
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.Statement

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/22/11
 * Time: 9:00 AM
 * To change this template use File | Settings | File Templates.
 */

trait ProcessorSegment extends SimpleSegment {

  override def createCode(implicit writer:CodeWriter):SegmentReturn = null

  def getAssignment:Option[SimpleSegment] = None

  def getStatement(signal:SignalTrait):Option[Statement] = None
  def <:= (rhs:SimpleSegment)(implicit base:ProcProgram):Expression = {
    //if (rhs.isInstanceOf[ProcessorSegment]) {
    val statement = new ProcessorStatement(this,rhs)
    base.signalAssigns.append(statement)
    //}
    null
  }
}
