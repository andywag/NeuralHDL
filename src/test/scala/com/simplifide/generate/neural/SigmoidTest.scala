package com.simplifide.generate.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.float.FloatMult
import com.simplifide.generate.blocks.neural
import com.simplifide.generate.blocks.neural.{Neuron, Sigmoid}
import com.simplifide.generate.model.{DataFileGenerator, NdUtils}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms

class SigmoidTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "sigmoid"

  val dutParser = new SigmoidTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  import org.nd4s.Implicits._
  import com.simplifide.generate.model.NdArrayWrap._

  val data = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data",
    DataFileGenerator.Ramp(-8.0,8.0))

  val result = Transforms.sigmoid(data.data)
  val out  = DataFileGenerator.createFlatten(s"$dataLocation/out",result)

  val dataInD   = dutParser.dataIn <-- data
  val dumpD     = dutParser.dataOut ---> (s"$dataLocation/rout", Some(out), "Sigmoid Output")
  override def createBody = {

  }

  override def postRun = {
    val data = dumpD.load()
    PlotUtility.plotError(data.data().asDouble(), result.data().asDouble())
    System.out.println("Here")

  }


}

object SigmoidTest {
  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {
    signal(clk.allSignals(INPUT))
    val dataIn    = signal(FloatSignal("data",INPUT))
    val dataOut   = signal(FloatSignal("out",OUTPUT))

    ->(new Sigmoid.AlawFloat2("sigmoid",dataOut, dataIn))

  }
}
