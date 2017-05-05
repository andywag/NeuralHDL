package com.simplifide.generate.test2.data

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.blocks.test.FileDump
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser.ConditionParser
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.TestEntityParser
import com.simplifide.generate.test2.blocktest.BlockTestParser
import com.simplifide.generate.test2.data.DataGenParser.{CompareItem, DisplayDump}
import com.simplifide.generate.test2.data.DataSet.CompareTest
import com.simplifide.generate.test2.data.FloatData.FloatWrap

import scala.collection.mutable.ListBuffer

/**
  * Created by andy on 4/30/17.
  */
trait DataGenParser {

  val checkItems = new ListBuffer[CompareItem[_]]

  implicit def signalToSignalGenerator(signal:SignalTrait) = new SignalGenerator(signal)

  def getLocation(parser:TestEntityParser, signal:SignalTrait) =
    s"${parser.dataLocation}/${signal.name}.hex"

  case class SignalGenerator(signal:SignalTrait) {
    def random[T](length:Int) = signal match {
      case x:FloatSignal => FloatData.randomList(signal,length)
      case _             => FloatData.randomList(signal,length)
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

    // FIXME : Not General
    def -->(data:DataSet[FloatWrap], title:String)(implicit clk:ClockControl,scope:TestEntityParser) = {

      val dataSet= signal match {
        //case x:FloatSignal => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(getLocation(scope,signal)))
        case _             => DataSet[FloatWrap](Seq(),signal,FloatWrap.GENERATOR,Some(getLocation(scope,signal)))
      }
      data-->(getLocation(scope,data.signal))
      scope->(DisplayDump(dataSet,signal))
      scope.compare(data,dataSet,title)
      dataSet
    }



  }

  def compare[T](ref:DataSet[T], result:DataSet[T], title:String) = {
    val item = new CompareItem[T](ref,result, title)
    checkItems.append(item)
    item
  }

  def compareAllResults() = {
    val result = checkItems.map(_.checkResults)
    result
  }


}

object DataGenParser {

  case class DisplayDump[T](data:DataSet[T],signal:SignalTrait)(implicit clk:ClockControl) extends SimpleSegment with ConditionParser {
    val dump = new FileDump.DisplayGroup(data.location.get,List(data.signal))
    val comment = new Comment.SingleLine(s"Store Store ${signal.name}")
    override def createCode(implicit writer: CodeWriter): SegmentReturn = {
      return writer.createCode(dump).extra(List(comment.create),List())
    }
  }

  case class CompareItem[T](in1:DataSet[T],in2:DataSet[T], title:String)   {
    def checkResults = {
      val d1 = in1.load
      val d2 = in2.load
      val result = d1.matchSet(d2)
      new CompareTest(title,result)
    }
  }

  trait TestType
  object RANDOM extends TestType

}
