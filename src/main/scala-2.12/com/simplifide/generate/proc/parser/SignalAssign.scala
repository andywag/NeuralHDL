package com.simplifide.generate.proc.parser

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.proc.{Controls, ProcProgram}
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.blocks.basic.Statement

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/22/11
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */

/** Class which supports holding the index of the program instruction */
  class SignalAssign(val signal:SignalTrait, val index:Int, val state:Option[Statement]) extends ProcessorSegment {

    override def getAssignment:Option[SimpleSegment] = signal.assignment

    /** @deprcated No Longer Need Index */
    def ~>(ind:Int) = new SignalAssign(signal,ind,None)

    /** Standard Assignment ParserStatement */
    /*override def <:= (rhs:Expression)(implicit base:ProcProgram):Expression = {
      val state = signal.createStatement(rhs)
      base.signalAssigns.append(new SignalAssign(this.signal,index,state))
      null
    } */

    /** Returns the segment associated with the output signal */

    //def getStatement(signal:SignalTrait):Option[SimpleStatement] = state
    /*
    def parseControls(context:ProcessorSegment):List[Controls.Value] = {
      if (state == None) return List() // Filter out when there isn't a statement (should be error)
      val statement = state.get

      val returnStatement = context.getStatement(statement.output.asInstanceOf[SignalTrait]) // Return the ParserStatement from the Module
      returnStatement match {
        case Some(x) => x.createControl(statement,context,this.index)
        case None => List()
      }
    } */

  }