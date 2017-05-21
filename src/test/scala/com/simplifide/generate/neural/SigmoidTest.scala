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
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms

class SigmoidTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "sigmoid"

  val range = (-9.0,9.0)

  val dutParser = new SigmoidTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  import org.nd4s.Implicits._
  import com.simplifide.generate.model.NdArrayWrap._

  val typ = DataFileGenerator.Random(range._1, range._2)//DataFileGenerator.Ramp(range._1, range._2)

  val data = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data",
    typ)

  val result = Transforms.sigmoid(data.data).d(1)
  val out  = DataFileGenerator.createFlatten(s"$dataLocation/out",result)

  val dataInD   = dutParser.dataIn <-- data
  val dumpD     = dutParser.dataOut ---> (s"$dataLocation/rout", None, "Sigmoid Output")
  override def createBody = {

  }

  override def postRun = {
    val data = dumpD.load()
    val error = PlotUtility.plotError(data.data().asDouble(),
      result.data().asDouble(),Some(s"$docLocation/discrim"))
    assert(error.max < .05)
  }

  override def document =
    s"""
This module is a block test wrapper for the sigmoid block. The block tests the output of this block
against a linear ramp over a range of ${range._1} to ${range._2}. The output of this is matched
against a reference model which give a maximum error of around 4.8%.

## Test Results

### Plot of RTL vs Reference Data

![Ref vs RTL](discrim.jpg)

### Actual Difference between Rtl and Reference Data
![RTL](discrime.jpg)

## Reference Code for Test
* [Testbench (Verilog)](../test/${name}.v)
* [Test Wrapper (C++)](../test/${name}.cpp)
* [Test Generator](${PathUtilities.nueralTestPath}/SigmoidTest.scala)
* [Code Generator](${PathUtilities.nueralPath}/Sigmoid.scala)

"""


}

object SigmoidTest {

  object RampTest extends SigmoidTest {

  }

  class Dut(val name:String)(implicit val clk:ClockControl) extends EntityParser {
    signal(clk.allSignals(INPUT))
    val dataIn    = signal(FloatSignal("data",INPUT))
    val dataOut   = signal(FloatSignal("out",OUTPUT))

    val sig = ->(new Sigmoid.AlawFloat2("sigmoid",dataOut, dataIn))

    override def document = sig.document

  }
}
