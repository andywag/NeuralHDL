package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.{FunctionParser, EntityParser}
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.{Isim, Test, TestEntityParser}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 2/28/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */

class FunctionTest {}

object FunctionTest {
  
  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "functions"
    // Definition of a clock for the module
    implicit val clk         = ClockControl("clk","reset")
   
    // Root Module Definition
    override val newRoot = new Entity().createEntity
    // Defines the Tests for this project
    override val tests    = List(Test(new TestCase(newRoot).createEntity))
  }
  
  class Entity(implicit val clk:ClockControl) extends EntityParser {
    override val name = "functions"
    signal(clk.allSignals(INPUT))
    
    function(Function1)
    
  }

  object Function1 extends FunctionParser.Function("function1") {

    val input  = signal("datain",INPUT)

    this.output := $iff (input) $then input $else input

  }

  class TestCase(val entity:NewEntity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity
  }
  
  
  def main(args:Array[String]) = {
    Project.createProject
    Project.runTests(new Isim(Project))
  }
}