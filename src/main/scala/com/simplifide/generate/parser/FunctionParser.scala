package com.simplifide.generate.parser

import com.simplifide.generate.language.DescriptionHolder
import factory.CreationFactory
import items.{ExpressionGroupParser, SingleCaseParser, SingleConditionParser}
import com.simplifide.generate.blocks.func.HardwareFunction
import com.simplifide.generate.signal.SignalTrait

/**
 * Parser which handles the creation of a function or trait
 */

trait FunctionParser extends ConditionParser with SignalParser with DescriptionHolder with SingleConditionParser with
  SingleCaseParser with ExpressionGroupParser with BasicParser {

  implicit val creator = CreationFactory.Function

  val name:String

  def createFunction:HardwareFunction = {
    val state = statements.toList.map(_.create).flatMap(_.createVector).flatMap(_.createSplit)
    this match {
      case x:FunctionParser.Task      => new HardwareFunction.Task(name,signals.toList, state)
      case x:FunctionParser.Function  => new HardwareFunction.Function(name,signals.toList, state)
    }
  }
  
}

object FunctionParser {
  class Task(val name:String) extends FunctionParser
  
  class Function(val name:String) extends FunctionParser {
    val output = SignalTrait(name)
  }
}