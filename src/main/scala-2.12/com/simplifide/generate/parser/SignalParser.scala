package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import factory.CreationFactory
import model.{Model, Expression}
import com.simplifide.generate.generator.CodeWriter.Fixed
import com.simplifide.generate.language.SignalFactory
import com.simplifide.generate.signal.FixedType
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType
import com.simplifide.generate.blocks.basic.fixed.{RoundSegment, Roundable}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/21/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

trait SignalParser  {

  def $round_generic(expression:Expression,internal:FixedType=FixedType.Simple,roundType:RoundType,name:String = "")(implicit creator:CreationFactory):Expression =
    expression match {
      case x:Roundable         => x.createNewRound(Roundable.Round,internal)
      case _                   => RoundSegment(name,expression.create,internal,FixedType.Simple,roundType)
    }

  def $round(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.round(expression,internal)
  def $roundClip(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.roundClip(expression,internal)
  def $truncate(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.truncate(expression,internal)
  def $truncateClip(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.truncateClip(expression,internal)

  def  R(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.round(expression,internal)
  def RC(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.roundClip(expression,internal)
  def  T(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.truncate(expression,internal)
  def TC(expression:Expression,internal:FixedType=FixedType.Simple):Expression =
    SignalFactory.truncateClip(expression,internal)

}

object SignalParser {

  def signal(name:String):String = name

}