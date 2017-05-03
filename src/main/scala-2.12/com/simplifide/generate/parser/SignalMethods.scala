package com.simplifide.generate.parser

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal._
import items.BusParser


/** Convenience Methods for Creating Signals */
trait SignalMethods extends SignalParser {

  val INPUT  = OpType.Input
  val OUTPUT = OpType.Output
  val WIRE   = OpType.Signal
  val REG    = OpType.Register
  val REGOUT = OpType.RegOutput
  val SIGNAL   = OpType.Signal
  val STRUCT   = OpType.Struct

  implicit def BusParser2BusDirect(parser:BusParser) = parser.createSignal

  def appendSignal[T <: SignalTrait](signal:T):T = {
    signal
  }

  def appendSignals(signals:List[SignalTrait]) = {
    signals.foreach(x => this.appendSignal(x))
  }

     
  /** Convenience method for creating a signal */
  def signal(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple,depth:Int = 0):SignalTrait =
    appendSignal(SignalTrait(name,typ,fixed,depth))

  /** Convenience method for creating a appendSignal */
  def array(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple)(arr:Int*):ArrayTrait[SignalTrait] = {
    val sig = SignalTrait(name,typ,fixed)
    val realSignal = if (arr.size > 0) {
      var useSignal =  ArrayTrait[SignalTrait](sig,arr(0))
      for (i <- 1 until arr.size) {
        useSignal = ArrayTrait[SignalTrait](useSignal,arr(i))
      }
      useSignal
    } 
    else {
      ArrayTrait[SignalTrait](sig,0)
    }
    appendSignal(realSignal)
    realSignal
  }




  /** Create a register from a appendSignal while specifying the clock */
  def register[T <: SignalTrait](signal1:T)(length:Int)(implicit clk:ClockControl):RegisterTrait[T] = {
    appendSignal(RegisterTrait(signal1,length,clk))
  }

  def register(name:String, typ:OpType = OpType.Signal,fixed:FixedType = FixedType.Simple)
              (length:Int)(implicit clk:ClockControl):RegisterTrait[SignalTrait] = {
    val sig:SignalTrait = signal(name,typ,fixed)
    register(sig)(length)(clk)
  }

  /** Convenience Method for Creating a Signed Type */
  def S(width:Int,fraction:Int)        = signed(width,fraction)
  /** Convenience Method for Creating a UnSigned Type */
  def U(width:Int,fraction:Int)        = unsigned(width,fraction)
   /** Convenience Method for Creating a Signed Type */
  def signed(width:Int,fraction:Int)   = FixedType.signed(width,fraction)
  /** Convenience Method for Creating a UnSigned Type */
  def unsigned(width:Int,fraction:Int) = FixedType.unsigned(width,fraction)

  /*
  /** Convenience method for creating a Bus */
  def bus[T <: BusType](name:String, typ:T):Bus[T] =  {
    appendSignal(Bus[T](name,typ))

  }
  /** Convenience method for creating an Array of Buses */
  def busArray(name:String, typ:BusType)(arr:Int*):SignalTrait =  {
    val bus = Bus(name,typ)
    val sig = ArrayTrait(bus,arr(0))
    appendSignal(sig)
  }
  */

  // Complex Signals
  /*
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
  def complex(real:Double, imag:Double) = ComplexConstant(real,imag)
  /** Complex Constant Creation */
  def complex(real:Double, imag:Double, fixed:FixedType) = ComplexConstant(fixed,real,imag)



  /** Signal Conjugation */
  def conj(signal:ComplexSignal) = conjugate(signal)
  /** Signal Conjugation */
  def conjugate(signal:ComplexSignal) = ComplexSignal.Conjugate(signal.prototype)
  */

}