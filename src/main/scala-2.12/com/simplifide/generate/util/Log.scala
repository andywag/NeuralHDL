package com.simplifide.generate.util

import com.typesafe.scalalogging.Logger


/**
  * Created by andy on 5/4/17.
  */
object Log {
  def apply(name:String) = Logger(name)
  def apply()            = Logger("MAIN")
}
