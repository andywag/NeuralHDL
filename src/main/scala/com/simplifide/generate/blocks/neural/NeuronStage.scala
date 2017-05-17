package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.typ.OutputAssignable
import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{OpType, SignalTrait}

import scala.concurrent.Future

/**
  * Created by andy on 5/8/17.
  */
case class NeuronStage(override val name:String,
                       valid:SignalTrait,
                       dataIn:Seq[SignalTrait],
                       tapIn:Seq[SignalTrait],
                       biasIn:Seq[SignalTrait],    // FIXME : Should probably be a scalar
                       dataOut:Seq[SignalTrait],   // FIXME : Should probably be a scalar as well
                       numberOfNeurons:Int,
                       neuron:SegmentEntity[Neuron])(implicit clk:ClockControl) extends  EntityParser{

  val share  = dataIn.length
  val depth  = tapIn.length

  def inputs = valid :: clk.allSignals(INPUT) ::: dataIn.toList ::: tapIn.toList ::: biasIn.toList

  sigs(inputs.map(_.changeType(OpType.Input)))
  sigs(dataOut.map(_.changeType(OpType.Output)))

  val biasInputDelay = register(biasIn(0))(depth)(clk).allSignalChildren.toSeq
  val neuronOut      = seq(dataOut(0).newSignal(name = "wireOut"))(numberOfNeurons)
  val neuronAccumIn  = seq(biasIn(0))(numberOfNeurons)

  val dataOutDelay    = seq(dataOut(0).newSignal(name = "outLine"),REG)(numberOfNeurons)
  val validD          = Seq.tabulate(2)(x => signal(valid.newSignal(name = s"valid$x",REG)))

  import com.simplifide.generate.blocks.basic.typ.SegmentParser._

  //val a = valid := valid ? valid :: valid
  /- ("Delay the Input Valid")
  Seq(validD(0),validD(1)) := Seq(valid,validD(0)) at (clk)

  /- ("Select the inputs to the Neuron\n")
  for (i <- 0 until depth) {
    val biasInput = if (i == depth-1) biasIn(0) else biasInputDelay(depth-2-i)
    neuronAccumIn(i) := validD(0) ? biasInput :: neuronOut(i)

  }



  /- ("Create the output Delay Line\n")
  dataOutDelay := ($if (validD(0)) $then neuronOut $else dataOutDelay.slice(1,dataOutDelay.length)) at (clk)

  /- ("Assign the outputs")
  dataOut(0) := dataOutDelay(0)
  //val rr = ($if (valid) $then biasSelect $else_if (valid) $then biasDelay $else biasDelay) at (clk)
  //val tt = new AssignType.Assignable.AssignableOutputAssignable(rr)


  //val r = biasSelect := rr

  //->(start.create)









  val neuronInput = biasIn.map(x => signal(x.newSignal(name = x.appendName("_input"),opType=OpType.Logic)))
  //List.tabulate(numberOfNeurons)(x => neuronInput(x) := valid ? biasIn(x) :: dataOut(x))

  for (i <- 0 until numberOfNeurons) {

    val connection = Map(
      neuron.segment.dataIn  -> dataIn(0),
      neuron.segment.taps    -> tapIn(i),
      neuron.segment.bias    -> neuronAccumIn(i),
      neuron.segment.dataOut -> neuronOut(i)
    )

    instance(neuron.createEntity,s"neuron$i",connection)
  }



}
