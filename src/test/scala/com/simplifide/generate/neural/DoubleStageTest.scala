package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.NeuralNetworkInterface
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.util.{InternalLogger, ProcessOps}
import org.nd4j.linalg.api.ndarray.INDArray

/**
  * Created by andy on 6/3/17.
  */
class DoubleStageTest extends BasicNetworkTest {
  /** Clock for the testbench */
  override def blockName: String = "full"

  // Get the description of this network from a common location due to it's use in many tests
  //val information = BasicTestInformation.getDualInformation(dataLocation)
  def getInformation =  BasicTestInformation.getDualInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*512
  // Use and identity matrix for the test
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val plot:Boolean = true
  override val failThreshold = Some(0.1)

  /** Design Under Test */


  override lazy val inputSize = 36

  // Control signals used for network stage

  override lazy val gain = 3

  dutParser.mStage(0).control.controlInterface.loadLength   := information(0).dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth    := information(0).dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength  := information(0).stateLength-1
  dutParser.mStage(0).control.controlInterface.errorLength  := information(0).errorTapLength-1
  dutParser.mStage(0).control.controlInterface.inputStage   := 1


  dutParser.mStage(1).control.controlInterface.loadLength   := information(1).dataLength-1
  dutParser.mStage(1).control.controlInterface.loadDepth    := information(1).dataFill-1
  dutParser.mStage(1).control.controlInterface.stateLength  := 1
  dutParser.mStage(1).control.controlInterface.errorLength  := information(1).errorTapLength-1


}
