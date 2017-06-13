package com.simplifide.generate.signal.sv

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/27/17.
  */

trait ReadyValid[T] {
  val value:T
  val name:String
  val signals:List[SignalTrait]
  val exp:Expression
  val signal:SignalTrait = value.asInstanceOf[SignalTrait]
}

object ReadyValid {
  case class ReadyValidInterface[T](value:ReadyValid[T]) extends SignalInterface {

    override val name = value.name
    val opType = OpType.Input

    val vld     = SignalTrait(appendName("vld"),opType ,FixedType.BIT)
    val fst     = SignalTrait(appendName("fst"),opType ,FixedType.BIT)
    val rdy     = SignalTrait(appendName("rdy"),opType.reverseType ,FixedType.BIT)

    val enable = rdy & vld

    override val inputs = vld :: fst :: value.signals
    override val outputs = List(rdy)
  }

  implicit class ReadyValidSignal(val value:SignalTrait) extends ReadyValid[SignalTrait]{
    val name = value.name
    val signals = List(value)
    val exp = value
  }

}


