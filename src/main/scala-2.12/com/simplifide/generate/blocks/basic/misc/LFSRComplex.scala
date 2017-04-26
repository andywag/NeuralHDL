package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.signal.SignalTrait._
import com.simplifide.generate.signal.RegisterTrait._
import com.simplifide.generate.generator.{SimpleSegment, ComplexSegment}
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.signal.Constant._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.{Constant, RegisterTrait, SignalTrait, FixedType}


/** Linear Feedback Shift Register which is based on a complex segment versus LFSR
 *
 *  @constructor Constructor for the Linear Feedback Shift Register
 *  @param output Output Signal
 *  @param poly Polynomial for the shift register larger number is closer to output
 *  @param length of the shift register
 *  @param init Initial Value of the Shift Register
 */

class LFSRComplex(val output:SignalTrait,
           val poly:List[Int],
           val length:Int,
           val init:List[Int])(implicit clk:ClockControl) extends ComplexSegment {

  def createBody {
    // Signal Declarations
    val input = signal("dline_input")
    val reg   = register(input)(length)
    // Tap Calculation
    val bin = poly.map(x => reg(x)).reduceLeft[SimpleSegment](BinaryOperator.XOR(_,_))
    // Delay Creation From the Flop
    this.assign(reg.createFlop(init.map(Constant(_,FixedType.unsigned(1,0)))))
    // Input to the LFSR
    input := bin
    // Output ProcStatement
    output := reg(length)
  }

}

/** Factory for creating the LFSR */
object LFSRComplex{

  def apply(output:SignalTrait,poly:List[Int],length:Int,init:List[Int])(implicit clk:ClockControl) = {
    new LFSRComplex(output,poly,length,init)
  }

}