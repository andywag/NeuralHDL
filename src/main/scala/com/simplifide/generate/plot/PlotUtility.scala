package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */

import javax.swing.JFrame

import com.quantifind.charts.Highcharts._
import org.jfree.chart.{ChartFactory, ChartPanel, ChartUtilities}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.{XYDataset, XYSeries, XYSeriesCollection}


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

  def jPlot(input:Seq[Double],ref:Seq[Double]) = {
    val data = new XYSeries("RTL")
    val rdata = new XYSeries("Reference")
    input.zipWithIndex.foreach(x => data.add(x._2,x._1))
    ref.zipWithIndex.foreach(x => rdata.add(x._2,x._1))

    val dataset = new XYSeriesCollection()
    dataset.addSeries(data)
    dataset.addSeries(rdata)

    val chart = ChartFactory.createXYAreaChart("Difference","input","value",dataset,PlotOrientation.VERTICAL,false,true,false)
    val panel = new ChartPanel(chart)
    panel.setPreferredSize(new java.awt.Dimension(800,800))
    panel.setVisible(true)
    val frame = new JFrame()
    frame.setContentPane(panel)
    frame.setVisible(true)
  }

  def plotError(input:Seq[Double], ref:Seq[Double]) = {
    val diff = (input zip ref).map(x => x._2 - x._1)
    jPlot(input,ref)
    /*plotLine(diff)
    unhold()
    title("Error")
    plotLine(input)
    hold
    plotLine(ref)
    unhold
    */
    ErrorStat(diff)

  }
}
