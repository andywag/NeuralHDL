package com.simplifide.generate.test2.data

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.blocks.test.{FileDump, FileLoad}
import com.simplifide.generate.generator.{CodeWriter, ComplexSegment, SegmentReturn, SimpleSegment}
import com.simplifide.generate.model.NdDataSet
import com.simplifide.generate.parser.{ConditionParser, SegmentHolder}
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.{TestEntityParser, TestParser}
import com.simplifide.generate.test2.blocktest.BlockTestParser
import com.simplifide.generate.test2.data.DataGenParser._
import com.simplifide.generate.test2.data.DataSet.CompareTest
import com.simplifide.generate.test2.data.FloatData.FloatWrap

import scala.collection.mutable.ListBuffer

/**
  * Created by andy on 4/30/17.
  */
trait DataGenParser {

  val checkItems    = new ListBuffer[CompareItem[_]]
  val newCheckItems = new ListBuffer[NdCompareItem]()

  implicit def signalToSignalGenerator(signal:SignalTrait) = new SignalGenerator(signal)

  def getLocation(parser:TestEntityParser, signal:SignalTrait) =
    s"${parser.dataLocation}/${signal.name}.hex"

  case class SignalGenerator(signal:SignalTrait) {
    def random[T](length:Int) = signal match {
      case x:FloatSignal => FloatData.randomList(signal,length)
      case _             => FloatData.randomList(signal,length)
    }

    def <--(data:NdDataSet, index:Option[SignalTrait]=None, len:Int=0)(implicit testLength:Int, parser:TestEntityParser) = {
      parser.->(new DataGenerator(this.signal,data,index.getOrElse(parser.index)))
    }

    def <---(data:NdDataSet, index:Option[SignalTrait]=None,length:Int)(parser:TestEntityParser) = {
      parser.->(new DataGenerator(this.signal,data,index.getOrElse(parser.index)))
    }

    def --->(name:String,compare:Option[NdDataSet]=None, title:String="",
             offset:Int = 0)(implicit clk:ClockControl,scope:TestEntityParser) = {
      val dataSet = NdDataSet.empty(name)
      scope->(DisplayDump2(s"${name}.hex",signal))
      compare.map(x => scope.compare(dataSet,x,title,offset))
      dataSet
    }

    def ---->(name:String,cl:ClockControl, compare:Option[NdDataSet]=None, title:String="",
             offset:Int = 0)(implicit scope:TestEntityParser) = {
      implicit val clk = cl
      --->(name,compare,title,offset)
    }






    def <-- (typ:DataGenParser.TestType)(implicit testLength:Int, parser:TestEntityParser) = {
      val dataSet = signal match {
        case x:FloatSignal => FloatData.randomList(signal,testLength)
        case _             => FloatData.randomList(signal,testLength)
      }
      val location:String = getLocation(parser,signal)
      dataSet <-- (location, parser.index)
    }

    def -->(name:String)(implicit clk:ClockControl,scope:TestEntityParser) = {
     val dataSet = signal match {
       case x:FloatSignal => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(name))
       case _             => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(name))
     }
     scope->(DisplayDump(dataSet,signal))
     dataSet
    }


    // FIXME : Not General (Deprecate DataSet)
    def -->(data:DataSet[FloatWrap], title:String, delay:Int = 0)(implicit clk:ClockControl,scope:TestEntityParser) = {

      val dataSet= signal match {
        //case x:FloatSignal => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(getLocation(scope,signal)))
        case _             => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(getLocation(scope,signal)))
      }

      data-->(getLocation(scope,data.signal))
      scope->(DisplayDump(dataSet,signal))
      scope.compare(data.delay(delay),dataSet,title,delay)
      dataSet
    }



  }

  def compare(ndDataSet: NdDataSet, ref:NdDataSet, title:String,offset:Int) = {
    newCheckItems.append(new NdCompareItem(ndDataSet,ref,title,offset))
  }

  def compare[T](ref:DataSet[T], result:DataSet[T], title:String, delay:Int=0) = {
    val item = new CompareItem[T](ref,result, title,delay)
    checkItems.append(item)
    item
  }

  def compareAllResults() = {
    val result2 = newCheckItems.map(_.checkResults)
    val result  = checkItems.map(_.checkResults)
    result2.toList ::: result.toList

  }


}

object DataGenParser {

  class DataGenerator(signal:SignalTrait, data:NdDataSet, index:SignalTrait)
    extends ComplexSegment {
    def createBody = {
      /-(s"Load ${signal.name}")
      val res = ->(new FileLoad(signal, data.resultLocation, data.data.length()))
      signal := index.sign ? 0 :: res.memory(index)

    }
  }
  case class DisplayDump2[T](location:String,signal:SignalTrait)(implicit clk:ClockControl)
      extends ComplexSegment {

    override def createBody = {
      ->(new Comment.SingleLine(s"Store Store ${signal.name}"))
      ->(new FileDump.DisplayGroup(location,List(signal)))
    }
  }


  case class DisplayDump[T](data:DataSet[T],signal:SignalTrait)(implicit clk:ClockControl) extends SimpleSegment with ConditionParser {
    val dump = new FileDump.DisplayGroup(data.location.get,List(data.signal))
    val comment = new Comment.SingleLine(s"Store Store ${signal.name}")
    override def createCode(implicit writer: CodeWriter): SegmentReturn = {
      return writer.createCode(dump).extra(List(comment.create),List())
    }
  }

  case class NdCompareItem(out:NdDataSet, ref:NdDataSet, title:String, offset:Int) {
    def checkResults = {
      val t = ref.compare(out, offset)
      new CompareTest(title,t)
    }
  }

  case class CompareItem[T](in1:DataSet[T],in2:DataSet[T], title:String,delay:Int=0)   {
    def checkResults = {
      val d1 = in1.load
      val d2 = in2.load
      val result = d1.matchSet(d2,delay)
      new CompareTest(title,result)
    }
  }

  trait TestType
  object RANDOM extends TestType

}
