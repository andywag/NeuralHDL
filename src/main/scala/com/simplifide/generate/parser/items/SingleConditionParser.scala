package com.simplifide.generate.parser.items

import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.model.{EnclosedExpression, BasicExpressions, Expression}
import com.simplifide.generate.parser.factory.CreationFactory


/**
 * Parser which handles a single condition statment for a variable assignment meaning
 *
 * y := $iff (aaa) {} $else_iff {} ..
 *
 **/
trait SingleConditionParser {

  /** Beginning of Condition ParserStatement */
  def $iff (condition:Expression)         = new SingleConditionParser.Open(List(),Some(condition))
  def $ifo (condition:Option[Expression]) = new SingleConditionParser.Open(List(),condition)

}


object SingleConditionParser {

  /** If Condition ParserStatement */
  class IfStatement (val condition:Option[Expression], val result:Expression) extends Expression with EnclosedExpression {

    /** Convert this segment to an output statement */
    private def createInternalSegment(state:SimpleSegment,index:Int)(implicit creator:CreationFactory) = {
      if (index == 0) ConditionStatement.First(condition.get.create,List(state))
      else condition match {
        case Some(x) => ConditionStatement.Middle(x.create,List(state))
        case None    => ConditionStatement.Last(List(state))
      }
    }
    
    def createSegment(index:Int)(implicit creator:CreationFactory) = {
      createInternalSegment(result.create,index)
    }

    /** Convert this segment to an output statement */
    def createSegment(output:SimpleSegment,index:Int)(implicit creator:CreationFactory) = {
      createInternalSegment(result.createOutput(output),index)
    }



    def createAssignment (output:SimpleSegment,index:Int)(implicit creator:CreationFactory) = {
      createInternalSegment(result.createAssignment(output),index)
    }

    override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = null
    override def create(implicit creator:CreationFactory) = null

  }

  class Open(val statements:List[IfStatement], val condition:Option[Expression]) {
    /** Closes out the condition statement with the results condition */
    def $then(result:Expression*) = new Close(statements ::: List(new IfStatement(condition,BasicExpressions.List(result.toList))))
    //-def $then(result:Seq[Expression]) = new Close(statements ::: List(new IfStatement(condition,BasicExpressions.List(result.toList))))

  }


  class Close(val statements:List[IfStatement]) extends Expression with EnclosedExpression {
    /** Combine multiple close statements to create a condition clause */
    def + (add:Close) : Close = new Close(this.statements ::: add.statements)
    /** Creates an else condition */
    def $else_if (condition:Expression) = new Open(statements,Some(condition))
    /** Creates an else condition */
    def $else (result:Expression*)       = new Close(statements ::: List(new IfStatement(None,BasicExpressions.List(result.toList))))
    /** Create a flop from this condition */
    //def $at(clk:ClockControl)

    override def create(implicit creator:CreationFactory):SimpleSegment =
      new ConditionStatement(statements.zipWithIndex.map(x => x._1.createSegment(x._2)))

    override def createOutput(lhs:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
      new ConditionStatement(statements.zipWithIndex.map(x => x._1.createSegment(lhs,x._2)))

    override def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
      new ConditionStatement(statements.zipWithIndex.map(x => x._1.createAssignment(output,x._2)))

    
  }
  


}