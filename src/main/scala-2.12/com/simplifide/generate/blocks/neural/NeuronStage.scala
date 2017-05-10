package com.simplifide.generate.blocks.neural

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.generator.ComplexSegment.SegmentEntity
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.signal.{OpType, SignalTrait}

/**
  * Created by andy on 5/8/17.
  */
case class NeuronStage(override val name:String,
                       valid:SignalTrait,
                       dataIn:Seq[SignalTrait],
                       tapIn:Seq[SignalTrait],
                       biasIn:Seq[SignalTrait],
                       dataOut:Seq[SignalTrait],
                       numberOfNeurons:Int,
                       neuron:SegmentEntity[Neuron]) extends  EntityParser{

  def inputs = valid :: dataIn.toList ::: tapIn.toList ::: biasIn.toList

  sigs(inputs.map(_.changeType(OpType.Input)))
  sigs(dataOut.map(_.changeType(OpType.Output)))

  val neuronInput = biasIn.map(x => signal(x.newSignal(name = x.appendName("_input"),opType=OpType.Logic)))
  List.tabulate(numberOfNeurons)(x => neuronInput(x) := valid ? biasIn(x) :: dataOut(x))

  for (i <- 0 until numberOfNeurons) {

    val connection = Map(
      neuron.segment.dataIn  -> dataIn(0),
      neuron.segment.taps    -> tapIn(i),
      neuron.segment.bias    -> biasIn(i),
      neuron.segment.dataOut -> dataOut(i)
    )

    instance(neuron.createEntity,s"neuron$i",connection)
  }

}
