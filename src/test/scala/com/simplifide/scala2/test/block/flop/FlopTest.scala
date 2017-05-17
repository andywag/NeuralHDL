package com.simplifide.scala2.test.block.flop






/*
import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import com.simplifide.generate.blocks.basic.flop.{SimpleFlopList, ClockControl}
import com.simplifide.generate.generator.CodeWriter
import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 5/29/11
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */

class FlopTest extends FeatureSpec with ShouldMatchers {




  feature("Set of Test for the Generation of Flops") {
    val head = ClockControl.default
    val out  = SignalTrait("out1")
    val in   = SignalTrait("in1")
    scenario("Simple Flop Creation") {
    val fl =  SimpleFlopList.newFlop(head,List(out),List(in))
    val ret = fl.createCode(CodeWriter.Verilog)
    val code = ret.code

    val lines = code.split("\n")
    lines(2).trim should equal ("out1 <= 1'd0;")
    lines(5).trim should equal ("out1 <= in1;")

    //ret.code should contain value "out1 <= 0;"
    //ret.code should contain value "out1 <= in1;"


    System.out.println(ret.code)

    }
  }

}
*/