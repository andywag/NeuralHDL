package com.simplifide.generate.blocks.basic.flop

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.blocks.basic.operator._
import com.simplifide.generate.generator._
import com.simplifide.generate.blocks.basic.condition.{ConditionStatement}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Flop which contains the structure of the flop but has a unique reset and enable
 * statement
 *
 * @constructor
 * @parameter name1 Name of the Flop
 * @parameter head Clock for the Flop
 * @parameter reset Reset Segment
 * @parameter enable Enable Segment
 *
 */
class SimpleFlop(val name1:Option[String],
					  val head:ClockControl,
					  val res:SimpleSegment,
					  val ena:SimpleSegment) extends SimpleSegment {


    private val resetCondition:Option[(Option[SimpleSegment],List[SimpleSegment])] = {
       head.reset match {
         case Some(x) => {
           val condition = if (x.activeLow) new UnaryOperator.NotLogical(x) else x;
           Some( ( Some(condition),List(res)) )
         }
         case None    => None
       }
    }

    private val enableCondition:Option[(Option[SimpleSegment],List[SimpleSegment])] = {
       head.enable match {
         case Some(x) => Some( (Some(x.signal),List(ena)) )
         case None    => Some( (None,List(ena)))
       }
    }

    val flop = {
     val conditions:List[(Option[SimpleSegment],List[SimpleSegment])] =
       if (resetCondition.isDefined && enableCondition.isDefined) List(resetCondition.get,enableCondition.get)
       else if (resetCondition.isDefined) List(resetCondition.get)
       else  List(enableCondition.get)

     val conditionStatement = ConditionStatement(conditions)
     Always.Sensitivity(name1,conditionStatement,head.createSensitivityList().toList)
    }

    override def create(implicit creator:CreationFactory) = flop.create






  /** No Longer in use --- Apparently this is not correct. Is in use.
   *  The function should be converted to lower level segments in the split operation
   **/
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {

     val conditions:List[(Option[SimpleSegment],List[SimpleSegment])] =
       if (resetCondition.isDefined && enableCondition.isDefined) List(resetCondition.get,enableCondition.get)
       else if (resetCondition.isDefined) List(resetCondition.get)
       else  List(enableCondition.get)

     val conditionStatement = ConditionStatement(conditions)

     val alw = Always.Sensitivity(name1,conditionStatement,head.createSensitivityList().toList)
     return writer.createCode(alw)

      null
  }




}

object SimpleFlop {
  def apply(reset:List[SimpleSegment],enable:List[SimpleSegment])(implicit clk:ClockControl) =
    new SimpleFlop(None,clk,BasicSegments.List(reset),BasicSegments.List(enable))


}
