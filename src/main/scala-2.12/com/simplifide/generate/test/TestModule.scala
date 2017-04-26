package com.simplifide.generate.test

import com.simplifide.generate.parser.ModuleParser
import com.simplifide.generate.generator.SimpleSegment
import javax.naming.InitialContext
import com.simplifide.generate.blocks.basic.misc.Counter
import sun.java2d.pipe.SpanShapeRenderer.Simple
import com.simplifide.generate.blocks.basic.flop.{SimpleFlop, ClockControl}
import com.simplifide.generate.blocks.basic.operator.BinaryOperator
import com.simplifide.generate.signal.{Constant, OpType, SignalTrait}
import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.blocks.test._
import com.simplifide.generate.project.{NewEntityInstance, Module}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/22/11
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */

/*
class TestModule(name:String, val dut:Entity)(implicit clk:ClockControl)
  extends Module(name) with TestParser with TestControlParser {

  implicit val testModule = this
  val counter = signal("test_counter",REG,U(32,0))
  val length  = 2000

  /** Create a list of signals for this module */
  def createSignals:List[SignalTrait] = dut.ioSignals.map(_.changeTestType)



  override def createStatements:List[SimpleSegment] = super.createStatements
    //::: (if (this.initials.length > 0) List(new Initial(this.initials.toList).split(0).asInstanceOf[SimpleSegment]) else List())

  override def createInstances:List[NewEntityInstance[_]] = List(NewEntityInstance(dut,dut.name))


  def createFinishFlop(clk:ClockControl) = {
     val condition = BinaryOperator.GTE(counter,Constant(length,counter.fixed.width))
     val st = (Some(condition),List(Functs.Finish))
     val total = ConditionStatement(List(st))
     new SimpleFlop(None,clk,null,total)
  }

  /** Function which is called after the test is created */
  def createTest {
    val clkWoReset = ClockControl(clk.clockSignal().name,"")

    this.assign(new ClockGenerator(clk))
    clk.clockSignal()      --> 0
    counter                --> 0
    clk.resetSignal().get  --> 1
    clk.resetSignal().get  -#> (0,50)
    this.assign(new Counter(counter)(clkWoReset))
    // Create the signals which are assoicated with the original entity
    this.appendSignals(createSignals)
    // Create the Control Segment
    this.assign(new TestControlParser.Mux(counter,controlValues.toList))
    // Create the flop specifying the end
    this.assign(this.createFinishFlop(clkWoReset))

  }


  /** Load this signal from a file */
  def loadSignal(signal:SignalTrait, filename:String, length:Int) = {
    val array = new SignalTrait.InternalArray(signal.name + "_arr",OpType.Register,signal.fixed, length)
    signals.append(array)
    initials.append(new FileLoad.ReadMemH(array,filename))
    assign(new FileLoad.LoadCommand(signal,array,counter))
  }

  def writeOutput(filename:String,inSignals:List[SignalTrait])(implicit clk:ClockControl) = {
    val fptr = SignalTrait(filename + "_ptr",REG,U(64,0))
    signals.append(fptr)
    initials.append(new FileDump.FOpen(filename + ".txt",fptr))
    assign(new FileDump.DisplayFlop(fptr,inSignals))
  }




}

*/