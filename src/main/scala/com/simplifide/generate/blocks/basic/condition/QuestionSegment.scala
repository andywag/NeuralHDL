package com.simplifide.generate.blocks.basic.condition

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.generator.CodeWriter
import com.simplifide.generate.generator.SegmentReturn
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.{ObjectFactory, ExpressionReturn}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.blocks.basic.Statement


/** Simple Mux which translates to a question mark statement
 *
 *  @constructor Simple Mux Creation
 *  @parameter condition Condition for the statement
 *  @parameter tr True result for the statement
 *  @prameter fa False results for the statement
 *
 **/

trait QuestionSegment extends SimpleSegment{

  val condition:SimpleSegment
  val tr:SimpleSegment
  val fa:SimpleSegment

  /*
  override def numberOfChildren = SimpleSegment.maxChildren(List(condition,tr,fa))

  override def child(i:Int)     =
    QuestionSegment(condition.child(i),tr.child(i),fa.child(i))
  override def child(index:Int,output:SimpleSegment):SimpleSegment =
    QuestionSegment(condition.child(index),tr.child(index,output),fa.child(index,output))
*/

  /*
  override def split(output:Expression,index:Int):ExpressionReturn = {
    if (this.shouldSplit) {
      val out   = (if (index == -1) output else output.copy(index)).asInstanceOf[SimpleSegment]
      val cond  = condition.split(out,0)
      val lp    = tr.split(out,1)
      val rp    = fa.split(out,2)
      val mux = new Statement.Assign(out,QuestionSegment(cond.output.create,
        lp.output.create,
        rp.output.create))
      new ExpressionReturn(out,cond.states ::: lp.states ::: rp.states ::: List(mux)  )
    }
    else {
      new ExpressionReturn(this,List())
    }
  }
  */

  override def createCode(implicit writer:CodeWriter):SegmentReturn =
    SegmentReturn("") + writer.createCode(condition) + " ? " + writer.createCode(tr) + " : " + writer.createCode(fa)

  
}

/** Factory Methods for Creating of a Simple Mux */
object QuestionSegment {

  def apply(condition:SimpleSegment,tr:SimpleSegment,fa:SimpleSegment) =new Impl(condition,tr,fa)
  class Impl(override val condition:SimpleSegment,override val tr:SimpleSegment,override val fa:SimpleSegment) extends QuestionSegment

}
