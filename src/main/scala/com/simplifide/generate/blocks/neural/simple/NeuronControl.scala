package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.neural.simple.DataControl.DataToOutput
import com.simplifide.generate.blocks.neural.simple.ErrorControl.ErrorToOutput
import com.simplifide.generate.blocks.neural.{NeuralStageInfo, NeuralStageInterface, NeuralStageTop}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}

/**
  * Created by andy on 5/26/17.
  */
case class NeuronControl[T](override val name:String,
                            info:NeuralStageInfo,
                            interface:NeuralStageInterface[T],
                            parent:NeuralStageTop[T]
                        )(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}

  val dataIn = interface.inRdy
  val tapIn  = interface.tapRdy
  val errorIn = interface.errorRdy

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(tapIn.signals)
  signal(errorIn.signals)

  signal(parent.memory.dataBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.tapBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.biasBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.stage.interface.reverse)

  // Datalength of this stage - used to count both input and output
  val dataLength       = signal("load_length",INPUT,U(info.dataSingleWidth,0))
  val loadDepth        = signal("load_depth",INPUT,U(info.dataFillWidth,0))
  val stateLength      = signal("state_length",INPUT,U(info.stateWidth,0))

  // Create the Data Input FIFO
  val params = DataControl.Params(info.dataLength,info.dataFill, info.stateLength,info.tapAddressLength, info.errorFill)
  val dataToOutput = new DataToOutput(params)
  val errorToOutput = new ErrorToOutput(params)

  val dataFifo = new DataControl(appendName("data_fifo"),params,this,dataToOutput,dataIn)
  val con1 = Map(dataFifo.loadLength -> this.dataLength, dataFifo.loadDepth -> this.loadDepth)
  instance(dataFifo.createEntity,dataFifo.name,con1)

  val errorFifo = new ErrorControl(appendName("error_fifo"),params,this,errorIn)
  instance(errorFifo.createEntity,errorFifo.name)

  val outputEntity = new OutputCtrl(appendName("out_ctrl"),info,this,dataToOutput,errorToOutput)
  instance(outputEntity.createEntity,outputEntity.name)

  // FIXME : I'm not sure why this is here but it is required
  val tapErrorLength        = signal("error_tap_length",INPUT,U(info.tapAddressWidth,0))




}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)






}