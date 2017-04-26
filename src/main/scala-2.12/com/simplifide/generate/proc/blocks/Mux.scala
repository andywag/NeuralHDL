package com.simplifide.generate.proc.blocks

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.condition.NewCaseStatement
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.{SegmentHolder, ObjectFactory, ExpressionReturn}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.proc.Controls
import com.simplifide.generate.signal.{Constant, SignalTrait}
import com.simplifide.generate.proc.parser.ProcessorSegment
import com.simplifide.generate.blocks.basic.Statement


/**
 * Class defining a simple multiplexor used for reconfigurable processor controls
 * @constructor
 * @parameter ctrl Control for Mux
 * @parameter results List of Possible results
 */

class Mux(val ctrl:SignalTrait,val results:List[SimpleSegment]) extends SimpleSegment {

  override def toString = "Mux(" + ctrl  + results.map("," + _.toString).reduceLeft(_+_) + ")"

  val control = Controls(ctrl)
  override lazy val controls:List[Controls] = List(control)

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    null
  }
  /*
  override def split(output:Expression,index:Int):ExpressionReturn = {

    val out     = output.copy(if (index > 0) index else 0).asInstanceOf[SimpleSegment]       // AlwaysBlock Split the Output
    val splits  = results.zipWithIndex.map(x => x._1.split(out,x._2))  // Create the subset of condition returns
    val expressions = splits.zipWithIndex.map(x => NewCaseStatement.Item(Constant(x._2,ctrl.fixed.width),new Statement.Reg(output,x._1.output)))
    val cas = new NewCaseStatement(ctrl,expressions)
    val alw = new Always.Star(None,cas,List())

    val extra = splits.map(_.states)
    val realExtra = if (extra.length == 0) List() else extra.reduceLeft(_ ::: _)
    //val cur = if (index < 0) List(new SimpleStatement.Assign(output,out)) else List()
    new ExpressionReturn(out, List(alw) :::  realExtra )
  }
  */

  override def createControl(actual:SimpleSegment,statements:ProcessorSegment, index:Int):List[Controls.Value] = {
    val location = this.results.indexWhere(_.controlMatch(actual,statements))
    if (location >= 0)
      List(Controls.Value(this.control,index,location))
    else
      List()
  }


}

/** Factory methods for creating a mux */
object Mux {
  def apply(ctrl:SignalTrait,results:SimpleSegment*) = new Mux(ctrl,results.toList)

}