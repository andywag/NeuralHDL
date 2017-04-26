
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
import com.simplifide.generate.test2.{Isim, Test, TestEntityParser}


class IirTest {}

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

object IirTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object IirProject extends Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "iir"
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
    val iir = new IirEntity("iir",num,den,x,z,iW)
    // Root Module Definition
    override val newRoot = iir.createEntity
    // Defines the Tests for this project
    override val tests    = List(Test(new TestCase(iir).createEntity))
    // Selects the simulator for this module - ISIM for this case
    //override val testType = Some(new Isim(this))

  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class IirEntity(override val name:String,
                       val a:ArrayTrait[SignalTrait],
                       val b:ArrayTrait[SignalTrait],
                       val x:SignalTrait,
                       val z:SignalTrait,
                       val iW:FixedType)(implicit val clk:ClockControl) extends EntityParser {


    // Defines the I-O signals for the module
    //override val entitySignals =  clk.allSignals(INPUT) ::: List(input,output,taps)
    // Function which defines the module included in this entity
    //override val createModule = new Fir(this,iW).createModule
    this.signal(clk.allSignals(INPUT))
    this.signal(a,b,x,z)


    val n = clk
    val len = a.numberOfChildren  // Number of Taps for the filter
    // Internal Signal Declarations
    val internal_multiplier = array("internal_mult",WIRE,iW)(len)

    val internal     = signal("internal_sig",WIRE,iW)
    val internal_out = signal("internal_out",WIRE,z.fixed)
    val y            = register(internal)(len-1)
    // Feedforward taps

    // Feedback Taps
    y(n) := x(n) + List.tabulate(3)(i => R(a(i)*y(n-i-1))).reduceLeft[Expression](_+_)
    // Feed-forward Taps
    internal_out(n) := List.tabulate(len)(i => R(b(i)*y(n-i-1))).reduceLeft[Expression](_+_)
    // Output Statement
    z := internal_out

  }


  class TestCase(val entity:IirEntity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity.createEntity

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
  }


  /** Test Case for the Filter */
  /*class TestCase(val entity:IirEntity)(implicit clk:ClockControl) extends TestModule("test_iir",entity) {
    // Tap Values for numerator and denominator
    val num = List(.3,.58,.3)
    val den = List(0.0,.17,0.0)
    // Input values currently a sine wave
    val inputs:List[Double] = List.tabulate(length + 3)(x => math.sin(x.toDouble/64.0))
    val outputs = inputs.map(x => Constant(x,entity.z.fixed))
    // Test ProcStatement to the numerator and the denominator
    num.zipWithIndex.foreach(x => entity.a(x._2) --> x._1)
    den.zipWithIndex.foreach(x => entity.b(x._2) --> x._1)
    entity.x           --> (inputs.map(Constant(_)))        // Assign the System Input
    entity.z           <-- (outputs,3,length+3,4)           // Check the System Output

    this.createTest
  }*/


  def main(args:Array[String]) = {
    IirTest.IirProject.createProject
    IirTest.IirProject.runTests(new Isim(IirTest.IirProject))
  }
}


