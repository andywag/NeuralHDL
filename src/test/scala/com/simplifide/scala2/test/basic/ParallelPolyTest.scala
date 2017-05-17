
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
import com.simplifide.generate.parser.EntityParser
import com.simplifide.scala2.test.basic.ShifterTest.Entity
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.blocks.basic.fixed.{Roundable, ClipSegment, FixedOperations}
import com.simplifide.scala2.test.basic.ParallelPolyTest.FirEntity
import com.simplifide.generate.blocks.signal.AdderTree
import com.simplifide.generate.test2.{Isim, Test, TestEntityParser}
import com.simplifide.generate.project.{NewEntity, Connection, Project}


class ParallelPolyTest {}

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

object ParallelPolyTest {

  def createTaps(x:Int, y:Int):SimpleSegment = NewConstant(x*y % 256 - 128)
  
  /** Simple ProjectGenerator which only contains an FIR filter */
  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "poly"
    implicit val clk         = ClockControl("clk","reset")

    val parallel = 64 // Number of Parallel Inputs

    val input  = array("datain",INPUT,S(8,6))(parallel)    // Filter Input
    val output = array("dataout",OUTPUT,S(8,6))(parallel)  // Filter Output
    val index  = array("index",INPUT,U(7,0))(parallel)     // Index of Filter
    
    /** Base Entity */
    val entity = new Entity(output,input,index, createTaps) // Top Level Module Creation
    // Root Module Definition
    override val newRoot = entity.createEntity  // Top Level Module

    override val tests    = List(Test(new TestCase(newRoot).createEntity)) // Test Case

  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class Entity(
    val output:ArrayTrait[SignalTrait],
    val input:ArrayTrait[SignalTrait],
    val index:ArrayTrait[SignalTrait],
    val tapCreator:(Int, Int) =>SimpleSegment)(implicit val clk:ClockControl) extends EntityParser {

    val filterLength  = 8
    val tapWidth      = S(8,6)
    val internalWidth = S(12,8)
    
    override val name = "poly"
    this.signal(clk.allSignals(INPUT))
    this.signal(List(output,input,index))

    // Create the Template Filter Module - With Standard Indexes
    val firTemplate   = new FirEntity(SignalTrait("dataout",OUTPUT,output.fixed),
      ArrayTrait(SignalTrait("datain",INPUT,input.fixed),filterLength),
      SignalTrait("index",INPUT,index.fixed),
      tapCreator,
      tapWidth,
      internalWidth).createEntity

    // Filter Instantiation
    // TODO The indexing is incorrect
    // TODO The real block requires a delayed version of input for calculating the initial outputs
    for (i <- 0 until output.length - filterLength) {
      val dataInMap = List.tabulate(input.length)(x => ("datain_"+x,"datain_"+(x+i))).toMap
      val totalMap = dataInMap ++  Map("dataout" -> ("dataout_" + i),"index"   -> ("index_"   + i))
      val connection = new Connection.MapConnection(totalMap)
      this.instance(firTemplate,"filter_" + i,connection)
    }
  }
  
  // Parallel Filter Module
  class FirEntity(val output:SignalTrait,
    val input:ArrayTrait[SignalTrait],
    val index:SignalTrait,
    val tapCreator:(Int, Int) =>SimpleSegment,
    val tapWidth:FixedType,
    val internalWidth:FixedType) (implicit val clk:ClockControl) extends EntityParser {
      
    override val name = "filter"
    val groupSize = 4

    signal(clk.allSignals(INPUT))
    signal(List(input,index,output))

    val taps       = array("taps",REG,tapWidth)(input.length)
    val multiplier = array("multiplier_output",WIRE,internalWidth)(input.length)
    val multiplierR = array("multiplier_outputR",REG,internalWidth)(input.length)

    // Tap Assignment which is based on the function tapCreator defined on top of module
    // This block will create  a set of LUTs
    for (i <- 0 until taps.length) {
      val total = math.pow(2.0,index.width).toInt
      /- ("Tap " + i + " Assignment")
      this.assign(new Lut(taps(i),index,List.tabulate(total)(x => tapCreator(i,x))))
    }
    // Multiplication and Delay
    /- ("Tap Multiplication")
    multiplier  := $round_generic(input*taps,internalWidth,Roundable.RoundClip)
    multiplierR := multiplier $at clk
    // Adder Tree
    /- ("Adder Tree")
    this.assign(AdderTree(output,multiplierR,4))

  }

  // Test Case only used to run ISIM and check syntax
  class TestCase(val entity:NewEntity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity
  }
  


  def main(args:Array[String]) = {
    Project.createProject
    Project.runTests(new Isim(Project))
  }
}


