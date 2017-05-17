package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{ Project}
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.proc2.parser.RegisterParser
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.blocks.proc2.{ProcessorBus, HtmlDescription}
import com.simplifide.generate.parser.EntityParser

/**
 * Test of Processor Interface Generator
 */

class ProcessorInterfaceTest extends Project {

  val location:String = TestConstants.locationPrefix + "proc"
  // Create the Clock
  implicit val clk = ClockControl("clk","reset")
  // Main Module for the Design
  override val newRoot = new ProcessorInterfaceTest.ProcessorEntity().createEntity

}

object ProcessorInterfaceTest {

  class ProcessorEntity()(implicit val clk:ClockControl) extends EntityParser with RegisterParser {

    override val name = "processor"

    implicit val processorBus = ProcessorBus(clk,
      SignalTrait("wrAddress",INPUT,U(3,0)),
      SignalTrait("wrValid",INPUT,U(1,0)),
      SignalTrait("wrData",INPUT,U(32,0)),
      SignalTrait("rdAddress",INPUT,U(3,0)),
      SignalTrait("rdValid",INPUT,U(1,0)),
      SignalTrait("rdData",REGOUT,U(32,0)))

    signal(processorBus.signals)

    registerGroup(8) (
      address("DEFAULT",0) registers (
        readWrite("i",5) start (0) default("25")
        readWrite("J",1) start (6)
      ) comment ("Basic Register")
      address("INITIAL",1) registers (
        readWrite("k",1) start (0)
        readWrite("l",1) start (1)
      ) comment("Initial Register")
      address("BASE",2) registers (
        readWrite("lll",44) start (0)
      ) comment("First Basic Register")
      address("BASE2",8) registers  (
        readWrite("mmm",45) start (0)
      ) comment("Second Basic Register")

    )

    signal(this.registerMap.parameters)
    /- ("Read Decoder")
    assign(readDecoder)
    /- ("Write Decoder")
    assign(writeDecoder)
    
    signal(this.registerMap.signals)

    //file(new HtmlDescription("registers.html",this.registerMap))

  }

  def main(args:Array[String]) = {
    new ProcessorInterfaceTest().createProject
  }

}