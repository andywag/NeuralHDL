package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.neural.NeuronControl.DataFifo.{DataToOutput, ErrorToData, ErrorToOutput}
import com.simplifide.generate.blocks.neural.NeuronControl.DataFifo.ErrorToData.{errorUpdateFirst, errorUpdateMode}
import com.simplifide.generate.blocks.neural.NeuronControl.{DataFifo, ErrorFifo, OutputCtrl}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.Connection
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.{FixedType, OpType, SignalTrait, sv}
import com.simplifide.generate.signal.sv.{ReadyValid, SignalInterface}
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

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
  val params = DataFifo.Params(info.dataLength,info.dataFill, info.stateLength,info.tapAddressLength, info.errorFill)
  val dataToOutput = new DataToOutput(params)
  val errorToOutput = new ErrorToOutput(params)

  val dataFifo = new DataFifo(appendName("data_fifo"),params,this,dataToOutput,dataIn)
  val con1 = Map(dataFifo.loadLength -> this.dataLength, dataFifo.loadDepth -> this.loadDepth)
  instance(dataFifo.createEntity,dataFifo.name,con1)

  val errorFifo = new ErrorFifo(appendName("error_fifo"),params,this,errorIn)
  instance(errorFifo.createEntity,errorFifo.name)

  val outputEntity = new OutputCtrl(appendName("out_ctrl"),info,this,dataToOutput,errorToOutput)
  instance(outputEntity.createEntity,outputEntity.name)

  // FIXME : I'm not sure why this is here but it is required
  val tapErrorLength        = signal("error_tap_length",INPUT,U(info.tapAddressWidth,0))




}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)

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
    tapMem.ctrl.rdAddress  :=  errorUpdateFirst ? (info.tapAddressLength + errorToOutput.errorPhaseRead) :: dataToOutput.tapAddress
    tapMem.ctrl.rdVld      := dataToOutput.activeNormal

    /- ("Tap Input Update Control")
    val rdAddressWire     = signal("rd_address_wire",WIRE,U(info.tapAddressWidth)) !-> tapMem.ctrl.rdAddress
    val rdAddressDelay    = register(rdAddressWire)(4)
    val rdAddressVldDelay = register(errorUpdateFirst)(6)



    /- ("Tap Input Memmory Control") // Fixme : Needs own module

    this.tapMem.ctrl.wrAddress  := rdAddressVldDelay(5) ? rdAddressDelay(4) :: info.tapAddressLength + errorToOutput.errorPhase
    this.tapMem.ctrl.wrVld      := rdAddressVldDelay(5) | errorToOutput.errorValid
    
    this.tapMem.ctrl.subVld     := rdAddressVldDelay(5) ? 0 :: errorToOutput.errorValid
    this.tapMem.ctrl.subAddress := errorToOutput.errorCount
    this.tapMem.ctrl.subData    := errorToOutput.errorValue




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
    // FIXME : Move to subblock
    //biasStart    := loadFinish & (stateAddress === 0)
    //biasEnable   := (readDataAddress <= biasLength) & (readDataAddress > 0)
    //biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk
    //val biasMem = parent.memory.biasBank.input
    //biasMem.ctrl.rdAddress := biasAddress
    //biasMem.ctrl.rdVld     := biasEnable


  }


  case class ErrorFifo(override val name:String,
                       params:DataFifo.Params,
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

    val errorFinishCount = signal("error_finish_count",REG,U(params.tapWidth,0))
    val errorFinishTap = signal("error_finish_tap",WIRE)

    val errorPhase      = parent.errorToOutput.errorPhase  //signal("error_phase",REG,U(params.errorWidth))
    val errorPhaseCount = signal("error_phase_count",REG,U(params.errorWidth,0))
    val errorPhaseRead  = parent.errorToOutput.errorPhaseRead //signal("error_phase_read",REG,U(params.errorWidth,0))

    val errorDepth = signal("error_depth",REG,U(params.errorWidth,0))
    val tapErrorLength        = signal("error_tap_length",INPUT,U(params.tapWidth,0))
    val errorUpdateLatch       = register("error_update_latch",REG)(2)
    val errorUpdateFirst       = register("error_update_first_internal",WIRE)(2)


    // Always enable the ready for error signal (temporary)
    input.rdy := 1

    /- ("Finish Conditions")
    errorFinish  := (errorCount === loadLength)
    errorFinishTap  := (errorFinishCount === tapErrorLength)


    /- ("Error Counters")
    errorCount            := ($iff(errorFinish)    $then 0 $else (errorCount + 1)).$at(clk.createEnable(input.vld))
    errorFinishCount      := ($iff(errorFinishTap) $then 0 $else (errorFinishCount + 1)).$at(clk.createEnable(input.vld))

    /- ("Error Input Operations")
    errorPhase      := ($iff (errorPhase === (params.errorLength-1)) $then (errorPhase ::= 0) $else (errorPhase +1)).$at(clk.createEnable(errorFinish))
    errorPhaseCount := ($iff (errorPhaseCount === (params.errorLength-1)) $then (errorPhaseCount ::= 0) $else (errorPhaseCount +1)).$at(clk.createEnable(errorFinishTap))
    errorUpdateMode       := errorPhaseCount > 0

    errorPhaseRead      := ($iff (errorPhaseRead === (params.errorLength-1)) $then 0 $else (errorPhaseRead +1)).$at(clk.createEnable(ErrorToData.errorUpdateFirst))

    errorUpdateLatch(0)      := errorUpdateMode.$at(clk.createEnable(ErrorToData.stateFinish))
    errorUpdateFirst(0)      := (errorUpdateMode & ErrorToData.readFinish).$at(clk)

    ErrorToData.errorUpdateFirst  := errorUpdateFirst(0) & errorUpdateLatch(0)
    parent.errorToOutput.errorValue := input.value.exp
    parent.errorToOutput.errorValid := input.vld & input.rdy

    // FIXME : Created def at (clk,delay)
    // FIXME : Create Tuple Input for Flop Delay
    /- ("Error Mode Driving Controls")
    parent.parent.stage.errorMode   := errorUpdateLatch(2)
    parent.parent.stage.errorFirst  := errorUpdateFirst(2) & errorUpdateLatch(2)



  }

  case class DataFifo(override val name:String,
                      params:DataFifo.Params,
                      parent:NeuronControl[_],
                      dataToOutput: DataToOutput,
                      input:ReadyValidInterface[_])(implicit clk:ClockControl) extends EntityParser {

    import com.simplifide.generate.newparser.typ.SegmentParser._

    signal(clk.allSignals(INPUT))
    signal(input.signals)
    signal(ErrorToData.signals)
    signal(dataToOutput.reverse)

    // Depth of the FIFO programmable register
    val loadLength       = signal("load_length",INPUT,U(params.inputWidth1))
    val loadDepth        = signal("load_depth", INPUT,U(params.inputWidth2))
    val stateLength      = signal("state_length",INPUT,U(params.stateWidth))

    val loadFinish       = signal("load_finish",OUTPUT)
    val dataReady        = signal("data_ready",OUTPUT)
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
    loadDepthCount    := $iff (loadInputDone) $then loadDepthCount + 1 $at clk

    /- ("Control signals for the Read/Write Fifo Operation")
    val fifoEmpty           = signal("fifo_empty")
    val fifoEmptyReg        = signal("fifo_empty_reg",REG)
    val fifoInputDepth      = signal("fifo_input_depth",  REG,U(params.inputWidth2))
    val fifoReadDepth       = signal("fifo_read_depth",  REG,U(params.inputWidth2))

    /- ("Fifo Controls - Used to Gate Inputs")
    fifoEmpty    := (readDepthCount == loadDepthCount)            // Empty Fifo when read depth equals write depth
    fifoEmptyReg := fifoEmpty $at clk.createEnable(stateFinish)  // Latch the Empty Signal to Continue Pipeline

    fifoInputDepth   := $iff (loadInputDone & 0) $then           // Fifo is cleared only when error is used (not implemented)
      fifoInputDepth $else_if (loadInputDone) $then
      fifoInputDepth + 1 $else_if (0) $then fifoInputDepth - 1  $at clk


    val readWidthCount  = signal("read_width_count",  WIRE,U(params.inputWidth1))
    val readStateCount  = signal("read_state_count",  WIRE,U(params.stateWidth))

    /- ("Internal Counter for which state the operation is in")
    val readFinish       = signal("read_finish",  OUTPUT, U(1)) !-> (readWidthCount == loadLength)
    stateFinish          := (readStateCount === stateLength) & (readFinish)
    val data_start       = register("data_start",WIRE)(3)
    val data_active      = register("data_active",WIRE)(params.inputLength1 + 6)
    val outputValid      = register("output_valid",WIRE)(params.inputLength1 + 6)

    data_start(0)        := (readFinish | loadInputDone) & (fifoInputDepth >= 0)

    // Data Active is either data is ready or error data is ready
    data_active(0)       := (fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty) | ErrorToData.errorUpdateMode
    outputValid(0)       := (fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty)


    val full_active      = (data_active | data_active(params.inputLength1))

    val updateCounter    = signal("update_counter",WIRE) !-> full_active & (~ErrorToData.errorUpdateFirst | outputValid(0))

    readWidthCount   := $iff (data_start | readFinish) $then 0 $else_if (updateCounter) $then readWidthCount + 1 $at clk
    readStateCount   := $iff (readFinish) $then readStateCount + 1 $at clk
    readDepthCount   := $iff (stateFinish & outputValid(0)) $then readDepthCount + 1 $at clk
    readErrorCount   := $iff (stateFinish & ~outputValid(0)) $then readErrorCount + 1 $at clk

    dataToOutput.tapAddress       := $iff (stateFinish) $then 0 $else_if (updateCounter) $then (dataToOutput.tapAddress + 1) $at clk

    /-("Data Memory Interface and Input Control")

    // Convenient Output Declarations
    dataToOutput.activeStart  := data_start
    dataToOutput.activeStartD := data_start(3)
    dataToOutput.activePre    := outputValid(params.inputLength1+4)
    dataToOutput.active       := outputValid(params.inputLength1+6)
    dataToOutput.activeNormal := (data_active | data_active(params.inputLength1))

    dataToOutput.dataWriteAdd := Operators.Concat(loadDepthCount,loadWidthCount)
    dataToOutput.dataReadAdd  :=
      (errorUpdateMode & ~outputValid(0)) ? Operators.Concat(readErrorCount,readWidthCount) :: Operators.Concat(readDepthCount,readWidthCount)

    loadFinish  := readFinish
    dataReady   := (fifoInputDepth <= loadDepth)

    /- ("Data Memory Interface")
    dataToOutput.dataValid := input.rdy & input.vld
    dataToOutput.dataValue := input.value.signals(0)

  }

  object DataFifo {

    def logWidth(input:Int) = {
      math.ceil(math.log(input)/math.log(2)).toInt
    }

    /**
      * @param inputLength1 : Length of the first input count
      * @param inputLength2 : Length of the second input count
      */
    case class Params(val inputLength1:Int,
                      val inputLength2:Int,
                      val stateLength:Int,
                     val tapLength:Int,
                     val errorLength:Int) {

      val inputWidth1 = logWidth(inputLength1)
      val inputWidth2 = logWidth(inputLength2)
      val stateWidth  = logWidth(stateLength)
      val errorWidth  = logWidth(errorLength)
      val tapWidth  = logWidth(tapLength)

      val dataWidth:Int = 32

    }

    case class ErrorToOutput(params:DataFifo.Params) extends SignalInterface{
      override val name = "error_to_data"

      val errorCount          = SignalTrait("error_count",OpType.RegOutput,FixedType.unsigned(params.tapWidth,0))
      val errorValid          = SignalTrait("error_valid")
      val errorValue          = SignalTrait("error_value",OpType.Input,FixedType.unsigned(params.dataWidth,0))
      val errorPhase          = SignalTrait("error_phase",OpType.RegOutput,FixedType.unsigned(params.dataWidth,0))
      val errorPhaseRead      = SignalTrait("error_phase_read",OpType.RegOutput,FixedType.unsigned(params.errorWidth,0))

      override val inputs = List(errorCount, errorValid, errorValue, errorPhase, errorPhaseRead,ErrorToData.errorUpdateFirst)
    }

    case object ErrorToData extends SignalInterface{
      override val name = "error_to_data"
      val errorUpdateMode       = SignalTrait("error_update_mode",OpType.Input)
      val errorUpdateLatch      = SignalTrait("error_update_latch",OpType.Input)
      val errorUpdateFirst       = SignalTrait("error_update_first",OpType.Input)
      val stateFinish         = SignalTrait("state_finish",OpType.Output)
      val readFinish         = SignalTrait("read_finish",OpType.Output)


      override val inputs = List(errorUpdateMode, errorUpdateFirst)
      override val outputs = List(stateFinish, readFinish)

    }

    case class DataToOutput(params:DataFifo.Params) extends SignalInterface {
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

      override val inputs = List(activePre, active, activeNormal, dataValue, dataValid, dataWriteAdd,dataReadAdd,tapAddress,
        activeStartD, readFinish)

    }

  }


}