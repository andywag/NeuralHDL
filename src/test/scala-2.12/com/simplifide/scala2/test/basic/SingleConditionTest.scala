package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{ Project}
import com.simplifide.generate.parser.EntityParser


/**
 * This test case is a simple state machine which shows the simplified syntax which can be used for a state machine
 * construction. For the case of the state machine additional documentation is created in the dot directory which
 * contains a .dot file which can be used for a diagram as well as some html descriptions.
 *
 * The design files can be found at       XXXX/state/design/XXXX
 * The test files can be found at         XXXX/state/test/XXXX
 * The html documentation can be found at XXXX/state/doc/XXXX
 *
 */

class SingleConditionTest {

}

object SingleConditionTest {

  /** ProjectGenerator which contains a simple state machine example */
  class StateMachineProject extends Project {
    // Set the Base Location for the ProjectGenerator
    val location:String = TestConstants.locationPrefix + "condition"
    // Create the Clock
    implicit val clk = ClockControl("clk","reset")

    override val newRoot = new StateMachineEntity().createEntity
    // Defines the Tests for this project
    //override val tests    = List(Test(new TestCase(root)))
    // Selects the simulator for this module - ISIM for this case
    //override val testType = Some(new Isim(this))
  }

  //** Entity which contains the state
  class StateMachineEntity()(implicit clk:ClockControl) extends EntityParser {

    override val name = "condition"
    // Creation of the Input and Output Signals for the test
    val condition = signal("condition",INPUT,U(3,0))
    val result    = signal("state",REGOUT,U(3,0))
    
    signal(clk.allSignals(INPUT))
    // Adding the Input and Output Signals to the module
    // override val entitySignals = clk.allSignals(INPUT) ::: List(condition,result)
    // Module Creation
    val alpha    = array("ttt")(5)

    val counter  = signal("counter",REG,S(5,0))
    
    val wrEnable = signal("wrEnable",REG)
    val temp     = array("temp")(2)

    val tt = G(wrEnable,temp)

    multiLineComment("Simple Counter")
    counter := $iff (counter == 5) $then 0 $else (counter + 3) $at (clk)

    multiLineComment("Nested Conditional Statement")
    temp := (
      $iff (temp) $then (
        $iff (temp) $then (
          $iff (temp) $then (
            temp
            )
          )
          $else  (temp)
        )
        $else_if (temp) $then {temp}
    )
    multiLineComment("Condition Statement inside body")
    $always_body(
      $iff (temp) $then (
        wrEnable ::= wrEnable
      )
    )
    multiLineComment("Case Statement Test")
    temp := wrEnable $match (
      $cases(C(1,0))     $then  (
        $iff (wrEnable) $then (temp)
      )
      $cases(C(1,1))    $then (temp)
    ) $at (clk)


  }



  def main(args:Array[String]) = {
    new StateMachineProject().createProject
  }
}