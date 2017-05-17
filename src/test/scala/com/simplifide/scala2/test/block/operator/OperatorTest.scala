package com.simplifide.scala2.test.block.operator





/*
import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import com.simplifide.generate.generator.CodeWriter
import com.simplifide.generate.blocks.basic.operator.{BinaryOperator, UnaryOperator}
import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 5/29/11
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */

class OperatorTest extends FeatureSpec with ShouldMatchers {


  val signala = SignalTrait("testa")
  val signalb = SignalTrait("testb")
  val writer  = CodeWriter.Verilog

  feature("Test the Major Operator Functionallity") {

    scenario("Unary Operator") {
      UnaryOperator.Not(signala).createCode(writer).code should equal ("~testa")
      UnaryOperator.And(signala).createCode(writer).code should equal ("&testa")
      UnaryOperator.NotLogical(signala).createCode(writer).code should equal ("!testa")
      UnaryOperator.Or(signala).createCode(writer).code should equal ("|testa")
      UnaryOperator.Not(signala).createCode(writer).code should equal ("~testa")
    }

    scenario("Binary Operator") {
      BinaryOperator.AND(signala,signalb).createCode(writer).code should equal ("testa & testb")
    }
  }

}
*/