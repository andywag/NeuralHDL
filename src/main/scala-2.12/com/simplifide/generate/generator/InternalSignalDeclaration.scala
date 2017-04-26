package com.simplifide.generate.generator

import com.simplifide.generate.signal.{SignalDeclaration, SignalTrait}


/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 2/10/12
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */

trait InternalSignalDeclaration extends SimpleSegment {

  val signals:List[SignalTrait]

  override def createCode(implicit writer:CodeWriter) = {
    signals.map(new SignalDeclaration(_).createCode).reduceLeft(_+_)
  }

}

object InternalSignalDeclaration {

  def apply(signals:List[SignalTrait]) = new Impl(signals)
  def apply(signals:SignalTrait*) = new Impl(signals.toList)

  class Impl(override val signals:List[SignalTrait]) extends InternalSignalDeclaration
  
}