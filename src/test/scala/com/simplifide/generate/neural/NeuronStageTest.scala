package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{Neuron, NeuronStage}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.model.{DataFileGenerator, NdDataSet, NdUtils}
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM
import org.nd4j.linalg.api.buffer.DataBuffer
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j



/**
  * Created by andy on 5/8/17.
  */
class NeuronStageTest extends BlockScalaTest with BlockTestParser {

  def blockName:String = "neuronStage"
  val depth = 8
  val share = 1

  val start = (math.log10(depth)/math.log10(2.0)).toInt

  val valid     = SignalTrait("valid",INPUT)
  val dataIn    = Seq.tabulate(share)(x => FloatSignal(s"dataIn_$x",INPUT))
  val biasIn    = Seq.tabulate(share)(x => FloatSignal(s"biasIn_$x",INPUT))
  val tapIn     = Seq.tabulate(depth)(x => FloatSignal(s"tapIn_$x",INPUT))
  val dataOut   = Seq.tabulate(share)(x => FloatSignal(s"dataOut_$x",OUTPUT))

  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  case class InputData(input:NdDataSet, taps:Seq[NdDataSet], bias:NdDataSet, output:NdDataSet)

  override def createBody = {
    val vectors = this.createDataSet(testLength/depth,this.dataLocation)

    /- ("Create a valid pulse")
    valid := (index(start-1,0) === (depth-2))
    /- ("Delay the bias to line up the data")
    delayIndex := index - (depth-2);

    val dataInD   = List.tabulate(share) {x => dataIn(x) <-- (vectors.input,Some(delayIndex))}
    val tapInD    = List.tabulate(depth) {x => tapIn(x)  <-- (vectors.taps(x),Some(delayIndex))}
    val biasInD   = List.tabulate(share) {x => biasIn(x) <-- vectors.bias}

    List.tabulate(share) {x => dataOut(x) ---> (s"$dataLocation/rout", Some(vectors.output), "Stage Output",8)}

    //dataOut ---> (s"$dataLocation/rout", Some(out), "Neuron Output")
  }


  val neuron    = new Neuron(dataOut(0),dataIn(0),tapIn(0),biasIn(0))
  val entity  = new ComplexSegment.SegmentEntity(neuron, "neuron")

  val dutParser = new NeuronStage(blockName,valid,dataIn,tapIn,biasIn,dataOut,depth, entity)



  override val dut: NewEntity = dutParser.createEntity

  import org.nd4s.Implicits._
  import collection.JavaConverters._
  import com.simplifide.generate.model.NdArrayWrap._

  def createDataSet(length:Int, location:String) = {
    Nd4j.setDataType(DataBuffer.Type.FLOAT);

    val tapData   = Nd4j.randn(Array(length,depth,depth))
    val taps   = DataFileGenerator.createSlices(s"$location/tap",tapData)

    val data = DataFileGenerator.createData(Array(length,depth,share),s"$dataLocation/data",DataFileGenerator.RANDOM)
    val bias = DataFileGenerator.createData(Array(length,depth,share),s"$dataLocation/bias",DataFileGenerator.RANDOM)

    val zeros = Array.tabulate(length*depth*share)(x => 0.0.toFloat).mkNDArray(Array(length,depth,share))


    System.out.println("Tap" + tapData.slice(0).transpose())
    System.out.println("Data" + data.data.slice(0))
    System.out.println("Bias" + bias.data.slice(0))
    //val operation:(Int)=>INDArray = x => tapData.slice(x) dot data.data.slice(x)
    val operation:(Int)=>INDArray = x => (tapData.slice(x).transpose() dot data.data.slice(x)) + bias.data.slice(x).transpose()
    val dataOut = NdUtils.vectorOp3(operation,length.toInt,data.data.shape()).d(2)


    val outFile = DataFileGenerator.createFlatten(s"$location/out",dataOut)



    new InputData(data,taps,bias,outFile)

  }

}

object NeuronStageTest {

}
