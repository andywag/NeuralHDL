package com.simplifide.generate.blocks.signal

import com.simplifide.generate.signal.{SignalTrait, FixedType}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.fixed.Roundable
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType


/**
  * Leaky accumulator
  *
  * z[n] = a*x[n] + (1-a)*z[n-1]
  *
  */

trait LeakyAccumulator extends ComplexSegment {
  
  implicit val clk:ClockControl
  /** Input to the block */
  val signalIn : SignalTrait
  /** Gain Signal associated with the block */
  val gain     : SignalTrait
  /** Output signal of this block */
  val signalOut : SignalTrait
  /** Internal Width of the Accumulator */
  val internal:FixedType
  
  val roundType:RoundType = Roundable.RoundClip

  def createBody {

    val internalSignal  = register("leaky_internal",WIRE,internal)(1)
    signalOut    := $round_generic (internalSignal,internal,roundType)
    
  }

}

object LeakyAccumulator {
  def apply(signalIn:SignalTrait, gain:SignalTrait, signalOut:SignalTrait, internal:FixedType)(implicit clk:ClockControl)  =
    new Impl(signalOut, signalIn, gain, internal)
  
  class Impl(
    override val signalIn  : SignalTrait,
    override val gain      : SignalTrait,
    override val signalOut : SignalTrait,
    override val internal  : FixedType
  )(implicit val clk:ClockControl) extends LeakyAccumulator
}