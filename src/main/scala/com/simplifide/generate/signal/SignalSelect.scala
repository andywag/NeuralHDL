package com.simplifide.generate.signal

import com.simplifide.generate.signal.FixedType.Signing
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.basic.fixed.FixedSelect._

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 12/2/11
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */

class SignalSelect(val original:SignalTrait, val top:Int, val bottom:Int) extends SignalTrait {

  override val name:String = original.name + "[" + (if (top == bottom) top else (top + ":" + bottom)) + "]"
  /** Type of Signal */
  override val opType:OpType = original.opType
  /** Fixed type of signal */
  override val fixed:FixedType = FixedType(FixedType.UnSigned,top-bottom + 1,0)

  override def apply(fixed:FixedType):SimpleSegment = this

  def newSignal(name:String = this.name,
    opType:OpType = this.opType,
    fix:FixedType = this.fixed):SignalTrait = this

}