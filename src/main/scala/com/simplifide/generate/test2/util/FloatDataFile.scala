package com.simplifide.generate.test2.util

import com.simplifide.generate.blocks.basic.float.Stimulus.{Fl, float2Hex}
import com.simplifide.generate.test2.util.DataFile.DataType
import com.simplifide.generate.test2.util.FloatDataFile.FloatWrapper

import scala.io.Source


/**
  * Created by andy on 4/30/17.
  */
case class FloatDataFile(fileLocation:String,data:Seq[FloatWrapper]) extends DataFile[FloatWrapper] {



  val debugString: (Float) => String = x =>
    FloatDataFile.Fl(x).toString

  override val createString: (FloatWrapper) => String = x =>
    Integer.toHexString(x.value)

  override val createDebugString: (FloatWrapper) => String = x =>
    x.toString

  override val readString: (String) => FloatWrapper = x => {
    FloatWrapper(java.lang.Float.intBitsToFloat(Integer.valueOf(x,16)))
  }

  def read = {
    val data = Source.fromFile(fileLocation).getLines().map(x => readString(x))
    new FloatDataFile(fileLocation,data.toSeq)
  }

  def * (x:FloatDataFile)(name:String) = {
    val newData = (this.data zip x.data).map(x => x._1 * x._2)
    FloatDataFile(name,newData)
    //transform(x)(_*_)(name)
  }
  def + (x:FloatDataFile)(name:String) = {
    val newData = (this.data zip x.data).map(x => x._1 + x._2)
    FloatDataFile(name,newData)
    //transform(x)(_*_)(name)
  }


}

object FloatDataFile {

  case class FloatWrapper(val float:Float) {

    val value  = float2Hex(float)
    val man   = 0x7FFFFF & value
    val exp   = (value >> 23) & 0xFF
    val sign  = (value >> 31) & 0x1


    def h(i:Int) = Integer.toHexString(i)
    override def toString =
      s"${h(value)} -> $sign : ${h(man)} : ${h(exp)} -> ${man} : ${exp} --- ${float}"

    // FIXME : fix with implicits
    def * (x:FloatWrapper) = {
      new FloatWrapper(this.float*x.float)
    }
    def + (x:FloatWrapper) = {
      new FloatWrapper(this.float+x.float)
    }

  }

  object FloatWrapper {
    val readString: (String) => FloatWrapper = x =>
      FloatWrapper(java.lang.Float.intBitsToFloat(Integer.parseInt(x,16)))
  }



  class Fl(value:Int, sign:Int, man:Int, exp:Int) {
    def h(i:Int) = Integer.toHexString(i)
    override def toString =
      s"${h(value)} -> $sign : ${h(man)} : ${h(exp)} -> ${man} : ${exp}"
  }
  object Fl {
    def apply(valueIn:Float) = {
      val value = float2Hex(valueIn)
      val man   = 0x7FFFFF & value
      val exp   = (value >> 23) & 0xFF
      val sign  = (value >> 31) & 0x1
      new Fl(value, sign,man,exp)
    }
  }


  //def apply(fileLocation:String, data:Seq[FloatWrapper]) =
  //  new FloatDataFile(fileLocation,data)

  def random(fileLocation:String, length:Int) = {
    def getValue = {
      val data = scala.util.Random.nextFloat()
      if (scala.util.Random.nextBoolean()) data else -data
      //-data
    }
    val data = Seq.tabulate(length)(x => FloatWrapper(getValue))
    new FloatDataFile(fileLocation,data)
  }

  def read(fileLocation:String) = {
    Source.fromFile(fileLocation).getLines().map(x => FloatWrapper.readString(x))

  }



}
