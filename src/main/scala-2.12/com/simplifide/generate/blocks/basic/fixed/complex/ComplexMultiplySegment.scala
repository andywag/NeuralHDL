package com.simplifide.generate.blocks.basic.fixed.complex





/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/*
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.signal.complex.ComplexSignal
import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.{SegmentHolder, ObjectFactory, ExpressionReturn}
import com.simplifide.generate.generator._
import com.simplifide.generate.blocks.basic.fixed.complex.ComplexMultiplySegment.RoundClip
import com.simplifide.generate.language.SignalFactory
import com.simplifide.generate.signal._
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.blocks.basic.BinarySegment
import com.simplifide.generate.blocks.basic.fixed.{MultiplySegment, AdditionTerm}


trait ComplexMultiplySegment[T <: ComplexSignal] extends MultiplySegment[T] {


  implicit val clk:ClockControl




  override def newMultiplier[S <: ComplexSignal](name:String            = this.name,
                    in1:S                  = this.in1,
                    in2:S                  = this.in2,
                    fixed:FixedType        = this.fixed,
                    internal:FixedType     = this.internal):MultiplySegment[S] = {

    this match {
      case x:ComplexMultiplySegment.Truncate[_]     => new ComplexMultiplySegment.Truncate[S](name,in1,in2,fixed,internal)
      case x:ComplexMultiplySegment.TruncateClip[_] => new ComplexMultiplySegment.TruncateClip[S](name,in1,in2,fixed,internal)
      case x:ComplexMultiplySegment.Round[_]        => new ComplexMultiplySegment.Round[S](name,in1,in2,fixed,internal)
      case x:ComplexMultiplySegment.RoundClip[_]    => new ComplexMultiplySegment.RoundClip[S](name,in1,in2,fixed,internal)
      case _                                     => new ComplexMultiplySegment.Truncate[S](name,in1,in2,fixed,internal)
    }
  }

  override def createRound(internal:FixedType = FixedType.Simple)
    = new ComplexMultiplySegment.Round(name,in1,in2,fixed,internal)
  override def createRoundClip(internal:FixedType = FixedType.Simple)
    = new ComplexMultiplySegment.RoundClip(name,in1,in2,fixed,internal)
  override def createTruncate(internal:FixedType = FixedType.Simple)
    = new ComplexMultiplySegment.Truncate(name,in1,in2,fixed,internal)
  override def createTruncateClip(internal:FixedType = FixedType.Simple)
    = new ComplexMultiplySegment.TruncateClip(name,in1,in2,fixed,internal)



  def createBody = {

    /*
    val inReRe = register(this.name+"_re_re", OpType.Signal, this.multiplierFixed)(1)
    val inReIm = register(this.name+"_re_im", OpType.Signal, this.multiplierFixed)(1)
    val inImRe = register(this.name+"_im_re", OpType.Signal, this.multiplierFixed)(1)
    val inImIm = register(this.name+"_im_im", OpType.Signal, this.multiplierFixed)(1)

    val realAdd = register(this.name+"_re_add", OpType.Signal, this.out.fixed)(1)
    val imagAdd = register(this.name+"_re_add", OpType.Signal, this.out.fixed)(1)

    inReRe(n) :=  in1.real * in2.real
    inReIm(n) :=  in1.real * in2.imag
    inImRe(n) :=  in1.imag * in2.real
    inImIm(n) :=  in1.imag * in2.imag

    if (in1.conjugate && in2.conjugate) {
      realAdd(n) := operate(inReRe(n-1)  - inImIm(n-1))
      imagAdd(n) := operate(-inImRe(n-1) - inReIm(n-1))
    }
    else if (in1.conjugate) {
      realAdd(n) := operate(inReRe(n-1) + inImIm(n-1))
      imagAdd(n) := operate(inImRe(n-1) - inReIm(n-1))
    }
    else if (in2.conjugate) {
      realAdd(n) := operate(inReRe(n-1) + inImIm(n-1))
      imagAdd(n) := operate(inReIm(n-1) - inImRe(n-1))
    }
    else {
      realAdd(n) := operate(inReRe(n-1) - inImIm(n-1))
      imagAdd(n) := operate(inImRe(n-1) + inReIm(n-1))
    }


    out.real := realAdd(n-1)
    out.imag := imagAdd(n-1)
    */
  }



}


object ComplexMultiplySegment {
  class Truncate[T <: ComplexSignal](override val name:String,
    override val in1:T,
    override val in2:T,
    override val fixed:FixedType,
    override val internal:FixedType)(implicit val clk:ClockControl) extends ComplexMultiplySegment[T]

  class TruncateClip[T <: ComplexSignal](override val name:String,
    override val in1:T,
    override val in2:T,
    override val fixed:FixedType,
    override val internal:FixedType)(implicit val clk:ClockControl) extends ComplexMultiplySegment[T]

  class Round[T <: ComplexSignal](override val name:String,
    override val in1:T,
    override val in2:T,
    override val fixed:FixedType,
    override val internal:FixedType)(implicit val clk:ClockControl) extends ComplexMultiplySegment[T]

  class RoundClip[T <: ComplexSignal](override val name:String,
    override val in1:T,
    override val in2:T,
    override val fixed:FixedType,
    override val internal:FixedType)(implicit val clk:ClockControl) extends ComplexMultiplySegment[T]

}

*/