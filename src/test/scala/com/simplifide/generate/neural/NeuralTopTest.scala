package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{NeuralStageTop, NeuronControl}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}

/**
  * Created by andy on 5/27/17.
  */
class NeuralTopTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "st"

  val dataLength    = 1024
  val tapLength     = 1024*1024
  val biasLength    = 1024
  val numberNeurons = 128
  val dataFill      = 10

  val information = NeuralStageTop.Info((dataLength,dataLength),dataLength,dataFill,numberNeurons)
  val dimension   = NeuronControl.Dimension(10,6,7,dataFill)

  val dataIn      = FloatSignal("data_in",INPUT)
  val dataOut     = FloatSignal("data_out",OUTPUT)

  val inRdy       = new ReadyValidInterface(dataIn)
  val outRdy      = new ReadyValidInterface(dataOut)


  override val dutParser = new NeuralStageTop(blockName, information, dimension,inRdy, outRdy)
  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  inRdy.vld := 1
  inRdy.value.exp := index
  dutParser.control.dataLength  := dataLength -1
  dutParser.control.stateLength := information.stateLength-1
  dutParser.control.biasLength  := information.biasLength-1

  dutParser.control.dataDepth  := 8 -1

}
