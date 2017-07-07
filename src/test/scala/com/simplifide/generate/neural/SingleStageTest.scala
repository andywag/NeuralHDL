package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.{NeuralNetworkInterface, NeuralStageInfo, NeuralStageTop}
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.neural.BasicTestInformation._
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
  //def getInformation = BasicTestInformation.getSingleInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*512
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val failThreshold = Some(0.2)
  override lazy val inputSize = 36
  override val plot = true
  override lazy val gain = 3

  lazy val inputLen      = 6
  def outputLen     = 12
  lazy val numberNeurons = 6
  lazy val dataFill      = 6
  lazy val errorFill     = 4
  lazy val outputFill    = 3
  lazy val errorLength   = 12

  // First Stage of data contains a 6x12 network stage
  def getInformation = Seq(NeuralStageInfo((inputLen,outputLen),
    dataFill,
    numberNeurons,
    errorFill,
    outputFill,
    dataLocation,
    errorLength,
    Some(s"${dataLocation}/init_taps0")))


}

class Single6x6 extends SingleStageTest{
  override def blockName: String = "simple6"
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS
  override def waveformEnable = true

  override def outputLen          = 6
  override lazy val errorLength   = 6

}

class Single5x5 extends SingleStageTest{
  override def blockName: String = "simple5"
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS
  override def waveformEnable = true

  override def outputLen     = 5
  override lazy val errorLength = 5
  override lazy val numberNeurons = 5


}

