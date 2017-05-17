package com.simplifide.scala2.test.block

import com.simplifide.generate.signal.complex.ComplexSignal
import com.simplifide.generate.signal.{FixedType, OpType}
import com.simplifide.generate.generator.CodeWriter

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 6/25/11
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */

object AdderTest {
  /*def main(args:Array[String]) = {
      val addIn0  = ComplexSignal("add1",OpType.Register,FixedType.signed(8,6))
      val addIn1  = ComplexSignal("add2",OpType.Register,FixedType.signed(8,6))
      val addOut  = ComplexSignal("out",OpType.Register,FixedType.signed(8,6))

      val adder = AdditionStatement.RoundClip("out",addOut,addIn0,addIn1)
      val code = adder.createCode(CodeWriter.Verilog)
      System.out.println(code.code)
  }*/

}