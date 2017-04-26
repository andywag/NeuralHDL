package com.simplifide.generate.parser.math

import com.simplifide.generate.parser.{ObjectFactory, ExpressionReturn}
import com.simplifide.generate.parser.model.{Model, Expression}
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.signal.FixedType

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/12/11
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */

/*
case class Multiplier(val lhs:Expression, rhs:Expression) extends Expression {
    override def toString = "(" + lhs.toString + " * " + rhs.toString + ")"

    override def split(output:Expression,index:Int):ExpressionReturn = {
      val signal = output.copy(index)
      val state  =  ObjectFactory.Statement(signal,this)
      new ExpressionReturn(signal,List(state))
    }

}

object Multiplier {
    abstract class Fixed(lhs:Expression, rhs:Expression, fixed:FixedType) extends Multiplier(lhs,rhs) {
      val prefix:String
      override def toString = prefix + fixed + "(" + lhs.toString + "*" + rhs.toString + ")"
    }
    class Truncation(lhs:Expression, rhs:Expression, fixed:FixedType) extends Fixed(lhs,rhs,fixed) {
      override val prefix = "T"
    }
    class TruncationClip(lhs:Expression, rhs:Expression, fixed:FixedType) extends Fixed(lhs,rhs,fixed) {
      override val prefix = "TC"
    }
    class Round(lhs:Expression, rhs:Expression, fixed:FixedType) extends Fixed(lhs,rhs,fixed) {
      override val prefix = "R"
    }
    class RoundClip(lhs:Expression, rhs:Expression, fixed:FixedType) extends Fixed(lhs,rhs,fixed) {
      override val prefix = "RC"
    }
}
*/