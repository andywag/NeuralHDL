package com.simplifide.generate.generator

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 12/16/11
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */

class SegmentGroup(val segments:List[SimpleSegment]) extends SimpleSegment.Combo {

  def apply(segment:SimpleSegment) = segments.find(_ == segment).get

  override def numberOfChildren:Int = segments.size
  /** Returns the child at the input index */
  override def child(index:Int):SimpleSegment = segments(index)

  override def outputs = segments.flatMap(_.outputs)
}

object SegmentGroup {

}