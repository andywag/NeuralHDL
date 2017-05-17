package com.simplifide.generate.neural

import com.simplifide.generate.model.DataWrapper.FloatWrap
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/15/17.
  */

import org.nd4s.Implicits._
import collection.JavaConverters._
import com.simplifide.generate.model.NdArrayWrap._
object Temp {

  def main(args: Array[String]): Unit = {

    val d2 = Array(-8.0, -4.0, -2.0, -1.0, 1.0, 2.0, 4.0, 8.0).toNDArray
    val res = Transforms.sigmoid(d2)

    val base = FloatWrap(0.0.toFloat)
    val data = List("bfc2d863","bfb76904","bfaf4fc1","3eb56a31").map(x => base.read(x))
    val taps = List("bff6d396","bf2a450d","3f00b438","3e86dbba").map(x => base.read(x))
    val bias = base.read("bfb80116")
    val res1  = (data zip taps).map(x => x._1 * x._2)
    val resa   = res1(0) + bias
    val resb   = resa + res1(1)
    val resc   = resb + res1(2)
    val resd   = resc + res1(3)

    val data1 = Array.tabulate(16)(x => x-8).mkNDArray(Array(1,16))
    val result  =  Transforms.sigmoid(data1)

    val temp = List(0.0, 0.0625, 0.125, 0.25, 0.75, 0.87, 0.937, 1.0).map(x => FloatWrap(x.toFloat))

    System.out.println("Here")

  }
}
