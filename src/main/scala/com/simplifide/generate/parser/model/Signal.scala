package com.simplifide.generate.parser.model







/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/12/11
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */

/*

trait Signal extends Expression {

    val name:String

    def isInput = false
    def isOutput = false

    override def toString = name
    //def apply(index:Int):Signal = Signal(this.name)

    override def copy(index:Int):Expression = Signal(name + "_" + index)

    // Unary Operators
    //override def unary_- : Expression = new Adder.NegativeTerm(this)


}

object Signal {

  def apply(name:String) = new Implementation(name)

  class Implementation(val name:String) extends Signal

  class Delay(signal:Signal, clk:Clock) extends Signal {
    override val name = signal.name
    override def toString = signal.name + "[" + clk + "]"
  }
}

*/