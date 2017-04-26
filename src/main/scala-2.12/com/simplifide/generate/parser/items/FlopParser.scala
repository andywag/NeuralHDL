package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.items.FlopParser.Base
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.blocks.basic.flop.{SimpleFlop, ClockControl}
import com.simplifide.generate.signal.{NewConstant, Constant}

/**
 * Method used to generate a flop based on expressions similar to
 *
 * output := $flop clk $reset xxx $enable xxx
 */

trait FlopParser {
  def $flop (clk:ClockControl) = new FlopParser.Base(clk,None,None)

}

object FlopParser {
  class Base(val clk:ClockControl, val reset:Option[Expression],val enable:Option[Expression]) extends Expression {
    
    def copy(clk:ClockControl = this.clk,reset:Option[Expression] = this.reset, enable:Option[Expression] = this.enable) =
      new Base(clk,reset,enable)
  
    def $reset(expression:Expression)  = copy(reset = Some(expression))
    def $enable(expression:Expression) = copy(enable = Some(expression))

    def create(implicit creator:CreationFactory):SimpleSegment = {
      null
    }
    def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment = {
      val resetStatement = reset match {
        case Some(x) => new Statement.Reg(output,x.createOutput(output))
        case None    => new Statement.Reg(output,NewConstant(0,output.fixed))
      }
      val enableStatement = enable match {
        case Some(x) => new Statement.Reg(output,x.createOutput(output))
        case None    => new Statement.Reg(output,NewConstant(0,output.fixed))
      }
      new SimpleFlop(None,clk,resetStatement,enableStatement)
    }

  
  }
}