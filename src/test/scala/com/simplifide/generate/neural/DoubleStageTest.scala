package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import org.nd4j.linalg.api.ndarray.INDArray

/**
  * Created by andy on 6/3/17.
  */
class DoubleStageTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "full"

  // Get test information from a shared location
  // Contains training data and initial taps
  //val information = BasicTestInformation.getInformation(dataLocation)
  val information = BasicTestInformation.getDualInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*1024
  val numberNeurons = BasicTestInformation.numberNeurons

  val interface   = new neural.NeuralStageInterface("st",FloatSignal("a",INPUT))

  //val tapData   = BasicTestInformation.getInitTaps
  val tapData   = BasicTestInformation.getRandomTaps

  /** Store the taps to a file after converting the order */
  createInitialTaps(BasicTestInformation.getRandomTaps,s"$dataLocation/init_taps0")
  createInitialTaps(BasicTestInformation.getRandomTaps,s"$dataLocation/init_taps1")

  // Store the initial data and taps to a file.
  val data1     = BasicTestInformation.getTrainIdent(6,6)
  val dataData  = BasicTestInformation.getTrainTest //getTrainIdent(6,6)
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)
  val expected  = DataFileGenerator.createFlatten2(s"$dataLocation/init_expected",dataData._2)



  // Create the interface for the expected data
  val expectedData = FloatSignal("expected",INPUT)
  val expectedRdy  = new ReadyValidInterface(expectedData)

  override val dutParser = new NeuralNetwork(blockName, BasicTestInformation.getDualInformation(dataLocation),
    interface,expectedRdy)

  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  val size = 6*6//input.data.length()
  /** Create the interface for the input data */
  val inRdyCount = signal("in_rdy_count",REG,U(32,0))
  inRdyCount := ($iff (inRdyCount === size-1) $then 0 $else (inRdyCount + 1)).$at(clk.createEnable(interface.inRdy.enable))
  interface.inRdy.vld := 1
  interface.inRdy.value.value             <-- (input,Some(inRdyCount))

  val size2 = 6*6//expected.data.length()
  /** Create the interface for the expected data */
  val expRdyCount = signal("exp_rdy_count",REG,U(32,0))
  expRdyCount := ($iff (expRdyCount === size2-1) $then 0 $else (expRdyCount + 1)).$at(clk.createEnable(expectedRdy.enable))
  expectedRdy.vld := 1//(index < expected.data.length()) ? 1 :: 0
  expectedRdy.value.value                 <-- (expected,Some(expRdyCount))


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

  //val errOut  = dutParser.mError.errorOut ---->(s"$dataLocation/rtl_error",Some("testSimple.simple.simple_err"))
  //val biasOut = dutParser.mStage.memory.biasStructW ---->(s"$dataLocation/rtl_bias",Some("testSimple.simple.simple_st1"))


  // Control signals used for network stage

  dutParser.mStage(0).control.controlInterface.loadLength   := information(0).dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth    := information(0).dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength  := information(0).stateLength-1
  dutParser.mStage(0).control.controlInterface.errorLength  := information(0).errorTapLength-1
  dutParser.mStage(0).control.controlInterface.inputStage   := 1
  dutParser.mStage(0).control.controlInterface.tapEnable    := 1
  dutParser.mStage(0).control.controlInterface.biasEnable   := 1

  dutParser.mStage(1).control.controlInterface.loadLength   := information(0).tapDimension._2-1
  dutParser.mStage(1).control.controlInterface.loadDepth    := information(0).dataFill-1
  dutParser.mStage(1).control.controlInterface.stateLength  := 1
  dutParser.mStage(1).control.controlInterface.errorLength  := information(1).errorTapLength-1
  dutParser.mStage(1).control.controlInterface.errorLength  := information(1).errorTapLength-1
  dutParser.mStage(1).control.controlInterface.tapEnable    := 1
  dutParser.mStage(1).control.controlInterface.biasEnable   := 1

  interface.outRdy.rdy := 1


  /** Method used to reorder taps and output them to files.It is an overly complex transpose
    * operation.
    **/
  def createInitialTaps(tapData:INDArray, base:String)  {
    val allSlices = List.tabulate(information(0).tapDimension._2)(x => tapData.slice(x,0))
    val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
    val cSlices   = nSlices.map(x => x._2.map(_._1))
    cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"${base}_${x._2}",x._1))

  }

  override def postRun = {

  }
}
