package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{NeuralStageTop, NeuronControl}
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._

import collection.JavaConverters._
import com.simplifide.generate.model.NdArrayWrap._
import com.simplifide.generate.plot.PlotUtility
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/27/17.
  */
class NeuralTopTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "st"

  lazy val dataLength    = 6
  lazy val outputLength  = 12
  lazy val tapLength     = dataLength*outputLength
  val biasLength    = 12
  val numberNeurons = 6
  val dataFill      = 8

  override def getTestLength = tapLength*12

  val information = NeuralStageTop.Info((dataLength,outputLength),dataLength,dataFill,numberNeurons,dataLocation)
  //val dimension   = NeuronControl.Dimension(10,6,7,dataFill)

  val dataIn      = FloatSignal("data_in",INPUT)
  val dataOut     = FloatSignal("data_out",OUTPUT)
  val dataOutPre     = FloatSignal("data_out_pre",OUTPUT)

  val inRdy          = new ReadyValidInterface(dataIn)
  val outRdy         = new ReadyValidInterface(dataOut)
  val outPreRdy      = new ReadyValidInterface(dataOutPre)

  // Tap Load Interface - Not currently implemented : Direct load of memory for now
  val tapIn       = FloatSignal("tap_in",INPUT)
  val tapRdy      = new ReadyValidInterface(tapIn)



  // Create the Data set to load the taps
  val tapData   = Nd4j.randn(Array(information.tapAddressLength,information.numberNeurons)) //.mul(1/outputLength.toFloat)
  val allSlices = List.tabulate(information.tapDimension._2)(x => tapData.slice(x,0))
  val nSlices   = allSlices.zipWithIndex.groupBy(x => (x._2 % numberNeurons)).toList.sortBy(_._1)
  val cSlices   = nSlices.map(x => x._2.map(_._1))
  cSlices.zipWithIndex.foreach(x => DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps_${x._2}",x._1))

  //DataFileGenerator.createFlattenCombine(s"$dataLocation/init_taps",tslices)


  //List.tabulate(numberNeurons)(x => DataFileGenerator.createFlatten3(s"$dataLocation/init_taps_$x",tapData.slice(x,0),'c'))

  val dataData  = NeuralTopTest.getTraining
  val input     = DataFileGenerator.createFlatten2(s"$dataLocation/init_data",dataData._1)

  val result      = tapData dot dataData._1
  val finalResult = Transforms.sigmoid(result)

  val out    = DataFileGenerator.createFlatten3(s"$dataLocation/pre_data",result,'f')
  val fin    = DataFileGenerator.createFlatten3(s"$dataLocation/out_data",finalResult,'f')

  override val dutParser = new NeuralStageTop(blockName, information,inRdy, tapRdy, outRdy, outPreRdy)
  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  inRdy.vld := 1
  inRdy.value.value             <-- (input)
  val rOut  = outRdy.value.value            ---->(s"$dataLocation/rtl_out",clk.createEnable(outRdy.vld), None, "Stage Output",8)
  val rpOut = outPreRdy.value.value         ----> (s"$dataLocation/rtl_pre",clk.createEnable(outPreRdy.vld), None, "Stage Pre Non",8)

  dutParser.control.dataLength  := information.dataLength-1
  dutParser.control.loadDepth   := information.dataFill-1
  dutParser.control.stateLength := information.stateLength-1
  dutParser.control.biasLength  := information.biasLength-1

  dutParser.control.loadDepth  := 8 -1


  override def postRun = {
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


  }


}

object NeuralTopTest {
  val trainingData =
    """1 0 0 0 0 0 0 0 0 0 1
0 1 0 0 0 0 0 0 0 0 1
0 0 0 0 1 0 0 0 0 0 1
1 0 1 0 0 0 0 0 0 1 0
1 0 1 0 1 1 0 0 0 1 1
1 1 1 1 0 0 0 0 1 0 0
1 0 1 0 0 0 0 0 1 1 0
1 0 0 1 0 0 0 0 1 1 1
0 1 1 0 1 1 0 1 0 0 0
1 0 0 1 1 1 0 1 0 0 1
0 1 1 1 0 0 0 1 0 1 1
1 0 0 0 1 0 0 1 1 0 0
1 1 1 0 0 0 0 1 1 0 1
1 0 1 1 0 1 0 1 1 1 0
1 1 0 1 1 0 1 0 0 0 0
1 1 1 0 0 1 1 0 0 0 1
1 0 0 1 1 0 1 0 0 1 0
1 1 1 0 1 0 1 0 0 1 1
0 1 1 0 1 0 1 0 1 0 1
0 1 1 1 1 0 1 0 1 1 0
1 1 0 0 0 1 1 0 1 1 1
1 0 0 0 1 1 1 1 0 0 0
1 0 1 1 0 0 1 1 0 1 0
1 1 0 0 0 0 1 1 0 1 1
1 0 0 0 0 1 1 1 1 0 0
1 1 1 1 0 1 1 1 1 0 1"""

val trainArray = {
  def hl(input:String) = {
    input.split(" ").slice(0,6).map(x => Integer.parseInt(x).toFloat)
  }
  def h2(input:String) = {
    input.split(" ").slice(0,6).map(x => Integer.parseInt(x))
  }
  val linesIn = trainingData.split("\n").map(x => hl(x))
  val linesOut = trainingData.split("\n").map(x => hl(x))

  (linesIn,linesOut)
}

  def getTraining = {
    val arr = trainArray
    val input =  Nd4j.create(arr._1).transpose()
    val output = Nd4j.create(arr._2).transpose()
    (input,output)
  }

}