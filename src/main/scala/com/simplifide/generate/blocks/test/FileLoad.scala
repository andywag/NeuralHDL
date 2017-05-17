package com.simplifide.generate.blocks.test

import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.signal.Constant._
import com.simplifide.generate.blocks.basic.condition.ConditionStatement._
import com.simplifide.generate.generator.{BasicSegments, SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.signal.OpType.Signal
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop}
import com.simplifide.generate.blocks.basic.operator.Operators.Slice
import com.simplifide.generate.blocks.basic.{Statement}
import com.simplifide.generate.signal.{NewConstant, Constant, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/26/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */

class FileLoad(val signal:SignalTrait, val filename:String, val length:Int) extends SimpleSegment{

  val memory = signal.createArray(appendName("mem"),length)
  val rmem   = new FileLoad.ReadMemH(memory, filename)
  val init   = Initial(List(rmem))

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      writer.createCode(init).extra(List(),List(memory))
  }

}

object FileLoad {

  class ReadMemH(val array:SignalTrait,val filename:String) extends SimpleSegment {

    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      SegmentReturn("$readmemh(\"" + filename + "\"," + array.name + ");\n")

  }

  class LoadCommand(val signal:SignalTrait,
                    val array:SignalTrait,
                    val control:SignalTrait)
                   (implicit clk:ClockControl) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn =  {
      val reset  = new Statement.Reg(signal,NewConstant(0,signal.fixed))
      val enable = new Statement.Reg(signal,new Slice(array,control))
      val flop = new SimpleFlop(None,clk,reset,enable)
      writer.createCode(flop)
    }
  }

}