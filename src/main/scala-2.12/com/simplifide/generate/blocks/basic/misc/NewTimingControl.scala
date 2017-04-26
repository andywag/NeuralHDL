package com.simplifide.generate.blocks.basic.misc

import java.sql.Time
import com.simplifide.generate.blocks.basic.misc.TimingControl.TimeSignalValue
import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.signal.{NewConstant, Constant, SignalTrait}
import com.simplifide.generate.generator._

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/23/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Flop which assigns statements as a function of time
 *
 **/
class NewTimingControl(
  val output:SimpleSegment,
  val counter:SimpleSegment,
  val values:List[NewTimingControl.TimeSignalValue])(implicit clk:ClockControl) extends ComplexSegment {

  def createBody() {}

  /*
  output := counter $match (
    values.map(x => $cases(x.time) $then x.value)
  )
  */

  
  /*
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val signals = values.map(_.signal).toSet.toList
    val groups = values.groupBy(_.time).toList.sortBy(_._1) // Create a list of time - values
    val condition = groups.map(x => ( Some(BinaryOperator.LTE(counter,Constant(x._1,counter.fixed))),
      x._2.map(y => new Statement.Reg(y.signal,y.value))))

    val resets = signals.map(x => new Statement.Reg(x,NewConstant(0,x.fixed)))
    val conditional = ConditionStatement(condition)
    val flop = new SimpleFlop(None,clk,BasicSegments.List(resets),conditional)

    writer.createCode(flop)
  }
  */

}

/** Factory Method and Classes */
object NewTimingControl {

  def apply(output:SimpleSegment,index:SimpleSegment,values:List[TimeSignalValue])(implicit clk:ClockControl) = new NewTimingControl(output,index,values)
  
  class TimeSignalValue(val time:Option[SimpleSegment],val value:SimpleSegment)



}
