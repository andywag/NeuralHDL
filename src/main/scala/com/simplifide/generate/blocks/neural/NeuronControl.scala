package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.blocks.neural.NeuronControl.{DataFifo, ErrorFifo}
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.Connection
import com.simplifide.generate.signal.OpType.Logic
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.ReadyValid
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

  val stateLength   = signal("state_length",INPUT,U(info.stateWidth,0))




  // Create the Data Input FIFO
  val params = DataFifo.Params(info.dataLength,info.dataFill, info.stateLength,info.tapAddressLength, info.errorFill)
  val dataFifo = new DataFifo(appendName("data_fifo"),params,this,dataIn)
  val con1 = Map(dataFifo.loadLength -> this.dataLength, dataFifo.loadDepth -> this.loadDepth)
  instance(dataFifo.createEntity,dataFifo.name,con1)
  val activeNormal        = signal("active_normal",WIRE)
  val activePre           = signal("active_pre",WIRE)
  val active              = signal("active",WIRE)
  val activeStart         = signal("active_start",WIRE)
  val activeStartD        = signal("active_start_d",WIRE)
  val dataWriteAdd        = signal("data_write_addr",WIRE,U(params.inputWidth1 + params.inputWidth2))
  val dataReadAdd         = signal("data_read_addr",WIRE,U(params.inputWidth1 + params.inputWidth2))
  val dataReady           = signal("data_ready",WIRE)
  val tapAddress          = signal("tap_address",  WIRE,U(params.inputWidth1 + params.stateWidth))
  val stateFinish         = signal("state_finish",WIRE)

  val errorFifo = new ErrorFifo(appendName("error_fifo"),params,this,errorIn)
  instance(errorFifo.createEntity,errorFifo.name)


  /- ("Data Input Memory Control")
  dataIn.rdy     := dataReady//(fifoInputDepth <= loadDepth)
  val dataMem = parent.memory.dataBank.input
  dataMem.wrData         := dataIn.value.exp
  dataMem.ctrl.wrAddress := dataWriteAdd; //Operators.Concat(loadDataWrite,loadInputCount)
  dataMem.ctrl.wrVld     := dataIn.rdy & dataIn.vld

  /- ("Data Output Memory Control")
  dataMem.ctrl.rdAddress := dataReadAdd; //Operators.Concat(readDataDepth, readDataAddress)
  dataMem.ctrl.rdVld     := activeNormal

  /- ("Tap Output Memory Control")
  val tapMem = parent.memory.tapBank.input
  tapMem.ctrl.rdAddress  := tapAddress //;this.errorUpdateFirst ? (info.tapAddressLength + errorPhaseRead) :: tapAddress
  tapMem.ctrl.rdVld      := activeNormal

  /- ("Bias Output Memory Control")
  // FIXME : Move to subblock
  //biasStart    := loadFinish & (stateAddress === 0)
  //biasEnable   := (readDataAddress <= biasLength) & (readDataAddress > 0)
  //biasAddress  := $iff (biasStart) $then 0 $else_if (biasEnable) $then biasAddress + 1 $at clk
  //val biasMem = parent.memory.biasBank.input
  //biasMem.ctrl.rdAddress := biasAddress
  //biasMem.ctrl.rdVld     := biasEnable


  /- ("Tap Input Memmory Control") // Fixme : Needs own module
  //val errorCount       = signal("error_count",REG,U(info.tapDimension._2,0))
  //val errorFinishCount = signal("error_finish_count",REG,U(info.tapDimension._2,0))

  //val errorFinish = signal("error_finish",WIRE)
  //val errorFinishTap = signal("error_finish_tap",WIRE)

  //val errorPhase      = signal("error_phase",REG,U(info.errorFillWidth,0))
  //val errorPhaseCount = signal("error_phase_count",REG,U(info.errorFillWidth,0))
  //val errorPhaseRead  = signal("error_phase_read",REG,U(info.errorFillWidth,0))

  //val errorDepth = signal("error_depth",REG,U(info.errorFillWidth,0))
  val tapErrorLength        = signal("error_tap_length",INPUT,U(info.tapAddressWidth,0))
  //val errorUpdateMode       = signal("error_update_mode",WIRE)
  //val errorUpdateLatch       = signal("error_update_latch",REG)
  //val errorUpdateFirst       = signal("error_update_first",WIRE)

  //errorCount := (errorCount + 1).$at(clk.createEnable(errorIn.vld))
  errorIn.rdy                 := 1
  this.tapMem.ctrl.wrAddress  := info.tapAddressLength //+ errorPhase
  this.tapMem.ctrl.wrVld      := errorIn.vld & errorIn.rdy
  this.tapMem.ctrl.subVld     := errorIn.vld & errorIn.rdy
  this.tapMem.ctrl.subAddress := 0//errorCount
  this.tapMem.ctrl.subData    := errorIn.value.value




  /- ("Output Driving Control")
  parent.stage.first       := activeStartD
  parent.stage.dataIn(0)   := parent.memory.dataBank.input.rdData
  parent.stage.biasIn(0)   := parent.memory.biasBank.input.rdData
  for (i <- 0 until info.numberNeurons) {
    val index = ((i+1)*info.memoryWidth-1,info.memoryWidth*i)
    parent.stage.tapIn.s(i)  := parent.memory.tapBank.input.rdData(index)
  }
  /- ("Final Output Control")
  dataOut.value.signals(0)    := parent.stage.dataOut
  dataOut.vld                 := active

  dataOutPre.value.signals(0) := parent.stage.dataOutPre
  dataOutPre.vld              := activePre

