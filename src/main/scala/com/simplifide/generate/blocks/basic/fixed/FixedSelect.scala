package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.blocks.basic.operator.Select
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}
import com.simplifide.generate.signal.{Constant, SignalTrait, FixedType}
import com.simplifide.generate.blocks.basic.fixed.FixedSelect.Impl

/**
 * Operation which converts a signal to a different fixed point type
 */
trait FixedSelect extends SimpleSegment{

  val signal:SimpleSegment
  val shift = 0

  override def apply(fixed:FixedType):SimpleSegment = this

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
     val bot = signal.fixed.fraction - fixed.fraction + shift
     val top = bot + this.fixed.width - 1
     val sel = new Select(signal,Some(top),Some(bot))
     writer.createCode(sel)
  }
  
}

object FixedSelect {
  /** Factory method for created a fixed select type */
  def apply(signal:SimpleSegment,fixed:FixedType) = new Impl(signal,fixed)
  
  class Impl(override val signal:SimpleSegment,override val fixed:FixedType) extends FixedSelect

  class Scale(override val signal:SimpleSegment, override val fixed:FixedType, override val shift:Int) extends FixedSelect


  class ConstantSelect(val constant:Constant, override val fixed:FixedType) extends SimpleSegment{
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return constant.createCode(writer,Some(fixed))
    }
  }
    

  
}
