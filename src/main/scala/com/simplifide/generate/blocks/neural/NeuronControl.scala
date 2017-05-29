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
                          dimension:NeuronControl.Dimension,
                          dataIn:ReadyValidInterface[T],
                           parent:NeuralStageTop[T]
                        )(implicit clk:ClockControl) extends EntityParser{

  override def createBody() {}

  signal(clk.allSignals(INPUT))
  signal(dataIn.signals)
  signal(parent.memory.dataBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.tapBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.memory.biasBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.stage.interface.reverse)

  val dataLength    = signal("load_length",INPUT,U(info.dataAddressWidth,0))
  val dataDepth     = signal("load_depth",INPUT,U(info.dataAddressWidth,0))
  val dataAddress   = signal("load_address",OUTPUT,U(info.dataAddressWidth,0))
  val dataDone      = signal("load_finish",  WIRE, U(1,0))

  val stateLength   = signal("state_length",INPUT,U(info.stateWidth,0))
  val stateAddress  = signal("state_address",WIRE,stateLength.fixed)

  val tapAddress    = signal("tap_address",OUTPUT,U(info.tapAddressWith,0))

  val biasLength    = signal("bias_length",INPUT,U(dimension.biasAddWidth,0))


  val biasAddress   = signal("bias_address",OUTPUT,U(dimension.biasAddWidth,0))

  val stateDone     = signal("state_finish", WIRE, U(1,0))
  val biasStart     = signal("bias_start",   WIRE, U(1,0))
  val biasEnable    = signal("bias_enable",  WIRE, U(1,0))



  /- ("Control for Input Data")

  val currentDataWrite = signal("current_data_write", REG,U(info.dataFillWidth))
  val currentDataRead  = signal("current_data_read",  REG,U(info.dataFillWidth))
  val dataInputDepth   = signal("current_input_depth",  REG,U(info.dataFillWidth))

  val dataInputCount   = signal("data_count",REG,U(info.dataSingleWidth,0))

  val inputDone = signal("data_input_done")
  inputDone := (dataInputCount === dataLength)
  /- ("Data Input Burst Counter")
  dataInputCount   := $iff (inputDone) $then 0 $else_if (dataIn.rdy & dataIn.vld) $then dataInputCount + 1 $at clk
  currentDataWrite := $iff (dataInputCount === dataLength) $then currentDataWrite + 1 $at clk
  dataInputDepth   := $iff (inputDone) $then dataInputDepth + 1 $at clk
  /- ("Data Input Memory Control")
  dataIn.rdy     := (dataInputDepth < dataDepth)
  val dataMem = parent.memory.dataBank.input
  dataMem.wrData         := dataIn.value.exp
  dataMem.ctrl.wrAddress := Operators.Concat(currentDataWrite,dataInputCount)
  dataMem.ctrl.wrVld     := dataIn.rdy & dataIn.vld
  /- ("Data Output Memory Control")
  val data_read_start       = register("data_start",WIRE)(3)
  val data_read_active      = signal("data_active",WIRE)

  data_read_start(0)   := (inputDone) & (dataInputDepth >= 0)
  data_read_active  := (dataInputDepth > 0)
  currentDataRead := $iff (stateDone) $then currentDataRead + 1 $at clk

  dataMem.ctrl.rdAddress := Operators.Concat(currentDataRead, dataAddress)
  dataMem.ctrl.rdVld     := data_read_active
  /- ("Tap Output Memory Control")
  val tapMem = parent.memory.tapBank.input
  tapMem.ctrl.rdAddress  := tapAddress
  tapMem.ctrl.rdVld      := data_read_active
  /- ("Bias Output Memory Control")
  val biasMem = parent.memory.biasBank.input
  biasMem.ctrl.rdAddress := biasAddress
  biasMem.ctrl.rdVld     := biasEnable
  /- ("Output Driving Control")
  parent.stage.valid       := data_read_start(3)
  parent.stage.dataIn(0)   := parent.memory.dataBank.input.rdData
  parent.stage.biasIn(0)   := parent.memory.biasBank.input.rdData
  for (i <- 0 until info.numberNeurons) {
    val index = ((i+1)*info.memoryWidth-1,info.memoryWidth*i)
    parent.stage.tapIn.s(i)  := parent.memory.tapBank.input.rdData(index)
  }

  /- ("Counter Controls")
  dataDone     := (dataAddress === dataLength)
  stateDone    := (stateAddress === stateLength)
  biasStart    := dataDone & (stateAddress === 0)
  biasEnable   := (dataAddress <= biasLength) & (dataAddress > 0)


  /- ("Internal Counter for which state the operation is in")
  stateAddress := $iff (stateDone) $then 0 $else_if (dataDone) $then stateAddress + 1 $at clk
  /- ("Data Address")
  dataAddress  := $iff (data_read_start | dataDone) $then 0 $else_if (data_read_active) $then dataAddress + 1 $at clk
  /- ("Tap Address")
  tapAddress   := $iff (stateDone) $then 0 $else_if (data_read_active) $then (tapAddress + 1) $at clk
  /- ("Bias Address")
  biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk

}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)
}