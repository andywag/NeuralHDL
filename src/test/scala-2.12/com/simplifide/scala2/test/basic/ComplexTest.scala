
package com.simplifide.scala2.test.basic

import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.model.{ Expression}
import math._
import com.simplifide.generate.parameter.{Parameter, ModuleScope}
import com.simplifide.generate.TestConstants
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter}
import com.simplifide.generate.signalproc.Filter
import com.simplifide.generate.blocks.basic.fixed.{ClipSegment, FixedOperations}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{NewEntity, Project, Module}
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.blocks.test.CompareTable
import com.simplifide.generate.test2.isim.Isim
import com.simplifide.generate.test2.{Isim, Test, TestEntityParser}
import com.simplifide.generate.parser.items.ComplexParser


class ComplexTest {}

/**
 * This test case is a simple example of a fir filter. It contains all of the classes required to build and test
 * this module. The code containing the actual operation of a fully parameterizable filter is only about 10 lines of
 * code.
 *
 * The design files can be found at       XXXX/fir/design/XXXX
 * The test files can be found at         XXXX/fir/test/XXXX
 * The html documentation can be found at XXXX/fir/doc/XXXX
 *
 */

object ComplexTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object ComplexProject extends Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "complex"
    // Definition of a clock for the module
    implicit val clk         = ClockControl("clk","reset")
    // Number of Taps for the filter
    val len = 3
    // Input-Output Signal Definition ---- S(8,6) means a signed number with 8 total bits and 6 fractional
    val x              = signal("signal_in",INPUT,S(8,6))
    val z              = signal("signal_out",OUTPUT,S(8,6))
    // Definition of taps which are an array of signals of the length defined in len
    val num           = array("num",INPUT,S(8,6))(len)
    val den           = array("den",INPUT,S(8,6))(len)

    // Internal Filter Width
    val iW = S(12,8)
    // Creation of the FIR filter entity
    val iir = new ComplexEntity("complex")
    // Root Module Definition
    override val newRoot = iir.createEntity
    // Defines the Tests for this project
    //override val tests    = List(Test(new TestCase(iir).createEntity))
    // Selects the simulator for this module - ISIM for this case
    //override val testType = Some(new Isim(this))

  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class ComplexEntity(override val name:String)(implicit val clk:ClockControl) extends EntityParser with ComplexParser {

    this.signal(clk.allSignals(INPUT))


    val gain1 = signal("gain_1",WIRE,S(8,6))
    val in1 = complex("signal_in_1",INPUT,S(8,6))
    val in2 = complex("signal_in_2",INPUT,S(8,6))

    val add            = complex("adder_out",WIRE,S(8,4))
    val multiplier_out = complex("multiplier_out",WIRE,S(8,4))

    // add  := RC(in1 + in2,S(8,6))
    multiplier_out := gain1 * in1
    

  }


  class TestCase(val entity:ComplexEntity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity.createEntity
    /*


    val num = List(.3,.58,.3)
    val den = List(0.0,.17,0.0)

    num.zipWithIndex.foreach(x => init(entity.a(x._2), x._1))
    den.zipWithIndex.foreach(x => init(entity.b(x._2), x._1))

    /- ("Counter to Index Test")
    val counter = signal("counter",REG,U(32,0))
    counter := (counter + 1) $at (clk)

    /- ("LUT")
    this.assign(Lut(entity.x,counter,List.tabulate(128)(x => C(8,x))))

    /- ("Output Comparison")
    this.assign(CompareTable(entity.z,counter,List.tabulate(128)(x => C(8,x)),0,128))

    $always_clk(clk) (
      $iff (counter == 128) $then $finish
    )
    */
  }





  def main(args:Array[String]) = {
    ComplexTest.ComplexProject.createProject
    //ComplexTest.IirProject.runTests(new Isim(IirTest.IirProject))
  }
}


