package com.simplifide.generate.signal

/** Class which defines different types of constant definitions */

import complex.ComplexNumber


/**
 * @deprecated : Need to fix the adder tree before removal
 */
abstract class ConstantValue {
  def getFloatValue(fixed:FixedType):Float; 
  def getComplexValue(fixed:FixedType):ComplexNumber;
  def getRealValue(fixed:FixedType):Float = getFloatValue(fixed)
  def getImagValue(fixed:FixedType):Float = (0.0).toFloat
  def getDoubleValue(fixed:FixedType):Double = getFloatValue(fixed).toDouble

}

object ConstantValue {
  
  class IntegerValue(val value:Int) extends ConstantValue {
    def getFloatValue(fixed:FixedType):Float = value.toFloat/math.pow(2.0, fixed.fraction).toFloat
    def getComplexValue(fixed:FixedType):ComplexNumber = new ComplexNumber(value.toDouble,0.0)
    override def toString = value.toString
  }
  
  class FloatValue(val value:Float) extends ConstantValue {
    def getFloatValue(fixed:FixedType):Float = value
    def getComplexValue(fixed:FixedType):ComplexNumber = new ComplexNumber(value.toDouble,0.0)
    override def toString = value.toString
  }

  class DoubleValue(val value:Double) extends ConstantValue {
    override def getDoubleValue(fixed:FixedType):Double = value
    def getFloatValue(fixed:FixedType):Float = value.toFloat
    def getComplexValue(fixed:FixedType):ComplexNumber = new ComplexNumber(value.toDouble,0.0)
    override def toString = value.toString
  }

  
  class Complex(val real:Double, val imag:Double) extends ConstantValue{
    def getFloatValue(fixed:FixedType):Float = return real.toFloat
    def getComplexValue(fixed:FixedType):ComplexNumber = return new ComplexNumber(real,imag)
    override def getRealValue(fixed:FixedType):Float = real.toFloat
    override def getImagValue(fixed:FixedType):Float = imag.toFloat
    override def toString = real.toString + "+j" + imag.toString

  }
  
}
