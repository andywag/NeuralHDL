package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.NeuralStageInfo
import com.simplifide.generate.blocks.neural.simple.DataControl.DataToOutput
import com.simplifide.generate.blocks.neural.simple.ErrorControl.{ErrorToData, ErrorToOutput}
import com.simplifide.generate.parser.EntityParser

/**
  * Created by andy on 6/8/17.
  */
case class OutputCtrl(override val name:String,
                      info:NeuralStageInfo,
                      //params:DataFifo.Params,
                      parent:NeuronControl[_],
                      dataToOutput:DataToOutput,
                      errorToOutput:ErrorToOutput)(implicit clk:ClockControl) extends EntityParser {

  import com.simplifide.generate.newparser.typ.SegmentParser._

  signal(clk.allSignals(INPUT))
  signal(parent.parent.stage.dataOut.asInput)
  signal(parent.parent.stage.dataOutPre.asInput)
  signal(parent.parent.stage.fullOut.asInput)
  signal(parent.parent.stage.dataOutBias.asInput)

  signal(dataToOutput.signals)
  signal(errorToOutput.signals)

  signal(parent.interface.outRdy.reverse)
  signal(parent.interface.outPreRdy.reverse)
  signal(parent.parent.stage.interface.reverse)


  signal(parent.parent.memory.dataBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.parent.memory.tapBank.input.reverse) // Connect this to the data port of the memory
  signal(parent.parent.memory.biasBank.input.reverse) // Connect this to the data port of the memory

  val tapErrorLength        = signal("error_tap_length",INPUT,U(info.tapAddressWidth,0))


  /- ("Data Input Memory Control")
  //dataIn.rdy     := dataReady//(fifoInputDepth <= loadDepth)
  val dataMem = parent.parent.memory.dataBank.input
  dataMem.wrData         := dataToOutput.dataValue // dataIn.value.exp
  dataMem.ctrl.wrAddress := dataToOutput.dataWriteAdd //Operators.Concat(loadDataWrite,loadInputCount)
  dataMem.ctrl.wrVld     := dataToOutput.dataValid //dataIn.rdy & dataIn.vld

  /- ("Data Output Memory Control")
  dataMem.ctrl.rdAddress := dataToOutput.dataReadAdd; //Operators.Concat(readDataDepth, readDataAddress)
  dataMem.ctrl.rdVld     := dataToOutput.activeNormal

  /- ("Tap Output Memory Control")
  val tapMem = parent.parent.memory.tapBank.input
  tapMem.ctrl.rdAddress  :=  ErrorControl.errorUpdateFirst ? (info.tapAddressLength + errorToOutput.errorPhaseRead) :: dataToOutput.tapAddress
  tapMem.ctrl.rdVld      := dataToOutput.activeNormal

  /- ("Tap Input Update Control")
  val rdAddressWire     = signal("rd_address_wire",WIRE,U(info.tapAddressWidth)) !-> tapMem.ctrl.rdAddress
  val rdAddressDelay    = register(rdAddressWire)(5)

  val rdAddressVld      = signal("wr_address_vld",WIRE)
  rdAddressVld          :=  ErrorControl.errorUpdateLatch & ~ErrorControl.errorUpdateFirst;
  val rdAddressVldDelay = register(rdAddressVld)(6)



  /- ("Tap Input Memmory Control") // Fixme : Needs own module

  this.tapMem.ctrl.wrAddress  := rdAddressVldDelay(5) ? rdAddressDelay(5) :: info.tapAddressLength + errorToOutput.errorPhase
  this.tapMem.ctrl.wrVld      := errorToOutput.errorValid | rdAddressVldDelay(5)

  this.tapMem.ctrl.subVld     := rdAddressVldDelay(5) ? 0 :: errorToOutput.errorValid
  this.tapMem.ctrl.subAddress := errorToOutput.errorSubAddress
  this.tapMem.ctrl.subData    := errorToOutput.errorValue
  this.tapMem.wrData          := parent.parent.stage.fullOut



  /- ("Output Driving Control")
  parent.parent.stage.first       := dataToOutput.activeStartD
  parent.parent.stage.dataIn(0)   := parent.parent.memory.dataBank.input.rdData
  parent.parent.stage.biasIn(0)   := parent.parent.memory.biasBank.input.rdData
  for (i <- 0 until info.numberNeurons) {
    val index = ((i+1)*info.memoryWidth-1,info.memoryWidth*i)
    parent.parent.stage.tapIn.s(i)  := parent.parent.memory.tapBank.input.rdData(index)
  }

  /- ("Final Output Control")
  val dataOut = parent.interface.outRdy
  dataOut.value.signals(0)    := parent.parent.stage.dataOut
  dataOut.vld                 := dataToOutput.active

  val dataOutPre = parent.interface.outPreRdy
  dataOutPre.value.signals(0) := parent.parent.stage.dataOutPre
  dataOutPre.vld              := dataToOutput.activePre


  /- ("Bias Output Memory Control")


  val biasMem = parent.parent.memory.biasBank.input
  biasMem.ctrl.rdAddress := dataToOutput.biasAddress//tapMem.ctrl.rdAddress $at (clk)
  biasMem.ctrl.rdVld     := tapMem.ctrl.rdVld//tapMem.ctrl.rdVld $at (clk)
  biasMem.ctrl.wrAddress := dataToOutput.biasWrAddress//tapMem.ctrl.rdAddress $at (clk)
  biasMem.ctrl.wrVld     := rdAddressVldDelay(4)
  biasMem.wrData         := parent.parent.stage.dataOutBias

}

