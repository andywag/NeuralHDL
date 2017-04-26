package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.generator.{ComplexSingleSegment, SimpleSegment, ComplexSegment}
import com.simplifide.generate.signal.FixedType
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Simple Binary Shift Operation
 */

trait BinaryShift extends ComplexSingleSegment with Roundable {

  val input:SimpleSegment
  val shift:Int


  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = BinaryShift(input,shift,output.fixed)

  override def createNewRound(roundType:RoundType,internal:FixedType = FixedType.Simple):SimpleSegment =
    RoundSegment(name,input,internal,fixed,roundType,shift)


  def createSegment = {
    new FixedSelect.Scale(input,this.fixed,this.shift)
  }
  
}

object BinaryShift {
  def apply(input:SimpleSegment, shift:Int,fixed:FixedType = FixedType.Simple) = new Impl(input,shift,fixed)
  
  class Impl(override val input:SimpleSegment,
             override val shift:Int,
             override val fixed:FixedType = FixedType.Simple) extends BinaryShift
  
}
