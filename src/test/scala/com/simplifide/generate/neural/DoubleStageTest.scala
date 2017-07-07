package com.simplifide.generate.neural

/**
  * Created by andy on 6/3/17.
  */
class DoubleStageTest extends BasicNetworkTest {
  /** Clock for the testbench */
  override def blockName: String = "full"

  // Get the description of this network from a common location due to it's use in many tests
  //val information = BasicTestInformation.getDualInformation(dataLocation)
  def getInformation =  BasicTestInformation.getDualInformation(dataLocation)
  // Set the test length
  override def getTestLength = BasicTestInformation.tapLength*512
  // Use and identity matrix for the test
  override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.IDENT_TYPE
  override val plot:Boolean = true
  override val failThreshold = Some(0.1)

  /** Design Under Test */


  override lazy val inputSize = 36

  // Control signals used for network stage

  override lazy val gain = 3




}
