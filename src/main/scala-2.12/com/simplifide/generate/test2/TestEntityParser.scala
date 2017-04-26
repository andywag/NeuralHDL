package com.simplifide.generate.test2

import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{NewEntityInstance, NewEntity}
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.BasicSegments
import com.simplifide.generate.blocks.test.{BasicBlocks, ClockGenerator}
import com.simplifide.generate.parser.items.InitialParser

/**
 * Entity which defines the parsing operations for creating the test
 */

trait TestEntityParser extends EntityParser with TestParser with InitialParser {
  
  /** Clock for the testbench*/
  implicit val clk:ClockControl
  /** Design Under Test */
  val dut:NewEntity

  def createTestBody {
    val internalSignals = dut.signals.filter(_.isIo).map(_.changeTestType)
    head(new BasicBlocks.TimeScale("10ns","1ns"))
    // Create the signals associated with this entity 
    signal(internalSignals)
    // Reset the Clock
    assign(this.init(clk))
    // Create the Clock
    /- ("Clock Generation Circuits")
    this.assign(ClockGenerator(clk))
    // Create Initial Statement
    /- ("Initial Statement")
    this.assign(this.createInitial)
    // Attach the instance
    instance(NewEntityInstance(dut))
  }

  override def createEntity:NewEntity  ={
    this.createTestBody
    super.createEntity
  }
  
}

