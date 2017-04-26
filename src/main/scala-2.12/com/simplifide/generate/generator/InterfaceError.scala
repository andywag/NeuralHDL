package com.simplifide.generate.generator

/**
 * Error, Warning or Messages which are used for SegmentReturn
 */

abstract class InterfaceError(val line:Int,
                              val message:String) {


}

object InterfaceError {
  
   class Error(line:Int,message:String) extends InterfaceError(line,message) {
   }
   class Warning(line:Int, message:String) extends InterfaceError(line,message) {
   }
   class Info(line:Int, message:String) extends InterfaceError(line,message) {
   }
   /** Real Location for the Error */
   def realOffset(message:String,offset:Int):Int = 0
  
}
