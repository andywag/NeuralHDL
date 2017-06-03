package com.simplifide.generate.newparser.typ

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.float.{FloatAddition, FloatMult}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{FloatSignal, OpType, SignalTrait}

/**
  * Created by andy on 5/9/17.
  */

object NumberType {
  trait NumberLike[T] {
    def value:T
    def times[S](in2:NumberLike[S])(implicit clk:ClockControl):Expression
    def plus[S](in2:NumberLike[S])(implicit clk:ClockControl):Expression
    def minus[S](in2:NumberLike[S])(implicit clk:ClockControl):Expression

  }

  object NumberLike {

    class NumberLikeFloatSignal(val value:FloatSignal) extends NumberLike[FloatSignal] {
      def times[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          case x:NumberLikeFloatSignal => new FloatMult("",value,x.value)
          case _                   => new FloatMult("",null,null)
        }
      }
      def plus[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          case x:NumberLikeFloatSignal => new FloatAddition("",value,x.value,false)
          case _                   => new FloatAddition("",value,null,false)
        }
      }
      def minus[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          case x:NumberLikeFloatSignal => new FloatAddition("",value,x.value,true)
          case _                   => new FloatAddition("",value,null,true)
        }
      }
    }

    class NumberLikeSignal(val value:SignalTrait) extends NumberLike[SignalTrait] {
      def times[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          //case x:NumberLikeFloatSignal => new FloatMult("",value,x.value)
          case _                   => new FloatMult("",null,null)
        }
      }
      def plus[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          //case x:NumberLikeFloatSignal => new FloatAddition("",value,x.value,false)
          case _                   => new FloatAddition("",null,null,false)
        }
      }
      def minus[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          case _                   => new FloatAddition("",null,null,true)
        }
      }

    }





  }

}



