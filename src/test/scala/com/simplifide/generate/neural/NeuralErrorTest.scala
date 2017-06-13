package com.simplifide.generate.neural

import com.simplifide.generate.blocks.neural.{NeuralError, NeuralStageInfo, NeuralStageTop}
import com.simplifide.generate.model.DataFileGenerator
import com.simplifide.generate.plot.PlotUtility
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}

/**
  * Created by andy on 6/1/17.
  */
class NeuralErrorTest extends BlockScalaTest with BlockTestParser {
  /** Clock for the testbench */
  override def blockName: String = "error"

  lazy val dataLength    = 6
  lazy val outputLength  = 12
  lazy val tapLength     = dataLength*outputLength
  val biasLength    = 12
  val numberNeurons = 6
  val dataFill      = 8
  val errorFill     = 4
  val outputFill    = 4

  val information = NeuralStageInfo((dataLength,outputLength),dataLength,dataFill,
    numberNeurons,errorFill,outputFill,dataLocation)

  override def getTestLength = outputLength*12

  //val data1 = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data1",DataFileGenerator.Ramp(1.0,testLength+3.0))
  //val data2 = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data2",DataFileGenerator.Ramp(1.0,testLength+1.0))
  val data1 = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data1",DataFileGenerator.RANDOM)
  val data2 = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data2",DataFileGenerator.RANDOM)

  val delta = data1.data.sub(data2.data)

  val outputRdy = new ReadyValidInterface(FloatSignal("out",INPUT))
  val dataRdy   = new ReadyValidInterface(FloatSignal("data",INPUT))
  val ctrlRdy   = new ReadyValidInterface(FloatSignal("zctrl",INPUT))
  val errorRdy  = new ReadyValidInterface(FloatSignal("error_sig",INPUT))

  val outputCnt = signal("outputCnt",WIRE,U(32,0))
  outputCnt := index - outputLength
  outputRdy.vld := (index >= outputLength) ? 1 :: 0
  outputRdy.value.signals(0) <-- (data1,Some(outputCnt))

  val dataCnt = signal("dataCnt",WIRE,U(32,0))
  dataCnt := index - outputLength*2
  dataRdy.vld   := (index >= (outputLength*2)) ? 1 :: 0
  dataRdy.value.signals(0) <-- (data2,Some(dataCnt))

  errorRdy.rdy  := 1
  val err = errorRdy.value.signals(0) ---->(s"$dataLocation/rtl_pre",clk.createEnable(errorRdy.vld), None, "Stage Pre Non",8)


  override val dutParser = new NeuralError(blockName, information,outputRdy,dataRdy,ctrlRdy,errorRdy)
  /** Design Under Test */
  override val dut: NewEntity = dutParser.createEntity

  override def postRun = {
    val output  = err.load()
    val del     = this.delta

    val plotEnable = true
    val plot1 = if (plotEnable) Some(s"$docLocation/results") else None
    val plot2 = if (plotEnable) Some(s"$docLocation/resultse") else None

    val error = PlotUtility.plotErrors(output,
     del,plot1)
    this.checkMaxError(error,.001)


  }


}
