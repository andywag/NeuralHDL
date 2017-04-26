package com.simplifide.generate.signal

/**
 *  Trait which allows creation of a signal
 */

trait SignalCreator {
  def createSignal:SignalTrait
  def createSignal(opType:OpType):SignalTrait
  def createSignal(name:String, opType:OpType):SignalTrait
}
