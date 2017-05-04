package com.simplifide.generate.test2.data

import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser.{ConditionParser, EntityParser, SegmentHolder}
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.test2.data.DataSet._
import com.simplifide.generate.test2.{TestEntityParser, TestParser}
import com.simplifide.generate.util.FileOps

import scala.io.Source

/**
  * Created by andy on 4/30/17.
  */
case class DataSet[T](data:Seq[DataTrait[T]], signal:SignalTrait, gen:DataTrait[T], location:Option[String] = None) {

  def load = {
    location match {
      case Some(x) => {
        val data = Source.fromFile(x).getLines().map(x => gen.read(x))
        this.copy(data = data.toSeq)
      }
      case None    => this
    }
  }

  def matchSet(x:DataSet[T]) = {
    def check(res:CompareResult) = {
      res match {
        case x:CompareError => Some(res)
        case CompareSuccess => None
      }
    }
    val results = (this.data zip x.data).zipWithIndex.map(x => x._1._1.compare(x._1._2,x._2))
    results.map(check(_)).flatten
  }


  def create =
    FileOps.createFile(new java.io.File(location.get),data.map(x => x.write).mkString("\n"))

  def createDebug =
    FileOps.createFile(new java.io.File(location.get+"t"),data.map(x => x.debug).mkString("\n"))




  def <--(loc:String, index:SignalTrait)(implicit scope:TestEntityParser):DataSet[T] = {
    val result = this.copy(location = Some(loc))
    result.create
    scope.->(new DataGenerator[T](result,index))
    result
  }

  def -->(loc:String):DataSet[T] = {
    val result = this.copy(location = Some(loc))
    result.create
    result.createDebug
    result
  }

  def *(data:DataSet[T]) = {
    val newData = (this.data zip data.data).map(x => x._1 * x._2)
    new DataSet[T](newData,signal,this.gen,None)
  }

  def +(data:DataSet[T]) = {
    val newData = (this.data zip data.data).map(x => x._1 + x._2)
    new DataSet[T](newData,signal,this.gen,None)
  }

  def transform[S,V](x:DataSet[S])(f:(DataTrait[T],DataTrait[S])=>DataTrait[V]) {
    //val newData = (data zip x.data).map(f(_))
    //new DataSet[V](newData,signal,None)
  }


}

object DataSet {
  def apply[T](data:Seq[DataTrait[T]],signal:SignalTrait, gen:DataTrait[T]) =
    new DataSet(data,signal,gen)

  class DataGenerator[T](data:DataSet[T], index:SignalTrait)
    extends SimpleSegment with ConditionParser with SegmentHolder with TestParser {
    /- (s"Load ${data.signal.name}")
    val res = ->($readMem(data))
    data.signal := res.memory(index)

    override def createCode(implicit writer: CodeWriter): SegmentReturn = {
      val states = statements.toList.map(_.create)
      val ret = states.map(x => writer.createCode(x)).reduceLeft(_+_)
      ret
    }
  }

  trait CompareResult
  case object CompareSuccess extends CompareResult
  case class CompareError(error:String, line:Int=0) extends CompareResult {
    override def toString = s"Line $line : $error"
  }

  case class CompareTest(title:String, errors:Seq[CompareResult]) {
    def debugString = s"$title\n   ${errors.mkString("\n   ")}"
  }



}
