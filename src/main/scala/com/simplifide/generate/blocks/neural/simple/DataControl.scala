package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Counter
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.neural.simple.DataControl.DataToOutput
import com.simplifide.generate.blocks.neural.simple.ErrorControl.ErrorToData
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * Created by andy on 6/8/17.
  */
case class DataControl(override val name:String,
                       params:DataControl.Params,
                       parent:NeuronControl[_],
                       dataToOutput: DataToOutput,
                       input:ReadyValidInterface[_])(implicit clk:ClockControl) extends EntityParser {

  import com.simplifide.generate.newparser.typ.SegmentParser._

  signal(clk.allSignals(INPUT))
  signal(input.signals)
  signal(ErrorToData.signals)
  signal(dataToOutput.reverse)

  val errorUpdateMode = ErrorToData.errorUpdateMode
  val errorUpdateLatch = ErrorToData.errorUpdateLatch

  // Depth of the FIFO programmable register
  val loadLength       = signal(parent.controlInterface.loadLength.newSignal(name="load_length",opType=INPUT))//signal("load_length",INPUT,U(params.inputWidth1))
  val loadDepth        = signal(parent.controlInterface.loadDepth.newSignal(name="load_depth",opType=INPUT))
  val stateLength      = signal("state_length",INPUT,U(params.stateWidth))

  val loadFinish       = signal("load_finish",OUTPUT)
  val dataReady        = signal(appendName("data_ready"),OUTPUT)
  val stateFinish      = signal("state_finish",OUTPUT)


  /- ("Ready Valid Input Interface")
  input.rdy     := dataReady

  // Control Parameters for loading the input data and counting

  val readDepthCount  = signal("read_depth_count",  REG,U(params.inputWidth2))
  val readErrorCount  = signal("read_error_count",  REG,U(params.inputWidth2))

  //val tapAddress         = signal("tap_address",  REG,U(params.inputWidth1 + params.stateWidth))

  // Control signals for the inputs to this block.
  // Counter checking the length of the read inputs followed by counter checkign the number of inputs
  /- ("Data Input Burst Counter")
  val loadWidthCount   = signal("load_width_count",REG,U(params.inputWidth1)) // Counter 1 : Width
  val loadDepthCount    = signal("load_depth_count", REG,U(params.inputWidth2)) // Counter 2 : Depth
  val loadInputDone    = signal("load_input_done",WIRE) !-> (loadWidthCount === loadLength)
  loadWidthCount   := $iff (loadInputDone) $then 0 $else_if (input.rdy & input.vld) $then loadWidthCount + 1 $at clk
  ->(Counter.Length(loadDepthCount,loadDepth,Some(loadInputDone)))
  //loadDepthCount    := $iff (loadInputDone) $then loadDepthCount + 1 $at clk

  /- ("Control signals for the Read/Write Fifo Operation")
  val fifoEmpty           = signal("fifo_empty")
  val fifoEmptyReg        = signal("fifo_empty_reg",REG)
  val fifoInputDepth      = signal("fifo_input_depth",  REG,U(params.inputWidth2))
  //val fifoReadDepth       = signal("fifo_read_depth",  REG,U(params.inputWidth2))

  /- ("Fifo Controls - Used to Gate Inputs")
  fifoEmpty    := (readDepthCount == loadDepthCount)            // Empty Fifo when read depth equals write depth
  fifoEmptyReg := fifoEmpty $at clk.createEnable(stateFinish)  // Latch the Empty Signal to Continue Pipeline

  val updateOn = ErrorControl.errorFinishTap
  fifoInputDepth   := $iff (loadInputDone & updateOn) $then           // Fifo is cleared only when error is used (not implemented)
    fifoInputDepth $else_if (loadInputDone) $then
    fifoInputDepth + 1 $else_if (updateOn) $then fifoInputDepth - 1  $at clk


  val errorUpdateCount = signal("error_update_count",REG,U(params.inputWidth1))
  val readWidthCount  = signal("read_width_count",  REG,U(params.inputWidth1))
  val readStateCount  = signal("read_state_count",  REG,U(params.stateWidth))

  /- ("Internal Counter for which state the operation is in")
  val readFinish       = signal("read_finish",  OUTPUT, U(1)) !-> (readWidthCount == loadLength)
  // For cases where there isn't a state set the output to the readcount end
  if (params.stateLength == 1) stateFinish := readFinish
  else stateFinish          := (readStateCount === stateLength) & (readFinish)

  val data_start       = register("data_start",WIRE)(3)
  val data_active      = register("data_active",WIRE)(params.inputLength1 + 6)
  val outputValid      = register("output_valid",WIRE)(params.inputLength1 + 6)

  data_start(0)        := (readFinish | (loadInputDone & ~errorUpdateMode & ~errorUpdateLatch)) & (fifoInputDepth >= 0)

  val dup = register(ErrorToData.errorUpdateMode)(3)
  // Data Active is either data is ready or error data is ready
  data_active(0)       := (fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty) | ErrorToData.errorUpdateMode | ErrorToData.errorUpdateLatch
  val temp = signal("temp",WIRE)
  temp := ((fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty))
  outputValid(0)       := ~errorUpdateLatch & temp
  // Create a gating signal for the output if there is only 1 stage
  val gateValidD = signal("gate_valid_d",WIRE)
  val gateValidE = signal("gate_valid_e",WIRE)
  val gateValid  = signal("gate_valid")

  // Creates a signal to gate the output enable only at the end of neural stage where the flop is active
  // Error has a slightly different length that data because of the double read
  gateValidD := ~errorUpdateLatch & (readWidthCount >= (loadLength - params.inputLength2 +1))
  gateValidE := errorUpdateLatch & (readWidthCount >= (loadLength - params.inputLength2 )) & (readWidthCount < (loadLength))
  if (params.inputLength2 < params.inputLength1) gateValid  := gateValidD | gateValidE
  else gateValid  := 1

  val full_active      = (data_active | data_active(params.inputLength1))

  val updateCounter    = signal("update_counter",WIRE)
  updateCounter        := full_active & (~ErrorToData.errorUpdateFirst | ~errorUpdateLatch & temp)

  readWidthCount   := $iff (data_start | readFinish) $then 0 $else_if (updateCounter) $then readWidthCount + 1 $at clk
  readStateCount   := $iff (readFinish) $then readStateCount + 1 $at clk

  val errFinish    = register("err_finish",  REG)(4)
  errFinish(0)     := (errorUpdateCount === params.inputLength2-1)
  errorUpdateCount := $iff (data_start | errFinish(0)) $then 0 $else_if (updateCounter) $then errorUpdateCount + 1 $at clk

  //readDepthCount   := $iff (stateFinish & outputValid(0)) $then readDepthCount + 1 $at clk
  ->(Counter.Length(readDepthCount,loadDepth,Some(stateFinish & outputValid(0))))
  ->(Counter.Length(readErrorCount,loadDepth,Some(stateFinish & ~ErrorControl.errorTapUpdateOut & errorUpdateLatch)))
  //->(Counter.Simple(errorUpdateCount,)

  dataToOutput.tapAddress       := $iff (stateFinish) $then 0 $else_if (updateCounter) $then (dataToOutput.tapAddress + 1) $at clk
  val tapAddressD = register(dataToOutput.tapAddress)(5)

  val dTapAddress = signal(dataToOutput.tapAddress.newSignal(name = "dtap_address",opType=REG))


  dataToOutput.biasAddress    := errorUpdateLatch ? dataToOutput.tapAddress :: tapAddressD(1)
  dataToOutput.biasWrAddress  := tapAddressD(4)

  //dataToOutput.biasValid      := dataToOutput.tap

  /-("Data Memory Interface and Input Control")

  // Convenient Output Declarations

  dataToOutput.activeStart  := data_start
  dataToOutput.activeStartD := data_start(3) //| (errFinish(3) & ErrorControl.errorTapUpdateOut)
  // Latency = inputLength + 2 memory + 2 mac + 2bias/non
  dataToOutput.activePre    := outputValid(params.inputLength1+4)
  dataToOutput.active       := outputValid(params.inputLength1+6) & gateValid
  dataToOutput.activeNormal := (data_active | data_active(params.inputLength1))

  dataToOutput.dataWriteAdd := Operators.Concat(loadDepthCount,loadWidthCount)
  dataToOutput.dataReadAdd  :=
    (errorUpdateLatch & ~outputValid(0)) ? Operators.Concat(readErrorCount,readWidthCount) :: Operators.Concat(readDepthCount,readWidthCount)

  loadFinish  := readFinish
  dataReady   := (fifoInputDepth != loadDepth )
  val test = signal("test")
  test := (fifoInputDepth != loadDepth )

  /- ("Data Memory Interface")
  dataToOutput.dataValid := input.rdy & input.vld
  dataToOutput.dataValue := input.value.signals(0)
  dataToOutput.errFinish := errFinish(4) & ErrorControl.errorTapUpdateOut



}

