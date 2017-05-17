package com.simplifide.generate.parameter

import com.simplifide.generate.language.Conversions._

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 9/6/11
 * Time: 6:42 AM
 * To change this template use File | Settings | File Templates.
 */

abstract class ParameterOperations[T] extends Parameter[T](""){
  def set(va:T)   {}
  def set(va:Parameter[T]) {}
}

object ParameterOperations {
  /*class Plus[T <: Int](val in1:Parameter[T],val in2:Parameter[T]) extends ParameterOperations[T] {
    def get = in1.get + in2.get
  }*/
}