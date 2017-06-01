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
                            info:NeuralStageTop.Info,
                            dataIn:ReadyValidInterface[T],
                            tapIn:ReadyValidInterface[T],
                            dataOut:ReadyValidInterface[T],
                            dataOutPre:ReadyValidInterface[T],
                            parent:NeuralStageTop[T]
                        )(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(tapIn.signals)
  signal(dataOut.reverse)
  signal(dataOutPre.reverse)

  signal(parent.memory.dataBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.tapBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.biasBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.stage.interface.reverse)

  val dataLength       = signal("load_length",INPUT,U(info.dataSingleWidth,0))
  val loadDepth        = signal("load_depth",INPUT,U(info.dataFillWidth,0))
  val loadFinish       = signal("load_finish",  WIRE, U(1,0))
  val loadInputCount   = signal("load_input_count",REG,U(info.dataSingleWidth,0))
  val loadInputDone    = signal("load_input_done")
  val loadDataWrite    = signal("load_data_write", REG,U(info.dataFillWidth))

  val dataAddress      = signal("data_address",WIRE,U(info.dataSingleWidth,0))


  val fifoEmpty           = signal("fifo_empty")
  val fifoEmptyReg        = signal("fifo_empty_reg",REG)

  val currentInputDepth   = signal("current_input_depth",  REG,U(info.dataFillWidth))

  val stateLength   = signal("state_length",INPUT,U(info.stateWidth,0))
  val stateAddress  = signal("state_address",WIRE,stateLength.fixed)

  val tapAddress    = signal("tap_address",OUTPUT,U(info.tapAddressWith,0))

  val biasLength    = signal("bias_length",INPUT,U(info.biasAddressWith,0))


  val biasAddress   = signal("bias_address",OUTPUT,U(info.biasAddressWith,0))

  val stateDone     = signal("state_done", WIRE, U(1,0))
  val biasStart     = signal("bias_start",   WIRE, U(1,0))
  val biasEnable    = signal("bias_enable",  WIRE, U(1,0))


  /- ("Control for Tap Loading")



  val currentDataRead  = signal("current_data_read",  REG,U(info.dataFillWidth))

  fifoEmpty    := (currentDataRead == loadDataWrite)
  fifoEmptyReg := fifoEmpty $at clk.createEnable(stateDone)

  /- ("Control for Input Data")
  loadInputDone := (loadInputCount === dataLength)
  /- ("Data Input Burst Counter")
  loadInputCount   := $iff (loadInputDone) $then 0 $else_if (dataIn.rdy & dataIn.vld) $then loadInputCount + 1 $at clk
  loadDataWrite    := $iff (loadInputCount === dataLength) $then loadDataWrite + 1 $at clk

  currentInputDepth   := $iff (loadInputDone & 0) $then
    currentInputDepth $else_if (loadInputDone) $then
    currentInputDepth + 1 $else_if (0) $then currentInputDepth - 1  $at clk

  /- ("Data Input Memory Control")
  dataIn.rdy     := (currentInputDepth <= loadDepth)
  val dataMem = parent.memory.dataBank.input
  dataMem.wrData         := dataIn.value.exp
  dataMem.ctrl.wrAddress := Operators.Concat(loadDataWrite,loadInputCount)
  dataMem.ctrl.wrVld     := dataIn.rdy & dataIn.vld
  /- ("Data Output Memory Control")
  val data_read_start       = register("data_start",WIRE)(3)
  val data_read_active      = register("data_active",WIRE)(info.dataLength + 6)

  data_read_start(0)   := (loadFinish | loadInputDone) & (currentInputDepth >= 0)
  data_read_active(0)  := (currentInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty)
  currentDataRead := $iff (stateDone) $then currentDataRead + 1 $at clk

  dataMem.ctrl.rdAddress := Operators.Concat(currentDataRead, dataAddress)
  dataMem.ctrl.rdVld     := data_read_active(0) | data_read_active(info.dataLength)
  /- ("Tap Output Memory Control")
  val tapMem = parent.memory.tapBank.input
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
  loadFinish     := (dataAddress === dataLength)
  stateDone    := (stateAddress === stateLength) & (loadFinish)
  biasStart    := loadFinish & (stateAddress === 0)
  biasEnable   := (dataAddress <= biasLength) & (dataAddress > 0)


  /- ("Internal Counter for which state the operation is in")
  // FIXME : Needs proper counter reset if not multiple of 2
  stateAddress := $iff (loadFinish) $then stateAddress + 1 $at clk
  /- ("Data Address")
  dataAddress  := $iff (data_read_start | loadFinish) $then 0 $else_if (data_read_active| data_read_active(info.dataLength)) $then dataAddress + 1 $at clk
  /- ("Tap Address")
  tapAddress   := $iff (stateDone) $then 0 $else_if (data_read_active | data_read_active(info.dataLength)) $then (tapAddress + 1) $at clk
  /- ("Bias Address")
  biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk

}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)
}