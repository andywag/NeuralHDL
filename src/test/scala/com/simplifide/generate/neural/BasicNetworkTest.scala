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

  val information:Seq[NeuralStageInfo] = getInformation



  // Tap Generation for the test
  // FIXME : Need to generalize tap length and create function of (x,y) passed to generator
  val taps = tapType match {
    case BasicNetworkTest.RAND_TAPS  => BasicTestInformation.getRandomTaps(12,6)
    case BasicNetworkTest.IDENT_TAPS => BasicTestInformation.getInitTaps

  }
  information.zipWithIndex.foreach(x => {
    createInitialTaps(taps,s"$dataLocation/init_taps${x._2}",12,6)
  })


  // Data Generation for the test
  val dataData     = inputType match {
    case BasicNetworkTest.IDENT_TYPE  =>  BasicTestInformation.getTrainIdent (6, 6)
    case BasicNetworkTest.BRAILE_TYPE => BasicTestInformation.getTrainTest
  }
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)
  val expected  = DataFileGenerator.createFlatten2(s"$dataLocation/init_expected",dataData._2)
  lazy val inputSize = expected.data.length()


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
  expRdyCount := ($iff (expRdyCount === inputSize-1) $then 0 $else (expRdyCount + 1)).$at(clk.createEnable(expectedRdy.enable))
  expectedRdy.vld := 1//(index < expected.data.length()) ? 1 :: 0
  expectedRdy.value.value                 <-- (expected,Some(expRdyCount))


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
    val command = s"python python/checkResults.py ${plotStr}${thrStr}-t ${blockName}"
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
