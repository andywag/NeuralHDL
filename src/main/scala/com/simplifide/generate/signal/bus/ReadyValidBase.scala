package com.simplifide.generate.signal.bus

import com.simplifide.generate.parser.items.BusParser
import com.simplifide.generate.signal.{BusDirect, SignalCreator, SignalTrait, OpType}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/10/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */

trait ReadyValidBase[T <: SignalCreator] extends BusParser {

  val opType:OpType

  val data:T

  val vData:SignalTrait
  val vVld:SignalTrait
  val vRdy:SignalTrait

  def enable = vRdy & vVld
}

object ReadyValidBase {

  class Signal[T <: SignalCreator](val data:T, val vld:SignalCreator, val rdy:SignalCreator) extends ReadyValidBase[T] {
    override val name = ""
    val vData = signal(data.createSignal)
    val vVld  = signal(vld.createSignal)
    val vRdy  = signal(rdy.createSignal)

    override def createSignal(opType:OpType):SignalTrait = new ReadyValidBase.Signal[T](
      vData.createSignal(opType).asInstanceOf[T],
      vVld.createSignal(opType),
      vRdy.createSignal(opType.reverseTypeWithReg)
    )


  }

}
