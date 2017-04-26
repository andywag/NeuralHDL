package com.simplifide.generate.parser.items

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.test.Initial
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.{ConditionParser, ModuleParser, SegmentHolder, EntityParser}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/30/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */

trait InitialParser {

  self: ConditionParser =>




  def $initial(expressions:Expression*) = {
    this.assign(Initial(expressions.map(_.create(CreationFactory.Initial)).toList))
  }


}

object InitialParser {

}
