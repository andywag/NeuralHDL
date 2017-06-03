package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.ReadyValid
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 5/26/17.
  */
case class NeuronControl[T](override val name:String,
                            info:NeuralStageInfo,
                            interface:NeuralStageTop.Interface[T],
                            parent:NeuralStageTop[T]
                        )(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}

  val dataIn = interface.inRdy
  val tapIn  = interface.tapRdy
  val errorIn = interface.errorRdy
  val dataOut = interface.outRdy
  val dataOutPre = interface.outPreRdy

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(tapIn.signals)
  signal(errorIn.signals)
  signal(dataOut.reverse)
  signal(dataOutPre.reverse)

  signal(parent.memory.dataBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.tapBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.biasBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.stage.interface.reverse)

  // Datalength of this stage - used to count both input and output
  val dataLength       = signal("load_length",INPUT,U(info.dataSingleWidth,0))
  // Depth of the FIFO programmable register
  val loadDepth        = signal("load_depth",INPUT,U(info.dataFillWidth,0))
  // Control Parameters for loading the input data and counting
  val loadFinish       = signal("load_finish",  WIRE, U(1,0))
  val loadInputCount   = signal("load_input_count",REG,U(info.dataSingleWidth,0))
  val loadInputDone    = signal("load_input_done")
  val loadDataWrite    = signal("load_data_write", REG,U(info.dataFillWidth))
  // Control signals for reading the data out of the FIFO
  val readDataAddress      = signal("read_data_address",WIRE,U(info.dataSingleWidth,0))
  val readDataDepth  = signal("read_data_depth",  REG,U(info.dataFillWidth))
  // Control signals for tap address generation
  val tapAddress    = signal("tap_address",OUTPUT,U(info.tapAddressWidth,0))
  // Bias Control Signals
  val biasLength    = signal("bias_length",INPUT,U(info.biasAddressWidth,0))
  val biasAddress   = signal("bias_address",OUTPUT,U(info.biasAddressWidth,0))
  val biasStart     = signal("bias_start",   WIRE, U(1,0))
  val biasEnable    = signal("bias_enable",  WIRE, U(1,0))
  // Control signals for sharing if the number of outputs is greater than the number of neurons
  val stateLength   = signal("state_length",INPUT,U(info.stateWidth,0))
  val stateAddress  = signal("state_address",WIRE,stateLength.fixed)
  val stateDone     = signal("state_done", WIRE, U(1,0))
  // Fifo Control Signals
  val fifoEmpty           = signal("fifo_empty")
  val fifoEmptyReg        = signal("fifo_empty_reg",REG)
  val fifoInputDepth      = signal("fifo_input_depth",  REG,U(info.dataFillWidth))


  /- ("Fifo Controls - Used to Gate Inputs")
  fifoEmpty    := (readDataDepth == loadDataWrite)
  fifoEmptyReg := fifoEmpty $at clk.createEnable(stateDone)
  fifoInputDepth   := $iff (loadInputDone & 0) $then
    fifoInputDepth $else_if (loadInputDone) $then
    fifoInputDepth + 1 $else_if (0) $then fifoInputDepth - 1  $at clk

  /- ("Control for Input Data")
  loadInputDone := (loadInputCount === dataLength)

  /- ("Data Input Burst Counter")
  loadInputCount   := $iff (loadInputDone) $then 0 $else_if (dataIn.rdy & dataIn.vld) $then loadInputCount + 1 $at clk
  loadDataWrite    := $iff (loadInputCount === dataLength) $then loadDataWrite + 1 $at clk

  /- ("Data Input Memory Control")
  dataIn.rdy     := (fifoInputDepth <= loadDepth)
  val dataMem = parent.memory.dataBank.input
  dataMem.wrData         := dataIn.value.exp
  dataMem.ctrl.wrAddress := Operators.Concat(loadDataWrite,loadInputCount)
  dataMem.ctrl.wrVld     := dataIn.rdy & dataIn.vld

  /- ("Data Output Memory Control")
  val data_read_start       = register("data_start",WIRE)(3)
  val data_read_active      = register("data_active",WIRE)(info.dataLength + 6)
  data_read_start(0)   := (loadFinish | loadInputDone) & (fifoInputDepth >= 0)
  data_read_active(0)  := (fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty)
  readDataDepth := $iff (stateDone) $then readDataDepth + 1 $at clk
  dataMem.ctrl.rdAddress := Operators.Concat(readDataDepth, readDataAddress)
  dataMem.ctrl.rdVld     := data_read_active(0) | data_read_active(info.dataLength)

  val tapMem = parent.memory.tapBank.input
  /- ("Tap Input Memmory Control")
  val errorCount = signal("errorCount",REG,U(info.tapDimension._2,0))
  errorCount := (errorCount + 1).$at(clk.createEnable(errorIn.vld))
  this.tapMem.ctrl.wrAddress  := info.tapAddressLength

  this.tapMem.ctrl.wrVld      := errorIn.vld
  this.tapMem.ctrl.subVld     := errorIn.vld
  this.tapMem.ctrl.subAddress := errorCount
  this.tapMem.ctrl.subData    := errorIn.value.value

  /- ("Tap Output Memory Control")
  tapMem.ctrl.rdAddress  := tapAddress
  tapMem.ctrl.rdVld      := data_read_active(0) | data_read_active(info.dataLength)

  /- ("Bias Output Memory Control")
  val biasMem = parent.memory.biasBank.input
  biasMem.ctrl.rdAddress := biasAddress
  biasMem.ctrl.rdVld     := biasEnable
  /- ("Output Driving Control")
  parent.stage.first       := data_read_start(3)
  parent.stage.dataIn(0)   := parent.memory.dataBank.input.rdData
  parent.stage.biasIn(0)   := parent.memory.biasBank.input.rdData
  for (i <- 0 until info.numberNeurons) {
    val index = ((i+1)*info.memoryWidth-1,info.memoryWidth*i)
    parent.stage.tapIn.s(i)  := parent.memory.tapBank.input.rdData(index)
  }
  /- ("Final Output Control")
  dataOut.value.signals(0)    := parent.stage.dataOut
  dataOut.vld                 := data_read_active(info.dataLength + 6)

  dataOutPre.value.signals(0) := parent.stage.dataOutPre
  dataOutPre.vld              := data_read_active(info.dataLength + 4)


  /- ("Counter Controls")
  loadFinish     := (readDataAddress === dataLength)
  stateDone    := (stateAddress === stateLength) & (loadFinish)
  biasStart    := loadFinish & (stateAddress === 0)
  biasEnable   := (readDataAddress <= biasLength) & (readDataAddress > 0)

  /- ("Internal Counter for which state the operation is in")
  stateAddress := $iff (loadFinish) $then stateAddress + 1 $at clk
  /- ("Data Address")
  readDataAddress  := $iff (data_read_start | loadFinish) $then 0 $else_if (data_read_active| data_read_active(info.dataLength)) $then readDataAddress + 1 $at clk
  /- ("Tap Address")
  tapAddress   := $iff (stateDone) $then 0 $else_if (data_read_active | data_read_active(info.dataLength)) $then (tapAddress + 1) $at clk
  /- ("Bias Address")
  biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk

}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)
}