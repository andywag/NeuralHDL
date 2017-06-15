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
  val dataFill      = 4
  val errorFill     = 4
  val outputFill    = 3

  def getInformation(dataLocation:String) = NeuralStageInfo((dataLength,outputLength),dataLength,dataFill,
    numberNeurons,errorFill,outputFill,dataLocation)

  /*val trainingData =
    """1 0 0 0 0 0 1 0 0 0 0 0
0 1 0 0 0 0 0 1 0 0 0 0
0 0 1 0 0 0 0 0 1 0 0 0
0 0 0 1 0 0 0 0 0 1 0 0
0 0 0 0 1 0 0 0 0 0 1 0
0 0 0 0 0 1 0 0 0 0 0 1"""
*/
  val trainingData =
    """1 0 0 0 0 0 1 0 0 0 0 0
0 1 0 0 0 0 0 1 0 0 0 0
0 0 1 0 0 0 0 0 1 0 0 0
0 0 0 1 0 0 0 0 0 1 0 0
0 0 0 0 1 0 0 0 0 0 1 0
0 0 0 0 0 1 0 0 0 0 0 1
"""

  /*
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
*/

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

  /*
  val trainingData =
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
*/

  val trainArray = {
    def hl(input:String) = {
      input.split(" ").slice(0,6).map(x => math.max(Integer.parseInt(x).toFloat,.515625))
    }
    def h2(input:String) = {
      input.split(" ").slice(0,6).map(x => Integer.parseInt(x))
    }
    val linesIn = trainingData.split("\n").map(x => hl(x))
    val linesOut = trainingData.split("\n").map(x => hl(x))

    (linesIn,linesOut)
  }

  def getTraining = {
    val arr = trainArray
    val input =  Nd4j.create(arr._1).transpose()
    val output = Nd4j.create(arr._2).transpose()
    (input,output)
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



}