/*
  errorFinish  := (errorCount === dataLength)
  errorFinishTap  := (errorFinishCount === tapErrorLength)


  /- ("Error Address")
  errorCount            := ($iff(errorFinish)    $then 0 $else (errorCount + 1)).$at(clk.createEnable(errorIn.vld))
  errorFinishCount      := ($iff(errorFinishTap) $then 0 $else (errorFinishCount + 1)).$at(clk.createEnable(errorIn.vld))
  errorUpdateMode       := errorPhaseCount > 0

  /- ("Error Input Operations")
  errorPhase      := ($iff (errorPhase === (info.errorFill-1)) $then (errorPhase ::= 0) $else (errorPhase +1)).$at(clk.createEnable(errorFinish))
  errorPhaseCount := ($iff (errorPhaseCount === (info.errorFill-1)) $then (errorPhaseCount ::= 0) $else (errorPhaseCount +1)).$at(clk.createEnable(errorFinishTap))
  errorUpdateMode       := errorPhaseCount > 0
  //errorUpdateLatch      := errorUpdateMode.$at(clk.createEnable(stateDone))
  //errorUpdateFirst      := (errorUpdateMode & stateDone).$at(clk)

  // FIXME : Created def at (clk,delay)
  // FIXME : Create Tuple Input for Flop Delay
  /- ("Error Mode Driving Controls")
  val dul = signal("dul",REG)
  val duf = signal("duf",REG)
  dul := errorUpdateLatch $at clk
  duf := errorUpdateFirst $at clk
  parent.stage.errorMode   := dul $at (clk)
  parent.stage.errorFirst  := duf $at (clk)
  */

}

object NeuronControl {
  case class Dimension(dataAddWidth:Int, stateAddWidth:Int, biasAddWidth:Int, dataNumber:Int)

  case class ErrorFifo(override val name:String,
                       params:DataFifo.Params,
                       parent:NeuronControl[_],
                       input:ReadyValidInterface[_])(implicit clk:ClockControl) extends EntityParser {

    signal(clk.allSignals(INPUT))
    signal(input.signals)
    val loadLength       = signal("load_length",INPUT,U(params.inputWidth1))
    val stateFinish         = signal("state_finish",INPUT)


    /- ("Tap Input Memmory Control") // Fixme : Needs own module
    val errorCount       = signal("error_count",REG,U(params.tapWidth,0))
    val errorFinish = signal("error_finish",WIRE)

    val errorFinishCount = signal("error_finish_count",REG,U(params.tapWidth,0))
    val errorFinishTap = signal("error_finish_tap",WIRE)

    val errorPhase      = signal("error_phase",REG,U(params.errorWidth))
    val errorPhaseCount = signal("error_phase_count",REG,U(params.errorWidth,0))
    val errorPhaseRead  = signal("error_phase_read",REG,U(params.errorWidth,0))

    val errorDepth = signal("error_depth",REG,U(params.errorWidth,0))
    val tapErrorLength        = signal("error_tap_length",INPUT,U(params.tapWidth,0))
    val errorUpdateMode       = signal("error_update_mode",WIRE)
    val errorUpdateLatch       = register("error_update_latch",REG)(2)
    val errorUpdateFirst       = register("error_update_first",WIRE)(2)


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

    errorUpdateLatch(0)      := errorUpdateMode.$at(clk.createEnable(stateFinish))
    errorUpdateFirst(0)      := (errorUpdateMode & stateFinish).$at(clk)

    // FIXME : Created def at (clk,delay)
    // FIXME : Create Tuple Input for Flop Delay
    /- ("Error Mode Driving Controls")
    /*
    val dul = signal("dul",REG)
    val duf = signal("duf",REG)
    dul := errorUpdateLatch $at clk
    duf := errorUpdateFirst $at clk
    parent.parent.stage.errorMode   := dul $at (clk)
    parent.parent.stage.errorFirst  := duf $at (clk)
*/


  }

