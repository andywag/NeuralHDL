package com.simplifide.generate.plot

/**
  * Created by andy on 5/16/17.
  */

import java.io.File
import javax.swing.JFrame

import org.jfree.chart.{ChartFactory, ChartPanel, ChartUtilities}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.time.TimeSeriesCollection
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



  def jPlot(input:Seq[Double],ref:Seq[Double],error:Seq[Double],file:String) = {

    val dataSet = new DefaultCategoryDataset()
    input.zipWithIndex.foreach(x => dataSet.addValue(x._1,"RTL",x._2))
    ref.zipWithIndex.foreach(x => dataSet.addValue(x._1,"Reference",x._2))

    val errorSet = new DefaultCategoryDataset()
    error.zipWithIndex.foreach(x => errorSet.addValue(x._1,"Error",x._2))


    val chart = ChartFactory.createLineChart("Difference","input","value",dataSet,PlotOrientation.VERTICAL,true,true,false)
    ChartUtilities.saveChartAsJPEG(new File(s"$file.jpg"),chart,500,300)



    val chart1 = ChartFactory.createLineChart("Error","input","value",errorSet,PlotOrientation.VERTICAL,false,true,false)
    ChartUtilities.saveChartAsJPEG(new File(s"${file}e.jpg"),chart1,500,300)


  }

  def plotError(input:Seq[Double], ref:Seq[Double], file:Option[String]=None, delay:Int=0, ignore:Int=0) = {
    val len = math.min(input.length,ref.length)
    val in  = input.slice(delay+ignore,len)
    val out = ref.slice(ignore,len)
    val diff = (in zip out).map(x => x._2 - x._1)
    file.map(x => jPlot(in,out,diff,x))

    ErrorStat(diff)

  }
}