object DataControl {

  def logWidth(input: Int) = {
    math.max(math.ceil(math.log(input) / math.log(2)),1.0).toInt
  }

  /**
    * @param inputLength1 : Length of the first input count
    * @param inputLength2 : Length of the second input count
    */
  case class Params(val inputLength1: Int,
                    val inputLength2: Int,
                    val stateLength: Int,
                    val tapLength: Int,
                    val errorLength: Int,
                    val numberOfNeurons:Int) {

    val inputWidth1 = logWidth(inputLength1)
    val inputWidth2 = logWidth(inputLength2)
    val stateWidth = logWidth(stateLength)
    val errorWidth = logWidth(errorLength)
    val tapWidth = logWidth(tapLength)

    val dataWidth: Int = 32

  }

  case class DataToOutput(params:DataControl.Params) extends SignalInterface {
    override val name = "data_to_output"

    def U(value: Int) = FixedType.unsigned(value, 0)

    val activeNormal = SignalTrait("active_normal", OpType.Input)
    val activePre = SignalTrait("active_pre", OpType.Input)
    val active = SignalTrait("active", OpType.Input)
    val activeStart = SignalTrait("active_start", OpType.Input)
    val activeStartD = SignalTrait("active_start_d", OpType.Input)
    val dataWriteAdd = SignalTrait("data_write_addr", OpType.Input, U(params.inputWidth1 + params.inputWidth2))
    val dataReadAdd = SignalTrait("data_read_addr", OpType.Input, U(params.inputWidth1 + params.inputWidth2))
    val loadFinish = SignalTrait("load_finish", OpType.Input)
    val dataReady = SignalTrait("data_ready", OpType.Input)
    val tapAddress = SignalTrait("tap_address", OpType.Input, U(params.inputWidth1 + params.stateWidth))

    val stateFinish = SignalTrait("state_finish", OpType.Input)
    val readFinish  = SignalTrait("read_finish",  OpType.Input, U(1))
    val dataValid   = SignalTrait("data_valid")
    val dataValue   = SignalTrait("data_value",OpType.Input,U(params.dataWidth))

    val biasValid   = SignalTrait("data_valid")
    val biasAddress    = SignalTrait("bias_address", OpType.Input, U(params.inputWidth1 + params.stateWidth))
    val biasWrAddress = SignalTrait("bias_wr_address", OpType.Input, U(params.inputWidth1 + params.stateWidth))
    val errFinish =   SignalTrait("err_finish_i",OpType.Input)


    override val inputs = List(activePre, active, activeNormal, dataValue, dataValid, dataWriteAdd,dataReadAdd,tapAddress,
      activeStartD, readFinish, biasAddress, biasWrAddress, errFinish)

  }


}
