package com.simplifide.generate.parameter

import com.simplifide.generate.language.DescriptionHolder
import com.simplifide.generate.html.Description

/**
 * Created by IntelliJ IDEA.
 * User: Andy
 * Date: 9/1/11
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Class which contains a parameter which can be overrwritten. This class can be used for the creating parameterizable
 * designs
 **/

abstract class Parameter[T](val name:String)  {

  var description:Option[Description] = None

  def -- (description:Description)  = {
    this.description = Some(description)
    this
  }

  def get:T
  def set(va:T)
  def set(va:Parameter[T])


}

object Parameter {
  def apply[T](name:String,default:Parameter[T]) = new Default[T](name,default)
  def apply[T](name:String,default:T) = new Default[T](name,new Value(default))

  class Value[T](val va:T) extends Parameter[T]("") {
    def get:T = va
    def set(va:T) {}
    def set(va:Parameter[T]) {}

  }

  class Default[T](name:String, val default:Parameter[T]) extends Parameter[T](name)  {
    var value = default

    def get:T = value.get
    def set(va:T) {value = new Value(va)}
    def set(va:Parameter[T])  {value = va}
  }
}