package com.simplifide.generate.language

import com.simplifide.generate.blocks.basic.fixed.MultiplySegment
import com.simplifide.generate.signal.complex.ComplexSignal
import com.simplifide.generate.blocks.basic.flop.{ClockControl, SimpleFlopList}
import com.simplifide.generate.parser.model.{ Clock}
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.signal.{OpType, SignalTrait}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.blocks.basic.Statement


class FlopFactory {

}


/**
 * Factory methods for creating flops
 **/
object FlopFactory {
  def apply(clk:Clock,output:SimpleSegment,input:SimpleSegment):SimpleSegment =  {
      input match {
        // Standard Operations
        /*case MultiplySegment.Truncate(name,a:ComplexSignal,b:ComplexSignal,fixed,internal) =>
          new ComplexMultiplySegment.Truncate(output.name,clk,output.asInstanceOf[ComplexSignal],a,b,internal)
        case MultiplySegment.TruncateClip(name,a:ComplexSignal,b:ComplexSignal,fixed,internal) =>
          new ComplexMultiplySegment.TruncateClip(output.name,clk,output.asInstanceOf[ComplexSignal],a,b,internal)
        case MultiplySegment.Round(name,a:ComplexSignal,b:ComplexSignal,fixed,internal) =>
          new ComplexMultiplySegment.Round(output.name,clk,output.asInstanceOf[ComplexSignal],a,b,internal)
        case MultiplySegment.RoundClip(name,a:ComplexSignal,b:ComplexSignal,fixed,internal) =>
          new ComplexMultiplySegment.RoundClip(output.name,clk,output.asInstanceOf[ComplexSignal],a,b,internal)
        */
        case _ => simpleFlop(clk,output,input)
      }
    }

  /** Create a new flop from the expressions. This operation breaks the operation out of the flop */
  def simpleFlop(clk:Clock,output:SimpleSegment,input:SimpleSegment) = {
    val outWire = output.asInstanceOf[SignalTrait].newSignal(opType = OpType.Signal)
    val assign = new Statement.Assign(outWire,input,List(outWire))
    val res = List(new SimpleFlopList.Segment(output,None))
    val en  = List(new SimpleFlopList.Segment(output,Some(outWire)))
    BasicSegments.List(List(assign,new SimpleFlopList(None,clk,res,en)))

  }

  /** Create an assign statement as well as the flop segment from the input statement
   *
   *  This method splits the logic from the flop to avoid the extra statements potentially being included in the delay
   *
   */
   // TODO Needs to be added to the SimpleFlop Function
  private def createAssign(statement:Statement):(Statement,SimpleFlopList.Segment) = {
    val outWire = statement.output.asInstanceOf[SignalTrait].newSignal(opType = OpType.Signal)
    val assign = new Statement.Assign(outWire,statement.input,List(outWire))
    val segment = new SimpleFlopList.Segment(statement.output,Some(outWire))
    (assign,segment)
  }

  /** Creates a flop containing the expressions included in the body of the function */
  def simpleFlopList(statements:List[Statement])(implicit clk:ClockControl):SimpleSegment = {
    val outputs = statements.map(_.output)
    val resets = outputs.map(x => new SimpleFlopList.Segment(x,None))
    val assignEnables = statements.map(createAssign(_))
    val flop = new SimpleFlopList(None,clk,resets,assignEnables.map(_._2))
    return BasicSegments.List(assignEnables.map(_._1) ::: List(flop))
  }
}