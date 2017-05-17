package com.simplifide.generate.parser

import block.ParserStatement
import model.Expression
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.signal.SignalTrait

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/12/11
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */

class ExpressionReturn(val output:Expression,
                       val states:List[SimpleSegment],
                       val signals:List[SignalTrait] = List()) {

  val segmentStates = states.map(_.asInstanceOf[SimpleSegment])
}

object ExpressionReturn {

}