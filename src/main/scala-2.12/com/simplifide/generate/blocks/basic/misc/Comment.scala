package com.simplifide.generate.blocks.basic.misc

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.generator.{SimpleSegment, SegmentReturn, CodeWriter}

/**
 * Class which defines a comment operation
 */
abstract class Comment(val text:String) extends SimpleSegment{

}

object Comment {
  /** Single Line Comment */
  class SingleLine(text:String) extends Comment(text)  {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn("// " + text + "\n")
    override def toString = "// " + text
  }

  class MultipleSingleLine(text:String) extends Comment(text)  {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val lines = text.split("\n")
      writer.createCode(SingleLine("")) + lines.map(new Comment.SingleLine(_)).map(writer.createCode(_)).reduceLeft(_+_) + writer.createCode(SingleLine(""))
    }
    override def toString = "// " + text
  }
  
  class Section(text:String) extends Comment(text) {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val sep = List.fill(80)("/").reduceLeft(_+_)
      SegmentReturn( sep + "\n// " + text + "\n" + sep + "\n\n")

    }


  }

  object SingleLine {
    def apply(text:String) = new SingleLine(text)
  }

}
