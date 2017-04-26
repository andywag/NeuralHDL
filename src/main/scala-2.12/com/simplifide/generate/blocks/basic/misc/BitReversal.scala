package com.simplifide.generate.blocks.basic.misc

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 3/23/12
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */

class BitReversal(val signal:SignalTrait) extends ComplexSegment {

  override def createBody {
    $cat(List.tabulate(signal.width)(x => signal(signal.width - x -1 )))
  }
  
}