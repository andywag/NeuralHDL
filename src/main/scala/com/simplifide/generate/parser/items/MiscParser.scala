package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.items.MiscParser.Width

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/29/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */

trait MiscParser {

  implicit def Int2Wrapper(value:Int) = new MiscParser.IntWrapper(value)
   
}

object MiscParser {
  class IntWrapper(val value:Int) {
    def ~> (bottom:Int) = new Width(value,bottom)
  }

  class Width(val top:Int, val bottom:Int)
}
