# Basic Test Description

A basic test bench is shown below. There is automatic signal connections in this tool so the creation of a test bench simply requires instantiating the module and connecting the signals with stimulus. Explanations of simple operations can be seen in the code below. This is a simple test bench but there are more complicated options available. 
 
This test bench is shown due to it's simplicity but the normal test strategy has changed a bit with new blocks. Numpy is far better than scala in terms of math so the plan going forward is to more matching/analysis operations to python. There was an initial desire to stay within a single language but in the end the choice was made to use the correct tools for the job.  

```scala
class NeuronTest extends BlockScalaTest with BlockTestParser  {
  def blockName:String = "neuron"

  val dutParser = new NeuronTest.Dut(blockName)
  override val dut: NewEntity = dutParser.createEntity

  val delayIndex = signal(SignalTrait("d_index",WIRE,U(31,0)))

  // Creation of Random Stimulus for block
  val data = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/data",DataFileGenerator.RANDOM)
  val tap  = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/tap",DataFileGenerator.RANDOM)
  val bias = DataFileGenerator.createData(Array(testLength,1),s"$dataLocation/bias",DataFileGenerator.RANDOM)
  
  // Output results to match against
  val result = ((tap.data*data.data).d(1) + bias.data.a(1)).d(1)
  val out  = DataFileGenerator.createFlatten(s"$dataLocation/out",result)

  /- ("Delay the bias to line up the data")
  delayIndex := index - 1;

  // Drive the design signals with the created data
  val dataInD   = dutParser.dataIn <-- data
  val tapInD    = dutParser.tapIn  <-- tap
  val biasInD   = dutParser.biasIn <-- bias

  // Dump the signal to a file. 
  val rout = dutParser.dataOut ---> (s"$dataLocation/rout", Some(out), "Neuron Output")

  // Operations to check post run
  override def postRun = {
    val output = rout.load()
    val error = PlotUtility.plotError(output.data().asDouble(),
      result.data().asDouble(),Some(s"$docLocation/results"))
    assert(error.max._1 < .05)
  }


 


}


```