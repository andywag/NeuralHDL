package com.simplifide.generate.proc

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.ClockControl

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/29/11
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */

/*

class InstructionDecoder(name:String,
    val inputSignal:SignalTrait,
    val instruction:Instruction)(implicit clk:ClockControl) extends Entity.Leaf(name) {


    var bottom = 0
    for (control <- instruction.controls) {
      control.signal := inputSignal( (bottom + control.signal.fixed.width-1) -> bottom )
      bottom = bottom + control.signal.fixed.width
    }

    override val entitySignals = clk.allSignals(INPUT) :::
      this.extraSignals :::
      List(inputSignal.changeType(INPUT)) :::
      instruction.controls.map(x => x.signal.changeType(OUTPUT))

  }

 */