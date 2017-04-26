package com.simplifide.generate.blocks.basic.flop

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.signal.{SignalTrait, OpType, FixedType}
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}


class Clocks {

}
/** Class which contains classes and methods for dealing with clocks */
object Clocks {

  /** Top Level Class for a clock signal */
  class ClockSignal(val signal:SignalTrait) extends SimpleSegment{
    override val name = signal.name
    override def createCode(implicit writer:CodeWriter):SegmentReturn = signal.createCode
  }
  /** Index of Clock. Used for Time Sharing
   **/
  class Index(signal:SignalTrait,index:Int) extends ClockSignal(signal) {
    def getSignal:SignalTrait = signal

  }

  /**
   * Class defining a clock
   */
  class Clock(signal:SignalTrait, posedge:Boolean) extends ClockSignal(signal){
  /** Returns the appendSignal associated with this clock */
    def sensitivityList():List[SimpleSegment] = {
      if (posedge) return List(new ClockEdgeHead(this, "posedge "))
      else         return List(new ClockEdgeHead(this, "negedge "))
    }
  }
   /**
   * Class defining a reset with options whether it is synchronous or not
   *
   */
  class Reset(signal:SignalTrait,async:Boolean = false,val activeLow:Boolean=false) extends ClockSignal(signal) {
    def sensitivityList():List[SimpleSegment] = {
      if (async) {
        if (activeLow) return List(new ClockEdgeHead(this, "negedge "))
        else           return List(new ClockEdgeHead(this, "posedge "))
      }
    return List()
    }
  }

  /** Enable Signal for the Clock */
  class Enable(val signal:SimpleSegment)

  /**
   * Class which creates part of the sensitivity list for signal edges
   */

  class ClockEdgeHead(signal:ClockSignal, edge:String) extends SimpleSegment {

    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      return SegmentReturn(edge) + writer.createCode(signal)

  }
  
}