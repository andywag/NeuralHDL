package com.simplifide.generate.blocks.basic.float

import com.simplifide.generate.signal.FloatSignal

/**
  * Created by andy on 4/29/17.
  */
object Stimulus {

  class Fl(value:Int, sign:Int, man:Int, exp:Int) {
    def h(i:Int) = Integer.toHexString(i)
    override def toString =
      s"${h(value)} -> $sign : ${h(man)} : ${h(exp)} -> ${man} : ${exp}"
  }

  def double2Hex(double:Double) = {

  }

  def float2Hex(valueIn:Float) = {
    val value = java.lang.Float.floatToIntBits(valueIn)
    //Integer.toHexString(value)
    value
  }

  def float2Fl(valueIn:Float) = {
    val value = float2Hex(valueIn)
    val man   = 0x7FFFFF & value
    val exp   = (value >> 23) & 0xFF
    val sign  = (value >> 31) & 0x1
    new Fl(value, sign,man,exp)
  }


  def randomFloat = {
    val r     = scala.util.Random
    val value = java.lang.Float.floatToIntBits(r.nextFloat())
    Integer.toHexString(value)
  }

  def randomFloats(len:Int) = {
    List.tabulate(len)(x => scala.util.Random.nextFloat())
  }

  val random:(Int)=>String = x => randomFloat


  def main(args: Array[String]): Unit = {
    val r = scala.util.Random
    val floats = List.tabulate(100)(x => r.nextFloat())
    val ints   = floats.map(x => float2Fl(x))
    val result = (floats zip ints).map(x => s"${x._1} -> ${x._2}").mkString("\n")
    System.out.println(result)
  }

}
