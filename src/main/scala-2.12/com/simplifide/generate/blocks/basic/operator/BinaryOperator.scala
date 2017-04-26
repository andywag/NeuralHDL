package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.BinarySegment


/**
 * Class which defines a binary operation
 *
 * @contructor
 * @parameter in1 Left Hand Side of the Operator
 * @parameter in2 Right Hand Side of the Operator
 */
trait BinaryOperator extends BinarySegment {


  /** Operator for this operation */
  val operator:String

  def newSegment(out:SimpleSegment,in1:SimpleSegment = this.in1,in2:SimpleSegment=this.in2):SimpleSegment = {
    this match {
      case x:BinaryOperator.And   => new BinaryOperator.And(in1,in2)
      case x:BinaryOperator.Or    => new BinaryOperator.Or(in1,in2)
      case x:BinaryOperator.Nand  => new BinaryOperator.Nand(in1,in2)
      case x:BinaryOperator.Nor   => new BinaryOperator.Nor(in1,in2)
      case x:BinaryOperator.Xor   => new BinaryOperator.Xor(in1,in2)
      case x:BinaryOperator.Nxor  => new BinaryOperator.Nxor(in1,in2)
      case x:BinaryOperator.Sl    => new BinaryOperator.Sl(in1,in2)
      case x:BinaryOperator.Sr    => new BinaryOperator.Sr(in1,in2)
      case x:BinaryOperator.LogicalAnd    => new BinaryOperator.LogicalAnd(in1,in2)
      case x:BinaryOperator.GT     => new BinaryOperator.GT(in1,in2)
      case x:BinaryOperator.GTE    => new BinaryOperator.GTE(in1,in2)
      case x:BinaryOperator.LT     => new BinaryOperator.LT(in1,in2)
      case x:BinaryOperator.LTE    => new BinaryOperator.LTE(in1,in2)
      case x:BinaryOperator.EQ     => new BinaryOperator.EQ(in1,in2)
      case x:BinaryOperator.NEQ    => new BinaryOperator.NEQ(in1,in2)
      case x:BinaryOperator.EQ3     => new BinaryOperator.EQ3(in1,in2)
      case x:BinaryOperator.NEQ3    => new BinaryOperator.NEQ3(in1,in2)
      case x:BinaryOperator.NOT     => new BinaryOperator.NOT(in1,in2)
      case x:BinaryOperator.Plus    => new BinaryOperator.Plus(in1,in2)
      case x:BinaryOperator.Minus   => new BinaryOperator.Minus(in1,in2)
      case x:BinaryOperator.Multiply => new BinaryOperator.Multiply(in1,in2)
    }
  }



  override def createCode(implicit writer:CodeWriter):SegmentReturn  =
    SegmentReturn("(") + writer.createCode(in1 ++ operator ++ in2) + SegmentReturn(")")



}

object BinaryOperator  {

  def AND(in1:SimpleSegment,in2:SimpleSegment)        = new And(in1,in2)
  def OR(in1:SimpleSegment,in2:SimpleSegment)         = new Or(in1,in2)
  def NAND(in1:SimpleSegment,in2:SimpleSegment)        = new Nand(in1,in2)
  def NOR(in1:SimpleSegment,in2:SimpleSegment)         = new Nor(in1,in2)
  def XOR(in1:SimpleSegment,in2:SimpleSegment)        = new Xor(in1,in2)
  def NXOR(in1:SimpleSegment,in2:SimpleSegment)         = new Nxor(in1,in2)

  def LogicalAnd(in1:SimpleSegment,in2:SimpleSegment) = new LogicalAnd(in1,in2)
  def GT(in1:SimpleSegment,in2:SimpleSegment) = new GT(in1,in2)
  def GTE(in1:SimpleSegment,in2:SimpleSegment) = new GTE(in1,in2)
  def LT(in1:SimpleSegment,in2:SimpleSegment) = new LT(in1,in2)
  def LTE(in1:SimpleSegment,in2:SimpleSegment) = new LTE(in1,in2)
  def EQ(in1:SimpleSegment,in2:SimpleSegment) = new EQ(in1,in2)
  def NEQ(in1:SimpleSegment,in2:SimpleSegment) = new NEQ(in1,in2)
  def EQ3(in1:SimpleSegment,in2:SimpleSegment) = new EQ3(in1,in2)
  def NEQ3(in1:SimpleSegment,in2:SimpleSegment) = new NEQ3(in1,in2)
  def NOT(in1:SimpleSegment,in2:SimpleSegment) = new NEQ3(in1,in2)
  def SL(in1:SimpleSegment,in2:SimpleSegment) = new Sl(in1,in2)
  def SR(in1:SimpleSegment,in2:SimpleSegment) = new Sr(in1,in2)

  def Plus(in1:SimpleSegment,in2:SimpleSegment) = new Plus(in1,in2)

  class And(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " & "
  }

  class Or(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " | "
  }
  class Nand(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " ~& "
  }
  class Nor(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " ~| "
  }
  class Xor(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " ^ "
  }
  class Nxor(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " ~^ "
  }
  class Sl(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " << "
  }
  class Sr(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " >> "
  }

  class LogicalAnd(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " && "
  }

  class GT(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " > "
  }
  class GTE(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " >= "
  }
  class LT(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " < "
  }
  class LTE(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " <= "
  }
  class EQ(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " == "
  }
  class NEQ(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " != "
  }
  class EQ3(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " === "
  }
  class NEQ3(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " !== "
  }
  class NOT(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " ~ "
  }

  class Plus(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " + "
  }

  class Minus(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " - "
  }

  class Multiply(val in1:SimpleSegment,val in2:SimpleSegment) extends BinaryOperator {
      override val operator = " * "

  }


}