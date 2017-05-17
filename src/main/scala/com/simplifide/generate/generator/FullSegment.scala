package com.simplifide.generate.generator

import com.simplifide.generate.parser.{ConditionParser, SignalParser}
import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/8/17.
  */
trait FullSegment extends SimpleSegment with ConditionParser with SignalParser {

  val result:SignalTrait

  def createCode(implicit writer:CodeWriter):SegmentReturn = {

    val internalSignals            = this.signals.toList
    val internalStatements = this.statements.toList
    return writer.createCode(result).extra(internalStatements.map(_.create),internalSignals)
  }


}
