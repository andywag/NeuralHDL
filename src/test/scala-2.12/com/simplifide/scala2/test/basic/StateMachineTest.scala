package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.project.{ Project}

import com.simplifide.generate.blocks.statemachine2.parser.StateMachineParser
import com.simplifide.generate.blocks.statemachine2.StateMachine
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.scala2.test.basic.StateMachineTest.StateMachineBlock
import com.simplifide.generate.parser.EntityParser

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 1/19/12
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */

class StateMachineTest extends Project {

  val location:String = TestConstants.locationPrefix + "proc"
  // Create the Clock
  implicit val clk = ClockControl("clk","reset")
  // Main Module for the Design
  override val newRoot = new StateMachineTest.StateMachineEntity().createEntity
}

object StateMachineTest {

  class StateMachineEntity()(implicit clk:ClockControl) extends EntityParser with StateMachineParser {
    override val name = "statemachine"
    this.assign(new StateMachineBlock)
  }
  
  class StateMachineBlock (implicit clk:ClockControl) extends ComplexSegment with StateMachineParser {
    def createBody {
      val x = signal("test")
      val current = signal("state",WIRE,unsigned(4,0))
      //val next    = signal("next",REG,unsigned(4,0))

      val stateA = state("STATEA",0x1)
      val stateB = state("STATEB",0x2)

      transition (
        stateA to stateB when (x == 10)
               to stateB when (x == 5),
        stateB to stateA when (x == 0x15)
      )

      this.assign(StateMachine(this.finalStates,current))
    }

  }
  
  
  def main(args:Array[String]) = {
    new StateMachineTest().createProject
  }
}