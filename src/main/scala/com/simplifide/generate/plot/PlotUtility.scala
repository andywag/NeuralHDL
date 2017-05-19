package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */

import java.io.File
import javax.swing.JFrame

import com.quantifind.charts.Highcharts._
import org.jfree.chart.{ChartFactory, ChartPanel, ChartUtilities}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.DefaultCategoryDataset
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

  def jPlot(input:Seq[Double],ref:Seq[Double],error:Seq[Double],file:Option[String]) = {


    val dataSet = new DefaultCategoryDataset()
    input.zipWithIndex.foreach(x => dataSet.addValue(x._1,"RTL",x._2))
    ref.zipWithIndex.foreach(x => dataSet.addValue(x._1,"Reference",x._2))

    val errorSet = new DefaultCategoryDataset()
    error.zipWithIndex.foreach(x => errorSet.addValue(x._1,"Error",x._2))



    val chart = ChartFactory.createLineChart("Difference","input","value",dataSet,PlotOrientation.VERTICAL,false,true,false)
    file.map(x => {
      ChartUtilities.saveChartAsJPEG(new File(s"$x.jpg"),chart,600,600)
    })

    val chart1 = ChartFactory.createLineChart("Error","input","value",errorSet,PlotOrientation.VERTICAL,false,true,false)
    file.map(x => {
      ChartUtilities.saveChartAsJPEG(new File(s"${x}e.jpg"),chart1,600,600)
    })

  }

  def plotError(input:Seq[Double], ref:Seq[Double], file:Option[String]=None) = {
    val diff = (input zip ref).map(x => x._2 - x._1)
    jPlot(input,ref,diff,file)
    ErrorStat(diff)

  }
}
