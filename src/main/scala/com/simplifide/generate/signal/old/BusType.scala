package com.simplifide.generate.signal.old

import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 7/16/11
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */

/**Class which contains the type of signals included in a bus */
trait BusType {
  /**List of Signals included in this bus */
  val signals: List[SignalTrait]

  /**Number of signals in the bus */
  lazy val length: Int = signals.size


  def changeTestType = BusType(signals.map(_.changeTestType))

  def changeType(typ: OpType) = BusType(signals.map(_.changeType(typ)))

  def reverseType = BusType(signals.map(_.reverseType))

  /**Create the signals associated with the name1 */
  def createSignals(functionName: String): List[SignalTrait] = {
    def signalName = if (functionName.equalsIgnoreCase("")) "" else functionName + "_"
    signals.map(x => x.newSignal(signalName + x.name))
  }
}

object BusType {
  def apply(signals: List[SignalTrait]) = new Impl(signals)

  def apply(signals: SignalTrait*) = new Impl(signals.toList)

  class Impl(override val signals: List[SignalTrait]) extends BusType

}