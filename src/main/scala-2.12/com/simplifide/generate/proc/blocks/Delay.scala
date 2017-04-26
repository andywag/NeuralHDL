package com.simplifide.generate.proc.blocks

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.proc.Controls
import com.simplifide.generate.proc.parser.ProcessorSegment


/**
 * Reconfigurable processor class defining a delay
 */
class Delay(val signal:SignalTrait, val delay:Int) extends SimpleSegment {

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    signal(delay).asInstanceOf[SignalTrait].createCode(writer)
  }


  override def controlMatch(actual:SimpleSegment,statements:ProcessorSegment):Boolean = {
    return signal.controlMatch(actual,statements)
  }

  override def createControl(actual:SimpleSegment,statements:ProcessorSegment, index:Int):List[Controls.Value] = {
    statements.getStatement(signal) match {
      case Some(x) => x.input.createControl(actual,statements,index-delay)
      case None    => List()
    }
  }


}

object Delay {
  def apply(signal:SignalTrait, delay:Int) = new Delay(signal,delay)
}