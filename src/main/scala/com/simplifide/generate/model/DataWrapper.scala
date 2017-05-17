package com.simplifide.generate.model

import com.simplifide.generate.blocks.basic.float.Stimulus.float2Hex
import com.simplifide.generate.test2.data.DataSet.{CompareError, CompareSuccess}
import com.simplifide.generate.test2.data.DataTrait
import com.simplifide.generate.test2.data.FloatData.FloatWrap
import com.simplifide.generate.util.StringOps

/**
  * Created by andy on 5/13/17.
  */
trait DataWrapper[T] {
  val value:T

  val write:String
  val debug:String
  val read:(String)=>DataWrapper[T]
}

object DataWrapper {
  case class FloatWrap(override val value:Float) extends DataWrapper[Float] {


    val realValue = float2Hex(value)
    val man = 0x7FFFFF & realValue
    val exp = (realValue >> 23) & 0xFF
    val sign = (realValue >> 31) & 0x1

    //override val element:FloatWrap = this
    override val write: String = Integer.toHexString(realValue)
    override val read: (String) => FloatWrap =
      x => {
        val data = java.lang.Long.parseLong(x.trim, 16)
        FloatWrap(java.lang.Float.intBitsToFloat(data.toInt))
      }
    def message(input:FloatWrap) = s"Mismatch ${this.write} - ${input.write} --- ${this.value} - ${input.value}"
    def compareExact(input:FloatWrap, line:Int) = {
      val eMatch = (exp == input.exp)
      val sMatch = (sign == input.sign)
      val mMatch = (man == input.man)

      if (!eMatch | !sMatch | !mMatch) {

        Some(CompareError(message(input), line))
      }
      else None
    }

    def compareApprox(input:FloatWrap, line:Int) = {
      val error = math.abs(input.value - this.value)
      if ((input.value/error < 10000) & (input.value > 1e-35) ) Some(CompareError(message(input), line)) else None
    }

    def compare(input: FloatWrap, line: Int) = {
      this.compareApprox(input,line)
    }


    def h(i: Int) = Integer.toHexString(i)

    val debug = {
      StringOps.formatLine(List(h(this.realValue),value.toString,h(exp),h(man) ),List(0,16,32,40))//s"${h(this.realValue)} ${this.value} ${h(exp)} ${h(man)}"
    }

    def *(wrap:FloatWrap) = new FloatWrap(this.value*wrap.value)
    def +(wrap:FloatWrap) = new FloatWrap(this.value + wrap.value)

    override def toString = debug
  }

}
