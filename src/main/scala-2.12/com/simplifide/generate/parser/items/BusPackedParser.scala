package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.SignalHolder
import com.simplifide.generate.signal.{SignalTrait, FixedType, OpType, SignalCreator}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/10/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */

class BusPackedParser(val name:String, val opType:OpType, val fixed:FixedType) extends SignalHolder with SignalCreator {

  val signal = SignalTrait(name,opType,fixed)
  
  def createSignal:SignalTrait = signal
  def createSignal(opType:OpType):SignalTrait = signal.copy(optype = opType)
  def createSignal(name:String, opType:OpType):SignalTrait = signal.copy(nam = name,optype = opType)


}
