package com.simplifide.generate.model

import org.nd4j.linalg.api.ndarray.INDArray

/**
  * Created by andy on 5/15/17.
  */

import org.nd4s.Implicits._
import collection.JavaConverters._

object NdArrayWrap {

  implicit class Wrap(input:INDArray)  {
    def d(index:Int) = NdUtils.delay(input,index)
    def a(index:Int) = NdUtils.zeroAppend(input,index)
  }

}
