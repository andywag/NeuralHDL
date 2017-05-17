package com.simplifide.generate.blocks.test

import java.io.File

import com.simplifide.generate.signal.{FixedType, SignalTrait}
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop}
import com.simplifide.generate.generator.{BasicSegments, CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.signal.OpType.Register


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
        def vals:String = List.fill(len)("%h ").reduceLeft(_+_)
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
  case class DisplayFlop(val fptr:SignalTrait,signals:List[SignalTrait])(implicit clk:ClockControl) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val flop = new SimpleFlop(None,clk,BasicSegments.List(List()),new FDisplay(fptr,signals))
      writer.createCode(flop)
    }
  }

  class DisplayGroup(val fileName:String,signals:List[SignalTrait])(implicit clk:ClockControl) extends SimpleSegment {

    val fptrName = {
      val file = new File(fileName)
      file.getName.split("\\.")(0)
    }

    val fptr = SignalTrait(s"${fptrName}_fptr",Register,FixedType.unsigned(32,0))

    val state = Initial(List(new FOpen(fileName,fptr)))
    val flop  = DisplayFlop(fptr,signals)

    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val flop = new SimpleFlop(None,clk,BasicSegments.List(List()),new FDisplay(fptr,signals))
      (writer.createCode(state) + writer.createCode(flop)).extra(List(),List(fptr))
    }
  }


}