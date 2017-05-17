package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */

import com.quantifind.charts.Highcharts._

object PlotUtility {

  def plotLine(input:Seq[Double]) = {
    val time = (0 until input.length)
    line(time,input)
  }

  def plotError(input:Seq[Double], ref:Seq[Double]): Unit = {
    val diff = (input zip ref).map(x => x._2 - x._1)
    plotLine(diff)
    unhold()
    title("Error")
    plotLine(input)
    hold
    plotLine(ref)
    unhold


  }
}
