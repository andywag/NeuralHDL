package com.simplifide.generate.blocks.neural

/**
  * Created by andy on 6/2/17.
  */
/**
  *
  * @param tapDimension    : Size of the Matrix Multiplication (Input x Output)
  * @param dataLength      : Length of the Input Data (Column of Matrix)
  * @param dataFill        : Amount of memory for Data storage
  * @param errorFill       : Amount of extra tap storage used to contain the errors
  * @param numberNeurons   : Number of MAC Units for this Stage
  * @param dataLocation    : Location where the initial taps are stored for test
  */
case class NeuralStageInfo(tapDimension:(Int,Int),
                           dataLength:Int,
                           dataFill:Int,
                           numberNeurons:Int,
                           errorFill:Int,
                           outputFill:Int,
                           dataLocation:String
                          ) {

  def logWidth(input:Int) = {
    math.ceil(math.log(input)/math.log(2)).toInt
  }


  def logWidthPlus1(input:Int) = {
    val c = math.ceil(math.log(input)/math.log(2)).toInt
    val f = math.floor(math.log(input)/math.log(2)).toInt
    if (f == c) (c + 1) else c
  }

  // Size of the data memory address
  val dataSingleWidth  = logWidth(dataLength)
  // Depth of the memory address
  val dataFillWidth    = logWidthPlus1(dataFill)
  // Width of Data Address (NxK)
  val dataAddressWidth =  dataSingleWidth + dataFillWidth

  // Number of passes through the input data required for each stage
  val stateLength      = tapDimension._2/numberNeurons
  val stateWidth       = logWidth(stateLength)
  // Width of tap address
  val tapAddressLength = (tapDimension._1*tapDimension._2)/numberNeurons
  val tapAddressWidth   = logWidth(tapAddressLength)
  // Width of Bias
  val biasLength       = numberNeurons
  val biasAddressWidth  = dataSingleWidth
  // Output Fifo Information
  val outputFifoDepth = tapDimension._2*outputFill
  val outputFifoAddressWidth = logWidth(outputFifoDepth)

  // FIXME : Need generic
  val memoryWidth = 32;

}
