package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.UnarySegment
import com.simplifide.generate.parser.model.Expression


/**
 * Class which defines a unary operation.
 */

trait UnaryOperator extends UnarySegment {

  /** Operator used for this function*/
  val operator:String

  def newSegment(out:SimpleSegment,in1:SimpleSegment = this.in1):SimpleSegment = this match {
    case x:UnaryOperator.Negative   => UnaryOperator.Negative(in1)
    case x:UnaryOperator.NotLogical => UnaryOperator.NotLogical(in1)
    case x:UnaryOperator.Not        => UnaryOperator.Not(in1)
    case x:UnaryOperator.And        => UnaryOperator.And(in1)
    case x:UnaryOperator.NotAnd     => UnaryOperator.NotAnd(in1)
    case x:UnaryOperator.Or         => UnaryOperator.Or(in1)
    case x:UnaryOperator.Positive   => new UnaryOperator.Positive(in1)
    case _                          => UnaryOperator.Or(in1)

  }


  override def createCode(implicit writer:CodeWriter):SegmentReturn  = {
    return writer.createCode(new SimpleSegment.Code(operator) ++ in1)
  }


}

object UnaryOperator  {

  def Negative(in:SimpleSegment) = new Negative(in)
  def NotLogical(in:SimpleSegment) = new NotLogical(in)
  def Not(in:SimpleSegment) = new Not(in)
  def And(in:SimpleSegment) = new And(in)
  def NotAnd(in:SimpleSegment) = new NotAnd(in)
  def Or(in:SimpleSegment) = new Or(in)


  class Positive(override val in1:SimpleSegment) extends UnaryOperator {
    override val operator = "+"
  }

  class Negative(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "-"
  }

  class NotLogical(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "!"
  }

  class Not(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "~"
  }

  class And(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "&"
  }

  class NotAnd(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "~&"
  }

  class Or(override val in1:SimpleSegment) extends UnaryOperator {
      override val operator = "|"
  }

  class Bang(override val in1:SimpleSegment) extends UnaryOperator {
        override val operator = "|"
   }

  class Tilda(override val in1:SimpleSegment) extends UnaryOperator {
        override val operator = "|"
    }




}