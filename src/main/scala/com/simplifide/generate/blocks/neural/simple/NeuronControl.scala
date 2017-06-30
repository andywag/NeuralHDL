package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.neural.NeuralStageTop.ControlStruct
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
  signal(parent.stage.dataOutBias.changeType(INPUT))

  // Attach a Control Structure as input to thsiblock
  val controlInterface = new ControlStruct(parent.appendName("ctrl_int"),this.info,INPUT)
  signal(controlInterface)
  // Control connections
  // This is a bit of a kludge but the values are reassigned to the original signals to avoid complications
  // FIXME : This should be cleaned up
  private val dataLength       = signal("load_length",WIRE,U(info.dataSingleWidth,0))
  private val loadDepth        = signal("load_depth",WIRE,U(info.dataFillWidth,0))
  private val stateLength      = signal("state_length",WIRE,U(info.stateWidth,0))
  private val tapErrorLength   = signal("error_tap_length",WIRE,U(info.tapAddressWidth,0))
  val inputStage       = signal("input_stage")

  val tapEnable        = signal(controlInterface.tapEnable.newSignal(name = "tap_enable",opType = WIRE))
  val biasEnable        = signal(controlInterface.biasEnable.newSignal(name = "bias_enable",opType = WIRE))


  dataLength     := controlInterface.loadLength
  loadDepth      := controlInterface.loadDepth
  stateLength    := controlInterface.stateLength
  tapErrorLength := controlInterface.errorLength
  inputStage     := controlInterface.inputStage
  tapEnable      := controlInterface.tapEnable
  biasEnable     := controlInterface.biasEnable
  // Datalength of this stage - used to count both input and output
  //


  // Create the Data Input FIFO
  val params = DataControl.Params(info.dataLength,
    info.dataFill,
    info.stateLength,
    info.tapAddressLength,
    info.errorFill,
  info.numberNeurons)



  val dataToOutput = new DataToOutput(params)
  val errorToOutput = new ErrorToOutput(params)

  val dataFifo = new DataControl(appendName("data_fifo"),params,this,dataToOutput,dataIn)
  val con1 = Map(dataFifo.loadLength -> this.controlInterface.loadLength,
    dataFifo.loadDepth -> this.controlInterface.loadDepth,
    dataFifo.stateFinish -> this.controlInterface.stateLength
  )
  instance(dataFifo.createEntity,dataFifo.name)

  val errorFifo = new ErrorControl(appendName("error_fifo"),params,this,errorIn)
  val errorCon = Map(errorFifo.loadLength -> this.controlInterface.loadLength)
  instance(errorFifo.createEntity,errorFifo.name)

  val outputEntity = new OutputCtrl(appendName("out_ctrl"),info,this,dataToOutput,errorToOutput)
  instance(outputEntity.createEntity,outputEntity.name)

  // FIXME : I'm not sure why this is here but it is required
  //val tapErrorLength        = signal("error_tap_length",INPUT,U(info.tapAddressWidth,0))




}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)






}