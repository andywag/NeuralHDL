package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.simple.NeuralNetwork
import com.simplifide.generate.blocks.neural.{NeuralNetworkInterface, NeuralStageInfo}
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.util.{InternalLogger, ProcessOps}
import org.nd4j.linalg.api.ndarray.INDArray

/**
  * Created by andy on 7/4/17.
  */

/**
  * Top level test class for the fully connected cases
  *
  */
trait BasicNetworkTest extends BlockScalaTest with BlockTestParser{

  def getInformation:Seq[NeuralStageInfo]
  lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.RAND_TAPS
  lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  val plot:Boolean = false
  val failThreshold:Option[Double] = None
  lazy val gain:Int = 3

  val information:Seq[NeuralStageInfo] = getInformation

  // Path to the highest level block under the test
  val topPathName = s"${name}.${blockName}"
  def stagePath(i:Int) = s"${topPathName}.${blockName}_st${i}"


  // Tap Generation for the test
  // FIXME : Need to generalize tap length and create function of (x,y) passed to generator

  information.zipWithIndex.foreach(x => {
    val t1 = x._1.tapDimension._1
    val t2 = x._1.tapDimension._2
    val taps = tapType match {
      case BasicNetworkTest.RAND_TAPS  => BasicTestInformation.getRandomTaps(t2,t1)
      case BasicNetworkTest.IDENT_TAPS => BasicTestInformation.getTapIdent(t2,t1)
    }
    createInitialTaps(taps,s"$dataLocation/init_taps${x._2}",t2,t1)
  })


  val inputLength  = information(0).tapDimension._1
  val outputLength = information(information.length-1).tapDimension._2
  // Data Generation for the test
  val dataData     = inputType match {
    case BasicNetworkTest.IDENT_TYPE  =>  BasicTestInformation.getTrainIdent2(inputLength, outputLength)
    case BasicNetworkTest.BRAILE_TYPE => BasicTestInformation.getTrainTest
  }
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)
  val expected  = DataFileGenerator.createFlatten2(s"$dataLocation/init_expected",dataData._2)

  val inputSize = dataData._1.length()
  val expectedSize = dataData._2.length()


  override val dutParser = new NeuralNetwork(blockName, information,
    topInterface)
  override val dut: NewEntity = dutParser.createEntity

  /** Create the interface for the input data */
  val inRdyCount = signal("in_rdy_count",REG,U(32,0))
  inRdyCount := ($iff (inRdyCount === inputSize-1) $then 0 $else (inRdyCount + 1)).$at(clk.createEnable(interface.inRdy.enable))
  interface.inRdy.vld := 1
  interface.inRdy.value.value             <-- (input,Some(inRdyCount))
  /** Create the interface for the expected data */
  val expRdyCount = signal("exp_rdy_count",REG,U(32,0))
  expRdyCount := ($iff (expRdyCount === expectedSize-1) $then 0 $else (expRdyCount + 1)).$at(clk.createEnable(expectedRdy.enable))
  expectedRdy.vld := 1//(index < expected.data.length()) ? 1 :: 0
  expectedRdy.value.value                 <-- (expected,Some(expRdyCount))


  // Setup controls for the stages
  for (i <- 0 until information.size) {
    dutParser.mStage(i).control.controlInterface.tapEnable     := 1
    dutParser.mStage(i).control.controlInterface.biasEnable    := 0
    dutParser.mStage(i).control.controlInterface.inputStage    := (if (i == 0) 1 else 0)
    dutParser.mStage(i).stage.ri.tapGain                       := gain
    dutParser.mStage(i).stage.ri.biasGain                      := gain
    dutParser.mStage(i).stage.ri.disableNonlinearity           := (if (i == information.size-1) 1 else 0)

    dutParser.mStage(i).control.controlInterface.loadLength   := information(i).dataLength-1
    dutParser.mStage(i).control.controlInterface.loadDepth    := information(i).dataFill-1
    dutParser.mStage(i).control.controlInterface.errorLength  := information(i).errorTapLength-1
    dutParser.mStage(i).control.controlInterface.stateLength  := information(0).stateLength-1


  }

  // Generic Output Dumps for the test
  for (i <- 0 until information.size) {
    dutParser.interfaces(i).errorRdy ---->(s"$dataLocation/rtl_error$i",Some(topPathName))
    dutParser.mStage(i).memory.biasStructW ---->(s"$dataLocation/rtl_bias$i",Some(stagePath(i)))
    dutParser.mStage(i).memory.tapStructW ---->(s"$dataLocation/rtl_tap$i",Some(stagePath(i)))
    dutParser.interfaces(0).outRdy ---->(s"$dataLocation/rtl_st$i",Some(topPathName))
  }

  // Output signals
  interface.outRdy.rdy := 1



  /** Method used to reorder taps and output them to files.It is an overly complex transpose
    * operation.
    **/
  def createInitialTaps(tapData:INDArray, base:String,r:Int, c:Int)  {
    val allSlices = List.tabulate(r)(x => tapData.slice(x,0))
    val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % c)).toList.sortBy(_._1)
    val cSlices   = nSlices.map(x => x._2.map(_._1))
    cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"${base}_${x._2}",x._1))
  }

  lazy val interface              = new neural.NeuralStageInterface("st",FloatSignal("a",INPUT))
  lazy val topInterface = new NeuralNetworkInterface("int",interface)
  lazy val expectedRdy = topInterface.expectedRdy



  override def postRun = {
    val plotStr = if (plot) "-p " else ""
    val thrStr  = failThreshold.map(x => s"-th ${x.toString} ").getOrElse("")
    val stStr   = s"-s ${information.size}"
    val command = s"python python/checkResults.py ${plotStr}${thrStr}-t ${blockName} $stStr"
    println (s"Running Command $command")
    val result = ProcessOps.exec(command)(ln => InternalLogger.booleanMessage(ln))
    assert(result == true)
  }

}

object BasicNetworkTest {
  trait INPUT_TYPE
  case object IDENT_TYPE extends INPUT_TYPE
  case object BRAILE_TYPE extends INPUT_TYPE

  trait TAP_TYPE
  case object IDENT_TAPS extends TAP_TYPE
  case object RAND_TAPS  extends TAP_TYPE

}
