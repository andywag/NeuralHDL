package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.blocks.basic.operator.{BinaryOperator, Operators}
import com.simplifide.generate.generator.{BasicSegments, CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.blocks.basic.{BinarySegment, Statement, fixed}
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType
import com.simplifide.generate.blocks.float.FloatAddition
import com.simplifide.generate.parser.items.SingleConditionParser.IfStatement
import com.simplifide.generate.parser.model.BasicExpressions
import com.simplifide.generate.parser.{ConditionParser, SegmentHolder, SignalParser}

/**
 * Addition Segment
 */

trait AdditionSegment extends BinarySegment with Roundable {
  /** Defines whether this segment is a subtraction */
  val negative:Boolean
  /** Internal Width for this calcuation */
  val internal:FixedType       = FixedType.Simple
  /** Internal width of this operation */
  val realInternal:FixedType = internal.getOrElse(this.in1.fixed.union(in2.fixed,fixed))
  /** Defines whether this segment contains a round operation */
  def round:Boolean = this match {
    case x:AdditionSegment.Round          => (realInternal.fraction > fixed.fraction)
    case x:AdditionSegment.RoundClip      => (realInternal.fraction > fixed.fraction)
    case _                                => false
  }
  /** Defines whether this segment contains a clip operation */
  def clip:Boolean = this match {
    case x:AdditionSegment.TruncateClip   => (realInternal.integer > fixed.integer)
    case x:AdditionSegment.RoundClip      => (realInternal.integer > fixed.integer)
    case _                                => false
  }
  private val shift = realInternal.fraction-fixed.fraction
  /** Rounding Term if round is required */
  val roundTerm:SimpleSegment = NewConstant(math.pow(2.0,shift-1).toInt,realInternal)
  /** Output of the initial round block */
  val internalSignal:SignalTrait = SignalTrait(name + "_i",OpType.Signal,realInternal)

  def newAdder(name:String = this.name,
    in1:SimpleSegment  = this.in1,
    in2:SimpleSegment  = this.in2,
    negative:Boolean   = this.negative,
    fixed:FixedType    = this.fixed,
    internal:FixedType = this.internal) = {
    this match {
      case x:AdditionSegment.Truncate      => new AdditionSegment.Truncate(name,in1,in2,negative,fixed,internal)
      case x:AdditionSegment.TruncateClip  => new AdditionSegment.TruncateClip(name,in1,in2,negative,fixed,internal)
      case x:AdditionSegment.Round         => new AdditionSegment.Round(name,in1,in2,negative,fixed,internal)
      case x:AdditionSegment.RoundClip     => new AdditionSegment.RoundClip(name,in1,in2,negative,fixed,internal)
      case x:FloatAddition => new FloatAddition(name,x.in1,x.in2,negative)(x.clk)
    }
  }


  
  override def newSegment(output:SimpleSegment,in1:SimpleSegment = this.in1,in2:SimpleSegment=this.in2):SimpleSegment =
    this.newAdder(name = output.name,in1 = in1, in2 = in2,fixed=output.fixed)


  def createNewRound(roundType:RoundType,internal:FixedType) = {
    roundType match {
      case Roundable.Truncate     => new AdditionSegment.Truncate(name,in1,in2,negative,fixed,internal)
      case Roundable.TruncateClip => new AdditionSegment.TruncateClip(name,in1,in2,negative,fixed,internal)
      case Roundable.Round        => new AdditionSegment.Round(name,in1,in2,negative,fixed,internal)
      case Roundable.RoundClip    => new AdditionSegment.RoundClip(name,in1,in2,negative,fixed,internal)
    }
  }



  // TODO Use of Instance of is a kludge so data is not indexed 2x. Needs to be fixed
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {

    val bypass1 = in1 match {case x:FixedSelect => true; case _ => false}
    val bypass2 = in2 match {case x:FixedSelect => true; case _ => false}
 
    if ( bypass1 | bypass2 | ((this.round == false) && (this.clip == false))) {
      val input = this.fixed match {case FixedType.Simple => in1; case _ => in1(this.fixed)}
      return writer.createCode(input) + (if (negative) " - " else " + ") + writer.createCode(this.in2(this.fixed))
    }

    val base = if (negative) in1(realInternal) - in2(realInternal) else in1(realInternal) + in2(realInternal)
    val baseSegment = if (round) base + roundTerm else base
    val state = (internalSignal ::= baseSegment).create

    if (this.round && this.clip) {
      return writer.createCode(FixedSelect(internalSignal,this.fixed)).extra(List(state))
    }
    else if (this.clip) {
      return writer.createCode(ClipSegment(internalSignal,this.fixed)).extra(List(state))
    }
    else if (this.round) {
      return writer.createCode(FixedSelect(internalSignal,this.fixed)).extra(List(state))
    }
    else {  // Neither Term just return this segment
      return writer.createCode(baseSegment.create)
    }
  }


}

object AdditionSegment {

  def apply(name:String, in1:SimpleSegment, in2:SimpleSegment, negative:Boolean) = {
    (in1,in2) match {
      //case (x:FloatSignal,y:FloatSignal) => new FloatAddition("",x,y,negative)
      case _ =>   new Truncate("",in1,in2,negative,FixedType.Simple)
    }
  }

   class Truncate(
    override val name:String, 
    override val in1:SimpleSegment,
    override val in2:SimpleSegment,
    override val negative:Boolean,
    override val fixed:FixedType = FixedType.Simple, 
    override val internal:FixedType = FixedType.Simple) extends AdditionSegment

  class TruncateClip(
    override val name:String,
    override val in1:SimpleSegment,
    override val in2:SimpleSegment,
    override val negative:Boolean,
    override val fixed:FixedType = FixedType.Simple,
    override val internal:FixedType = FixedType.Simple) extends AdditionSegment

  class Round(
    override val name:String,
    override val in1:SimpleSegment,
    override val in2:SimpleSegment,
    override val negative:Boolean,
    override val fixed:FixedType = FixedType.Simple,
    override val internal:FixedType = FixedType.Simple) extends AdditionSegment

  class RoundClip(
    override val name:String,
    override val in1:SimpleSegment,
    override val in2:SimpleSegment,
    override val negative:Boolean,
    override val fixed:FixedType = FixedType.Simple,
    override val internal:FixedType = FixedType.Simple) extends AdditionSegment


  }