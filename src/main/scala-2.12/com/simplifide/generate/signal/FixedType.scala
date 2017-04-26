package com.simplifide.generate.signal

import com.simplifide.generate.parser.model.Model
import com.simplifide.generate.signal.FixedType.Signing



/**
 * Class describing the fixed type properties of a signal
 **/
trait FixedType {
  val width:Int

  val fraction:Int
  /** Signing of the value either signed or unsigned */
  val signed:Signing

  val functionName = (if (this.isSigned) "s" else "u") + "_" + width + "_" + fraction

  /** Number of Integer bits */
  val integer:Int = if (this.isSigned) width - fraction -1 else width - fraction
  /** Returns the description for this type */
  def getDescription:String = return "<" + width + "," + fraction + ">"
  /** Method which specifies if this type is signed*/
  def isSigned = signed match {
    case FixedType.Signed    => true
    case _                   => false
  }

  override def toString = "<" + (if (this.isSigned) "s" else "u") + "," + this.width + "," + this.fraction + ">"
  
  def declaration:String = if (this.width <= 1) "" else "[" + (width-1) + ":" + "0]"

  //signed.isSigned
  
  def copy(signed:Signing = this.signed, width:Int = this.width, fraction:Int = this.fraction) =
    FixedType(signed,width,fraction)

  private def equals(typ:FixedType):Boolean =
    if (signed == this.signed && typ.width == this.width && typ.fraction == this.fraction) return true
    else return false


  def plus(fix:FixedType):FixedType = {
    FixedType(signed,this.width + fix.width,this.fraction + fix.fraction)
  }

  /** Result of multiplying 2 fixed types together */
  def * (fix:FixedType) = FixedType(this.signed,this.width + fix.width, this.fraction + fix.fraction)
  /** Result of adding 2 fixed types together */
  def + (fix:FixedType):FixedType = FixedType(signed,this.width + fix.width,this.fraction + fix.fraction)

  def == (fix:FixedType):Boolean = equals(fix)

  /** Create the total width which would occur by adding 2 fixed types together */
  def union(fixed:FixedType*):FixedType = {
    val integer     = math.max(this.integer,fixed.map(_.integer).reduceLeft(math.max(_,_))) // math.max(this.integer,fixed.integer)
    val fraction    = math.max(this.fraction,fixed.map(_.fraction).reduceLeft(math.max(_,_))) //math.max(this.fraction,fixed.fraction)
    FixedType(signed,integer+fraction,fraction)
  }



  def getOrElse(fixed:FixedType):FixedType = this

}

/**
 * Factory methods for creating a fixed point type
 **/
object FixedType {
  def apply(signed:Signing, width:Int,frac:Int) = new FixedType.Main(signed,width,frac)
  /** Creation of Signed type */
  def signed(fixed:FixedType):FixedType           = signed(fixed.width,fixed.fraction)
  /** Creation of signed type with width width and fraction */
  def signed(width:Int, fraction:Int):FixedType   = new FixedType.Main(FixedType.Signed,width,fraction)
  /** Creation of unsigned type with width width and fraction */
  def unsigned(width:Int, fraction:Int):FixedType = new FixedType.Main(FixedType.UnSigned,width,fraction)
  /** Implementation of Fixed Type */
   class Main(val signed:Signing, val width:Int,val fraction:Int) extends FixedType

  object Simple extends Main(FixedType.UnSigned,1,0)  {
    override def getOrElse(fixed:FixedType):FixedType = fixed
  }

  object SimpleSigned extends Main(FixedType.Signed,1,0)  {
    override def getOrElse(fixed:FixedType):FixedType = fixed
  }


  class Signing {}


  object Signed extends FixedType.Signing
  object UnSigned extends Signing
  object Control extends Signing


}
