package com.simplifide.generate.blocks.test

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.{SimpleFlop, ClockControl}
import com.simplifide.generate.generator.{BasicSegments, SegmentReturn, CodeWriter, SimpleSegment}


/** Class which contains simulations methods and classes for dumping data to a file */
class FileDump  {

}

object FileDump {

  /** Segment which opens up a file
   *    fptr = $fopen(filename);
   **/
  class FOpen(val filename:String, val fptr:SignalTrait) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      writer.createCode(fptr) + " = $fopen(\"" + filename + "\",\"w\");\n"
  }

  /** Segment which creates an fdisplay statement
   *  $fdisplay(fptr,"%d ...",signals...)
   **/
  class FDisplay(val fptr:SignalTrait,signals:List[SignalTrait]) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      def createQuotes(len:Int):String = {
        def vals:String = List.fill(len)("%d ").reduceLeft(_+_)
        "\"" + vals + "\""
      }
      def createSignals:String = {
        def createSignal(signal:SignalTrait, index:Int) = if (index == 0) signal.createCode.code else "," + signal.createCode.code
        signals.zipWithIndex.map(x => createSignal(x._1,x._2)).reduceLeft(_ + _)
      }
      SegmentReturn("$fdisplay(") + fptr.name + "," + createQuotes(signals.length) + "," + createSignals + ");\n"
    }
  }

  /** Flop which contains the fdisplay statement */
  class DisplayFlop(val fptr:SignalTrait,signals:List[SignalTrait])(implicit clk:ClockControl) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val flop = new SimpleFlop(None,clk,BasicSegments.List(List()),new FDisplay(fptr,signals))
      writer.createCode(flop)
    }
  }


}