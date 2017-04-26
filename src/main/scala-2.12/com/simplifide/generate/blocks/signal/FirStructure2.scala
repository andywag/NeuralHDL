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

trait FirStructure2 extends ComplexSegment {

  implicit val clk:ClockControl
  
  val output:SignalTrait

  val input:SignalTrait
  val taps:ArrayTrait[SignalTrait]


  val internalWidth:FixedType

  val roundType = Roundable.RoundClip

  def createBody = {

    val multiplier = array("multiplier_output",WIRE,internalWidth)(taps.length)
    val stageOut   = array("stage_out",WIRE,internalWidth)(taps.length-1)
    val regOut     = array("reg_out",REG,internalWidth)(taps.length-1)
    
    val lastStage  = signal("stage_last",WIRE,output.fixed)
    
    /- ("Multiplier Stage")
    multiplier  := $round_generic(taps*input,internalWidth,roundType)
    /- ("Stage Addition")
    stageOut(0) := multiplier(0)
    List.tabulate(taps.length-1)(x => stageOut(x+1) := $round_generic(stageOut(x) + multiplier(x+1),internalWidth,roundType))
    lastStage   := stageOut(taps.length-1) + multiplier(taps.length-1)
    /- ("Stage Delay")
    regOut := stageOut  $at clk
    output := lastStage $at clk

  }
  
}

object FirStructure2 {

  def apply(output:SignalTrait, input:SignalTrait, taps:ArrayTrait[SignalTrait],internalWidth:FixedType)(implicit clk:ClockControl) =
    new Impl(output,input,taps,internalWidth)
  
  class Impl(
    override val output:SignalTrait,
    override val input:SignalTrait,
    override val taps:ArrayTrait[SignalTrait],
    override val internalWidth:FixedType)(implicit val clk:ClockControl) extends FirStructure2
}
