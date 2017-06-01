package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 5/26/17.
  */
class NeuralStageTop[T](val name:String,
                       info:NeuralStageTop.Info,
                     val dataIn:ReadyValidInterface[T],
                     val tapIn:ReadyValidInterface[T],
                     val dataOut:ReadyValidInterface[T],
                     val dataPreOut:ReadyValidInterface[T])(implicit clk:ClockControl) extends EntityParser {

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(dataOut.reverse)
  signal(dataPreOut.reverse)

  val stage = new NeuralStage(appendName("st"),info.numberNeurons)
  instance(stage)

  val memorySize = NeuralMemory.Dimensions(info.tapDimension,info.numberNeurons,info.dataFill)
  val memory = new NeuralMemory(appendName("mem"),memorySize, info)
  instance(memory)

  val control    = new NeuronControl[T](appendName("ctrl"),info, dataIn, tapIn, dataOut,dataPreOut, this)
  instance(control)


  //this.createNeuronStage

}

object NeuralStageTop {
  case class Info(tapDimension:(Int,Int),
                  dataLength:Int,
                  dataFill:Int,
                  numberNeurons:Int,
                  dataLocation:String
                 ) {

    def logWidth(input:Int) = {
      val c = math.ceil(math.log(input)/math.log(2)).toInt
      val f = math.floor(math.log(input)/math.log(2)).toInt
      if (f == c) c + 1 else c
      c
    }

    def logWidth2(input:Int) = {
      val c = math.ceil(math.log(input)/math.log(2)).toInt
      val f = math.floor(math.log(input)/math.log(2)).toInt
      if (f == c) (c + 1) else c
    }

    val dataSingleWidth  = logWidth(dataLength)
    val dataFillWidth    = logWidth2(dataFill)
    // Width of Data Address
    val dataAddressWidth =  dataSingleWidth + dataFillWidth
    // Width of number of passes of data required for this run
    val stateLength      = tapDimension._2/numberNeurons
    val stateWidth       = logWidth(stateLength)
    // Width of tap address
    val tapAddressLength = (tapDimension._1*tapDimension._2)/numberNeurons
    val tapAddressWith   = logWidth(tapAddressLength)
    // Width of Bias
    val biasLength       = numberNeurons
    val biasAddressWith  = dataSingleWidth

    // FIXME : Need generic
    val memoryWidth = 32;

  }
}
