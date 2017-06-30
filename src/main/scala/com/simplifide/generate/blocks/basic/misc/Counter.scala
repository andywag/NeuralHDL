package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlop}
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.generator.{CodeWriter, ComplexSegment, SegmentReturn, SimpleSegment}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{Constant, NewConstant, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/23/11
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */

class Counter(val counter:SignalTrait)(implicit clk:ClockControl) extends SimpleSegment {

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val flop = new SimpleFlop(None,
        clk,
        new Statement.Reg(counter,NewConstant(0,counter.fixed)),
        new Statement.Reg(counter,BinaryOperator.Plus(counter,NewConstant(1,counter.fixed)))
      )
      writer.createCode(flop)
  }

}

object Counter {

  case class Simple(val signal:SignalTrait, val reset:Option[Expression], enable:Option[Expression],
                    incr:Expression = Constant(1.0))(implicit clk:ClockControl) extends ComplexSegment {
    /** Defines the body in the block */
    override def createBody: Unit = {}

    val op = reset match {
      case Some(x) => $iff (x) $then 0 $else (signal + incr)
      case _       => signal + incr
    }
    val cl = enable.map(clk.createEnable(_)).getOrElse(clk)
    signal := op.$at(cl)

    def end = reset.getOrElse(Constant(1.0))

  }

  def Increment(signal:SignalTrait,inc:Expression,
                reset:Option[Expression]=None,enable:Option[Expression]=None)(implicit clk:ClockControl) = {
    new Simple(signal,reset,enable,inc)
  }

  def Length(signal:SignalTrait, length:Expression, enable:Option[Expression]=None)(implicit clk:ClockControl) = {
    new Simple(signal,Some(signal === length),enable)
  }

}