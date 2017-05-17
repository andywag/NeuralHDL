package com.simplifide.scala2.test.basic

import com.simplifide.generate.TestConstants
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.{FunctionParser, EntityParser}
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.{Isim, Test, TestEntityParser}
import com.simplifide.generate.signal.{NewConstant, Constant, SignalTrait}
import com.simplifide.generate.blocks.interfac.{PacketDecoder, PacketModel, PacketEncoder, PacketField}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 2/28/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */

class PacketInterfaceTest {}

object PacketInterfaceTest {

  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "packet"
    // Definition of a clock for the module
    implicit val clk         = ClockControl("clk","reset")

    // Root Module Definition
    override val newRoot = new Entity().createEntity
    // Defines the Tests for this project
    override val tests    = List(Test(new TestCase(newRoot).createEntity))
  }

  class Entity(implicit val clk:ClockControl) extends EntityParser {
    override val name = "packet"
    signal(clk.allSignals(INPUT))

    
    val inputBus = signal("packet",REG,U(32,0))
    val index    = signal("counter",WIRE,U(4,0))
    val typ      = signal("packet_type",REG,U(4,0))
    
    val signal1 = signal("signal1",WIRE,U(32,0))
    val signal2 = signal("signal2",WIRE,U(32,0))
    val signal3 = signal("signal3",WIRE,U(32,0))



    val field = PacketField(List(NewConstant.Hex(0xFFFF, 16),signal1,signal2,signal3))
    val model = PacketModel(Map(0 -> field, 1 -> field))

    this.assign(PacketEncoder(inputBus,index,typ,model))

    this.assign(PacketDecoder(inputBus,index,typ,model.convertToRegister))


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