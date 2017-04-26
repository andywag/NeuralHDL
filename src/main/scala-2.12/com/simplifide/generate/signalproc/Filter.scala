package com.simplifide.generate.signalproc

class Filter {

}

object Filter {

  def fir(taps:List[Double],inputs:List[Double]):List[Double] = {
    def zp(results:List[Double],pre:Int,pos:Int) = List.fill(pre)(0.0) ::: results ::: List.fill(pos)(0.0)
    val results:List[List[Double]] = taps.zipWithIndex.map(x => zp(inputs.map(y => y*x._1),x._2,taps.length-x._2))
    results.transpose.map(x => x.reduceLeft(_+_))
  }

}