  case class DataFifo(override val name:String,
                      params:DataFifo.Params,
                      parent:NeuronControl[_],
                      input:ReadyValidInterface[_])(implicit clk:ClockControl) extends EntityParser {

    import com.simplifide.generate.newparser.typ.SegmentParser._

    signal(clk.allSignals(INPUT))
    signal(input.signals)
    // Depth of the FIFO programmable register
    val loadLength       = signal("load_length",INPUT,U(params.inputWidth1))
    val loadDepth        = signal("load_depth", INPUT,U(params.inputWidth2))
    val stateLength      = signal("state_length",INPUT,U(params.stateWidth))

    val activeNormal     = signal("active_normal",OUTPUT)
    val activePre        = signal("active_pre",OUTPUT)
    val active           = signal("active",OUTPUT)
    val activeStart      = signal("active_start",OUTPUT)
    val activeStartD     = signal("active_start_d",OUTPUT)
    val dataWriteAdd     = signal("data_write_addr",OUTPUT,U(params.inputWidth1 + params.inputWidth2))
    val dataReadAdd      = signal("data_read_addr",OUTPUT,U(params.inputWidth1 + params.inputWidth2))
    val loadFinish       = signal("load_finish",OUTPUT)
    val dataReady        = signal("data_ready",OUTPUT)
    val tapAddress       = signal("tap_address",  OUTPUT,U(params.inputWidth1 + params.stateWidth))
    val stateFinish      = signal("state_finish",OUTPUT)

    // Control Parameters for loading the input data and counting

    val readDepthCount  = signal("read_depth_count",  REG,U(params.inputWidth2))
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
    val readFinish       = signal("read_finish",  WIRE, U(1)) !-> (readWidthCount == loadLength)
    stateFinish          := (readStateCount === stateLength) & (readFinish)
    val data_start       = register("data_start",WIRE)(3)
    val data_active      = register("data_active",WIRE)(params.inputLength1 + 6)
    data_start(0)        := (readFinish | loadInputDone) & (fifoInputDepth >= 0)
    data_active(0)       := (fifoInputDepth > 0) & ~(fifoEmptyReg|fifoEmpty)

    val updateCounter    = signal("update_counter",WIRE) !-> (data_active | data_active(params.inputLength1))

    readWidthCount   := $iff (data_start | readFinish) $then 0 $else_if (updateCounter) $then readWidthCount + 1 $at clk
    readStateCount   := $iff (readFinish) $then readStateCount + 1 $at clk
    readDepthCount   := $iff (stateFinish) $then readDepthCount + 1 $at clk
    tapAddress       := $iff (stateFinish) $then 0 $else_if (updateCounter) $then (tapAddress + 1) $at clk

    /-("Data Memory Interface and Input Control")

    // Convenient Output Declarations
    activeStart  := data_start
    activeStartD := data_start(3)
    activePre    := data_active(params.inputLength1+4)
    active       := data_active(params.inputLength1+6)
    activeNormal := (data_active | data_active(params.inputLength1))

    dataWriteAdd := Operators.Concat(loadDepthCount,loadWidthCount)
    dataReadAdd := Operators.Concat(readDepthCount,readWidthCount)
    loadFinish  := readFinish
    dataReady   := (fifoInputDepth <= loadDepth)

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

    }
  }


}