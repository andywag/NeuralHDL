package com.simplifide.generate.blocks.neural.simple

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.neural.{NeuralError, NeuralStageInfo, NeuralStageInterface, NeuralStageTop}
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.FloatSignal
import com.simplifide.generate.signal.sv.ReadyValid.ReadyValidInterface

/**
  * Created by andy on 6/3/17.
  */
class NeuralNetwork[T](val name:String,
                        neuralInfo:Seq[NeuralStageInfo],
                        interface:NeuralStageInterface[T],
                        expected:ReadyValidInterface[_]
                       )(implicit clk:ClockControl) extends EntityParser {

  //val info = neuralInfo(0)
  import com.simplifide.generate.newparser.typ.SegmentParser._

  // FIXME : Need to fix automatic connections
  // Attach the output signals
  signal(interface.inRdy.signals)
  signal(interface.outRdy.reverse)
  signal(interface.outPreRdy.reverse)

  val numberOfStages = neuralInfo.size

  // FIXME : Need to fix prototype to real value
  val interfaces = List.tabulate(neuralInfo.size+1)(x => new NeuralStageInterface(s"stage_$x",null))
  // Attach the signals as wires
  interfaces.foreach(x => signal(x.signals.map(_.changeType(WIRE))))
  // Convenience Assignment to handle hierarchy creation : output goes both to error and out of block

  val first_error_rdy = signal("st_error_rdy")

  val ctrlRdy   = new ReadyValidInterface(FloatSignal("zctrl",INPUT))


  // Create the instances
  val created = neuralInfo.zipWithIndex.map(x => {
    val last  = if (x._2 > 0) interfaces(x._2-1) else interface
    val input = if (x._2 > 0) interfaces(x._2-1).outRdy else interface.inRdy
    // Connect the input and errors
    interfaces(x._2).inRdy !:= input
    last.errorRdy          !:= interfaces(x._2).errorOutRdy

    val mStage = new NeuralStageTop(appendName(s"st${x._2}"),x._1,interfaces(x._2))
    val iStage  = instance(mStage)
    mStage
  })
  val mStage = created
    /*
  val outInternalRdy = new ReadyValidInterface(FloatSignal("internal_out",WIRE))
  signal(outInternalRdy.signals.map(_.changeType(WIRE)))
  outInternalRdy.vld !:= interface.outRdy.vld
  outInternalRdy.value.value !:= interface.outRdy.value.value
  */
  val mError = new NeuralError(appendName("err"),neuralInfo(0), expected,
    interfaces(numberOfStages-1).outRdy,ctrlRdy,interfaces(numberOfStages-1).errorRdy)
  val iError = instance(mError)

  /- ("Attach the output stage to the last block")
  interface.outRdy    !:= interfaces(numberOfStages-1).outRdy
  interface.outPreRdy !:= interfaces(numberOfStages-1).outPreRdy

}
