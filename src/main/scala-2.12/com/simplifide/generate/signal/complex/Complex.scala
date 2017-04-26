package com.simplifide.generate.signal.complex

import com.simplifide.generate.generator.SimpleSegment

/** */

trait Complex {

  type T <: SimpleSegment

  val real:T
  val imag:T

  val isConjugate:Boolean = false


}