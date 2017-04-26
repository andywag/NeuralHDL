package com.simplifide.generate.proc

import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.generator.SimpleSegment
import parser.ProcessorSegment


/**
 * Trait which defines operations for creating a reconfigurable processor
 */

trait ControlHolder {

  /** Controls which are included in this block */
  lazy val controls:List[Controls] = List()
  /** Determines whether this control signal matches the current control */
  def controlMatch(actual:SimpleSegment,statements:ProcessorSegment):Boolean = false
  /** Creates a list of controls based on this object -- Tree Walking */
  def createControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = List()
  /** Creates a list of controls based on this object -- Tree Walking */
  def createOutputControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] =
    createControl(actual,statements,index)
  /** Output delay from the input segments */
  def outputDelay:Int = 0
}