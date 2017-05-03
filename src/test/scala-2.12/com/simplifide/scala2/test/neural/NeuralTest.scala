
package com.simplifide.scala2.test.neural

import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.model.Expression

import math._
import com.simplifide.generate.parameter.{ModuleScope, Parameter}
import com.simplifide.generate.TestConstants
import com.simplifide.generate.generator.{CodeWriter, SimpleSegment}
import com.simplifide.generate.signalproc.Filter
import com.simplifide.generate.blocks.basic.fixed.{ClipSegment, FixedOperations}
import com.simplifide.generate.blocks.basic.float.Stimulus
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.project.Project
import com.simplifide.scala2.test.basic.ShifterTest.Entity
import com.simplifide.generate.blocks.signal.FirStructure2
import com.simplifide.generate.blocks.test.CompareTable
import com.simplifide.generate.signal.bus.FloatSignal2
import com.simplifide.generate.signal.sv.Struct.StructDeclaration
import com.simplifide.generate.test2.data.FloatData.FloatWrap
import com.simplifide.generate.test2.util.FloatDataFile
import com.simplifide.generate.test2.util.FloatDataFile.FloatWrapper
import com.simplifide.generate.test2.verilator.RunVerilator
import com.simplifide.generate.test2.{Test, TestEntityParser}
import com.simplifide.generate.util.FileOps
import com.simplifide.scala2.test.basic.IirTest.{IirEntity, TestCase}
import com.simplifide.scala2.test.basic.IirTest.IirProject.iir
import com.simplifide.scala2.test.neural.NeuralTest.Project


class NeuralTest {}



object NeuralTest {

  /** Simple ProjectGenerator which only contains an FIR filter */
  object Project extends com.simplifide.generate.project.Project {
    // Location where the project outputs are going to be stored
    val location:String = TestConstants.locationPrefix + "neural"
    implicit val clk         = ClockControl("clk","reset")

    /** Base Entity */
    val entity = new Entity()
    // Root Module Definition
    override val newRoot = entity.createEntity
    val testCase = new TestCase(entity)
    override val tests    = List(Test(testCase.createEntity))

  }

  /**Entity containing the FIR Filter. An Entity contains the Input-Output Definitions for the Module. Simiilar to VHDL
   * the I/O is split from the actual code.
   **/
  class Entity()(implicit val clk:ClockControl) extends EntityParser {

    override val name = "neuron"
    this.signal(clk.allSignals(INPUT))

    // Input Signals
    val tapIn   = signal(FloatSignal("tap_in",INPUT))
    val dataIn  = signal(FloatSignal("data_in",INPUT))
    val biasIn  = signal(FloatSignal("bias_in",INPUT))

    // Output Signals
    val dataOut  = signal(FloatSignal("data_out",OUTPUT))
    val dataOut2  = signal(FloatSignal("add_out",OUTPUT))
    val dataOut3  = signal(FloatSignal("data_out3",OUTPUT))
    // Internal Signals
    val internalWidth = tapIn.man.fixed*dataIn.man.fixed
    val multOut   = signal(FloatSignal("mult_out",WIRE))


    multOut := dataIn * tapIn
    dataOut3 := multOut
    /- ("Addition Operations")
    dataOut2 := dataIn + biasIn
    /- ("Final Add of Neuron Stage")
    dataOut := multOut + biasIn

  }

  class TestCase(val entity:Entity)(implicit val clk:ClockControl) extends TestEntityParser {

    override val name = "test_" + entity.name
    override val dut = entity.createEntity

    val testLength = 10000

    /- ("Counter to Index Test")
    val counter = signal("counter",REG,U(32,0))
    counter := (counter + 1) $at (clk)

    val path = NeuralTest.Project.projectStructure.test

    val data = entity.dataIn.random(testLength) <-- (s"$path/dataIn.hex", counter)
    val tap  = entity.tapIn.random(testLength)  <-- (s"$path/tapIn.hex", counter)
    val bias = entity.biasIn.random(testLength) <-- (s"$path/biasIn.hex", counter)

    val out    = data*tap        --> (s"$path/dataOut.hex")
    val add    = data+bias        --> (s"$path/addOut.hex")
    val total  = out+bias        --> (s"$path/totalOut.hex")

    val rmout   = entity.dataOut3 --> (s"$path/results3.hex")
    val rout    = entity.dataOut2 --> (s"$path/results4.hex")
    val rtout   = entity.dataOut  --> (s"$path/results5.hex")

    compare(out,rmout,"Multiplier")
    compare(add,rout,"Adder")
    compare(total,rtout,"Output")


    //val dump2 = -> ($fdisplay(s"$path/results2.hex",List(entity.dataOut2)))
    //val dump3 = -> ($fdisplay(s"$path/results3.hex",List(entity.dataOut)))


