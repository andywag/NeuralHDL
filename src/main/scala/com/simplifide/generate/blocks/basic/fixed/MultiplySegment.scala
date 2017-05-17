package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.generator.{BasicSegments, CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser._
import com.simplifide.generate.blocks.basic.{BinarySegment, Statement}
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType
import com.simplifide.generate.blocks.basic.float.RangeShift
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.parser.items.SingleConditionParser
import com.simplifide.generate.signal._
import com.simplifide.generate.signal.bus.FloatSignal2


/**
 * Class which controls the creation of a multiplication segment
 *
 * @constructor
 * @parameter name Name of Segment
 * @parameter in1 First Input
 * @parameter in2 Second Input
 * @parameter fixed Output Fixed Type
 * @parameter internal Internal Fixed Type
 */

trait MultiplySegment extends BinarySegment with Roundable {

  type T = SimpleSegment

  val in1:T
  val in2:T
  val internal:FixedType             = FixedType.Simple
  val roundType:Roundable.RoundType  = Roundable.Truncate


  val  multiplierFixed = in1.fixed * in2.fixed

  lazy val round:Boolean = roundType.round && (multiplierFixed.fraction > fixed.fraction)
  lazy val clip:Boolean = roundType.clip && (multiplierFixed.integer > fixed.integer)


  def newMultiplier(name:String                = this.name,
    in1:T                                      = this.in1,
    in2:T                                      = this.in2,
    fixed:FixedType                            = this.fixed,
    internal:FixedType                         = this.internal,
    roundType:Roundable.RoundType              = this.roundType):MultiplySegment =
  {
    this match {
      //case x:FloatMult =>  new FloatMult(name,x.in1,x.in2)
      case _           =>  new MultiplySegment.Impl(name,in1,in2,fixed,internal,roundType)
    }
  }




  def createNewRound(roundType:RoundType,internal:FixedType) =
    this.newMultiplier(roundType = roundType, internal = internal)


  override def newSegment(output:SimpleSegment,in1:SimpleSegment = this.in1,in2:SimpleSegment=this.in2):SimpleSegment =
    this.newMultiplier(name = output.name,in1 = in1.asInstanceOf[T], in2 = in2.asInstanceOf[T],fixed=output.fixed)

  /** Calculates the Real Internal Value which is used for the initial calculation. If not specified this assumes that
   *  the width is equal to the total width of the inputs */
  val realInternal:FixedType = {
    internal.getOrElse(this.in1.fixed + this.in2.fixed)
  }


  /** Output of the initial round block */
  val internalSignalM:SignalTrait = SignalTrait(appendName("im"),OpType.Signal,multiplierFixed)
  //val internalSignalR:SignalTrait = SignalTrait(name + "_ir",OpType.Signal,realInternal)


  // TODO Clean up this code. All the cases are actually the same. Multiply might not even require to be combined with round
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {

    val state = (internalSignalM ::= new MultiplySegment.Basic(in1,in2)).create
    val multiplier = state

    val result = if (this.fixed == this.multiplierFixed) {

      writer.createCode(internalSignalM)
    }
    else if (round && clip) {
      writer.createCode(RoundSegment.RoundClip(this.internalSignalM,this.multiplierFixed,this.fixed))
    }
    else if (clip) {
      writer.createCode(ClipSegment(this.internalSignalM,this.fixed))
    }
    else if (round) {
      writer.createCode(RoundSegment.Round(this.internalSignalM,this.multiplierFixed,this.fixed))
    }
    else {
      writer.createCode(this.internalSignalM(fixed))
    }
    result.extra(List(state),List(internalSignalM))
  }


}

object MultiplySegment {

  /** Factory Method for creating multiplier */
  def apply(in1:SimpleSegment, in2:SimpleSegment) = {
    (in1,in2) match {
      case (x:NewConstant,_) =>
        if (x.binaryFactor.isDefined) BinaryShift(in2,x.binaryFactor.get)
        else  new Impl("",in1,in2,FixedType.Simple,FixedType.Simple)
      //case (x:FloatSignal,y:FloatSignal) =>
       // new FloatMult("",x,y)
      case _ =>
        new Impl("",in1,in2,FixedType.Simple,FixedType.Simple)
      }
    }

  /** Truncation Addition Segment */
  class Impl(override val name:String,
    override val in1:MultiplySegment#T,
    override val in2:MultiplySegment#T,
    override val fixed:FixedType,
    override val internal:FixedType,
    override val roundType:Roundable.RoundType = Roundable.Truncate) extends MultiplySegment


  class Basic(val in1:SimpleSegment, val in2:SimpleSegment) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return writer.createCode(in1) + " * " + writer.createCode(in2)
    }
  }


  
}

