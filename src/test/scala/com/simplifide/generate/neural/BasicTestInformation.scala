package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.NeuralStageInfo
import org.nd4j.linalg.factory.Nd4j

/**
  * Created by andy on 6/3/17.
  */
object BasicTestInformation {

  lazy val dataLength    = 6
  lazy val outputLength  = 12
  lazy val tapLength     = dataLength*outputLength
  val biasLength    = 12
  val numberNeurons = 6
  val dataFill      = 6
  val errorFill     = 4
  val outputFill    = 3

  // First Stage of data contains a 6x12 network stage
  def getInformation(dataLocation:String) = NeuralStageInfo((dataLength,outputLength),
    dataLength,
    dataFill,
    numberNeurons,
    errorFill,
    outputFill,
    dataLocation,
    12,
    Some(s"${dataLocation}/init_taps0"))

  // First Stage of data contains a 6x12 network stage
  def getInformationH(dataLocation:String) = NeuralStageInfo((outputLength,outputLength),
    outputLength,
    dataFill,
    numberNeurons,
    errorFill,
    outputFill,
    dataLocation,
    12,
    Some(s"${dataLocation}/init_taps0"))

  // Second Stage of data contains a 12x6 network stage
  def getInformation2(dataLocation:String) = NeuralStageInfo((outputLength,dataLength),
    outputLength,
    dataFill,
    numberNeurons,
    errorFill,
    outputFill,
    dataLocation,
    6,
    Some(s"${dataLocation}/init_taps1"))

  def getSingleInformation(loc:String) = Seq(getInformation(loc))
  def getDualInformation(loc:String) = Seq(getInformation(loc),getInformation2(loc))
  def getThreeInformation(loc:String) = Seq(getInformation(loc),getInformationH(loc),getInformation2(loc))





val trainingData =
    """1 0 0 0 0 0 1 0 0 0 0 0
"""

def inIdent(a:Int,b:Int)  = Array.tabulate(a,b)((x,y) =>
  if (y == x) (if (x % 2 == 0) 1.0 else -1.0) else 0.0)

def getTrainIdent(a:Int,b:Int) = {

  val input =  Nd4j.create(inIdent(a,a))
  val output = Nd4j.create(inIdent(b,a))
  (input,output)
}

  /*
  def inTest(a:Int,b:Int,o:Int=0)  = Array.tabulate(a,b)((x,y) =>
    if (x < 6) {
      if (y == x) (if (x % 2 == 0) 1.0 else -1.0) else 0.0
    }
    else if (x < 12) {
       if (y == (x+o)%6 | y == (x+1+o)%6) (if (x % 2 == 0) 1.0 else -1.0) else 0.0
    }
    else {
      if ((y) == ((x+o)%6) | y == ((x+o)%6)+1) (if (x % 2 == 0) 1.0 else -1.0) else 0.0
    })
    */
  val trainingData1 = List(
    Array(1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0),
    Array(0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0),
    Array(0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0),
    Array(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0),
    Array(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1),
  Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0),
  Array(1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0),
  Array(1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0),
  Array(1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0),
  Array(1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0),
  Array(0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0),
  Array(1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0),
  Array(0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0),
  Array(1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0),
  Array(1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0),
  Array(1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0),
  Array(1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0),
  Array(1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0),
  Array(1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0),
  Array(1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0),
  Array(0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
  Array(0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0),
  Array(1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0),
  Array(1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0),
  Array(1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0),
  Array(1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0),
  Array(1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0),
  Array(1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0))


  lazy val getTrainTest = {
    val input1 = trainingData1.map(x => x.slice(0,6).map(_.toDouble))
    val output1 = trainingData1.map(x => x.slice(6,12).map(_.toDouble))

    val input = Nd4j.create(input1.toArray).transpose()
    val output = Nd4j.create(output1.toArray).transpose()

    (input,output)
  }


  val identTaps ="""1 0 0 0 0 0
0 1 0 0 0 0
0 0 1 0 0 0
0 0 0 1 0 0
0 0 0 0 1 0
0 0 0 0 0 1
1 0 0 0 0 0
0 1 0 0 0 0
0 0 1 0 0 0
0 0 0 1 0 0
0 0 0 0 1 0
0 0 0 0 0 1""".stripMargin

/*
  val identTaps ="""0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0""".stripMargin
*/

  val trainingDataB ="""1 0 0 0 0 0 1 0 0 0 0"""

  val trainingDataA =
    """1 0 0 0 0 0 0 0 0 0 1
0 1 0 0 0 0 0 0 0 1 0
0 0 1 0 0 0 0 0 1 0 0
1 0 1 0 0 0 0 0 0 1 0
1 0 1 0 1 1 0 0 0 1 1
1 1 1 1 0 0 0 0 1 0 0
1 0 1 0 0 0 0 0 1 1 0
1 0 0 1 0 0 0 0 1 1 1
0 1 1 0 1 1 0 1 0 0 0
1 0 0 1 1 1 0 1 0 0 1
0 1 1 1 0 0 0 1 0 1 1
1 0 0 0 1 0 0 1 1 0 0
1 1 1 0 0 0 0 1 1 0 1
1 0 1 1 0 1 0 1 1 1 0
1 1 0 1 1 0 1 0 0 0 0
1 1 1 0 0 1 1 0 0 0 1
1 0 0 1 1 0 1 0 0 1 0
1 1 1 0 1 0 1 0 0 1 1
0 1 1 0 1 0 1 0 1 0 1
0 1 1 1 1 0 1 0 1 1 0
1 1 0 0 0 1 1 0 1 1 1
1 0 0 0 1 1 1 1 0 0 0
1 0 1 1 0 0 1 1 0 1 0
1 1 0 0 0 0 1 1 0 1 1
1 0 0 0 0 1 1 1 1 0 0
1 1 1 1 0 1 1 1 1 0 1"""


  def trainArray(out:Int) = {
    def hl(input:String) = {
      input.split(" ").slice(0,6).map(x => Integer.parseInt(x).toFloat)
    }
    def h2(input:String) = {
      input.split(" ").slice(0,out).map(x => Integer.parseInt(x))
    }
    val linesIn = trainingData.split("\n").map(x => hl(x))
    val linesOut = trainingData.split("\n").map(x => hl(x))

    (linesIn,linesOut)
  }

  def getTraining = {
    /*val arr = trainArray(12)
    val input =  Nd4j.create(arr._1).transpose()
    val output = Nd4j.create(arr._2).transpose()
    (input,output)
    */
    getTrainIdent(6,12)
  }

  def getInitTaps = {
    def hl(input:String) = {
      input.split(" ").slice(0,6).map(x => .5*Integer.parseInt(x).toFloat)
    }
    val arr = identTaps
    val data1 = arr.split("\n")
    val data2 = data1.map(x => hl(x))
    val input =  Nd4j.create(data2)
    input
  }


  def getRandomTaps(x:Int, y:Int) = {
    val input =  Nd4j.randn(x,y).mul(.25)
    input
  }



}
