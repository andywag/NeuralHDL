
package com.simplifide.scala2.test.basic

import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.model.Expression

import math._
import com.simplifide.generate.parameter.{ModuleScope, Parameter}
import com.simplifide.generate.TestConstants
import com.simplifide.generate.generator.{CodeWriter, SimpleSegment}
import com.simplifide.generate.signalproc.Filter
import com.simplifide.generate.blocks.basic.fixed.{ClipSegment, FixedOperations}
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.Project
import com.simplifide.scala2.test.basic.ShifterTest.Entity
import com.simplifide.generate.blocks.signal.FirStructure2
import com.simplifide.generate.blocks.test.CompareTable
import com.simplifide.generate.test2.verilator.RunVerilator
import com.simplifide.generate.test2.{Test, TestEntityParser}
import com.simplifide.scala2.test.basic.IirTest.{IirEntity, TestCase}
import com.simplifide.scala2.test.basic.IirTest.IirProject.iir


class FirTest {}



object FirTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "fir"
    implicit val clk         = ClockControl("clk","reset")

    /** Base Entity */
    val entity = new Entity()
    // Root Module Definition
    override val newRoot = entity.createEntity
    val testCase = new TestCase(entity)
    override val tests    = List(Test(testCase.createEntity))

  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class Entity()(implicit val clk:ClockControl) extends EntityParser {

    override val name = "shifter"
    this.signal(clk.allSignals(INPUT))
    
    val input  = signal("signalIn",INPUT,S(8,6))
    val output = signal("signalOut",REGOUT,S(8,6))

    val taps   = array("taps",INPUT,S(8,6))(4)
    
    val internalWidth = S(12,8)
    
    this.assign(FirStructure2(output,input,taps,internalWidth))

  }

  class TestCase(val entity:Entity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity.createEntity

    /- ("Counter to Index Test")
    val counter = signal("counter",REG,U(32,0))
    counter := (counter + 1) $at (clk)



    $always_clk(clk) (
      $iff (counter == 128) $then (
        $display("Test Finished"),
        $finish
        )
    )
  }



  def main(args:Array[String]) = {
    Project.createProject
  }
}

object RunFir {
  def main(args: Array[String]): Unit = {
    //val run = new RunVerilator(FirTest.Project.tests(0),FirTest.Project.testCase.createEntity,FirTest.Project)
    //run.runAll
  }
}


