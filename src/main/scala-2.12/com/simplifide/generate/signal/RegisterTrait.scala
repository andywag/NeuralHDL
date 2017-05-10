package com.simplifide.generate.signal

import com.simplifide.generate.blocks.basic.flop.{SimpleFlopList, ClockControl}
import com.simplifide.generate.parser.model.Clock
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.generator.SegmentReturn._

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 7/31/11
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */

/** Trait containing addresses which are automatically created for the module */
trait RegisterTrait[T <: SignalTrait] extends ArrayTrait[T] {
  val clock:ClockControl

  override val name:String = prototype.name + "_reg"
  //override val opType:OpType = OpType.Register

  override def apply(clk:Clock):T = this.apply(clk.delay)
  override def apply(index:Int):T = this.slice(index)

  override def baseSignal = this.prototype

  override def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn(prototype.name)

  override def slice(index:Int):T = {
    if (index == 0) return this.prototype
    prototype.createSlice(index,"r").asInstanceOf[T]
  }

  override def children:List[SignalTrait] = {
    List.tabulate(length)(i => this.prototype.createSlice(i+1,"r"))
  }
  override def allChildren:List[SimpleSegment] = {
    val ch = this.children.flatMap(x => x.allChildren)
    ch.map(_.asInstanceOf[SignalTrait].changeType(OpType.Register))
  }

  def createFlop(init:List[SimpleSegment]):SimpleFlopList = {
    val thisChildren = this.children
    val res = thisChildren.zipWithIndex.map(x => new SimpleFlopList.Segment(x._1,Some(init(x._2))))
    val ena = thisChildren.zipWithIndex.map(x => new SimpleFlopList.Segment(x._1,Some(if (x._2 == 0) this.prototype else children(x._2-1))))
    new SimpleFlopList(None,this.clock,res,ena)
  }

  def createFlop:SimpleFlopList = {

    val thisChildren = this.children
    val res = thisChildren.map(x => new SimpleFlopList.Segment(x,None))
    val ena = thisChildren.zipWithIndex.map(x => new SimpleFlopList.Segment(x._1,Some(if (x._2 == 0) this.prototype else children(x._2-1))))
    new SimpleFlopList(None,this.clock,res,ena)
  }



}

object RegisterTrait {
  def apply[T <: SignalTrait](prototype:T, length:Int, clock:ClockControl):RegisterTrait[T] =
    new Register(length,prototype,clock)


  class Register[T <: SignalTrait](val length:Int,val prototype:T, val clock:ClockControl) extends RegisterTrait[T] {
    def newObject(length:Int,prototype:T):ArrayTrait[T] = new Register[T](length,prototype,clock)
    override def newSignal(name:String = this.name,
      opType:OpType = this.opType,
      fixed:FixedType = this.fixed):SignalTrait = this
  }

}

