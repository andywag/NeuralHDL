package com.simplifide.generate.blocks.test

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}


/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 3/12/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */

class BasicBlocks {

}

object BasicBlocks {
  class TimeScale(base:String, increment:String) extends SimpleSegment {

    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return SegmentReturn("`timescale " + base + "/" + increment + "\n\n")
    }

  }
}