package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.NeuralStageInfo

/**
  * Created by andy on 7/10/17.
  */
class TwoStageTest() extends BasicNetworkTest{
  /** Clock for the testbench */
  override def blockName: String = "two"

  // Get the description of this network from a common location due to it's use in many tests
  //val information = BasicTestInformation.getDualInformation(dataLocation)
  // Set the test length
  // Use and identity matrix for the test
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val plot:Boolean = true
  override val failThreshold = Some(0.1)
  override lazy val tapScale:Seq[Double] = Seq(.5,.5)


  override lazy val tapEnable = List(1,1)
  override lazy val biasEnable = List(1,1)
  override def getTestLength = BasicTestInformation.tapLength*1024

  // Fifo depths for test currently constant
  lazy val dataFill      = Seq(10,10)
  lazy val errorFill     = Seq(10,10)
  lazy val outputFill    = Seq(10,10)


  lazy val numberNeurons = Seq(6,6)
  lazy val dimensions = Seq((6,12),(12,6))

  def getInformation = dimensions.zipWithIndex.map(x => {
    NeuralStageInfo(x._1,
      dataFill(x._2),
      numberNeurons(x._2),
      errorFill(x._2),
      outputFill(x._2),
      dataLocation,x._1._2,
      Some(s"${dataLocation}/init_taps${x._2}")
    )
  })


  override lazy val gain = Seq(3,3)
}

class BaseTwo(val first:Int, val middle:Int) extends TwoStageTest {
  override def blockName: String = s"two${first}_${middle}"

  override lazy val numberNeurons = Seq(first,first)
  override lazy val dimensions = Seq((first,middle),(middle,first))
  override lazy val gain = Seq(6,3)
  override lazy val disableNonlinearity = true
}

class Two6x6x6 extends BaseTwo(6,6) {
  override lazy val tapScale:Seq[Double] = Seq(.25,.25)
}

class Two6x6_B extends BaseTwo(6,6) {
  override def blockName: String = s"two${first}_${middle}_b"
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.BRAILE_TYPE
  override lazy val tapScale:Seq[Double] = Seq(.25,.25)
}

class Two10x10x10 extends BaseTwo(10,10) {
  override lazy val tapScale:Seq[Double] = Seq(.5,.5)
}

class Two6x12x6 extends BaseTwo(6,12) {

}





