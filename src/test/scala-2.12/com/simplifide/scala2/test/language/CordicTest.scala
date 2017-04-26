package com.simplifide.scala2.test.language





/*
import com.simplifide.generate.generator.CodeWriter
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parser.model.{ Expression}
import com.simplifide.generate.TestConstants
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.parameter.{Parameter, ModuleScope}
import com.simplifide.generate.blocks.test.ClockGenerator
import com.simplifide.generate.blocks.basic.misc.Counter
import com.simplifide.generate.test.{Isim, TestModule, Test}
import com.simplifide.generate.project.{Entity, ProjectGenerator, Module}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 7/12/11
 * Time: 8:44 AM
 * To change this template use File | Settings | File Templates.
 */

class CordicTest {

}

object CordicTest {

  // Clock which is used throughout this project
  implicit val clk = ClockControl("clk","reset")




  class Ent(implicit clk:ClockControl) extends Entity.Root("cordic") {


     val stages             = Parameter[Int]("stages",8)
     val internalWidth      = Parameter[FixedType]("internalWidth",S(12,8))
     val internalAngleWidth = Parameter[FixedType]("internalAngleWidth",S(12,8))

     val signal              = complex("signal_in",INPUT,S(8,6))
     val angle               = signal ("angle_in",INPUT,S(8,8))
     val signal_out          = complex("signal_out",OUTPUT,S(8,6))

     override val entitySignals = clk.allSignals(INPUT) ::: List(signal,angle,signal_out)
     override def createModule = new Mod(this).createModule
  }


  class Mod(entity:CordicTest.Ent) extends Module("alpha") {
    import entity.{angle,signal_out,stages,internalWidth,internalAngleWidth}



     val iW  = internalWidth.get
     val iaW = internalAngleWidth.get
     val st  = stages.get


     val signalI     = complex_array("signal_internal",WIRE,iW)(stages)
     val angleI      = array("angle_internal",WIRE,iW)(st)

     val signalIr     = complex_array("signal_internalr",REG,iW)(st)
     val angleIr      = array("angle_internalr",REG,iW)(st)

     // Register the signal Calculation
     //signalIr := signalI @@ clk
     // Register the angle Calculation
     //angleIr  := angleI  @@ clk
     // Cordic Stages without the initial stage
    /*
    val alpha = signal("alpha",WIRE,S(8,6))
    val beta  = signal("beta",WIRE,S(6,5))

    beta := alpha + (alpha + alpha)
    */


    for (i <- 1 until st) {
        val sh = math.pow(2.0,-i)
        comment("Cordic Stage" + i)
        comment("Real Calculation")
        signalI(i).real := (signalIr(i).imag)      ? (signalIr(i-1).real + sh*signalIr(i-1).imag) :: (signalIr(i-1).real - sh*signalIr(i-1).imag)
        comment("Imaginary Calculation")
        signalI(i).imag := (signalIr(i).imag)      ? (signalIr(i-1).imag - sh*signalIr(i-1).real) :: (signalIr(i-1).real + sh*signalIr(i-1).imag)
        comment("Angle Calculation")
        angleI(i)       := (signalIr(i).imag.sign) ?  (angleIr(i-1) + math.atan2(1.0,sh)) :: (angleIr(i-1) - math.atan2(1.0,sh))
     }


  }

   class TestCase(val entity:CordicTest.Ent) extends TestModule("test_cordic",entity) {

     entity.angle           --> ( 10 -> 0.0, 20 -> 0.5 )
     entity.signal.real     --> ("realin.txt",1000)
     entity.signal.imag     --> ("imagin.txt",1000)

     this.writeOutput("dataout",List(entity.signal_out.real,entity.signal_out.imag))(ClockControl("clk",""))
     this.writeOutput("datain" ,List(entity.signal.real,entity.signal.imag))(ClockControl("clk",""))

     this.createTest

   }

   object Proj extends ProjectGenerator {

    val fileLocation:String = TestConstants.locationPrefix + "cordic"
    override val root     = new Ent()
    override val tests    = List(Test(new TestCase(root)))
    override val testType = Some(new Isim(this))

  }



  def main(args:Array[String]) = {
    CordicTest.Proj.createProject2
  }
}
*/