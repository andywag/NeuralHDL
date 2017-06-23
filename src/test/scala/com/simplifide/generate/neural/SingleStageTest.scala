package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.NeuralStageTop
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import org.nd4s.Implicits._

/**
  * Created by andy on 6/3/17.
  */
class SingleStageTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "simple"

  // Get test information from a shared location
  // Contains training data and initial taps
  val information = BasicTestInformation.getInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*64
  val numberNeurons = BasicTestInformation.numberNeurons

  val interface   = new neural.NeuralStageInterface("st",FloatSignal("a",INPUT))

  val tapData   = BasicTestInformation.getInitTaps
  /** Store the taps to a file after converting the order */
  createInitialTaps

  // Store the initial data and taps to a file.
  val dataData  = BasicTestInformation.getTrainIdent(6,12)
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)
  val expected  = DataFileGenerator.createFlatten2(s"$dataLocation/init_expected",dataData._2)

  // Create the interface for the expected data
  val expectedData = FloatSignal("expected",INPUT)
  val expectedRdy  = new ReadyValidInterface(expectedData)

  override val dutParser = new NeuralNetwork(blockName, List(information),
    interface,expectedRdy)

  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  val size =input.data.length()
  /** Create the interface for the input data */
  val inRdyCount = signal("in_rdy_count",REG,U(32,0))
  inRdyCount := ($iff (inRdyCount === size-1) $then 0 $else (inRdyCount + 1)).$at(clk.createEnable(interface.inRdy.enable))
  interface.inRdy.vld := 1
  interface.inRdy.value.value             <-- (input,Some(inRdyCount))

  val size2 = expected.data.length()
  /** Create the interface for the expected data */
  val expRdyCount = signal("exp_rdy_count",REG,U(32,0))
  expRdyCount := ($iff (expRdyCount === size2-1) $then 0 $else (expRdyCount + 1)).$at(clk.createEnable(expectedRdy.enable))
  expectedRdy.vld := 1//(index < expected.data.length()) ? 1 :: 0
  expectedRdy.value.value                 <-- (expected,Some(expRdyCount))


  // Dump RTL output signals to a file for later comparison
  val rIn  = interface.inRdy.value.value            ---->(s"$dataLocation/rtl_in",clk.createEnable(interface.inRdy.enable), None, "Stage Output",8)
  val rOut  = interface.outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(interface.outRdy.vld), None, "Stage Output",8)
  val rpOut = interface.outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(interface.outPreRdy.vld), None, "Stage Pre Non",8)
  val errOut  = dutParser.mError.errorOut ---->(s"$dataLocation/rtl_error",Some("testSimple.simple.simple_err"))
  val biasOut = dutParser.mStage(0).memory.biasStructW ---->(s"$dataLocation/rtl_bias",Some("testSimple.simple.simple_st0"))
  expectedRdy ---->(s"$dataLocation/rtl_expected")

  dutParser.mStage(0).memory.dataStructW sread(s"$dataLocation/rtl_mem",Some("testSimple.simple.simple_st0"))

  // Control signals used for network stage
  //dutParser.mStage(0).control.dataLength  := information.dataLength-1
  //dutParser.mStage(0).control.loadDepth   := information.dataFill-1
  //dutParser.mStage(0).control.stateLength     := information.stateLength-1
  dutParser.mStage(0).control.controlInterface.errorLength  := information.errorTapLength-1
  //dutParser.mStage(0).control.loadDepth  := 8 -1


  dutParser.mStage(0).control.controlInterface.loadLength    := information.dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth     := 5//information.dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength   := information.stateLength-1


  /** Method used to reorder taps and output them to files.It is an overly complex transpose
    * operation.
    **/
  def createInitialTaps  {
    val allSlices = List.tabulate(information.tapDimension._2)(x => tapData.slice(x,0))
    val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
    val cSlices   = nSlices.map(x => x._2.map(_._1))
    cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps_${x._2}",x._1))

  }

  override def postRun = {

  }
}