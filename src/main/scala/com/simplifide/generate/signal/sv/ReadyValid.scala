package com.simplifide.generate.signal.sv

import com.simplifide.generate
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.model.NdDataSet
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal
import com.simplifide.generate.signal.SignalTrait.SignalPath
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}
import com.simplifide.generate.test2.TestEntityParser
import com.simplifide.generate.test2.data.DataGenParser.DisplayDump2

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

    /** Should move to a dump type class */
    def ---->(name:String,path:Option[String]=None)(implicit clk:ClockControl,scope:EntityParser) = {
      val dataSet = NdDataSet.empty(name)

      val newVld = path.map(x => new signal.SignalTrait.SignalPath(x,vld)).getOrElse(vld)
      val newRdy = path.map(x => new signal.SignalTrait.SignalPath(x,rdy)).getOrElse(rdy)
      val newClk = clk.createEnable(newVld & newRdy)

      val newSignal = path.map(x => new SignalTrait.SignalPath(x,value.signal)).getOrElse(value.signal)

      scope->(DisplayDump2(s"${name}.hex",newSignal)(newClk))
      dataSet
    }

  }

  implicit class ReadyValidSignal(val value:SignalTrait) extends ReadyValid[SignalTrait]{
    val name = value.name
    val signals = List(value)
    val exp = value
  }

}


