package com.simplifide.generate.blocks.basic.typ

import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.items
import com.simplifide.generate.parser.items.SingleConditionParser
import com.simplifide.generate.parser.items.SingleConditionParser.IfStatement
import com.simplifide.generate.parser.model.Expression

/**
  * Created by andy on 5/11/17.
  */
class ConditionType {

}

object ConditionType {



  case class IfState[T](condition:Option[Expressable[T]], result:Expressable[T]) {

    private def createInternalSegment[S](index:Int, output:Option[Assignable[S]])(implicit creator:CreationFactory) = {
      def resultStatement = {
        output match {
          case Some(x) => (x ::= result)
          case _       => result.expression.create
        }
      }
      if (index == 0) {
        val cond = condition.map(_.expression).map(_.create)
        ConditionStatement.First(cond.get,List(resultStatement))
      }
      else condition match {
        case Some(x) => ConditionStatement.Middle(x.expression.create,List(resultStatement))
        case None    => ConditionStatement.Last(List(resultStatement))
      }
    }

    def createSegment(index:Int)(implicit creator:CreationFactory) = {
      createInternalSegment(index,None)
    }

    /** Convert this segment to an output statement */
    def createStatement[S](output:Assignable[S],index:Int)(implicit creator:CreationFactory) = {
      createInternalSegment(index,Some(output))
    }



  }

  case class Close[T](val statements:List[IfState[T]])   {
    def $else_if (condition:Expressable[T]) = new Open(statements,Some(condition))
    def $else (result:Expressable[T])       = new Close(statements ::: List(new IfState(None,result)))

    def createStatement[S](output:Assignable[S])(implicit creator:CreationFactory) = {
      val result = new ConditionStatement(statements.zipWithIndex.map(x => x._1.createStatement(output,x._2)))
      result
    }
    /*
    /** Create the simple segment */
    override def create(implicit creator: CreationFactory): SimpleSegment =
      new ConditionStatement(statements.zipWithIndex.map(x => x._1.createSegment(x._2)))
    /** Create Expression as a function of the output */
    override def createOutput(output: SimpleSegment)(implicit creator: CreationFactory): SimpleSegment =
      new ConditionStatement(statements.zipWithIndex.map(x => x._1.createSegment(output,x._2)))
  */
  }

  case class Open[T](val statements:List[IfState[T]],condition:Option[Expressable[T]]) {
    def $then(in:Expressable[T]) = Close(statements ::: List(new IfState[T](condition,in)))
    def $then(in:Expressable[T]*) = Close(statements ::: List(new IfState[T](condition,in(0))))


  }

}
