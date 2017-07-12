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
  override def getTestLength = BasicTestInformation.tapLength*512
  // Use and identity matrix for the test
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val plot:Boolean = true
  override val failThreshold = Some(0.1)


  // Fifo depths for test currently constant
  lazy val dataFill      = 6
  lazy val errorFill     = 4
  lazy val outputFill    = 3


  lazy val numberNeurons = 6
  lazy val dimensions = Seq((6,12),(12,6))

  def getInformation = dimensions.zipWithIndex.map(x => {
    NeuralStageInfo(x._1,dataFill,numberNeurons,errorFill,
      outputFill,dataLocation,x._1._2,
      Some(s"${dataLocation}/init_taps${x._2}")
    )
  })


  override lazy val gain = 3
}

class Two6x6x6 extends TwoStageTest {
  override def blockName: String = "two6x6"
  override lazy val dimensions = Seq((6,6),(6,6))
  //override def waveformEnable = true
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS

  override lazy val tapEnable = List(1,0)
  override lazy val biasEnable = List(1,0)
  override lazy val gain = 3
  override def getTestLength = BasicTestInformation.tapLength*512
  override lazy val disableNonlinearity = true

}