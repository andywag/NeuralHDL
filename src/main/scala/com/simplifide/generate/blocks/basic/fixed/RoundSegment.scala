package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.generator.{BasicSegments, SimpleSegment, CodeWriter, SegmentReturn}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.{ObjectFactory, ExpressionReturn}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.blocks.basic.{UnarySegment, Statement}
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType

/**
 * Class which defines a round operation
 */
trait RoundSegment extends UnarySegment with Roundable{

  val internal:FixedType       = FixedType.Simple

  val roundType:Roundable.RoundType= Roundable.Truncate
  
  val scale:Int

  def newSegment(output:SimpleSegment,in1:SimpleSegment = this.in1) =
    new RoundSegment.Impl(output.name,this.in1,output.fixed,this.internal,this.roundType,this.scale)

  def createNewRound(roundType:RoundType,internal:FixedType = FixedType.Simple):SimpleSegment =
    new RoundSegment.Impl(this.name,this.in1,this.fixed,this.internal,this.roundType,this.scale)
  

  lazy val inFixed = in1.fixed.copy(fraction = in1.fixed.fraction + scale)
  lazy val useIn = in1.asInstanceOf[SignalTrait].newSignal(fix = inFixed)

  val round:Boolean = roundType.round  & (inFixed.fraction > fixed.fraction)
  val clip:Boolean =  roundType.clip   & (inFixed.integer > fixed.integer)
  
  //override def shouldSplit:Boolean = false
  /** Output of the initial round block */
  private val realInternal:FixedType = internal.getOrElse(this.inFixed)
  private val internalSignal:SignalTrait = SignalTrait(name + "_i",OpType.Signal,realInternal)
  private val shift = realInternal.fraction-fixed.fraction
  private val roundTerm:SimpleSegment = NewConstant(math.pow(2.0,shift-1).toInt,realInternal.width)


  def createCode(implicit writer:CodeWriter):SegmentReturn  = {
    if (this.fixed == this.inFixed) {
      writer.createCode(in1)
    }
    else if (round && clip) {   // Both Round and Clip
      val state = (internalSignal ::= useIn(realInternal) + this.roundTerm).create
      val cl = ClipSegment(internalSignal,this.fixed)
      writer.createCode(cl).extra(List(state),List(internalSignal))
    }
    else if (clip) {            // Only Clipping
      val cl = ClipSegment(useIn,this.fixed)
      writer.createCode(cl)
    }
    else if (round) { // Only Rounding
      val state = (internalSignal ::= useIn(realInternal) + this.roundTerm).create
      writer.createCode(FixedSelect(internalSignal,this.fixed)).extra(List(state),List(internalSignal))
    }
    else {
      writer.createCode(FixedSelect(useIn,this.fixed))
    }
  }


}

object RoundSegment {

  def apply(name:String, in1:SimpleSegment, internal:FixedType, fixed:FixedType, roundType:Roundable.RoundType, scale:Int = 0) =
    new RoundSegment.Impl(name,in1,fixed,internal,roundType,scale)

  
  def Truncate(in1:SimpleSegment,internal:FixedType, fixed:FixedType = FixedType.Simple) 
    = new Impl(in1.name,in1,fixed,internal,Roundable.Truncate)
  def TruncateClip(in1:SimpleSegment,internal:FixedType, fixed:FixedType = FixedType.Simple)
    = new Impl(in1.name,in1,fixed,internal,Roundable.TruncateClip)
  def Round(in1:SimpleSegment,internal:FixedType, fixed:FixedType = FixedType.Simple)
    = new Impl(in1.name,in1,fixed,internal,Roundable.Round)
  def RoundClip(in1:SimpleSegment,internal:FixedType, fixed:FixedType = FixedType.Simple)
    = new Impl(in1.name,in1,fixed,internal,Roundable.RoundClip)
  
  class Impl(override val name:String,
    override val in1:SimpleSegment,
    override val fixed:FixedType,
    override val internal:FixedType,
    override val roundType:Roundable.RoundType,
    override val scale:Int= 0) extends RoundSegment

  
  
}

