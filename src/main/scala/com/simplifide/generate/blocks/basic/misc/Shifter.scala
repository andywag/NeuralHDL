package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.signal.{FixedType, SignalTrait}
import com.simplifide.generate.generator.{ComplexSingleSegment, ComplexSegment, SimpleSegment}
import com.simplifide.generate.parser.model.EnclosedExpression
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType
import com.simplifide.generate.blocks.basic.fixed.{RoundSegment, ClipSegment, Roundable, FixedSelect}
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Barrel Shifter 
 */

trait Shifter extends ComplexSegment with EnclosedExpression with Roundable {

  val output:SimpleSegment
  /** Input Segment */
  val input:SimpleSegment
  /** Control for Shifter */
  val condition:SimpleSegment
  /** Offset for the barrel shifter */
  val offset:Int
  /** Number of Steps for the shifting operation */
  val steps:Int

  val internal:FixedType = FixedType.Simple

  val roundType:Roundable.RoundType = Roundable.RoundClip

  def newShifter(output:SimpleSegment = this.output, 
    input:SimpleSegment               = this.input, 
    condition:SimpleSegment           = this.condition,
    offset:Int                        = this.offset,
    steps:Int                         = this.steps,
    internal:FixedType                = this.internal,
    roundType:Roundable.RoundType     = this.roundType) = new Shifter.Impl(output,input,condition,offset,steps,internal,roundType)
  


  override def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
    this.newShifter(output = output).create


  def createNewRound(roundType:RoundType,internal:FixedType = FixedType.Simple):SimpleSegment =
    this.newShifter(roundType = roundType, internal = internal)

    

  val realSteps = if (steps == 0) math.pow(2.0,condition.fixed.width).toInt else steps

  private def createCase(index:Int) = {
    val scale = math.pow(2.0,(index + offset))
    $cases(index) $then  (output ::= $round_generic(scale*input,this.internal,this.roundType,output + "_" + index))
  }


  
  def createBody = {

    val case1 = condition $match  (
      List.tabulate(realSteps)( x => createCase(x))
    )
    this.assign(case1)
  }
  
}

object Shifter {
  
  def apply(input:SimpleSegment, condition:SimpleSegment, offset:Int,  steps:Int) = {
    new Impl(null,input,condition,offset,steps)
  }
  
  class Impl(
    override val output:SimpleSegment,
    override val input:SimpleSegment,
    override val condition:SimpleSegment,
    override val offset:Int,
    override val steps:Int,
    override val internal:FixedType = FixedType.Simple,
    override val roundType:Roundable.RoundType = Roundable.Truncate) extends Shifter


}