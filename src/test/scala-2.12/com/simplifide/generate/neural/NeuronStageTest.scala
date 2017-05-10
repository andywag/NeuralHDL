package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{Neuron, NeuronStage}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.{FloatSignal, SignalTrait}
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM



/**
  * Created by andy on 5/8/17.
  */
class NeuronStageTest extends BlockScalaTest with BlockTestParser {

  def blockName:String = "neuronStage"

  val depth = 8;
  val start = (math.log10(depth)/math.log10(2.0)).toInt
  //override implicit val testLength = 1000



  val valid     = SignalTrait("valid",INPUT)
  val dataIn    = Seq(FloatSignal("dataIn",INPUT))
  val tapIn     = Seq.tabulate(depth)(x => FloatSignal(s"tapIn_$x",INPUT))
  val biasIn    = Seq.tabulate(depth)(x => FloatSignal(s"biasIn_$x",INPUT))
  val dataOut   = Seq.tabulate(depth)(x => FloatSignal(s"dataOut_$x",OUTPUT))

  val dataInD   = dataIn(0) <-- RANDOM
  val tapInD    = List.tabulate(depth) {x => tapIn(x) <-- RANDOM }
  val biasInD   = List.tabulate(depth) {x => biasIn(x) <-- RANDOM }

  valid := (index(start-1,0) === 0)

  val data = List.tabulate(depth)(x => dataInD * tapInD(x) + biasInD(x))
  def getData(x:Int) = dataOut(x) --> (data(x),s"Results$x")
  val dataOutD = List.tabulate(depth)(x => getData(x))



  val neuron    = new Neuron(dataOut(0),dataIn(0),tapIn(0),biasIn(0))
  val entity  = new ComplexSegment.SegmentEntity(neuron, "neuron")

  val dutParser = new NeuronStage(blockName,valid,dataIn,tapIn,biasIn,dataOut,depth, entity)



  override val dut: NewEntity = dutParser.createEntity


}
