package com.simplifide.generate.signal.complex

import com.simplifide.generate.signal.{ ConstantValue, Constant, FixedType}
import com.simplifide.generate.generator.SimpleSegment

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
class ComplexConstant(proto:Constant) extends SimpleSegment with Complex{
//extends ComplexSignal(proto) {

  override val real =  getRealConstant
  override val imag =  getImagConstant

  def partialString(str:String,max:Int) = {
    val umax1 = if (str.startsWith("-")) max + 1 else max
    val umax = if (str.length <= umax1-1) str.length - 1 else umax1
    str.subSequence(0,umax)
  }

  /** Debug Description of the String */
  val complexString:String = {
    val builder = new StringBuilder
    builder.append(partialString(getRealConstant.value.toString.trim,4))
    builder.append("+j")
    builder.append(partialString(getImagConstant.value.toString.trim,4))
    builder.toString
  }

  /** Debug description of the string in magnitude phase */
  val magPhaseString:String = {
    val re = getRealConstant.value.getFloatValue(null).toDouble
    val im = getImagConstant.value.getFloatValue(null).toDouble
    val mag = math.sqrt(re*re + im*im)
    val ang = (180.0/math.Pi)*math.atan2(im,re)
    val builder = new StringBuilder
    builder.append(partialString(mag.toString,5))
    builder.append("<")
    builder.append(partialString(ang.toString,5))
    builder.toString
  }

  def getRealConstant:Constant =
    return Constant(proto.value.getRealValue(fixed),fixed)

  
  def getImagConstant:Constant =
    return Constant(proto.value.getImagValue(fixed),this.fixed)

  
  def getNegativeImagConstant:Constant =
    return  Constant(-proto.value.getImagValue(fixed),this.fixed)

  

  /*
  override def getSegment(index:Int):StatementSegment = {
     if (proto.vector.arr.size > 0) {
       val nvec = proto.vector.arr.slice(1, proto.vector.arr.size)
       val nproto = proto.copyAsSignalNew(proto.name1 + "_" + index, None, None,Some(new VectorType(nvec,proto.vector.reg)))
       return new ComplexSignal(nproto)
    }
     if (index == 0) return getRealConstant
     else return getImagConstant
  } */
    
  
}

object ComplexConstant {

  // TODO : Isn't functioning properly
  def apply(real:Double, imag:Double):ComplexConstant = {
    val fixed = Constant(real).fixed.union(Constant(imag))
    new ComplexConstant( Constant(new ConstantValue.Complex(real,imag),fixed))
  }

	def apply(fixed:FixedType,re:Float,im:Float):ComplexConstant = {
		val value = new ConstantValue.Complex(re,im)
		val con   =  Constant(value,fixed)
		new ComplexConstant(con)
	}

  def apply(fixed:FixedType,re:Double,im:Double):ComplexConstant = {
		ComplexConstant(fixed,re.toFloat,im.toFloat)
	}
		
}

*/