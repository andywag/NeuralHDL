package com.simplifide.generate.test2.data

import com.simplifide.generate.blocks.basic.float.Stimulus.float2Hex
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.test2.data.DataSet.{CompareError, CompareSuccess}
import com.simplifide.generate.test2.data.FloatData.FloatWrap
import com.simplifide.generate.test2.util.FloatDataFile
import com.simplifide.generate.test2.util.FloatDataFile.FloatWrapper

/**
  * Created by andy on 4/30/17.
  */
class FloatData(val element:FloatWrap)  {
/*
  override val write: String = Integer.toHexString(element.value)
  override val debug: String = element.debug
  override val read: (String) => FloatWrap =
    x => FloatWrap(java.lang.Float.intBitsToFloat(Integer.valueOf(x,16)))
  //override val random: FloatWrap = _

*/
}

object FloatData {

  def random = {
    def getValue = {
      val data = scala.util.Random.nextFloat()
      if (scala.util.Random.nextBoolean()) data else -data
      //-data
    }
    FloatWrap(getValue)
  }

  def randomList(signal:SignalTrait, length:Int) = {
    val data = Seq.tabulate(length)(x => FloatData.random)
    DataSet(data,signal,FloatWrap.GENERATOR)
  }


  case class FloatWrap(val float:Float) extends DataTrait[FloatWrap] {



    def *(input:DataTrait[FloatWrap]) = new FloatWrap(float*input.element.float)
    def +(input:DataTrait[FloatWrap]) = new FloatWrap(float + input.element.float)

    val value  = float2Hex(float)
    val man   = 0x7FFFFF & value
    val exp   = (value >> 23) & 0xFF
    val sign  = (value >> 31) & 0x1

    override val element:FloatWrap = this
    override val write: String = Integer.toHexString(value)
    override val read: (String) => FloatWrap =
      x => {
        val data = java.lang.Long.parseLong(x.trim,16)
        FloatWrap(java.lang.Float.intBitsToFloat(data.toInt))
      }

    def compare(input:DataTrait[FloatWrap], line:Int) = {
      val eMatch =  (exp == input.element.exp)
      val sMatch =  (sign == input.element.sign)
      val mMatch =  (man  == input.element.man)

      if (!eMatch | !sMatch | !mMatch) {
        val message = s"Mismatch ${this.write} - ${input.element.write} --- ${this.float} - ${input.element.float}"
        CompareError(message,line)
      }
      else CompareSuccess
    }


    def h(i:Int) = Integer.toHexString(i)
    val debug =
      s"${h(value)} -> $sign : ${h(man)} : ${h(exp)} -> ${man} : ${exp} --- ${float}"

    override def toString = debug

    // FIXME : fix with implicits and make generic
    def * (x:FloatWrap) = {
      new FloatWrap(this.float*x.float)
    }
    def + (x:FloatWrap) = {
      new FloatWrap(this.float+x.float)
    }

  }
  object FloatWrap {
    val GENERATOR = FloatWrap(1.0.toFloat)
  }
}
