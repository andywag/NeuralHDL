package com.simplifide.generate.proc.parser

import com.simplifide.generate.proc.Controls
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/23/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */


class ProcessorStatement(val output:SimpleSegment, val input:SimpleSegment) extends ProcessorSegment {


  def createControls(index:Int):List[Controls.Value] = {
    val outputControl = output.createOutputControl(null,null,index + input.outputDelay)

    val inputControl = output.assignment match {
      case Some(x) => x.createControl(input,null,index)
      case None    => input.createControl(null,null,index)
    }
    outputControl ::: inputControl
  }

}