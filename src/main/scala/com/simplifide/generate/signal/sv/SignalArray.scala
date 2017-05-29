package com.simplifide.generate.signal.sv

import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/28/17.
  */
trait SignalArray[T] {
  val value:T
  val signal:SignalTrait
}

object SignalArray {

  // FIXME : Subclass of Struct probably bad idea but convenient
  case class Arr[T](override val name:String, input:SignalArray[T],length:Int) extends SignalTrait with Struct {


    override val packed: Boolean = true
    override val typeName: String = s"${input.signal.name}_typ_$length"
    override val signals: List[SignalTrait] = List.tabulate(length)(x => input.signal.newSignal(name = s"${createName(s"v$x")}"))
    override val opType = input.signal.opType

    def s(index:Int): T = signals(index).asInstanceOf[T]

    /** Kind of a kludge need a better way to copy objects (shapeless maybe) */
    override def copyStruct(n: String, o: OpType): SignalTrait = {
      new Arr(n, input.signal.newSignal(n,o),length)
    }

    /** Creates a New Signal (Virtual Constructor) */
    override def newSignal(name: String, opType: OpType, fix: FixedType): SignalTrait = {
      new Arr(name, input.signal.newSignal(name,opType,fix),length)
    }

    //def g(index:Int):

  }

  implicit class SignalArraySignal[S <: SignalTrait](input:S) extends SignalArray[S] {
    val value = input
    val signal = input
  }

}
