package com.simplifide.generate.test2.data

import com.simplifide.generate.test2.data.DataSet.CompareResult

/**
  * Created by andy on 4/30/17.
  */
trait DataTrait[T] {

  val element:T
  val write:String
  val debug:String
  val read:(String)=>DataTrait[T]

  def *(input:DataTrait[T]):DataTrait[T]
  def +(input:DataTrait[T]):DataTrait[T]

  def compare(input:DataTrait[T], line:Int):CompareResult

  //val random:T
}

object DataTrait {

}
