package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Counter
import com.simplifide.generate.blocks.basic.misc.Counter.Simple
import com.simplifide.generate.blocks.neural.simple.ErrorControl.ErrorToData
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * Created by andy on 6/8/17.
  */
case class ErrorControl(override val name:String,
                        params:DataControl.Params,
                        parent:NeuronControl[_],
                        input:ReadyValidInterface[_])(implicit clk:ClockControl) extends EntityParser {

  signal(clk.allSignals(INPUT))
  signal(input.signals)
  signal(ErrorToData.reverse)
  signal(parent.errorToOutput.reverse)

  signal(parent.parent.stage.errorMode.changeType(OUTPUT))
  signal(parent.parent.stage.errorFirst.changeType(OUTPUT))


  val errorUpdateMode       = ErrorToData.errorUpdateMode


  val loadLength       = signal("load_length",INPUT,U(params.inputWidth1))


  /- ("Tap Input Memmory Control") // Fixme : Needs own module
  val errorCount       = parent.errorToOutput.errorCount //signal("error_count",REG,U(params.tapWidth,0))
  val errorFinish = signal("error_finish",WIRE)
  val errorFifoFull = signal("error_fifo_full",WIRE)
  val errorWriteCount = signal("error_write_count",REG,U(params.tapWidth,0))

  val errorFinishTap = ErrorControl.errorFinishTap

  val errorPhase      = parent.errorToOutput.errorPhase  //signal("error_phase",REG,U(params.errorWidth))
  //val errorPhaseCount = signal("error_phase_count",REG,U(params.errorWidth,0))
  val errorFifoDepth  = signal("error_fifo_depth",REG,U(params.errorWidth+1,0))
  val errorPhaseRead  = parent.errorToOutput.errorPhaseRead //signal("error_phase_read",REG,U(params.errorWidth,0))

  val tapErrorLength        = signal("error_tap_length",INPUT,U(params.tapWidth,0))
  val errorUpdateLatch       = register("error_update_latch",REG)(6)
  val errorUpdateFirst       = register("error_update_first_internal",WIRE)(4)
  val errorUpdateLast        = register("error_update_last_internal",WIRE)(4)


  // FIXME : Duplicate Operations
  /- ("Create the Tap Update Control which gates the error feedback")
  val rdAddressVld      = signal("wr_address_vld",WIRE)
  rdAddressVld          :=  ErrorControl.errorUpdateLatch & ~ErrorControl.errorUpdateFirst;
  val rdAddressVldDelay = register(rdAddressVld)(6)
  // Always enable the ready for error signal (temporary)
  input.rdy := (~rdAddressVldDelay(4) & ~errorFifoFull) & ~errorUpdateLatch(6)

  /- ("Finish Conditions")
  errorFinish     := (errorCount === (tapErrorLength)) & (input.enable)
  errorFinishTap  := ErrorToData.stateFinish & ErrorControl.errorUpdateLatch


  /- ("Input Control and Tap Addressiong")
  errorCount            := ($iff(errorFinish)    $then 0 $else (errorCount + 1)).$at(clk.createEnable(input.enable))
  val wrCount = ->(Counter.Length(errorWriteCount,loadLength,Some(input.enable)))
  ->(Counter.Length(errorPhase,params.errorLength-1,Some(wrCount.end & input.enable)))
  ->(Counter.Length(parent.errorToOutput.errorSubAddress,loadLength,Some(input.enable)))

  /- ("Error Input Operations")
  //errorPhase      := ($iff (errorPhase === (params.errorLength-1)) $then (errorPhase ::= 0) $else (errorPhase +1)).$at(clk.createEnable(errorFinish))
  //errorPhaseCount := ($iff (errorPhaseCount === (params.errorLength-1)) $then (errorPhaseCount ::= 0) $else (errorPhaseCount +1)).$at(clk.createEnable(errorFinishTap))
  errorFifoDepth   := $iff (errorUpdateLast(0) & errorFinish) $then           // Fifo is cleared only when error is used (not implemented)
    errorFifoDepth $else_if (errorFinish) $then
    errorFifoDepth + 1 $else_if (errorUpdateLast(0)) $then errorFifoDepth - 1  $at clk

  errorFifoFull := (errorFifoDepth === params.errorLength-2)


  //errorUpdateMode       := errorPhase > 0

  //errorUpdateMode       := (errorFifoDepth > 0) & ~((errorFifoDepth === 1) & ErrorToData.stateFinish & errorUpdateLatch)
  errorUpdateMode       := (errorFifoDepth > 0) // & ErrorToData.stateFinish

  errorPhaseRead      := ($iff (errorPhaseRead === (params.errorLength-1)) $then 0 $else (errorPhaseRead +1)).$at(clk.createEnable(errorUpdateFirst))

  errorUpdateLatch(0)      := errorUpdateMode.$at(clk.createEnable(ErrorToData.stateFinish))

  errorUpdateFirst(0)      := (ErrorToData.stateFinish ? (errorUpdateMode & ErrorToData.readFinish) :: (errorUpdateLatch & ErrorToData.readFinish)).$at(clk)
  //errorUpdateLast(0)       := (errorUpdateLatch & ErrorToData.stateFinish).$at(clk)
  errorUpdateLast(0)       := ((errorFifoDepth > 0) & ErrorToData.stateFinish).$at(clk)

  ErrorControl.errorUpdateFirst  := errorUpdateFirst(0) & errorUpdateLatch(0)
  parent.errorToOutput.errorValue := input.value.exp
  parent.errorToOutput.errorValid := input.vld & input.rdy

  // FIXME : Created def at (clk,delay)
  // FIXME : Create Tuple Input for Flop Delay
  /- ("Error Mode Driving Controls")
  parent.parent.stage.errorMode   := errorUpdateLatch(2)
  parent.parent.stage.errorFirst  := errorUpdateFirst(2) & errorUpdateLatch(2)


}

