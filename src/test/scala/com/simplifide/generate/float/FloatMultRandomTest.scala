package com.simplifide.generate.float

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.newparser.typ.NumberType
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

class FloatMultRandomTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "floatMultRandom"

  val dutParser = new FloatMultRandomTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  //override val dut = dutParser.createEntity
  /** Design Under Test */
  val in1 = dutParser.in1 <-- RANDOM
  val in2 = dutParser.in2 <-- RANDOM

  dutParser.out1 --> ( (in1 * in2), "Multiplier",1)

}

object FloatMultRandomTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser  {
    import com.simplifide.generate.newparser.typ.SegmentParser._


    signal(clk.allSignals(INPUT))
    val in1   = signal(FloatSignal("in1",INPUT))
    val in2   = signal(FloatSignal("in2",INPUT))
    val out1  = signal(FloatSignal("out",OUTPUT))

    //val result = in1 * in2
    out1 := in1 times in2

  }
}

