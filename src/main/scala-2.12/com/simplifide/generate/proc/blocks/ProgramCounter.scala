package com.simplifide.generate.proc.blocks

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.Conversions._


/**
 * Block which defines a program counter for this block
 */
class ProgramCounter(val jump:SignalTrait,
                     val branch:SignalTrait,
                     val enableJump:SignalTrait,
                     val enableBranch:SignalTrait,
                     val programCount:SignalTrait)(implicit clk:ClockControl) extends ComplexSegment {

  val programCountIn = signal("progamCountIn",WIRE,programCount.fixed)

  def createBody {
    $always_star(
      $iff (enableBranch) $then (
        programCountIn ::= branch
      )
      $else_if (enableJump) $then (
        programCountIn ::= programCountIn + jump
      )
      $else (
       programCountIn  ::= programCountIn + 1
      )
    )
    flop(programCount ::= programCountIn)
  }

}