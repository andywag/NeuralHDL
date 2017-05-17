package com.simplifide.generate.generator

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



abstract class CodeWriter() {

  val isVerilog:Boolean = false;
  val isVhdl:Boolean = false;
  val isHeader:Boolean = false;
  val isFloat:Boolean = false;
  val isFixed:Boolean = false;
  
  def createCode(segment:SimpleSegment):SegmentReturn = segment.createCode(this)
  def createCodeRoot(segment:SimpleSegment):SegmentReturn = segment.createCodeRoot(this)
  //def createSimpleCode(segment:SimpleSegment):String = createCode(segment).code
}



object CodeWriter {
  object Verilog extends CodeWriter
  object VerilogFunction extends CodeWriter

  object Vhdl extends CodeWriter {
    //override val isVhdl:Boolean = true;
    //override def createCode(segment:SimpleSegment):SegmentReturn = return segment.createVhdlCode(this)
  }

  object CHeader extends CodeWriter {
    //override val isHeader:Boolean = true;
    //override def createCode(segment:SimpleSegment):SegmentReturn = return segment.createHeaderCode(this)
  }

  object Float extends CodeWriter {
    //override val isFloat:Boolean = true;
    //override def createCode(segment:SimpleSegment):SegmentReturn = return segment.createFloatCode(this)
  }

  object Fixed extends CodeWriter {
    //override val isFixed:Boolean = true;
    //override def createCode(segment:SimpleSegment):SegmentReturn = return segment.createFixedCode(this)
  }
}

