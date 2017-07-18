package com.simplifide.generate.neural

/**
  * Created by andy on 7/17/17.
  */
class MultiStageTest {

}

class Three6x6x6I extends TwoStageTest {

  override lazy val dataFill      = Seq(20,40,20)
  override lazy val errorFill     = Seq(40,40,40)
  override lazy val outputFill    = Seq(40,40,40)

  override def blockName: String = "three6x6"
  override lazy val numberNeurons = Seq(6,12,6)
  override lazy val dimensions = Seq((6,12),(12,12),(12,6))
  override def waveformEnable = true
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS

  //override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.BRAILE_TYPE


  override lazy val tapEnable = List(0,0,0)
  override lazy val biasEnable = List(0,0,0)
  override lazy val gain = Seq(9,7,5).map(x => x-1)
  //override lazy val biasGain = 3
  override def getTestLength = BasicTestInformation.tapLength*math.pow(2.0,9).toInt
  override lazy val disableNonlinearity = true
  //override lazy val tapScale:Seq[Double] = Seq(1.0,2.0,.5)
  override lazy val tapScale:Seq[Double] = Seq(1.0,1.0,0.5)


}

class Three6x6x6 extends TwoStageTest {

  override lazy val dataFill      = Seq(20,40,20)
  override lazy val errorFill     = Seq(10,10,10)
  override lazy val outputFill    = Seq(10,10,10)

  override def blockName: String = "three6x6a"
  override lazy val numberNeurons = Seq(6,6,6)
  override lazy val dimensions = Seq((6,6),(6,6),(6,6))
  //override def waveformEnable = true
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS

  //override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.BRAILE_TYPE


  override lazy val tapEnable = List(1,1,1)
  override lazy val biasEnable = List(1,1,1)
  override lazy val gain = Seq(9,7,5).map(x => x-1)
  //override lazy val biasGain = 3
  override def getTestLength = BasicTestInformation.tapLength*math.pow(2.0,13).toInt
  override lazy val disableNonlinearity = true
  //override lazy val tapScale:Seq[Double] = Seq(1.0,2.0,.5)
  override lazy val tapScale:Seq[Double] = Seq(.5,.5,.5)


}

class Four6x6x6 extends TwoStageTest {

  override lazy val dataFill      = Seq(20,20,40,20)
  override lazy val errorFill     = Seq(10,10,10,10)
  override lazy val outputFill    = Seq(10,10,10,10)

  override def blockName: String = "four6x6"
  override lazy val numberNeurons = Seq(6,6,6,6)
  override lazy val dimensions = Seq((6,6),(6,6),(6,6),(6,6))
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.RAND_TAPS

  //override def waveformEnable = true
  //override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.IDENT_TAPS

  //override lazy val inputType:BasicNetworkTest.INPUT_TYPE = BasicNetworkTest.BRAILE_TYPE


  override lazy val tapEnable = List(1,1,1,1)
  override lazy val biasEnable = List(1,1,1,1)
  override lazy val gain = Seq(15,11,9,7).map(x => x)
  //override lazy val biasGain = 3
  override def getTestLength = BasicTestInformation.tapLength*math.pow(2.0,13).toInt
  override lazy val disableNonlinearity = true
  //override lazy val tapScale:Seq[Double] = Seq(1.0,2.0,.5)
  override lazy val tapScale:Seq[Double] = Seq(.5,.5,.5,.5)


}

class FourSame(siz:Int) extends TwoStageTest {
  override lazy val dataFill      = Seq(20,20,40,20)
  override lazy val errorFill     = Seq(10,10,10,10)
  override lazy val outputFill    = Seq(10,10,10,10)
  override def blockName: String = s"four${siz}${siz}"
  override lazy val numberNeurons = Seq(siz,siz,siz,siz,siz)
  override lazy val dimensions = Seq((siz,siz),(siz,siz),(siz,siz),(siz,siz))
  override lazy val tapType:BasicNetworkTest.TAP_TYPE = BasicNetworkTest.RAND_TAPS

  override lazy val tapEnable = List(1,1,1,1)
  override lazy val biasEnable = List(1,1,1,1)
  override lazy val gain = Seq(15,11,9,7).map(x => x)
  //override lazy val biasGain = 3
  override def getTestLength = BasicTestInformation.tapLength*math.pow(2.0,12).toInt
  override lazy val disableNonlinearity = true
  //override lazy val tapScale:Seq[Double] = Seq(1.0,2.0,.5)
  override lazy val tapScale:Seq[Double] = Seq(.5,.5,.5,.5)

}

class Four12 extends FourSame(12) {

}

class Four15 extends FourSame(15) {

}
