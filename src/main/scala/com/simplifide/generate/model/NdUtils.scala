package com.simplifide.generate.model

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

/**
  * Created by andy on 5/14/17.
  */
object NdUtils {
  import org.nd4s.Implicits._
  import collection.JavaConverters._

  def vectorOp3(op:(Int)=>INDArray, length:Int, shape:Array[Int]) = {
    val result = List.tabulate(length)(x =>
      op(x)
    )
    val test  = Nd4j.toFlattened(result.asJavaCollection)
    val test2 = Nd4j.create(test.data(),shape)
    test2
  }



  def delay(data:INDArray, offset:Int) = {
    val shape  = data.shape();
    val length = shape.slice(1,shape.size).fold(1)(_*_)
    Nd4j.prepend(data,offset,0.0,0)
  }

  def zeroAppend(data:INDArray, offset:Int) = {
    val shape  = data.shape();
    val length = shape.slice(1,shape.size).fold(1)(_*_)
    Nd4j.append(data,offset,0.0,0)
  }


}
