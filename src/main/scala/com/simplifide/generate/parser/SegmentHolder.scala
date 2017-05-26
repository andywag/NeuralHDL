package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import model.{ Expression}
import com.simplifide.generate.generator.{SimpleSegment}
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.signal.{OpType, RegisterTrait, SignalTrait}
import com.simplifide.generate.blocks.basic.{Statement}


/**
 * Trait which contains a list of statements which are appended using the assign method contained in this trait.
 */

trait SegmentHolder extends SignalHolder{
  /** List of Expressions contained in this trait*/
  val statements = new ListBuffer[Expression]()

  /** Finds a statement associated with this output */
  def getStatement(signal:SignalTrait):Option[Statement] = {
    statements.filter(x => x.isInstanceOf[Statement]).find(x => signal.generalEquals(x.asInstanceOf[Statement].output)) match {
      case Some(x) => if (x.isInstanceOf[Statement]) Some(x.asInstanceOf[Statement]) else None
      case _ => None
    }
  }

  /** List of all of the statements associated with this segment of code */
  def allStatements = autoFlops ::: statements.toList


  def ->[T <: Expression](statement:T):T = {
    statements.append(statement)
    statement
  }
  /** Attaches and assign statement */
  def assign[T <: Expression](statement:T):T = {
    statements.append(statement)
    statement
  }

  /** Attaches a list of statements to the design */
  def assign(statement:List[Expression]) =
    statements.appendAll(statement)

  /** Convenience Operation for adding a comment to the code */
  def /- (value:String) = comment(value)
  /** Adds a comment to the code */
  def comment(value:String) = statements.append(new Comment.SingleLine(value))
  def multiLineComment(value:String) = statements.append(new Comment.MultipleSingleLine(value))



  /** Create flops which are automatically create from addresses */
  def autoFlops:List[SimpleSegment] = {
    val registers = this.signals.filter(x => x.isInstanceOf[RegisterTrait[_]]).map(x => x.asInstanceOf[RegisterTrait[_]])
    if (registers.length > 0) List(registers.map(x => x.createFlop).reduceLeft(_ + _)) else List()
  }


}

object SegmentHolder {
  object Impl extends SegmentHolder
}