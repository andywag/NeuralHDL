package com.simplifide.scala2.test.language





/*
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.model.Expression
import math._
import com.simplifide.generate.parameter.{Parameter, ModuleScope}
import com.simplifide.generate.TestConstants
import com.simplifide.generate.test.Test._
import com.simplifide.generate.test.{Test, Isim, TestModule}
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter}
import com.simplifide.generate.project.{Entity, ProjectGenerator, Module}
import com.simplifide.generate.signalproc.Filter


class FirTest {}

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

object FirTest {

    /** Simple ProjectGenerator which only contains an FIR filter */
  object FirProject extends ProjectGenerator {
    // Location where the project outputs are going to be stored
    val fileLocation:String = TestConstants.locationPrefix + "outputs" + TestConstants.separator + "fir"
    // Definition of a clock for the module
    implicit val clk         = ClockControl("clk","reset")
    // Number of Taps for the filter
    val len = 4
    // Input-Output Signal Definition ---- S(8,6) means a signed number with 8 total bits and 6 fractional
    val x              = signal("signal_in1",INPUT,S(8,6))
    val z              = signal("signal_out",OUTPUT,S(8,6))
    // Definition of taps which are an array of signals of the length defined in len
    val taps           = array("taps",INPUT,S(8,6))(len)
    // Internal Filter Width
    val iW = S(12,8)
    // Creation of the FIR filter entity
    val iir = new FirEntity("fir",x,z,taps,iW)
    // Root Module Definition
    override val root = iir
    // Defines the Tests for this project
    override val tests    = List(Test(new TestCase(root)))
    // Selects the simulator for this module - ISIM for this case
    override val testType = Some(new Isim(this))
  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class FirEntity(name:String,
                  val input:SignalTrait,
                  val output:SignalTrait,
                  val taps:ArrayTrait[SignalTrait],
                  val iW:FixedType)(implicit clk:ClockControl) extends Entity.Root(name)(clk) {

    // Defines the I-O signals for the module
    override val entitySignals =  clk.allSignals(INPUT) ::: List(input,output,taps)
    // Function which defines the module included in this entity
    override val createModule = new Fir(this,iW).createModule
  }

  /** FunctionBody of the FIR Filter */
  class Fir(val entity:FirEntity,
          val iW:FixedType)(implicit val clk:ClockControl) extends Module(entity.name) {

  // Imports all of the signals from the entity
  import entity._
  // Assigns the clock to this module
  this.assignClock(clk)
  // Sets a description for this module. This is used in both the generated code as well as the generated html
  // The description like all comments can be either html or plain text
  description = Some(<p>This module is a generic FIR filter ...</p>)
  // Number of Taps - Length of Filter
  val length    = taps.length
  // Number of Rows in the Adder Tree
  val logLength = ceil(log(length)/log(2.0)).toInt + 1
  // Length of Each Row in the Adder Tree
  val rowLengths = List.tabulate(logLength)(i => math.ceil(length/pow(2.0,i)).toInt)
  // Create the Delay Line for the filter
  val delayLine       = register(input)(taps.length)             --: "Registers for the Delay Line with the Delay Line as Well"
  // Create the multiplier output
  val multiplierOut   = array("mult_out",WIRE,iW)(taps.length)   --: "Output of the Multiplier"
  // Create the output for each row of the filter
  val adderRow   = List.tabulate(logLength)(i => array("mult_row_" + i,WIRE,iW)(rowLengths(i)) --: ("Adder Row " + i)) // List of Rows inside the adder tree

  /- ("Initial Tap Multiplication")          // Adds comment to the generated code
  multiplierOut := RC(delayLine * taps)      // Initial Tap Multiplication Section
  // Adder Tree
  // Simple For Loop which generates the code for the filter.
  // All of the real logic for this filter is included in this 10 lines of code
  for (i <- 1 until logLength) {
    /- ("Adder Tree Stage " + (i-1))
    val treeInput = if (i == 1) multiplierOut else adderRow(i-1)
    for (j <- 0 until rowLengths(i)) {
      if (2*j+1 >= adderRow(i-1).length) adderRow(i)(j) := treeInput(2*j)
      else                               adderRow(i)(j) := RC(treeInput(2*j) + treeInput(2*j+1))
    }
  }
  /- ("Output Stage")
  output := RC(adderRow(logLength-1)(0))

  }


  /** Test Case for the filter */
  class TestCase(val entity:FirEntity)(implicit clk:ClockControl) extends TestModule("test_cordic",entity) {
    // Defines a set of taps for the filter test case
    val taps = List(-.5,1.0,-.5,.25)
    // Creates a set of inputs to test the filter - For this case a simple sine wave
    val inputs:List[Double] = List.tabulate(length + 3)(x => math.sin(x.toDouble/64.0))
    // Set of Expected Outputs for the filter
    // Filter is defined elsewhere but is a somewhat trivial function in scala
    val outputs = Filter.fir(taps,inputs).map(x => Constant(x,entity.output.fixed))
    // Assigns the taps to the proper values for the simulation
    // -->, --< Notation describes setting values and testing outputs
    taps.zipWithIndex.foreach(x => entity.taps(x._2) --> x._1)  // Assign the Taps for the Module
    entity.input           --> (inputs.map(Constant(_)))        // Assign the System Input
    entity.output          <-- (outputs,3,length+3,4)           // Check the System Output

    this.createTest
  }

  def createProject = FirProject.createProject2

  def main(args:Array[String]) = {
    this.createProject
  }

}

*/


