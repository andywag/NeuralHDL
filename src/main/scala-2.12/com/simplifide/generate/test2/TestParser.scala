package com.simplifide.generate.test2

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.test.Initial
import com.simplifide.generate.blocks.test.Initial.InitialSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.signal.{NewConstant, Constant, SignalTrait}
import com.simplifide.generate.parser.items.InitialParser

/**
 *  Enables the creation of a test block
 */

trait TestParser {

  val initials = new ListBuffer[SimpleSegment]()

  def init(signal: SignalTrait, value: Long, delay: Int) =
    initials.append(new Initial.Delay(signal, value, delay))

  def init(signal: SignalTrait, value: Long) =
    initials.append(new Initial.Assignment(signal, value))

  def init(signal: SignalTrait, value: Double) =
    initials.append(new Initial.AssignSegment(signal, NewConstant(value, signal.fixed)))

  def init(signal: SignalTrait, value: SimpleSegment) =
    initials.append(new Initial.AssignSegment(signal, value))

  def init(segment: SimpleSegment) = {
    initials.append(segment)
  }

  /**Create a parser which creates full initial statements */
  def init(clk: ClockControl) = {
    val currentInitials = new ListBuffer[SimpleSegment]
    currentInitials.append(new Initial.Assignment(clk.clockSignal(), 0))
    clk.resetSignal() match {
      case Some(x) => {
        currentInitials.append(new Initial.Assignment(x, if (clk.reset.get.activeLow) 0 else 1))
        currentInitials.append(new Initial.Delay(x, if (clk.reset.get.activeLow) 1 else 0, clk.period * 10))

      }
      case _ =>
    }
    Initial(currentInitials.toList)

  }

  /**Create a set of signals based on the initial */
  def createInitial = Initial(initials.toList)

  /**Create a set of initials based on the segments defined as well as the signals not defined which are set to 0*/
  //def createInitial(signals:List[SignalTrait]) = Initial(initials.toList, signals.toList)

  def $fclose(fptr: SignalTrait) = new BasicSegments.Identifier("$fclose(" + fptr.name + ");\n")

  def $finish = new BasicSegments.Identifier("$finish;")

}