object ErrorControl {
  val errorUpdateLatch      = SignalTrait("error_update_latch",OpType.Input)
  val errorUpdateFirst       = SignalTrait("error_update_first",OpType.Input)
  val errorFinishTap         = SignalTrait("error_finish_tap",OpType.Output)

  case class ErrorToOutput(params:DataControl.Params) extends SignalInterface{
    override val name = "error_to_data"

    val errorSubAddress     = SignalTrait("error_sub_address",OpType.RegOutput,FixedType.unsigned(params.dataWidth,0))
    val errorCount          = SignalTrait("error_count",OpType.RegOutput,FixedType.unsigned(params.tapWidth,0))
    val errorValid          = SignalTrait("error_valid")
    val errorValue          = SignalTrait("error_value",OpType.Input,FixedType.unsigned(params.dataWidth,0))
    val errorPhase          = SignalTrait("error_phase",OpType.RegOutput,FixedType.unsigned(params.errorWidth,0))
    val errorPhaseRead      = SignalTrait("error_phase_read",OpType.RegOutput,FixedType.unsigned(params.errorWidth,0))

    override val inputs = List(errorCount, errorValid, errorValue,
      errorPhase, errorPhaseRead, errorUpdateLatch, errorUpdateFirst, errorSubAddress)
  }

  case object ErrorToData extends SignalInterface{
    override val name = "error_to_data"
    val errorUpdateMode       = SignalTrait("error_update_mode",OpType.Input)
    val stateFinish         = SignalTrait("state_finish",OpType.Output)
    val readFinish         = SignalTrait("read_finish",OpType.Output)

    val errorUpdateLatch   = ErrorControl.errorUpdateLatch
    val errorUpdateFirst   = ErrorControl.errorUpdateFirst


    override val inputs = List(errorUpdateMode, errorUpdateFirst, errorUpdateLatch, errorFinishTap)
    override val outputs = List(stateFinish, readFinish)

  }

}
