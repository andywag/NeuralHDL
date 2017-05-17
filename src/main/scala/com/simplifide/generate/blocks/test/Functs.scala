package com.simplifide.generate.blocks.test

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 9/29/11
 * Time: 8:57 AM
 * To change this template use File | Settings | File Templates.
 */

class Functs {

}

object Functs {

  object Finish extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return SegmentReturn("$finish();\n")
    }
  }
}