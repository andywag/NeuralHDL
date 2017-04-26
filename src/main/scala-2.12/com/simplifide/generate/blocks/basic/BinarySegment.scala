package com.simplifide.generate.blocks.basic

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.ExpressionReturn
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Trait which defines operations on 2 input segments
 */

trait BinarySegment extends SimpleSegment {
  /** First Input for the operatoration */
  val in1:SimpleSegment
  /** Second Input for the operation */
  val in2:SimpleSegment
  /** Method used to create new segment */
  def newSegment(out:SimpleSegment,in1:SimpleSegment = this.in1,in2:SimpleSegment=this.in2):SimpleSegment


  override def createIndividualSplit(output:SimpleSegment,index:Int=0):(SimpleSegment,List[SimpleSegment]) = {
    val use1 = in1.createIndividualSplit(output.createSubOutput(0),1)
    val use2 = in2.createIndividualSplit(output.createSubOutput(1),2)
    (newSegment(output,use1._1,use2._1), use1._2 ::: use2._2)
  }


  override def numberOfChildren:Int = in1.numberOfChildren

  override def child(index:Int):SimpleSegment = newSegment(in1,in1.child(index),in2.child(index))
  override def child(index:Int,output:SimpleSegment):SimpleSegment = newSegment(output,in1.child(index),in2.child(index))

  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = newSegment(output,in1.createOutput(output),in2.createOutput(output))



}