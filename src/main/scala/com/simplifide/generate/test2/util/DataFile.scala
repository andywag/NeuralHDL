package com.simplifide.generate.test2.util

import com.simplifide.generate.test2.util
import com.simplifide.generate.util.FileOps

import scala.io.Source

/**
  * Created by andy on 4/30/17.
  */
trait DataFile[T] {

  val fileLocation:String
  val data:Seq[T]
  val createString:(T)=>String
  val createDebugString:(T)=>String

  val readString:(String)=>T

  def length = data.length

  //def newFile(name:String, data:Seq[T]) = apply(name,data)
  //def apply(name:String, data:Seq[T]) = DataFile.Basic[T](name,data)




  def create =
    FileOps.createFile(new java.io.File(fileLocation),data.map(x => createString(x)).mkString("\n"))

  def createDebug =
    FileOps.createFile(new java.io.File(fileLocation+"t"),data.map(x => createDebugString(x)).mkString("\n"))


  /*
  def transform[S](x:DataFile[S])(f:(T,S)=>T)(name:String):DataFile[T] = {
    val newData = (this.data zip x.data).map(x => f(x,x))
    newFile(name,newData)
  }
  */
}

object DataFile {

  trait DataType[T] {
    val createString:(T)=>String
    val createDebugString:(T)=>String
    val readString:(String)=>T
  }

}

