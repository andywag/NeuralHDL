package com.simplifide.generate.model

import com.simplifide.generate.test2.data.DataSet.CompareResult
import org.nd4j.linalg.api.buffer.DataBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import scala.io.Source

/**
  * Created by andy on 5/13/17.
  */

import org.nd4s.Implicits._
import collection.JavaConverters._

case class NdDataSet(location:String, data:INDArray) {
  val resultLocation = s"${location}.hex"

  def transpose = NdDataSet(location,data.transpose())

  def createResultLocation(x:String) = s"${location}.hex"

  lazy val proto = data.data().dataType() match {
    case DataBuffer.Type.FLOAT => DataWrapper.FloatWrap(0.0.toFloat)
    case _                     => ???
  }

  def compare(out:NdDataSet, offset:Int):Seq[CompareResult] = {

    val rtl = out.loadWrap
    val ref = this.loadWrap
    val result =(rtl zip ref).zipWithIndex.map(x => x._1._1.compare(x._1._2,x._2)).toList.flatten
    result
  }

  def loadWrap = {
    Source.fromFile(resultLocation).getLines().map(x => proto.read(x))
  }

  def load(size:Option[Array[Int]] = None) = {
    val result = loadWrap
    //  val usize = size.getOrElse(Array(result.length))
    val t = result.map(_.value).toArray
    val result1 = t.toNDArray
    result1
  }

}

object NdDataSet {
  val EMPTY = {
    Array(0.0).mkNDArray(Array(0))
  }
  def empty(name:String) = NdDataSet(name,EMPTY)
}
