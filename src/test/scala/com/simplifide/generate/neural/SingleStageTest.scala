package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.{NeuralNetworkInterface, NeuralStageTop}
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.util.{InternalLogger, ProcessOps}
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import org.nd4s.Implicits._

/**
  * Created by andy on 6/3/17.
  */
class SingleStageTest extends BasicNetworkTest {
  /** Clock for the testbench */
  override def blockName: String = "simple"

  // Contains training data and initial taps
  //override val information = BasicTestInformation.getSingleInformation(dataLocation)
  def getInformation = BasicTestInformation.getSingleInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*512
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val failThreshold = Some(0.2)
  override lazy val inputSize = 36
  override val plot = true



  override lazy val gain = 3
  dutParser.mStage(0).control.controlInterface.errorLength  := information(0).errorTapLength-1
  dutParser.mStage(0).control.controlInterface.loadLength    := information(0).dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth     := information(0).dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength   := information(0).stateLength-1



}
