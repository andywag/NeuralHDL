package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.condition.NewCaseStatement
import com.simplifide.generate.signal.{Constant, SignalTrait}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Class which defines a look up table. This is implemented as a case statement
 *
 * @constructor
 * @parameter output Output of the Look up table
 * @parameter input function for the table
 * @parameter input values associated with the table
 */
class Lut(val output:SignalTrait,
  val condition:SimpleSegment,
  val inputs:List[SimpleSegment]) extends SimpleSegment.Combo {


  override def create(implicit creator:CreationFactory) = {
    val values = inputs.zipWithIndex.map(x => NewCaseStatement.Item(Constant(x._2,condition.fixed),
      new Statement.Reg(output,x._1)))
    Always.Star(NewCaseStatement(condition,values)).create
  }

  /*
  override def split:List[Expression] = {
    val values = inputs.zipWithIndex.map(x => NewCaseStatement.Item(Constant(x._2,condition.fixed.width),
      new Statement.Reg(output,x._1)))
    Always.Star(NewCaseStatement(condition,values)).split
  }
  */


  /** Controls which are included in this block */
  //val controls:List[Controls] = List(Controls(condition))
  /** Determines whether this control signal matches the current control */
  //def controlMatch(actual:SimpleSegment,statements:SegmentHolder):Boolean = true
  /** Creates a list of controls based on this object -- Tree Walking */
  //def createControl(actual:SimpleSegment,statements:SegmentHolder,index:Int):List[Controls.Value] = List()




}

/** Factory Methods for Creating a Look up table */
object Lut {

  def apply(output:SignalTrait,
            condition:SimpleSegment,
            values:List[SimpleSegment]) =
    new Lut(output,condition,values)

  def apply(output:SignalTrait,
            condition:SimpleSegment,
            creator:(Int)=>SimpleSegment,
            length:Int) =
    new Lut(output,condition,List.tabulate(length)(creator(_)))

}