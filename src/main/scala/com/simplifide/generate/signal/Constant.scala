package com.simplifide.generate.signal

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}
import com.simplifide.generate.blocks.basic.fixed.FixedSelect
import com.simplifide.generate.parser.ExpressionReturn
import com.simplifide.generate.parser.model.Expression
import math._

/**
 *  Constant value
 *  @deprecated --- Need to fix the adder tree before removal
 */

trait Constant extends SimpleSegment{

  override val opType = OpType.Constant
  /** Value of the Constant */
  val value:ConstantValue

  override def apply(fixed:FixedType):SimpleSegment = new FixedSelect.ConstantSelect(this,fixed)

  /*
  override def newSignal(name:String=this.name,
    opType:OpType = this.opType,
    fix:FixedType = this.fixed):SignalTrait = this
  */

  /** Operating Type for this signal */
  /** Create a select for this Constant */
  /** Return the Integer Value for this Constant. This function Scales up the value by the fraction point of the fixed
   * value*/
  private def getInteger:Int = {
     val flo = value.getDoubleValue(fixed)
     round((flo*pow(2.0,fixed.fraction))).toInt
  }
  /** Creates a CSD Number based on this value */
  def createCSD:List[Constant.CSD] = Constant.createCSD(getInteger)


  def createCode(writer:CodeWriter, fixedIn:Option[FixedType]):SegmentReturn = {

    val uFixed = fixedIn.getOrElse(fixed)
    val flo = value.getDoubleValue(uFixed)
    val res:Double = math.round((flo*math.pow(2.0,uFixed.fraction)))
    val ival = res.toLong

    val builder = new StringBuilder
    if (ival < 0) builder.append("-")
    if (uFixed.width > 0) builder.append(uFixed.width.toString)
    if (uFixed.isSigned) builder.append("'sd") else builder.append("'d")

    builder.append(math.abs(ival).toString)
    return SegmentReturn(builder.toString)
  }

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
     this.createCode(writer,None)
  }

  

   /** Returns a String with Debug Information */
   def debugCSDString():String = {
     val value = "[" + this.value.getFloatValue(this.fixed) + "," + this.getInteger + "]("
     val csd = this.createCSD.zipWithIndex.map(x => if (x._2 == 0) x._1.debugString else "," + x._1.debugString).reduceLeft(_+_)
     return value + csd + ")"
  }


}

/** Factory Methods for dealing with constants */
object Constant {

  // Convenience method for using a signal as it's fixed type
  implicit def SignalTrait2Fixed(signal:SignalTrait):FixedType = signal.fixed

  /** Create a Constant based on a value and a width */
  //def apply(value:Int) = new Impl("",FixedType.unsigned(1,0),new ConstantValue.IntegerValue(value))
  //def apply(value:Int,width:Int) = new Impl("",FixedType.unsigned(width,0),new ConstantValue.IntegerValue(value))
  

  
  

  /** Create a constant from a value and the fixed type */
  //def apply(value:ConstantValue, fixed:FixedType) = new Impl("",fixed,value)
  /** Create a constant from an Integer and a fixed type */
  //def apply(value:Int,fixed:FixedType) =new Impl("",fixed,new ConstantValue.IntegerValue(value))
  /** Create a Constant from a Double and a Fixed Type */
  def apply(value:Double,fixed:FixedType) = new Impl("",fixed,new ConstantValue.DoubleValue(value))


  /** Create a constant without any fixed type specifier */
  def apply(value:Double):Constant = {
    /*val integerValue = math.max(0.0,math.log(value)/math.log(2.0)).toInt
    val values = List.tabulate(32)(i => value*scala.math.pow(2.0,i-16))
    val fracValue = values.indexWhere(x => (x - scala.math.floor(x) == 0)) - 16
    */
    new Derived(new ConstantValue.DoubleValue(value))
  }

  /** Main implementation of the Class */
  class Impl(override val name:String,
             override val fixed:FixedType,
             override val value:ConstantValue) extends Constant

  class Derived(override val value:ConstantValue) extends Constant {
    override val name = ""
    override val fixed:FixedType = FixedType.Simple
    override def split(output:Expression,index:Int):ExpressionReturn = {
      val outFixed = output.asInstanceOf[SimpleSegment].fixed
      new ExpressionReturn(Constant(value.getDoubleValue(outFixed),outFixed),List())
    }

    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return SegmentReturn("'d") + SegmentReturn(value.getFloatValue(fixed).toInt.toString)
    }

  }

  /** Maximum Value for this fixed type */
  //def max(fixed:FixedType) = Constant((math.pow(2.0,fixed.width-1)-1)/math.pow(2.0,fixed.fraction),fixed)
  /** Minimum Value for this fixed type */
  //def min(fixed:FixedType) = Constant(-(math.pow(2.0,fixed.width-1)-1)/math.pow(2.0,fixed.fraction),fixed)


  /** Create a set of CSD for this value */
  def createCSD(value:Int):List[CSD] = {
     if (value == 0) return List()
    
     val avalue:Int = math.abs(value)    // Integer Value of the CSD
     val sign:Int = math.signum(value)   // Sign of the CSD
    
     val l2:Int = math.ceil(math.log(avalue)/math.log(2.0)).toInt
     val pos:Int = -math.pow(2.0,l2).toInt   + avalue        // Finds the positive value
     val neg:Int = -math.pow(2.0,l2-1).toInt + avalue        // Finds the negative value
     
     if (pos == 0) return List(new CSD(sign < 0,math.abs(l2).toInt))
     if (neg == 0) return List(new CSD(sign < 0,math.abs(l2-1).toInt))
     
     val pc:List[CSD] = Constant.createCSD(sign*pos)
     val nc:List[CSD] = Constant.createCSD(sign*neg)
     
     if (pc.size < nc.size) return List(new CSD(sign < 0,math.abs(l2).toInt)) ::: pc
     else return List(new CSD(sign < 0,math.abs(l2-1).toInt)) ::: nc
    
   }
  
  class CSD(val negative:Boolean,val value:Int) {
    def debugString:String = if (negative) "-" + value.toString else value.toString
  }

  class Hex(val hexValue:String,override val fixed:FixedType) extends Constant {

    override val value:ConstantValue = null

    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      SegmentReturn(fixed.width.toString) + "'h" + hexValue
    }
  }

  trait Format
  object HEX extends Format
  object DEC extends Format

  case class ConstantInt(const:Int, width:Int, format:Format = HEX) extends Constant {
    override val value = null

    override def createCode(writer:CodeWriter, fixedIn:Option[FixedType]) = createCode(writer)
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val number = format match {
        case HEX => Integer.toHexString(const)
        case _   => const.toString
      }
      SegmentReturn(width.toString) + "'h" + number
    }
    def +(value:Int) = this.copy(const = const + value)

  }

  
}

