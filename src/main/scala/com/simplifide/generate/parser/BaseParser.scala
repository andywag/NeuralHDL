package com.simplifide.generate.parser

import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.parser.factory.CreationFactory

import collection.mutable.ListBuffer
import model._
import operator.BitOperations
import com.simplifide.generate.signal.{Constant, FixedType, NewConstant}

/**
 * Parser which contains contructs for basic low level operations
 */

trait BaseParser extends SegmentHolder  {

  /** Scope where assignments and declarations are appended to */
  implicit val scope = this


  //def cat(expressions:Seq[Expression])(implicit creator:CreationFactory):Expression   = new Operators.Concat(expressions.map(_.create).toList)
  def cat(expressions:Expression*)(implicit creator:CreationFactory):Expression   = new Operators.Concat(expressions.map(_.create).toList)

  /** Concatenate the List of Expressions */
  def $cat(expressions:Expression*):Expression   =  new BitOperations.Concatenation(expressions.toList)
  def $cat(expressions:List[Expression]):Expression  =  new BitOperations.Concatenation(expressions)

  /** Repeat the condition */
  def $repeat(expression:Expression, length:Int):Expression =  new BitOperations.Repeat(expression)
  def $reverse(expression:Expression):Expression =  new BitOperations.Reverse(expression)


  implicit def Integer2Constant(value:Int)   = NewConstant(value)
  implicit def Float2Constant(value:Float)   = NewConstant(value.toDouble)
  implicit def Double2Constant(value:Double) = NewConstant(value)
  

}

object BaseParser {

}