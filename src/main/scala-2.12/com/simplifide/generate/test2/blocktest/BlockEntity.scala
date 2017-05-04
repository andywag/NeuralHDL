package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.EntityParser

/**
  * Created by andy on 5/3/17.
  */
class BlockEntity(segment:SimpleSegment with BlockTestable) extends EntityParser {
  val name = segment.testName

}
