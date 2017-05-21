package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{Neuron, NeuronStage, Sigmoid}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.model.{DataFileGenerator, NdDataSet, NdUtils}
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.api.buffer.DataBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms



/**
  * Created by andy on 5/8/17.
  */
class NeuronStageTest extends BlockScalaTest with BlockTestParser {

  import org.nd4s.Implicits._
  import collection.JavaConverters._
  import com.simplifide.generate.model.NdArrayWrap._

  def blockName:String = "neuronStage"
  val depth = 128
  val share = 1

  val start = (math.log10(depth)/math.log10(2.0)).toInt

  val valid        = SignalTrait("valid",INPUT)
  val dataIn       = Seq.tabulate(share)(x => FloatSignal(s"dataIn_$x",INPUT))
  val biasIn       = Seq.tabulate(share)(x => FloatSignal(s"biasIn_$x",INPUT))
  val tapIn        = Seq.tabulate(depth)(x => FloatSignal(s"tapIn_$x",INPUT))
  val dataOut      = Seq.tabulate(share)(x => FloatSignal(s"dataOut_$x",OUTPUT))
  val dataOutPre   = Seq.tabulate(share)(x => FloatSignal(s"dataOutPre_$x",OUTPUT))


  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  case class InputData(input:NdDataSet, taps:Seq[NdDataSet], bias:NdDataSet, output:NdDataSet, output2:NdDataSet)

  val vectors = this.createDataSet(testLength/depth,this.dataLocation)

  /- ("Create a valid pulse")
  valid := (index(start-1,0) === (depth-2))
  /- ("Delay the bias to line up the data")
  delayIndex := index - (depth-2);

  val dataInD   = List.tabulate(share) {x => dataIn(x) <-- (vectors.input,Some(delayIndex))}
  val tapInD    = List.tabulate(depth) {x => tapIn(x)  <-- (vectors.taps(x),Some(delayIndex))}
  val biasInD   = List.tabulate(share) {x => biasIn(x) <-- vectors.bias}

  //val rout  = List.tabulate(share) {x => dataOutPre(x) ---> (s"$dataLocation/rout", None, "Stage Output",8)}
  val rout1 = List.tabulate(share) {x => dataOut(x) ---> (s"$dataLocation/rout1", None, "Sigmoid Output",8)}


  override def postRun = {
    //val output  = rout(0).load()
    val output2 = rout1(0).load()

    val plotEnable = true
    val plot1 = if (plotEnable) Some(s"$docLocation/results") else None
    val plot2 = if (plotEnable) Some(s"$docLocation/results") else None
/*
    val error = PlotUtility.plotError(output.data().asDouble(),
      vectors.output.data.data().asDouble(),plot1)
    assert(error.max < .001)
*/

    val error2 = PlotUtility.plotError(output2.data().asDouble(),
      vectors.output2.data.data().asDouble(),plot2,1,2*depth)
    assert(error2.max < .06)


  }


  val neuron    = new Neuron(dataOut(0),dataIn(0),tapIn(0),biasIn(0))

  /* FIXME : 1. Need generic copy of signal
             2. Need better way to pass a generic typ for creation (probably pass the segment down rather than isntance)
  */
  val sigmoid   = new Sigmoid.AlawFloat2("sigmoid", dataOut(0), dataOutPre(0))


  val entity    = new ComplexSegment.SegmentEntity(neuron, "neuron")
  val sigEntity = new ComplexSegment.SegmentEntity(sigmoid, "sigmoid")

  val dutParser = new NeuronStage(blockName,valid,dataIn,tapIn,biasIn,
    dataOut, dataOutPre, depth, entity, sigEntity)



  override val dut: NewEntity = dutParser.createEntity



  def createDataSet(length:Int, location:String) = {
    Nd4j.setDataType(DataBuffer.Type.FLOAT);

    val tapData   = Nd4j.randn(Array(length,depth,depth))
    val taps   = DataFileGenerator.createSlices(s"$location/tap",tapData)

    val data = DataFileGenerator.createData(Array(length,depth,share),s"$dataLocation/data",DataFileGenerator.RANDOM)
    val bias = DataFileGenerator.createData(Array(length,depth,share),s"$dataLocation/bias",DataFileGenerator.RANDOM)

    val zeros = Array.tabulate(length*depth*share)(x => 0.0.toFloat).mkNDArray(Array(length,depth,share))

    val operation:(Int)=>INDArray = x => (tapData.slice(x).transpose() dot data.data.slice(x)) + bias.data.slice(x).transpose()
    val operation2:(Int)=>INDArray = x => Transforms.sigmoid(operation(x))

    val dataOut = NdUtils.vectorOp3(operation,length.toInt,data.data.shape()).d(2)
    val dataOut2 = NdUtils.vectorOp3(operation2,length.toInt,data.data.shape()).d(2)


    val outFile = DataFileGenerator.createFlatten(s"$location/out",dataOut)
    val outFile2 = DataFileGenerator.createFlatten(s"$location/out2",dataOut2)



    new InputData(data,taps,bias,outFile, outFile2)

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

object NeuronStageTest {

}
