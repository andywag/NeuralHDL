package com.simplifide.generate.blocks.func

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.signal.{SignalDeclaration, SignalTrait}
import com.simplifide.generate.generator.BasicSegments.BeginEnd

/**
 * Function
 */

trait HardwareFunction extends SimpleSegment {

  /** Name of the function */
  val name:String
  /** Signals contained in this function */
  val signals:List[SignalTrait]
  /** Segments contained in this function */
  val segments:List[SimpleSegment]

  def head:SegmentReturn = {
    this match {
      case x:HardwareFunction.Function => SegmentReturn ("function " + fixed.declaration + " " + name + ";\n\n")
      case x:HardwareFunction.Task     => SegmentReturn ("task " + name + ";\n\n")
    }
  }
  
  def end:SegmentReturn = {
    this match {
      case x:HardwareFunction.Function => SegmentReturn ("endfunction\n\n")
      case x:HardwareFunction.Task     => SegmentReturn ("endtask\n\n")
    }
  }
  

  
  override def createCode(implicit writer:CodeWriter):SegmentReturn  = {
    val signal:SegmentReturn = signals.map(new SignalDeclaration(_).createCode).foldLeft(SegmentReturn(""))(_+_)
    val segment:SegmentReturn = new BeginEnd(segments).createCode

    head + signal + segment +  end
  }

}

object HardwareFunction {
  
  class Task(override val name:String,
    override val signals:List[SignalTrait],
    override val segments:List[SimpleSegment]) extends HardwareFunction

  class Function(override val name:String,
    override val signals:List[SignalTrait],
    override val segments:List[SimpleSegment]) extends HardwareFunction


}