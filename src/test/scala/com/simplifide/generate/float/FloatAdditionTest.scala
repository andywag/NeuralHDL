package com.simplifide.generate.float

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.newparser.typ.NumberType
import com.simplifide.generate.blocks.float.{FloatAddition, FloatMult}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

/**
  * Created by andy on 5/4/17.
  */
class FloatAdditionRandomTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "floatAddRandom"

  val dutParser = new FloatAdditionRandomTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  //override val dut = dutParser.createEntity
  /** Design Under Test */
  val in1 = dutParser.in1 <-- RANDOM
  val in2 = dutParser.in2 <-- RANDOM

  dutParser.out1 --> (in1 + in2, "Adder",1)



}

object FloatAdditionRandomTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {


    import com.simplifide.generate.newparser.typ.SegmentParser._

    signal(clk.allSignals(INPUT))
    val in1   = signal(FloatSignal("in1",INPUT))
    val in2   = signal(FloatSignal("in2",INPUT))
    val out1  = signal(FloatSignal("out",OUTPUT))

    out1 := in1 plus in2

  }
}