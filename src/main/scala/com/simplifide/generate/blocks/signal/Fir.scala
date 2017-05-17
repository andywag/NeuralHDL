package com.simplifide.generate.blocks.signal

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._

import math._
import com.simplifide.generate.signal.{RegisterTrait, FixedType, ArrayTrait, SignalTrait}

/**
 * FIR Filter
 *
 * @constructor
 * @parameter input Input to the Filter
 * @parameter output Output of the Fitler
 * @parameter taps Taps for the filter
 * @parameter iW Internal Fixed Type of the Filter
 *
 */

class Fir(val input:SignalTrait,
          val output:SignalTrait,
          val taps:ArrayTrait[SignalTrait],
          val iW:FixedType)(implicit val clk:ClockControl) extends ComplexSegment {

  // Number of Taps - Length of Filter
  val length    = taps.length
  // Number of Rows in the Adder Tree
  val logLength = ceil(log(length)/log(2.0)).toInt + 1
  // Length of Each Row in the Adder Tree
  val rowLengths = List.tabulate(logLength)(i => math.ceil(length/pow(2.0,i)).toInt)
  // Create the Delay Line for the filter
  val delayLine     = register(input)(taps.length)             --: "Registers for the Delay Line with the Delay Line as Well"
  // Create the multiplier output
  val multiplierOut   = array("mult_out",WIRE,iW)(taps.length)   //-- "Output of the Multiplier"
  // Create the output for each row of the filter
  val adderRow   = List.tabulate(logLength)(i => array("mult_row_" + i,WIRE,iW)(rowLengths(i))) // List of Rows inside the adder tree


  def createBody {
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

}