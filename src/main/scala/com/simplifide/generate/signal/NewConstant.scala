package com.simplifide.generate.signal

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.signal.NewConstant.{HEX, Long}
import com.simplifide.generate.parser.items.MiscParser

/**
 * Constant Definition
 */

trait NewConstant extends SimpleSegment {

  val outputType:NewConstant.OutputType = NewConstant.DECIMAL

  
  /** Does a bit slice operation by creating */
  override def apply(index:(Int,Int)) =
    new NewConstant.Long(selectLongValue(index),FixedType.unsigned(index._1-index._2+1,0),outputType)
  /** Does a bit slice operation by creating */
  override def apply(width:MiscParser.Width) =
    new NewConstant.Long(selectLongValue( (width.top,width.bottom) ),FixedType.unsigned(width.top - width.bottom +1,0),outputType)
  
  def selectLongValue(index:(Int, Int)):scala.Long = {
    val long = longValue
    val scaleValue = math.pow(2.0,index._1-index._2+1).toLong-1 << index._2
    val delta      = (longValue & scaleValue) >> index._2
    delta.toLong
  }
  
  
  

  val hex = outputType match {
    case NewConstant.HEX => true
    case _               => false
  }


  def isInteger(double:scala.Double) = math.floor(double) == math.ceil(double)

  def binaryFactor:Option[Int] = {
    this match {
      case x:NewConstant.Long => {
        val number = math.log(x.value)/math.log(2.0)
        if (isInteger(number)) Some(number.toInt) else None
      }
      case x:NewConstant.Double => {
        val number = math.log(x.value)/math.log(2.0)
        if (isInteger(number))
          Some(number.toInt)
        else
          None
      }
    }
  }
  
  def newConstant(fixed:FixedType = this.fixed):SimpleSegment = {
    this match {
      case x:NewConstant.Long   => new NewConstant.Long(x.value,fixed,x.outputType)
      case x:NewConstant.Double => new NewConstant.Double(x.value,fixed)
      case x:NewConstant.NewLong   => new NewConstant.Long(x.value,fixed,x.outputType)
      case _                    => null
    }
  }

  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = newConstant(output.fixed)

  def longValue:scala.Long = {
    this match {
      case x:NewConstant.Long   => x.value
      case x:NewConstant.NewLong => x.value
      case x:NewConstant.Double => (math.pow(2.0,x.fixed.fraction)*x.value).toLong
      case _                    => this.trueValue
    }
  }
  
  
  /** Creates the prefix for the constant value */
  private def prefixString(value:String, negative:Boolean) = {
    val prefix = this.outputType match {
      case NewConstant.BINARY  => if (this.fixed.isSigned) "'sb" else "'b"
      case NewConstant.OCTAL   => if (this.fixed.isSigned) "'so" else "'o"
      case NewConstant.DECIMAL => if (this.fixed.isSigned) "'sd" else "'d"
      case NewConstant.HEX     => if (this.fixed.isSigned) "'sh" else "'h"
    }
    val neg = if (negative) "-" else ""
    if (this.fixed.width > 0) neg + fixed.width.toString + prefix + value else negative + prefix + value
  }
  
  /** Creates the true value of the constant */
  private def trueValue = {
    val value = if (!this.fixed.isSigned & this.longValue > math.pow (2.0,63)) this.longValue - math.pow(2.0,64).toLong
                else this.longValue
    value
  }

  private def stringValue = {
    val negative = (fixed.isSigned & longValue < 0)
    this.outputType match {
      case NewConstant.BINARY   => prefixString(java.lang.Long.toBinaryString(trueValue),negative)
      case NewConstant.OCTAL    => prefixString(java.lang.Long.toOctalString(trueValue),negative)
      case NewConstant.DECIMAL  => prefixString(trueValue.toString,negative)
      case NewConstant.HEX      => prefixString(java.lang.Long.toHexString(trueValue),negative)
    }
  }

