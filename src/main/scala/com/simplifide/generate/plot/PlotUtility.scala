package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */

import com.quantifind.charts.Highcharts._

object PlotUtility {

  case class ErrorStat(max:Double, mean:Double, va:Double) {
    override def toString = s"Max : $max, Mean : $mean, Var : $va"
  }

  object ErrorStat {
    def apply(input:Seq[Double]) = {
      val abs = input.map(x => math.abs(x))
      val max  = abs.foldLeft(0.0)(math.max(_,_))
      val mean = abs.foldLeft(0.0)(_+_) / abs.length.toDouble
      val va   = abs.map(x => (x - mean)*(x-mean)).foldLeft(0.0)(_+_)/abs.length.toDouble
      new ErrorStat(max,mean,va)
    }
  }

  def plotLine(input:Seq[Double]) = {
    val time = (0 until input.length)
    line(time,input)
  }

  def plotError(input:Seq[Double], ref:Seq[Double]) = {
    val diff = (input zip ref).map(x => x._2 - x._1)
    plotLine(diff)
    unhold()
    title("Error")
    plotLine(input)
    hold
    plotLine(ref)
    unhold
    ErrorStat(diff)

  }
}
