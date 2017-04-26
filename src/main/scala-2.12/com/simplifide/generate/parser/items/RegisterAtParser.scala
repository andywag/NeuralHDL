package com.simplifide.generate.parser.items

import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.blocks.basic.flop.{SimpleFlopSegment, SimpleFlop, ClockControl}
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Flop generator class
 */
trait RegisterAtParser {


}

object RegisterAtParser {
  
  class Flop(val register:Expression, val clk:ClockControl) extends Expression {


    override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = {
      val internal = new Statement.Reg(output,register.createOutput(output))
      new SimpleFlopSegment(clk,internal).create
    }

    override def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = {
      val internal = register.createAssignment(output)
      new SimpleFlopSegment(clk,internal).create
    }

    override def create(implicit creator:CreationFactory):SimpleSegment = {
      null
    }

  }
}