  def createCodeSimple2 (implicit writer:CodeWriter):SegmentReturn =  {
    val result = this.stringValue
    result
  }

  def createCodeSimple(value:scala.Long, fixed:FixedType) (implicit writer:CodeWriter):SegmentReturn =  {
    
    val numberType    = if (hex) "h" else "d"
    val widthString   = if (fixed == FixedType.Simple || fixed.width == 0) "" else fixed.width.toString
    val prefix        = if (fixed.isSigned) "'s" + numberType else "'" + numberType // Select between hex and decimal
    val negative      = (value < 0)
    val value1        = if (fixed.isSigned) math.abs(value) else value
    val numberString  = if (hex) java.lang.Long.toHexString(value1) else value1.toString
    val index         = math.max(math.ceil(fixed.width/4).toInt,1).toInt
    val numberString1 = if (hex) {
      if (16 - index < numberString.length()) numberString.substring(16-index) else numberString
    }
    else numberString

    val negativeString = if (negative && fixed.isSigned)  "-" else ""
    
    SegmentReturn(negativeString) + widthString + prefix + numberString1
     
        
  }
  
  def createCode(implicit writer:CodeWriter) = createCodeSimple(this.longValue, this.fixed)
  
  
}

object NewConstant {
  


  /** 
   * Factory Methods to create a constant 
   **/
  def apply(value:Int):NewConstant = new Long(value.toLong, FixedType.Simple)
  def apply(value:Int,width:Int):NewConstant = new Long(value.toLong, FixedType.unsigned(width,0))
  def apply(value:scala.Long):NewConstant = new Long(value, FixedType.Simple)
  def apply(value:scala.Long,width:Int):NewConstant = new Long(value, FixedType.unsigned(width,0))
  def apply(value:Int,fixed:FixedType):NewConstant = new Long(value.toLong, fixed)
  def apply(value:scala.Long,fixed:FixedType):NewConstant = new Long(value, fixed)
  /** Double Methods */
  def apply(value:scala.Double):NewConstant = new Double(value,FixedType.Simple)
  def apply(value:scala.Double,fixed:FixedType):NewConstant = new Double(value,fixed)
  /** Maximum Value for this fixed type */
  def max(fixed:FixedType) = NewConstant((math.pow(2.0,fixed.width-1)-1)/math.pow(2.0,fixed.fraction),fixed)
  /** Minimum Value for this fixed type */
  def min(fixed:FixedType) = NewConstant(-(math.pow(2.0,fixed.width-1)-1)/math.pow(2.0,fixed.fraction),fixed)
 
  class Long(val value:scala.Long,  override val fixed:FixedType, override val outputType:OutputType = DECIMAL) extends NewConstant
  class Double(val value:scala.Double,  override val fixed:FixedType) extends NewConstant


  class NewLong(val value:scala.Long,  override val fixed:FixedType, override val outputType:OutputType = DECIMAL) extends NewConstant {
    override def createCode(implicit writer:CodeWriter) = this.createCodeSimple2
  }
  
  object HighImpedance extends NewConstant {
    override def createCode(implicit writer:CodeWriter) = SegmentReturn("'bz")

    override def create(implicit creator:CreationFactory):SimpleSegment = this
    /** Create Expression as a function of the output */
    override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = this
  }


  def Hex(value:scala.Long, width:Int) = new NewConstant.Long(value, FixedType.unsigned(width,0),HEX)
  def Octal(value:scala.Long, width:Int) = new NewConstant.Long(value, FixedType.unsigned(width,0),OCTAL)
  def Decimal(value:scala.Long, width:Int) = new NewConstant.Long(value, FixedType.unsigned(width,0),DECIMAL)


  class OutputType
  class OutputSigned
  
  object DECIMAL extends OutputType
  object OCTAL   extends OutputType
  object HEX     extends OutputType
  object BINARY  extends OutputType


  
  
}