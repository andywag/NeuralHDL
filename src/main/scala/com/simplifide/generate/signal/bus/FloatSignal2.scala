package com.simplifide.generate.signal.bus

import com.simplifide.generate.parser.items.BusParser
import com.simplifide.generate.signal.OpType

/**
  * Created by andy on 4/27/17.
  */
trait FloatSignal2 extends BusParser {

  val sigWidth:Int
  val expWidth:Int

  val man = signal(s"${name}_man",opType,S(sigWidth,0))
  val exp = signal(s"${name}_exp",opType,U(expWidth,0))

}

object FloatSignal2 {
  def apply(name:String,opType:OpType,sig:Int=24,exp:Int=8) =
    new Impl(name,opType,sig,exp)

  case class Impl(val name:String, override val opType:OpType = OpType.Register,
                  val sigWidth:Int, val expWidth:Int) extends FloatSignal2
}
