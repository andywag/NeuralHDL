package com.simplifide.generate.blocks.basic.fixed

import com.simplifide.generate.signal.FixedType
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.basic.fixed.Roundable.RoundType

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 2/13/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */

trait Roundable {

  def createNewRound(roundType:RoundType,internal:FixedType = FixedType.Simple):SimpleSegment

  /*
  def createRound(internal:FixedType = FixedType.Simple):SimpleSegment
  def createRoundClip(internal:FixedType = FixedType.Simple):SimpleSegment
  def createTruncate(internal:FixedType = FixedType.Simple):SimpleSegment
  def createTruncateClip(internal:FixedType = FixedType.Simple):SimpleSegment
  */
}

object Roundable {
  
  class RoundType {
    def round:Boolean = this match {
      case Round     => true
      case RoundClip => true
      case _         => false
    }
    
    def clip:Boolean = this match {
      case TruncateClip => true
      case RoundClip    => true
      case _            => false
    }
  }
  
  object Truncate extends RoundType
  object TruncateClip extends RoundType
  object Round extends RoundType
  object RoundClip extends RoundType
  
}
