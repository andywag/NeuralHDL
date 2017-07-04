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
  override val plot:Boolean = false
  override val failThreshold = Some(0.1)


  /** Design Under Test */


  override lazy val inputSize = 36

  // Dump RTL output signals to a file for later comparison
  val rOut  = interface.outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(interface.outRdy.vld), None, "Stage Output",8)
  val rpOut = interface.outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(interface.outPreRdy.vld), None, "Stage Pre Non",8)

  val stage0Out = dutParser.interfaces(0).outRdy ---->(s"$dataLocation/rtl_st0",Some("testFull.full"))
  val stage1Out = dutParser.interfaces(1).outRdy ---->(s"$dataLocation/rtl_st1",Some("testFull.full"))

  val errOut  = dutParser.mError.errorOut ---->(s"$dataLocation/rtl_error",Some("testFull.full"))
  dutParser.interfaces(0).errorRdy ---->(s"$dataLocation/rtl_error0",Some("testFull.full"))
  dutParser.interfaces(1).errorRdy ---->(s"$dataLocation/rtl_error1",Some("testFull.full"))
  dutParser.mStage(0).memory.biasStructW ---->(s"$dataLocation/rtl_bias0",Some("testFull.full.full_st0"))
  dutParser.mStage(1).memory.biasStructW ---->(s"$dataLocation/rtl_bias1",Some("testFull.full.full_st1"))
  dutParser.mStage(0).memory.tapStructW ---->(s"$dataLocation/rtl_tap0",Some("testFull.full.full_st0"))
  dutParser.mStage(1).memory.tapStructW ---->(s"$dataLocation/rtl_tap1",Some("testFull.full.full_st1"))



  // Control signals used for network stage

  val gain = 3

  dutParser.mStage(0).control.controlInterface.loadLength   := information(0).dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth    := information(0).dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength  := information(0).stateLength-1
  dutParser.mStage(0).control.controlInterface.errorLength  := information(0).errorTapLength-1
  dutParser.mStage(0).control.controlInterface.inputStage   := 1
  dutParser.mStage(0).control.controlInterface.tapEnable    := 1
  dutParser.mStage(0).control.controlInterface.biasEnable   := 1
  dutParser.mStage(0).stage.ri.tapGain                 := gain
  dutParser.mStage(0).stage.ri.biasGain                := gain
  dutParser.mStage(0).stage.ri.disableNonlinearity      := 0


  dutParser.mStage(1).control.controlInterface.loadLength   := information(0).tapDimension._2-1
  dutParser.mStage(1).control.controlInterface.loadDepth    := information(0).dataFill-1
  dutParser.mStage(1).control.controlInterface.stateLength  := 1
  dutParser.mStage(1).control.controlInterface.errorLength  := information(1).errorTapLength-1
  dutParser.mStage(1).control.controlInterface.errorLength  := information(1).errorTapLength-1
  dutParser.mStage(1).control.controlInterface.tapEnable    := 1
  dutParser.mStage(1).control.controlInterface.biasEnable   := 1
  dutParser.mStage(1).stage.ri.tapGain       := gain
  dutParser.mStage(1).stage.ri.biasGain      := gain
  dutParser.mStage(1).stage.ri.disableNonlinearity      := 1


  interface.outRdy.rdy := 1



/*
  override def postRun = {
    val result = ProcessOps.exec("python python/checkResults.py -t Double -th 0.05")(ln => InternalLogger.booleanMessage(ln))
    assert(result == true)
  }
 */
}
