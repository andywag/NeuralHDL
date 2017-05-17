package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */


import com.quantifind.charts.Highcharts._

object SimplePlot {

  def main(args: Array[String]): Unit = {
    //setPort(10000)
    line((1 to 10), (1 to 10))
    hold
    line((2 to 20), (2 to 20))

  }

}
