package com.simplifide.generate.test2

import java.sql.Time
import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop}
import com.simplifide.generate.signal.{Constant, SignalTrait}
import com.simplifide.generate.generator.{BasicSegments, SegmentReturn, CodeWriter, SimpleSegment}
import collection.mutable.ListBuffer
import com.simplifide.generate.signal.OpType.Signal
import com.simplifide.generate.parser.model.Expression
import com.sun.org.apache.xml.internal.utils.LocaleUtility
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.blocks.test.CompareTable
import com.simplifide.generate.blocks.basic.Statement

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/23/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */

/*
trait TestControlParser {

  val controlValues = new ListBuffer[TimeSignalValue]()


  implicit def Signal2SignalWrap(signal:SignalTrait):SignalWrap = new SignalWrap(signal)
  implicit def Int2Time(int:Int):TimeWrap = new TimeWrap(int)



}

object TestControlParser {

  class TimeWrap(time:Int) {
    def -> (value:SimpleSegment) = new TimeValue(time,value)
    def -> (value:Int)           = new TimeValue(time,Constant(value.toDouble))
  }

  class SignalWrap(signal:SignalTrait) {



    def --> (value:Long)(implicit testModule:TestModule) =
      testModule.init(signal,value)

    def --> (value:Double)(implicit testModule:TestModule) =
      testModule.init(signal,Constant(value,signal.fixed))

    def --> (value:SimpleSegment)(implicit testModule:TestModule) =
      testModule.init(signal,value)

    def --> (values:TimeValue*)(implicit testModule:TestControlParser) = {
      val timeValues = values.toList.map(x => new TimeSignalValue(x.time,signal,x.value))
      testModule.controlValues.appendAll(timeValues)
    }

    /** Dump the data to a file */
    def --> (filename:String,length:Int)(implicit testModule:TestModule) = {
       testModule.loadSignal(signal,filename,length)
    }


    /** Create a LUT based on these values */
    def --> ( segments:List[SimpleSegment])(implicit testModule:TestModule) = {
      val lut = Lut(signal,testModule.counter,segments,segments.length)
      testModule.assign(lut)
    }

    /** Create a LUT based on these values */
    def --> ( creator:(Int)=>SimpleSegment, length:Int)(implicit testModule:TestModule) = {
      val lut = Lut(signal,testModule.counter,creator,length)
      testModule.assign(lut)
    }

    /** Assign a value after a delay */
    def -#> (value:Long, delay:Int)(implicit testModule:TestModule) =
      testModule.init(signal,value,delay)

    /** Compares the results of the output signals */
    def <-- (values:List[SimpleSegment], start:Int, end:Int, threshold:Int = 0)(implicit testModule:TestModule) =  {
      val tab = CompareTable(this.signal,testModule.counter,values,start,end,threshold)
      testModule.assign(tab)
    }

    /** Write the output values to a file*/
    def <-- (filename:String)(implicit testModule:TestModule,clk:ClockControl) =  {
      testModule.writeOutput(filename,List(this.signal))
    }

  }

  class Mux(val counter:SignalTrait,val values:List[TimeSignalValue])(implicit clk:ClockControl) extends SimpleSegment {

    /*
  override def split:List[Expression] = {
     if (values.length == 0) return List()
     val signals = values.map(_.signal).toSet.toList
     val groups = values.groupBy(_.time).toList.sortBy(_._1) // Create a list of time - values
     val condition = groups.map(x => ( Some(BinaryOperator.LTE(counter,Constant(x._1,counter.fixed.width))),
       x._2.map(y => new Statement.Reg(y.signal,y.value))))

     val resets = signals.map(x => new Statement.Reg(x,Constant(0,x.fixed.width)))
     val conditional = ConditionStatement(condition)
     val flop = new SimpleFlop(None,clk,BasicSegments.List(resets),conditional)
     return flop.split
  }
  */

    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      if (values.length == 0) return SegmentReturn("")
     val signals = values.map(_.signal).toSet.toList
     val groups = values.groupBy(_.time).toList.sortBy(_._1) // Create a list of time - values
     val condition = groups.map(x => ( Some(BinaryOperator.LTE(counter,Constant(x._1,counter.fixed.width))),
       x._2.map(y => new Statement.Reg(y.signal,y.value))))

     val resets = signals.map(x => new Statement.Reg(x,Constant(0,x.fixed.width)))
     val conditional = ConditionStatement(condition)
     val flop = new SimpleFlop(None,clk,BasicSegments.List(resets),conditional)

     writer.createCode(flop)
    }
  }



  class TimeValue(val time:Int,val value:SimpleSegment)
  class TimeSignalValue(val time:Int,val signal:SimpleSegment,val value:SimpleSegment)
  class FileLoader(val filename:String,val length:Int)

}
*/

