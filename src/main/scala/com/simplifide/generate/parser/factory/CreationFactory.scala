package com.simplifide.generate.parser.factory

import com.simplifide.generate.parser.items.SingleCaseParser
import com.simplifide.generate.parser.model.Expression

/**
  * Factory used to convert the expressions to segments
  */

trait CreationFactory {}

object CreationFactory {
  object Hardware extends CreationFactory
  object Function extends CreationFactory
  object Initial  extends CreationFactory
}