package com.simplifide.generate.generator

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.{ExpressionReturn, SignalParser, ConditionParser}
import com.simplifide.generate.parser.model.Expression

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 1/26/12
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */

trait ComplexSingleSegment extends ConditionParser with SignalParser with SimpleSegment  {
  /** Defines the body in the block */
  def createSegment:Expression

  //override def split  = List(this.createSegment)

  //override def split(output:Expression,index:Int):ExpressionReturn = this.createSegment.split(output,index)

  override def createCode(implicit writer:CodeWriter):SegmentReturn  = {
    writer.createCode(this.createSegment.create)
  }

}