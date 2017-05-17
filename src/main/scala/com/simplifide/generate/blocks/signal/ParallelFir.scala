package com.simplifide.generate.blocks.signal

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.{FixedType, ArrayTrait, SignalTrait}
import com.simplifide.generate.blocks.signal.FirStructure2.Impl
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.fixed.Roundable


/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 2/15/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */

trait ParallelFir extends ComplexSegment {

  implicit val clk:ClockControl
  
  val output:SignalTrait

  val input:ArrayTrait[SignalTrait]
  val taps:ArrayTrait[SignalTrait]


  val internalWidth:FixedType

  val roundType = Roundable.RoundClip

  def createBody = {

    val multiplier = array("multiplier_output",WIRE,internalWidth)(taps.length)
    
    /- ("Multiplier Stage")
    multiplier  := $round_generic(taps*input,internalWidth,roundType)


  }
  
}

object ParallelFir {

  def apply(output:SignalTrait, input:SignalTrait, taps:ArrayTrait[SignalTrait],internalWidth:FixedType)(implicit clk:ClockControl) =
    new Impl(output,input,taps,internalWidth)
  
  class Impl(
    override val output:SignalTrait,
    override val input:SignalTrait,
    override val taps:ArrayTrait[SignalTrait],
    override val internalWidth:FixedType)(implicit val clk:ClockControl) extends FirStructure2
}
