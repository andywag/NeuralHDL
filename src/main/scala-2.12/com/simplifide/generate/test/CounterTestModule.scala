package com.simplifide.generate.test

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.test.ClockGenerator
import com.simplifide.generate.blocks.basic.misc.Counter

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 9/26/11
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */

/*

class CounterTestModule(name:String, dut:Entity)(implicit clk:ClockControl) extends TestModule(name,dut) {
  //val counter = signal("counter",WIRE,U(32,0))

    /** Function which is called after the test is created */
  override def createTest {
    // Create the signals which are assoicated with the original entity
    this.appendSignals(createSignals)
    // Create the Control Segment
    this.assign(new TestControlParser.Mux(counter,controlValues.toList))

    assign(new ClockGenerator(clk,10))
    assign(new Counter(counter)(ClockControl("clk","")))


     clk.clockSignal()                                 --> 0
     if (clk.reset.isDefined)  clk.resetSignal().get   --> 0
     if (clk.enable.isDefined) clk.enableSignal().get  --> 0
     counter                                           --> 0



  }

}
*/