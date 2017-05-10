package com.simplifide.generate.blocks.basic.typ

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{FloatSignal, OpType, SignalTrait}

/**
  * Created by andy on 5/9/17.
  */

object NumberType {
  trait NumberLike[T] {
    def value:T
    def times[S](in2:NumberLike[S])(implicit clk:ClockControl):Expression
  }

  object NumberLike {

    implicit class MultiplyLikeFloat(val value:FloatSignal) extends NumberLike[FloatSignal] {
      def times[S](in2:NumberLike[S])(implicit clk:ClockControl) = {
        in2 match {
          case x:MultiplyLikeFloat => new FloatMult("",value,x.value)
          case _                   => new FloatMult("",null,null)
        }
      }
    }

    //def times[S](in1:S, in2:S)(implicit multiplyType: MultiplyLike[S]) = multiplyType

  }

}

object Test {

  import NumberType.NumberLike._
  implicit val clk: ClockControl = ClockControl("clk","reset")

  val a = FloatSignal("alpha",OpType.Signal,24,8)
  val b = FloatSignal("beta",OpType.Signal,24,8)
  a times b




}

