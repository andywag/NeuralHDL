package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{NeuralStage, Neuron, NeuronStage, Sigmoid}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.model.{DataFileGenerator, NdDataSet, NdUtils}
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.api.buffer.DataBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/28/17.
  */
class NeuralStageTest extends BlockScalaTest with BlockTestParser {

  import org.nd4s.Implicits._
  import collection.JavaConverters._
  import com.simplifide.generate.model.NdArrayWrap._



  def blockName:String = "neural_stage"
  lazy val depth = 16
  lazy val share = 1

  override def getTestLength = depth*256


  val start = (math.log10(depth)/math.log10(2.0)).toInt

  val dutParser = new NeuralStage(blockName,depth)
  override val dut: NewEntity = dutParser.createEntity



  val delayIndex = signal(SignalTrait("d_index",WIRE,U(32,0)))
  val delayIndex2 = signal(SignalTrait("d_index2",WIRE,U(32,0)))

  case class InputData(input:NdDataSet, taps:Seq[NdDataSet], bias:NdDataSet, output:NdDataSet, output1:NdDataSet, output2:NdDataSet)

  val vectors = this.createDataSet(testLength/depth,this.dataLocation)

  /- ("Create a valid pulse")
  dutParser.first := (index(start-1,0) === 0)
  /- ("Delay the bias to line up the data")

  delayIndex2 := index - (depth +2);

  val delayIndex3 = signal(SignalTrait("d_index3",WIRE,U(32,0)))
  delayIndex3(start-1,0) := index(start-1,0)

  def createTap(x:Int) = dutParser.tapIn.s(x)  <-- (vectors.taps(x),Some(delayIndex3))

  val dataInD   = List.tabulate(share) {x => dutParser.dataIn(x) <-- (vectors.input)}
  val tapInD    = List.tabulate(depth) {x => createTap(x)}
  val biasInD   = List.tabulate(share) {x => dutParser.biasIn(x) <-- (vectors.bias,Some(delayIndex2))}

  val rout  = List.tabulate(share) {x => dutParser.dataOutPre   ---> (s"$dataLocation/rout", None, "Stage Output",8)}
  val rout1  = List.tabulate(share) {x => dutParser.dataOutBias ---> (s"$dataLocation/rout1", None, "Stage Output",8)}
  val rout2 = List.tabulate(share) {x => dutParser.dataOut      ---> (s"$dataLocation/rout2", None, "Sigmoid Output",8)}


  override def postRun = {
    val output  = rout(0).load()
    val output1 = rout1(0).load()
    val output2 = rout2(0).load()

    val plotEnable = false
    val plot1 = if (plotEnable) Some(s"$docLocation/results") else None
    val plot2 = if (plotEnable) Some(s"$docLocation/results1") else None
    val plot3 = if (plotEnable) Some(s"$docLocation/results2") else None

    // Check the Matrix Multiple Output
    val error = PlotUtility.plotErrors(output, vectors.output.data,plot1,depth+2,0)
    this.checkMaxError(error,.001)
    // Check the Matrix Multiply Plus Bias Addition
    val error2 = PlotUtility.plotErrors(output1, vectors.output1.data,plot2,depth+3,0)
    this.checkMaxError(error2,.001)
    // Check the Sigmoid result
    val error3 = PlotUtility.plotErrors(output2, vectors.output2.data,plot3,depth+4,0)
    this.checkMaxError(error3,.06)


  }






  def createDataSet(length:Int, location:String) = {
    Nd4j.setDataType(DataBuffer.Type.FLOAT);

    val tapData   = Nd4j.randn(Array(depth,depth))
    val taps      = List.tabulate(depth)(x => DataFileGenerator.createFlatten(s"$dataLocation/taps_$x",tapData.slice(x,0)))

    //val slicedTaps = Seq.tabulate(depth)(x => new NdDataSet(s"$location/tap$x",tapData.slice(x,2)))

    val data = DataFileGenerator.createData(Array(length,depth),s"$dataLocation/data",DataFileGenerator.CONST(1.0,1)).transpose
    val bias = DataFileGenerator.createData(Array(length,depth),s"$dataLocation/bias",DataFileGenerator.RANDOM).transpose

    val result1 = tapData dot data.data
    val result2 = result1 + bias.data
    val result3 = Transforms.sigmoid(result2,true)

    //val fa = Nd4j.toFlattened('f',result2)
    //val fl = Nd4j.toFlattened('f',result3)

    val outFile  = DataFileGenerator.createFlatten2(s"$location/out",result1)
    val outFile1 = DataFileGenerator.createFlatten2(s"$location/out1",result2)
    val outFile2 = DataFileGenerator.createFlatten2(s"$location/out2",result3)



    new InputData(data,taps,bias,outFile, outFile1,outFile2)

  }

  override def document =
    s"""
This module is a block test wrapper for the a fully connected neuron stage. This is the most basic test which
has the same number of MAC units as neurons and an equal number of inputs and outputs.

## Test Results

### Plot of RTL vs Reference Data for output before non-linearity

![Ref vs RTL](results.jpg)

### Error between Rtl and Reference Data before non-linearity

![RTL](resultse.jpg)

### Plot of RTL vs Reference Data for output before non-linearity
![Ref vs RTL](results2.jpg)
       |
### Error between Rtl and Reference Data before non-linearity
       |
![RTL](results2e.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/${name}.v)
* [Test Wrapper (C++)](../test/${name}.cpp)
* [Test Generator](${PathUtilities.nueralTestPath}/NeuronStateTest.scala)
* [Code Generator](${PathUtilities.nueralPath}/NeuronStage.scala)

"""

}

object NeuralStageTest {

}

