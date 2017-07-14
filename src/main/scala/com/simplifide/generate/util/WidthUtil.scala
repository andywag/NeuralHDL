package com.simplifide.generate.util

/**
  * Created by andy on 7/14/17.
  */
object WidthUtil {
  def logCeil(input:Int):Int =
    math.pow(2.0,math.ceil(math.log(input)/math.log(2.0))).toInt

  def logCeil(i:(Int,Int)):(Int,Int) = (logCeil(i._1),logCeil(i._2))

}
