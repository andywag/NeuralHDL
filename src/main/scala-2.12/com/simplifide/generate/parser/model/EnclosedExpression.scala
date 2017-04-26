package com.simplifide.generate.parser.model

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Expression which needs to be enclosed inside an always block
 */

trait EnclosedExpression {
  def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment
}
