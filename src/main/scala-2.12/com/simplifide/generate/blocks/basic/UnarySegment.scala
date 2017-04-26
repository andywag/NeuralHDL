package com.simplifide.generate.blocks.basic

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.{ObjectFactory, ExpressionReturn}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.factory.CreationFactory

/**
  * Segment which is based on only one input
 */

trait UnarySegment extends SimpleSegment {
  /** First input segment */
  val in1:SimpleSegment
  /** Virtual constructor to create a new segment */
  def newSegment(out:SimpleSegment,in1:SimpleSegment = this.in1):SimpleSegment


  override def numberOfChildren:Int = in1.numberOfChildren
  override def child(index:Int):SimpleSegment = newSegment(in1,in1.child(index))



  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = newSegment(output,this.in1)

  /*
  override def split(output:Expression,index:Int):ExpressionReturn = {
    
    if (this.shouldSplit) {
      val out      = (if (index == -1) output else output.copy(index)).create
      val lp       = this.in1.split(out,0)
      val segment  = newSegment(out,lp.output.create)
      val adder    = new Statement.Assign(out,segment)
      new ExpressionReturn(out,lp.states ::: List(adder))
    }
    else {
      new ExpressionReturn(this,List())
    }

  }
  */
}

object UnarySegment {

}