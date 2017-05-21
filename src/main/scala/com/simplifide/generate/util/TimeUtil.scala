package com.simplifide.generate.util

/**
  * Created by andy on 5/21/17.
  */
object TimeUtil {
  def time[R](title:String)(block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println(s"${title} Elapsed time: " + (t1 - t0)/1e9 + "sec")
    result
  }
}
