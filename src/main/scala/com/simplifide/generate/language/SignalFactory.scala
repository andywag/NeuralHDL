package com.simplifide.generate.language

import com.simplifide.generate.parser.model.{Model, Expression}
import com.simplifide.generate.parser.ObjectFactory
import com.simplifide.generate.signal.FixedType
import com.simplifide.generate.blocks.basic.fixed.{Roundable, MultiplySegment, RoundSegment, AdditionSegment}

/**
  * Factory methods for creating rounding expression
  */

class SignalFactory {

}

object SignalFactory {
   def round(expression:Expression,internal:FixedType = FixedType.Simple):Expression = {
    expression match {
      case x:Roundable         => x.createNewRound(Roundable.Round,internal)
      case _                   => null//RoundSegment.Round(expression.create,internal)   //ObjectFactory.RoundInt(expression,fixed,internal)
    }
  }

  def roundClip(expression:Expression,internal:FixedType = FixedType.Simple):Expression = {
    expression match {
      case x:Roundable         => x.createNewRound(Roundable.RoundClip,internal)
      case _                   => null //RoundSegment.RoundClip(expression.create,internal)
    }
  }

  def truncate(expression:Expression,internal:FixedType = FixedType.Simple):Expression = {
    expression match {
      case x:Roundable         => x.createNewRound(Roundable.Truncate,internal)
      case _                   => null//RoundSegment.Truncate(expression.create,internal)
    }
  }

  def truncateClip(expression:Expression,internal:FixedType = FixedType.Simple):Expression = {
    expression match {
      case x:Roundable         => x.createNewRound(Roundable.TruncateClip,internal)
      case _                   => null //RoundSegment.TruncateClip(expression.create,internal)
    }
  }
}