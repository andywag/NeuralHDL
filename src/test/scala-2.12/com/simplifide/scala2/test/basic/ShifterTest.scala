
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
import com.simplifide.scala2.test.basic.ShifterTest.Entity


class ShifterTest {}

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

object ShifterTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "shifter"
    implicit val clk         = ClockControl("clk","reset")

    /** Base Entity */
    val entity = new Entity()
    // Root Module Definition
    override val newRoot = entity.createEntity
    
  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class Entity()(implicit val clk:ClockControl) extends EntityParser {

    override val name = "shifter"
    this.signal(clk.allSignals(INPUT))
    
    val output = signal("signalOut",REGOUT,S(8,6))
    val input  = signal("signalIn",INPUT,S(8,6))
    val gain   = signal("gain",INPUT,S(2,0))
    
    output := $round($shift(input,gain),S(9,7))




  }
  

  def main(args:Array[String]) = {
    Project.createProject
  }
}


