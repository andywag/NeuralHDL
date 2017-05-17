package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.generator.ComplexSingleSegment

/**
 * Simple small fixed point operations
 */

class FixedOperations {

}

object FixedOperations {

  // TODO Fix this when question fixed
  class AbsoluteValue(val input:SignalTrait) extends ComplexSingleSegment {
    override def createSegment = null//input.sign ? input :: -input
  }
  
}

