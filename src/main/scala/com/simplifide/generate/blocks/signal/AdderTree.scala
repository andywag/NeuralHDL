package com.simplifide.generate.blocks.signal

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.{FixedType, ArrayTrait, SignalTrait}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.fixed.Roundable


/**
 * Created by IntelliJ IDEA.
 * User: Andy
 * Date: 2/16/12
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */

trait AdderTree extends ComplexSegment {

  implicit val clk:ClockControl
  
  val output:SignalTrait
  val input:ArrayTrait[SignalTrait]
  val groupSize:Int

  val internalWidth:FixedType = input.fixed
  val roundType = Roundable.RoundClip

  def term(outTerm:SignalTrait, inRow:ArrayTrait[SignalTrait], start:Int) = {
    val length = if ((inRow.length - start) < groupSize) (inRow.length) - start else groupSize
    outTerm := List.tabulate(length)(x => inRow(start + x)).reduceLeft[Expression](_+_).create
  } 
  
  def row(outRow:ArrayTrait[SignalTrait],outRowReg:ArrayTrait[SignalTrait],inRow:ArrayTrait[SignalTrait],length:Int) = {
    for (j <- 0 until length) {
      term(outRow(j),inRow, groupSize*j)
    }
    outRowReg := outRow $at clk
  }
  
  
  override def createBody {
    val stages      = (math.log(input.length/groupSize)/math.log(2.0)).toInt + 1
    val stageLength = List.tabulate(stages)(x => math.ceil(input.length/(groupSize*math.pow(2.0,x))).toInt)
    val adderRow    = List.tabulate(stages)(i => array("adder_row_" + i,WIRE,internalWidth)(stageLength(i)))
    val adderRowR   = List.tabulate(stages)(i => array("adder_row_r_" + i,REG,internalWidth)(stageLength(i)))
    
    row(adderRow(0),adderRowR(0),input,stageLength(0))
    for (i <- 1 until stages-1) {
      row(adderRow(i),adderRowR(i),adderRowR(i-1),stageLength(i))
    }
    output := $round_generic(adderRowR(stages-2)(0) + adderRowR(stages-2)(1),internalWidth,roundType)
    
    
  }

  
}

object AdderTree {

  def apply(output:SignalTrait,input:ArrayTrait[SignalTrait], groupSize:Int)(implicit clk:ClockControl) =
    new Impl(output,input,groupSize)

  class Impl(
    val output:SignalTrait,
    val input:ArrayTrait[SignalTrait],
    val groupSize:Int)(implicit val clk:ClockControl) extends AdderTree
}