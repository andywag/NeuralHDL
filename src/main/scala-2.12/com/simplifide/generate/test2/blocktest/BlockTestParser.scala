package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.TestEntityParser

/**
  * Created by andy on 5/3/17.
  */
trait BlockTestParser extends TestEntityParser{
  /** Clock for the testbench */
  def blockName:String
  override val name = "test" + blockName.capitalize

  override implicit val clk: ClockControl = ClockControl("clk","reset")

  val dutParser:EntityParser
  val testLength:Int


  /- ("Counter to Index Test")
  val counter = signal("counter",REG,U(32,0))
  counter := (counter + 1) $at (clk)
  /- ("Stop the test when the data runs out")
  $always_clk(clk) (
    $iff (counter == testLength) $then (
      $finish
      )
  )


  def create = {
    new BlockProject(this).createProject
  }

}
