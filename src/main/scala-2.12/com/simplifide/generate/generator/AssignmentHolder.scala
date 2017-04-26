package com.simplifide.generate.generator

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/23/11
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */

trait AssignmentHolder {

  /** Place where value is assigned */
  var assignment:Option[SimpleSegment] = None

  /** Trace the path of the signal declaration */
  def trace:String = this.toString + " <- " + (if (assignment.isDefined) assignment.get.trace else "END")

}