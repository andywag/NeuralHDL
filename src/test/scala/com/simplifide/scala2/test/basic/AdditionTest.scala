
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
import com.simplifide.generate.project.Project


class AdditionTest {}

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

object AdditionTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object FirProject extends Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "fir"
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
    val iir = new AdditionEntity("fir",x,z,taps,iW)
    // Root Module Definition
    override val newRoot = iir.createEntity
    // Defines the Tests for this project
    //override val tests    = List(Test(new TestCase(root)))
    // Selects the simulator for this module - ISIM for this case
    //override val testType = Some(new Isim(this))
  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class AdditionEntity(override val name:String,
                       val input:SignalTrait,
                       val output:SignalTrait,
                       val taps:ArrayTrait[SignalTrait],
                       val iW:FixedType)(implicit val clk:ClockControl) extends EntityParser {


    // Defines the I-O signals for the module
    //override val entitySignals =  clk.allSignals(INPUT) ::: List(input,output,taps)
    // Function which defines the module included in this entity
    //override val createModule = new Fir(this,iW).createModule
    this.signal(clk.allSignals(INPUT))
    this.signal(input)
    this.signal(output)
    this.signal(taps)

    val delayLine       = register(input)(taps.length)
    //val alpha           = array("alpha",WIRE,S(8,6))(2)
    //val beta            = array("beta",WIRE,S(10,6))(2)

    val singleInput  = signal("singleIn",WIRE,S(4,2))

    val outWidth = 6
    val roundOutput = List.tabulate(outWidth+1)(x => signal("roundOut_"+x,WIRE,S(outWidth,x)))
    val roundClipOutput = List.tabulate(outWidth+1)(x => signal("roundClipOut_"+x,WIRE,S(outWidth,x)))
    val roundClipBoth = signal("roundClipBoth",WIRE,S(2,1))
    
    val adderOutput = List.tabulate(outWidth)(x => signal("adderOut_"+x,WIRE,S(outWidth,x)))
    val roundAdderOutput = List.tabulate(outWidth)(x => signal("roundAdderOut_"+x,WIRE,S(outWidth,x)))
    val roundClipAdderOutput = List.tabulate(outWidth)(x => signal("roundClipAdderOut_"+x,WIRE,S(outWidth,x)))


    val multiplyOutput = List.tabulate(outWidth)(x => signal("multOut_"+x,WIRE,S(outWidth,x)))
    val roundMultiplyOutput = List.tabulate(outWidth)(x => signal("roundMultOut_"+x,WIRE,S(outWidth,x)))
    val roundClipMultiplyOutput = List.tabulate(outWidth)(x => signal("roundClipMultOut_"+x,WIRE,S(outWidth,x)))



    /*
    multiLineComment("Round Test")
    List.tabulate(outWidth+1)(x => {
      /- ("Round " + x + "(" + roundOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundOutput(x) := R(singleInput,S(8,4))
      }
    )

    multiLineComment("Round Clip Test")
    List.tabulate(outWidth+1)(x => {
      /- ("Round-Clip " + x + "(" + roundOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundOutput(x) := RC(singleInput,S(8,4))
      }
    )
    multiLineComment("Round Clip Both")
    roundClipBoth := RC (singleInput,S(8,4))


    multiLineComment("Adder Test")
    List.tabulate(outWidth)(x => {
      /- ("Round-Clip " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      adderOutput(x) := singleInput + singleInput
      }
    )

    multiLineComment("Round Adder Test")
    List.tabulate(outWidth)(x => {
      /- ("Round " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundAdderOutput(x) := R(singleInput + singleInput,S(8,4))
      }
    )

    multiLineComment("Round Clip Adder Test")
    List.tabulate(outWidth)(x => {
      /- ("Round-Clip " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundClipAdderOutput(x) := RC(singleInput + singleInput,S(8,4))
      }
    )
    */

    multiLineComment("Multiplier Test")
    List.tabulate(outWidth)(x => {
      /- ("Round " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      this.multiplyOutput(x) := R(singleInput * singleInput,S(8,4))
    }
    )

    multiLineComment("Round Clip Multiplier Test")
    List.tabulate(outWidth)(x => {
      /- ("Round " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundMultiplyOutput(x) := RC(singleInput * singleInput,S(8,4))
    }
    )


    /*
    multiLineComment("Round Clip Multiplier Test")
    List.tabulate(outWidth)(x => {
      /- ("Round-Clip " + x + "(" + adderOutput(x).fixed + "---" + singleInput.fixed + ")")
      roundClipMultiplyOutput(x) := R(singleInput * singleInput,S(8,4))
    }
    )
    */
    // roundAdderOutput(0) := (singleInput & (singleInput | singleInput))


  }
  /*
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
  //multiplierOut := RC(delayLine * taps)      // Initial Tap Multiplication Section
  //multiplierOut := delayLine * delayLine      // Initial Tap Multiplication Section
  multiplierOut := delayLine

  /*
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
   */
  }
  */

  /** Test Case for the filter */
  /*
  class TestCase(val entity:AdditionEntity)(implicit clk:ClockControl) extends TestModule("test_cordic",entity) {
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
  */
  def createProject = FirProject.createProject

  def main(args:Array[String]) = {
    this.createProject
  }
}


