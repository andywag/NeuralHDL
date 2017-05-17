package com.simplifide.generate.signal

import com.simplifide.generate.signal.FixedType.Signing

/**
  *
  */

trait ExponentType extends FixedType {

  val fraction:Int = 0
  val signed:Signing = FixedType.UnSigned
  /** Offset of this type */
  val offset:Int = 0
}