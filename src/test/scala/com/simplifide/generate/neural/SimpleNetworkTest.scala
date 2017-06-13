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
class SimpleNetworkTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "simple"


  val information = BasicTestInformation.getInformation(dataLocation)
  override def getTestLength = BasicTestInformation.tapLength*12*8
  val numberNeurons = BasicTestInformation.numberNeurons

  val interface   = new neural.NeuralStageInterface("st",FloatSignal("a",INPUT))

  //val tapData   = Nd4j.randn(Array(information.tapAddressLength,information.numberNeurons)) //.mul(1/outputLength.toFloat)
  val tapData   = BasicTestInformation.getInitTaps
  val allSlices = List.tabulate(information.tapDimension._2)(x => tapData.slice(x,0))
  val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
  val cSlices   = nSlices.map(x => x._2.map(_._1))
  cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps_${x._2}",x._1))

  val dataData  = BasicTestInformation.getTraining
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)
  val expected  = DataFileGenerator.createFlatten2(s"$dataLocation/init_expected",dataData._2)

  val result      = tapData dot dataData._1
  val finalResult = Transforms.sigmoid(result)

  val out    = DataFileGenerator.createFlatten3(s"$dataLocation/pre_data",result,'f')
  val fin    = DataFileGenerator.createFlatten3(s"$dataLocation/out_data",finalResult,'f')

  val expectedData = FloatSignal("expected",INPUT)
  val expectedRdy  = new ReadyValidInterface(expectedData)

  override val dutParser = new NeuralNetwork(blockName, List(information),
    interface,expectedRdy)

  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  val inRdyCount = signal("in_rdy_count",REG,U(32,0))
  inRdyCount := ($iff (inRdyCount === 35) $then 0 $else (inRdyCount + 1)).$at(clk.createEnable(interface.inRdy.enable))

  interface.inRdy.vld := 1
  interface.inRdy.value.value             <-- (input,Some(inRdyCount))

  val expRdyCount = signal("exp_rdy_count",REG,U(32,0))
  expRdyCount := ($iff (expRdyCount === 35) $then 0 $else (expRdyCount + 1)).$at(clk.createEnable(expectedRdy.enable))

  expectedRdy.vld := (index < expected.data.length()) ? 1 :: 0
  expectedRdy.value.value                 <-- (expected,Some(expRdyCount))


  val rOut  = interface.outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(interface.outRdy.vld), None, "Stage Output",8)
  val rpOut = interface.outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(interface.outPreRdy.vld), None, "Stage Pre Non",8)


  dutParser.stage.control.dataLength  := information.dataLength-1
  dutParser.stage.control.loadDepth   := information.dataFill-1
  dutParser.stage.control.stateLength     := information.stateLength-1
  //dutParser.stage.control.biasLength      := information.biasLength-1
  dutParser.stage.control.tapErrorLength  := information.errorTapLength-1

  dutParser.stage.control.loadDepth  := 8 -1

  this.createErrorCalculator

  def createErrorCalculator = {
    import com.simplifide.generate.newparser.typ.SegmentParser._
    val mem     = signal("expected_memory",REG,U(32,0),128)
    val expected = signal(FloatSignal("expected",WIRE))
    interface.errorIn !:= expected minus interface.dataOut
  }

  override def postRun = {
    val output  = rpOut.load()
    val output2 = rOut.load()

    val plotEnable = true
    val plot1 = if (plotEnable) Some(s"$docLocation/results") else None
    val plot2 = if (plotEnable) Some(s"$docLocation/resultse") else None

    val error = PlotUtility.plotErrorRepeat(output.data().asDouble(),
      out.data.data().asDouble(),plot1)
    this.checkMaxError(error,.001)


    val a = output2.data().asDouble()
    val b = fin.data.data().asDouble()

    val error2 = PlotUtility.plotErrors(output2, fin.data,plot2)
    this.checkMaxError(error2,.06)
    //assert(error2.max._1 < .06)
  }
}
