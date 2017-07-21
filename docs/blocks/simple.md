# Simple Block Description

A description of a neuron(MAC) is shown below. This is a relatively simple example but in general what the descriptions of more complicated blocks look like. For this you can see that there is no concept of buswidths or even signal types. All operations on the signals depend on the signal type. This means that a fixed point signal will automatically create a fixed point description while a floating point signal will create a floating point signal. This leads to fully generic code.  

```scala

case class Neuron(proto:SignalTrait)(implicit val clk:ClockControl) extends ComplexSegment  {


  // Input/Output Declarations
  val dataOut:SignalTrait = proto.newSignal(name = "data_out",opType = OUTPUT)
  val dataIn:SignalTrait = proto.newSignal(name = "data_in",opType = INPUT)
  val taps:SignalTrait = proto.newSignal(name = "taps",opType = INPUT)
  val bias:SignalTrait = proto.newSignal(name = "bias",opType = INPUT)

  val internalSignal = signal(dataOut.newSignal(name = dataOut.appendName("tap_data_out"),OpType.Signal))

  // MAC Operation
  internalSignal := dataIn times taps
  dataOut        := internalSignal plus bias

  val result = dataOut.newSignal(name = dataOut.appendName("result"))

  // Convenience information to allow automatic block testing
  // This is only required when the operation needs to work both inside a module and as a segment
  override def inputs: Seq[SignalTrait] = {
    val clkSignals   = clk.allSignals(OpType.Input)
    val inputSignals = List(dataIn,taps,bias)
    clkSignals ::: inputSignals
  }

  override def outputs:List[SignalTrait] = List(dataOut)


}

```