package com.simplifide.generate.project

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/24/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */

class ConnectionNew(val connections:List[ConnectionNew.Item]) extends Connection {


  override def connectOption(signal:SignalTrait):Option[SignalTrait] = {

    val options = connections.map(x => x.connectOption(signal)).filter(x => x.isDefined)
    if (options.size > 0) {
      if (options(0) == ConnectionNew.EMPTY) None else options(0)
    } else Some(signal)
  }

  override def connectSegment(signal:SignalTrait):SimpleSegment = {
    val options = connections.map(x => x.connectSegment(signal)).filter(x => x.isDefined).map(_.get)
    if (options.size > 0) {
      if (options(0) == ConnectionNew.EMPTY) BasicSegments.Empty else options(0)
    } else signal
  }
  
}

object ConnectionNew {

  val EMPTY = SignalTrait("EMPTY")


  abstract class Item {
    def connectOption(signal:SignalTrait):Option[SignalTrait]
    def connectSegment(signal:SignalTrait):Option[SimpleSegment]
  }
  
  class NameToSegment(val name:String, val segment:SimpleSegment) extends Item {
    def connectOption(signal:SignalTrait):Option[SignalTrait] = {
      segment match {
        case x:SignalTrait => if (signal.name == name) Some(x.copy(optype = signal.opType)) else None
        case _             => None
      }
    }

    def connectSegment(signal:SignalTrait) = if (signal.name == name ) Some(segment) else None
  }
  
  class SignalToSegment(val signal:SignalTrait,val segment:SimpleSegment) extends Item  {
    def connectOption(signal:SignalTrait):Option[SignalTrait] = {
      segment match {
        case x:SignalTrait => if (this.signal == signal) Some(x) else None
        case _             => None
      }
    }
    
    def connectSegment(signal:SignalTrait) = if (this.signal == signal) Some(segment) else None
  }
  
  
}
