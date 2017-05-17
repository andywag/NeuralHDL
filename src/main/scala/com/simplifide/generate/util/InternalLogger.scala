package com.simplifide.generate.util

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/6/12
 * Time: 8:45 AM
 * To change this template use File | Settings | File Templates.
 */

object InternalLogger {

  var DEBUG = false

  def debug(value:String) = if (DEBUG) System.out.println(value)
  def message(value:String) = System.out.println(value)
  def warning(value:String) = System.out.println(value)
  def error(value:String)   = System.out.println(value)

  def booleanMessage(value:String):Boolean = {System.out.println(value); return false;}




}
