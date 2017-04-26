package com.simplifide.generate.signal

import com.simplifide.generate.signal.FloatType.Impl
import com.simplifide.generate.signal.FixedType.Signing

/**
 * Description of a floating point type
 */

trait FloatType extends FixedType {

  val fraction = 0

  val signalWidth:Int
  val exponentWidth:Int
  
  
  override val width = signalWidth + exponentWidth
  override val signed = FixedType.Signed
  
}

object FloatType {
  
  def apply(signalWidth:Int,  exponentWidth:Int) = new Impl(signalWidth,exponentWidth)
  class Impl(override val signalWidth:Int,  override val exponentWidth:Int) extends FloatType
  
}