package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.{NeuralStageInfo, NeuralStageTop}
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._

import collection.JavaConverters._
import com.simplifide.generate.model.NdArrayWrap._
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/27/17.
  */
class NeuralTopTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "stage"



  val information = BasicTestInformation.getInformation(dataLocation)
  override def getTestLength = BasicTestInformation.tapLength*12
  val numberNeurons = BasicTestInformation.numberNeurons

  val interface   = new neural.NeuralStageInterface("st",FloatSignal("a",INPUT))

  val tapData   = Nd4j.randn(Array(information.tapAddressLength,information.numberNeurons)) //.mul(1/outputLength.toFloat)
  val allSlices = List.tabulate(information.tapDimension._2)(x => tapData.slice(x,0))
  val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
  val cSlices   = nSlices.map(x => x._2.map(_._1))
  cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps_${x._2}",x._1))

  val dataData  = BasicTestInformation.getTraining
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)

  val result      = tapData dot dataData._1
  val finalResult = Transforms.sigmoid(result)

  val out    = DataFileGenerator.createFlatten3(s"$dataLocation/pre_data",result,'f')
  val fin    = DataFileGenerator.createFlatten3(s"$dataLocation/out_data",finalResult,'f')

  override val dutParser = new NeuralStageTop(blockName, information,
    interface)
  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  interface.inRdy.vld := 1
  interface.inRdy.value.value             <-- (input)
  val rOut  = interface.outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(interface.outRdy.vld), None, "Stage Output",8)
  val rpOut = interface.outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(interface.outPreRdy.vld), None, "Stage Pre Non",8)


  //dutParser.control.dataLength  := information.dataLength-1
  //dutParser.control.loadDepth   := information.dataFill-1
  //dutParser.control.loadDepth  := 8 -1

  dutParser.control.controlInterface.loadLength  := information.dataLength-1
  dutParser.control.controlInterface.loadDepth   := information.dataFill-1
  dutParser.control.controlInterface.loadDepth  := 8 -1
  dutParser.control.controlInterface.stateLength := information.stateLength-1


  this.createErrorCalculator

  def createErrorCalculator = {
    import com.simplifide.generate.newparser.typ.SegmentParser._
    val mem     = signal("expected_memory",REG,U(32,0),128)
    val expected = signal(FloatSignal("expected",WIRE))
    interface.errorIn !:= expected minus interface.dataOut
  }

  override def postRun = {
    /*
    val output  = rpOut.load()
    val output2 = rOut.load()

    val plotEnable = true
    val plot1 = if (plotEnable) Some(s"$docLocation/results") else None
    val plot2 = if (plotEnable) Some(s"$docLocation/resultse") else None

    val error = PlotUtility.plotError(output.data().asDouble(),
      out.data.data().asDouble(),plot1)
    this.checkMaxError(error,.001)


    val a = output2.data().asDouble()
    val b = fin.data.data().asDouble()

    val error2 = PlotUtility.plotErrors(output2, fin.data,plot2)
    this.checkMaxError(error2,.06)
    //assert(error2.max._1 < .06)
    */
  }

  override def document =
    s"""
This module is a block test wrapper for the a fully connected neuron stage with control and memories. The block
is currently configured to operate with

* 6  inputs
* 12 outputs
* 6  neurons

This configuration was selected for a simple test case with a 6 input, 6 output test case. The plan was to put another
12x12 hidden layer and a 12x6 output layer to complete the test. This test might be simplified to a more simple test
to verify the convergence of a single stage with a simple transfer function.

##

This test matches the output stage versus a model of the system defined in this module using Nd4j. The match is not perfect
due to quantization effects especially due to the sigmoid approximation with a maximum error of .048. Given this error the block
should definitely be done in fixed point.

```scala

val result      = tapData dot dataData._1
val finalResult = Transforms.sigmoid(result)

```

## Reference Code for Test
* [Testbench (Verilog)](../test/${name}.v)
* [Test Wrapper (C++)](../test/${name}.cpp)
* [Test Generator](${PathUtilities.nueralTestPath}/NeuralTopTest.scala)
* [Code Generator](${PathUtilities.nueralPath}/NeuralStageTop.scala)
* [Test Directory](../test/)
* [Design Directory](../design/)
* [Docs Directory](../doc/)

"""




}

object NeuralTopTest {

}