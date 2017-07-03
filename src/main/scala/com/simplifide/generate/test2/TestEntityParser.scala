package com.simplifide.generate.test2

import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.{NewEntity, NewEntityInstance, Project}
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



  def dataLocation:String = ""

  /** Design Under Test */
  val dut:NewEntity
  val index = signal("counter",REG,U(32,0))

  def checkMatch(signal:List[SignalTrait], input:SignalTrait) = {
    signal.map(_.name).contains(input.name)
  }

  def createTestBody {
    val internalSignals1 = dut.signals.filter(_.isIo).map(_.changeTestType)
    val internalSignals  = internalSignals1.filterNot(x => checkMatch(clk.allSignals(INPUT),x))

    head(new BasicBlocks.TimeScale("10ns","1ns"))

    //appendSignals(clk.allSignals(INPUT))

    /- ("Counter to Index Test")

    index := (index + 1) $at (clk)

    // Create the signals associated with this entity 
    signal(internalSignals)

    /- ("Initial Statement")
    this.assign(this.createInitial)
    /-("DUT")
    instance(NewEntityInstance(dut))
  }

  override def createEntity:NewEntity  ={

    this.createTestBody
    val result = super.createEntity.connect
    val newSignals = result.signals.map(_.changeTestType)
    val temp = result.newEntity(signals = newSignals)
    temp

  }
  
}

