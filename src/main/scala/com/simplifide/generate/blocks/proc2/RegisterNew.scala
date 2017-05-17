package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 12/30/11
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */

trait RegisterNew {
  /** Signal included in this register */
  val signal:SignalTrait
  /** Location of this address */
  //def at(address:Int,start:Int) = FullRegister(this,address,start,start+signal.fixed.width)

  val isRead = this match {
    case x:RegisterNew.Write => false
    case _     => true
  }

  val isWrite = this match {
    case x:RegisterNew.Read => false
    case _    => true
  }
  
  
}

object RegisterNew {
  class Read      (val signal:SignalTrait) extends RegisterNew
  class Write     (val signal:SignalTrait) extends RegisterNew
  class ReadWrite (val signal:SignalTrait) extends RegisterNew



}