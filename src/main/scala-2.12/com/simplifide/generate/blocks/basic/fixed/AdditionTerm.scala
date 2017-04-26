package com.simplifide.generate.blocks.basic.fixed

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.generator.{SimpleSegment, SegmentReturn, CodeWriter}
import com.simplifide.generate.signal.{FixedType, SignalTrait}
import com.simplifide.generate.generator.SegmentReturn._


abstract class AdditionTerm(val term:SimpleSegment) extends SimpleSegment {
  
  val operator:String = " + "
  override val fixed:FixedType = term.fixed
  override def numberOfChildren:Int = term.numberOfChildren


  /** Create a new Addition Term */
  def newTerm(term:SimpleSegment):AdditionTerm

  override def child(index:Int):SimpleSegment = newTerm(term.child(index))


  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
     return operator + writer.createCode(term)
  }

}

object AdditionTerm {
   
  class AddTerm(term:SimpleSegment) extends AdditionTerm(term) {
    def newTerm(term:SimpleSegment):AdditionTerm = new AddTerm(term)
  }
  
  class SubTerm(term:SimpleSegment) extends AdditionTerm(term) {
    override val operator = " - "
    def newTerm(term:SimpleSegment):AdditionTerm = new SubTerm(term)
  }
  
  
  
  class Empty(term:SimpleSegment) extends AdditionTerm(term) {
    override val operator = ""
    def newTerm(term:SimpleSegment):AdditionTerm = new Empty(term)
  }
  
}
