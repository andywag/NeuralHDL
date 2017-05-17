package com.simplifide.generate.parser.items

import com.simplifide.generate.signal.complex.ComplexSignal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.{ArrayTrait, FixedType, OpType}
import com.simplifide.generate.signal.ArrayTrait._
import com.simplifide.generate.parser.{SignalMethods, SignalParser}
import com.simplifide.generate.signal.complex.ComplexSignal

/**
 * Parasing Methods to handle complex numbers
 */

trait ComplexParser {

  self : SignalMethods =>
  /** Standard Complex Signal Creation */
  def complex(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple):ComplexSignal =
    appendSignal(ComplexSignal(name,typ,fixed))

  /* Convenience method for creating a complex register */
  def complex_reg(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple)
                 (length:Int)(implicit clk:ClockControl) = {
    val signal1 = appendSignal(ComplexSignal(name,typ,fixed))
    register(signal1)(length)(clk)
  }

  /* Convenience method for creating an array of complex numbers */
  def complex_array(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple)
                   (length:Int)(implicit clk:ClockControl):ArrayTrait[ComplexSignal] = {
    val signal1 = ComplexSignal(name,typ,fixed)
    appendSignal(ArrayTrait(signal1,length))
  }


  /** Create a Complex Constant */
  //def complex(real:Double, imag:Double) = ComplexConstant(real,imag)
  /** Complex Constant Creation */
  //def complex(real:Double, imag:Double, fixed:FixedType) = ComplexConstant(fixed,real,imag)



  /** Signal Conjugation */
  def conj(signal:ComplexSignal) = conjugate(signal)
  /** Signal Conjugation */
  def conjugate(signal:ComplexSignal) = ComplexSignal.Conjugate(signal.prototype)

}