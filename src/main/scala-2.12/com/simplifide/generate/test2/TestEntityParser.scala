package com.simplifide.generate.test2

import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{NewEntity, NewEntityInstance}
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.BasicSegments
import com.simplifide.generate.blocks.test.{BasicBlocks, ClockGenerator}
import com.simplifide.generate.parser.items.InitialParser
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.test2.data.DataGenParser

/**
 * Entity which defines the parsing operations for creating the test
 */

trait TestEntityParser extends EntityParser with TestParser with InitialParser with DataGenParser{
  
  /** Clock for the testbench*/
  implicit val clk:ClockControl
  implicit val parser:TestEntityParser = this

  /** Design Under Test */
  val dut:NewEntity

  def checkMatch(signal:List[SignalTrait], input:SignalTrait) = {
    signal.map(_.name).contains(input.name)
  }

  def createTestBody {
    val internalSignals1 = dut.signals.filter(_.isIo).map(_.changeTestType)
    val internalSignals  = internalSignals1.filterNot(x => checkMatch(clk.allSignals(INPUT),x))

    head(new BasicBlocks.TimeScale("10ns","1ns"))

    appendSignals(clk.allSignals(INPUT))

    // Create the signals associated with this entity 
    signal(internalSignals)
    // Reset the Clock
    //assign(this.init(clk))
    // Create the Clock
    ///- ("Clock Generation Circuits")
    //this.assign(ClockGenerator(clk))
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