    // Input Files
    //val dataFile = FloatDataFile.random(s"$path/dataIn.hex",testLength)
    //val tapFile  = FloatDataFile.random(s"$path/tapIn.hex",testLength)
    //val biasFile = FloatDataFile.random(s"$path/biasIn.hex",testLength)

    // Output File Generation
    //val outFile    = (dataFile*tapFile)(s"$path/dataOut.hex")
    //val addFile    = (dataFile+tapFile)(s"$path/addOut.hex")
    //val totalFile  = (outFile+biasFile)(s"$path/totalOut.hex")


    //Seq(outFile,addFile,totalFile).foreach(_.create)
    //Seq(outFile,addFile,totalFile).foreach(_.createDebug)

    /*
    /- ("Load Bias")
    val res = ->($readMem(entity.biasIn,biasFile))
    entity.biasIn := res.memory(counter)
    /- ("Load Taps")
    val tres = ->($readMem(entity.tapIn,tapFile))
    entity.tapIn := tres.memory(counter)
    /- ("Load Data")
    val dres = ->($readMem(entity.dataIn,dataFile))
    entity.dataIn := dres.memory(counter)
    */



    //FileLoad()
/*
    $always_clk(clk) (
      $iff (counter == testLength) $then (
        $display("Test Finished"),
        $finish
        )
      $else (
        $display("Input %h -> %h : %h %h :: %h %h %h :: %h %h %h :: %h %h %h : %h %h",
          Some("counter, data_out, data_in, tap_in, " +
            "data_out.sgn," +
            "data_out.exp," +
            "data_out.man," +
            "data_in.sgn," +
            "data_in.exp, " +
            "data_in.man," +
            "tap_in.sgn, " +
            "tap_in.exp, " +
            "tap_in.man," +
            "neuron.mult_out_res_im," +
            "neuron.mult_out_res_im[47:47-23]"))
        )
    )

    $always_clk(clk) (
      $iff (counter == testLength) $then (

      )
        $else (
        $display("Adder %h %h %h : %h %h %h : %h %h : %h %h : %h %h : %h %h : %h %h %h",
          Some("data_out2," +
            "data_out2.exp," +
            "data_out2.man," +
            "neuron.data_out2_del," +
            "neuron.data_out2_shift," +
            "neuron.data_out2_sgn, " +
            "neuron.data_out2_ain1," +
            "neuron.data_out2_ain2, " +
            "neuron.data_out2_nsh_in," +
            "neuron.data_out2_sh_in," +
            "neuron.data_out2_sh_out," +
            "neuron.data_out2_nsh_out," +
            "neuron.data_out2_add_out," +
            "neuron.data_out2_abs_out," +
            "neuron.data_out2_exp," +
            "neuron.data_out2_int.exp," +
            "neuron.data_out2_int.man"))
    )
    )
*/
    $always_clk(clk) (
      $iff (counter == testLength) $then (
        $finish
      )
        $else (
        $display("Adder2 %h %h %h %h : %h %h %h %h : %h %h %h %h",
          Some(
            "data_out," +
            "data_out.exp," +
              "data_out.man," +
              "data_out.sgn," +
              "neuron.mult_out," +
            "neuron.mult_out.exp," +
            "neuron.mult_out.man," +
            "neuron.mult_out.sgn," +
              "bias_in," +
              "bias_in.exp," +
            "bias_in.man, " +
            "bias_in.sgn")
        )
    ))

  }



  def main(args:Array[String]) = {
    Project.createProject
  }
}

object RunNeural {
  def main(args: Array[String]): Unit = {
    NeuralTest.Project.createProject
    val run = new RunVerilator(NeuralTest.Project.tests(0),
      NeuralTest.Project.testCase,
      NeuralTest.Project)
    //run.buildOnl
    run.runAll
    //val res = NeuralTest.Project.testCase.compareAllResults()
    //val errors = res.map(_.debugString).mkString("\n")
    //System.out.println(errors)

  }
}

object Add {

  //Adder2 3f809b78 7f 009b78 0 : 3d8c90cb 7b 0c90cb 0 : 3f6fa4d8 7e 6fa4d8 0

  def main(args: Array[String]): Unit = {
    val a = java.lang.Float.intBitsToFloat(java.lang.Long.parseLong("3d8c90cb",16).toInt)
    val b = java.lang.Float.intBitsToFloat(java.lang.Long.parseLong("3f6fa4d8",16).toInt)
    val d = a + b
    System.out.println(s"$d = $a + $b")
    System.out.println(s"${FloatWrap(d)} = ${FloatWrap(a)} + ${FloatWrap(b)}")

    System.out.println("Hellow")
  }
}


