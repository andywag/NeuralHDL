package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}


/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/31/12
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */

class DelayOperator(val input:SimpleSegment, val delay:Int) extends SimpleSegment {

  def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn(" #" + delay + " ") + input.createCode


}
