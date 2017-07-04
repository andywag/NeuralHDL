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




  /** Design Under Test */

  override lazy val inputSize = 36
  /** Create the interface for the input data */

  // Dump RTL output signals to a file for later comparison
  interface.inRdy.value.value            ---->(s"$dataLocation/rtl_in",clk.createEnable(interface.inRdy.enable))
  interface.outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(interface.outRdy.vld))
  interface.outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(interface.outPreRdy.vld))
  dutParser.mError.errorOut ---->(s"$dataLocation/rtl_error",Some("testSimple.simple.simple_err"))
  dutParser.mStage(0).memory.biasStructW ---->(s"$dataLocation/rtl_bias",Some("testSimple.simple.simple_st0"))
  dutParser.mStage(0).memory.tapStructW ---->(s"$dataLocation/rtl_tap",Some("testSimple.simple.simple_st0"))


  expectedRdy ---->(s"$dataLocation/rtl_expected")

  dutParser.mStage(0).memory.dataStructW sread(s"$dataLocation/rtl_mem",Some("testSimple.simple.simple_st0"))

  // Control signals used for network stage
  //dutParser.mStage(0).control.dataLength  := information.dataLength-1
  //dutParser.mStage(0).control.loadDepth   := information.dataFill-1
  //dutParser.mStage(0).control.stateLength     := information.stateLength-1
  dutParser.mStage(0).control.controlInterface.errorLength  := information(0).errorTapLength-1
  //dutParser.mStage(0).control.loadDepth  := 8 -1


  dutParser.mStage(0).control.controlInterface.loadLength    := information(0).dataLength-1
  dutParser.mStage(0).control.controlInterface.loadDepth     := 5//information.dataFill-1
  dutParser.mStage(0).control.controlInterface.stateLength   := information(0).stateLength-1
  dutParser.mStage(0).control.controlInterface.inputStage    := 1
  dutParser.mStage(0).control.controlInterface.tapEnable    := 1
  dutParser.mStage(0).control.controlInterface.biasEnable    := 1


  /** Method used to reorder taps and output them to files.It is an overly complex transpose
    * operation.
    **/
  /*
  def createInitialTaps  {
    val allSlices = List.tabulate(information.tapDimension._2)(x => tapData.slice(x,0))
    val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
    val cSlices   = nSlices.map(x => x._2.map(_._1))
    cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps0_${x._2}",x._1))
  }
  */


}
