package com.simplifide.generate.parser

import factory.CreationFactory
import model.Expression
import com.simplifide.generate.blocks.basic.misc.Shifter

/**
 * Trait which defines numerous simple built in methods
 */

trait BasicParser {
  
  def $shift(input:Expression,condition:Expression,offset:Int = 0,range:Int=0)(implicit creator:CreationFactory) = {
    Shifter(input.create,condition.create,offset,range)
  }

}
