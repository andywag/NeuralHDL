package com.simplifide.generate.model

import java.io.File

import com.simplifide.generate.util.FileOps
import org.nd4j.linalg.api.buffer.DataBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

/**
  * Created by andy on 5/13/17.
  */
object DataFileGenerator {

  import org.nd4s.Implicits._

  def getDataWrap(data:INDArray, f:(Int)=>Array[Int], length:Option[Int]) = {
    def convert(value:Double, typ:DataBuffer.Type) = {
      typ match {
        case DataBuffer.Type.FLOAT => DataWrapper.FloatWrap(value.toFloat)
        case _                     => ???
      }
    }
    val typ = data.data().dataType()
    val length1 = length.getOrElse(data.length())
    val result =Seq.tabulate(length1)(x => {
      val index = f(x)
      (index,convert(data.get(index),typ))
    })
    result

  }


  def createHexFile(location:String, data:INDArray, f:(Int)=>Array[Int], length:Option[Int]=None) = {


    val len = data.length()
    val typ = data.data().dataType()
    val dataSet = getDataWrap(data,f,length)

    val contents      = dataSet.map(x => x._2.write).mkString("\n")
    val debugContents = dataSet.map(x => s"${x._1.mkString(" ")} -- ${x._2.debug}").mkString("\n")

    FileOps.createFile(new File(s"${location}.hex"),contents)
    FileOps.createFile(new File(s"${location}.hext"),debugContents)
    new NdDataSet(location,data)
  }


  def orderDim2(data:INDArray):Int => Array[Int] = x => {
    val shape = data.shape()
    val dim1 = x % shape(1)
    val dim2 = (x/shape(1))
    Array(dim2,dim1)
  }
  /** FIXME : This whole set of slices need to be generalized */
  def orderDim3(data:INDArray):Int => Array[Int] = x => {
    val shape = data.shape()
    val dim1 = x % shape(2)
    val dim2 = (x/shape(2)) % shape(1)
    val dim3 = x/(shape(2)*shape(1))
    Array(dim3,dim2,dim1)
  }

  def sliceDim3(data:INDArray, offset:Int):Int => Array[Int] = x => {
    val shape = data.shape()
    val dim1 = offset
    val dim2 = x % shape(1)
    val dim3 = x/(shape(1))
    Array(dim3,dim2,dim1)
  }

  def createFlatten(location:String, data:INDArray) = {
    val order = data.shape().length match {
      case 2 => orderDim2(data)
      case 3 => orderDim3(data)
      case _ => ???
    }
    createHexFile(location,data,order)
  }

  // FIXME : This should return a slice of data but is only returnign teh original data
  def createSlices(location:String, data:INDArray) = {
    val shape = data.shape()

    val result = Seq.tabulate(shape(1))(x => {
      val slice = sliceDim3(data,x)
      createHexFile(s"${location}${x}",data,slice,Some(shape(2)*shape(0)))

    })
    result
  }

  trait DataGenType
  object RANDOM extends DataGenType
  object ZEROS extends DataGenType
  object ONES extends DataGenType
  case class Ramp(min:Double, max:Double) extends DataGenType
  case class Random(min:Double, max:Double) extends DataGenType

  def createData(size:Array[Int], file:String, typ:DataGenType, offset:Int=0) = {
    val len = size.foldLeft(1)(_*_)
    val data1   = typ match {
      case RANDOM        => Nd4j.randn(size)
      case Random(mi,ma) => Nd4j.rand(size,mi,ma,Nd4j.getRandom)
      case ONES          => Array.tabulate(len)(x => 1.0.toFloat).mkNDArray(size)
      case ZEROS         => Array.tabulate(len)(x => 0.0.toFloat).mkNDArray(size)
      case Ramp(mi,ma)   => {
        val slope = (ma - mi).toFloat/len.toFloat
        Array.tabulate(len)(x => (mi + x.toFloat*slope)).mkNDArray(size)
      }
      case _      => ???
    }
    val data = if (offset > 0) NdUtils.delay(data1,offset) else data1
    DataFileGenerator.createFlatten(s"$file",data)
  }


}
