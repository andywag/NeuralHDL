
package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{Project}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.scala2.test.basic.OperatorTest.OperatorEntity
import com.simplifide.generate.signal.old.BusType
import com.simplifide.generate.signal.{BusType, FixedType, ArrayTrait, SignalTrait}


class OperatorTest {}

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

object OperatorTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object FirProject extends Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "operator"
    // Definition of a clock for the module
    implicit val clk         = ClockControl("clk","reset")
    // Number of Taps for the filter

    val iir = new OperatorEntity()


    override val newRoot = iir.createEntity
    // Defines the Tests for this project
    //override val tests    = List(Test(new TestCase(root)))
    // Selects the simulator for this module - ISIM for this case
    //override val testType = Some(new Isim(this))
  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class OperatorEntity()(implicit clk:ClockControl) extends EntityParser {
    override val name = "operator"
    this.signal(clk.allSignals(INPUT))

    val a = array("a")(8)
    val ar = array("ar",REG)(8)
    val b = signal("b")
    val c = signal("c")
    
    multiLineComment("Simple Operation Statements")
    a(0)   := (b & b | (b & c))
    multiLineComment("Simple Operation Statements With Flop")
    ar(0)  := (b | b) $at (clk)
    multiLineComment("Question Mark Operation")
    a(1)   := c ? a :: b ? c :: a
    multiLineComment("Question Mark Operation with Flop")
    ar(1)  := (c ? a :: b ? c :: a) $at clk

    val aa  = array("aa")(2,2)
    val aar = array("aa",REG)(2,2)
    val bb  = array("bb")(2)

    multiLineComment("Simple Operation Statements")
    aa(0) := (bb | ~bb)
    multiLineComment("Question Statement")
    aa(1) := bb ? bb :: bb
    multiLineComment("Simple Operation Statements with Register")
    aar(0) := (bb | ~bb) $at (clk)
    multiLineComment("Question Statement with Register")
    aar(1) := bb ? bb :: bb  $at (clk)

    /*
    val testBus = BusType(SignalTrait("a"),SignalTrait("b"))
    val busA = busArray("busA",testBus)(2)
    val busB = bus("busB",testBus)
    multiLineComment("Simple Bus Assignment")
    busA(0) := busB | busB
    */

    
    



  }

  def main(args:Array[String]) = {
    OperatorTest.FirProject.createProject
  }

  /** Test Case for the filter */
  /*
  class TestCase(val entity:OperatorEntity)(implicit clk:ClockControl) extends TestModule("test_cordic",entity) {
    // Defines a set of taps for the filter test case
    val taps = List(-.5,1.0,-.5,.25)
    // Creates a set of inputs to test the filter - For this case a simple sine wave
    val inputs:List[Double] = List.tabulate(length + 3)(x => math.sin(x.toDouble/64.0))
    // Set of Expected Outputs for the filter
    // Filter is defined elsewhere but is a somewhat trivial function in scala
    // Assigns the taps to the proper values for the simulation
    // -->, --< Notation describes setting values and testing outputs
  }

  def createProject = FirProject.createProject


  */
}


