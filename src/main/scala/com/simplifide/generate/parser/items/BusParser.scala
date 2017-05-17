package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.SignalHolder
import com.simplifide.generate.signal.{SignalCreator, SignalTrait, OpType, BusDirect}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/11/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

trait BusParser extends SignalHolder with SignalCreator {

  val name:String
  val opType:OpType = OpType.Signal

  def createSignal:SignalTrait = BusDirect(name,opType,signals.toList)
  def createSignal(opType:OpType):SignalTrait = BusDirect(name,opType,signals.toList.map(_.changeType(opType)))
  def createSignal(name:String, opType:OpType):SignalTrait = BusDirect(name + "_" + this.name,opType,signals.toList.map(_.changeType(opType)))


}
