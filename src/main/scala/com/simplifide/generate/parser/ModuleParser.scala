package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.project.{ModuleProvider, NewEntityInstance}
import com.simplifide.generate.project.ModuleProvider._
import com.simplifide.generate.language.{DescriptionHolder, ExtraFile}
import com.simplifide.generate.blocks.func.HardwareFunction
import factory.CreationFactory
import items._

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/21/11
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */

// TODO Replace the extra file outputs for the processor interface and state machine

trait ModuleParser extends ConditionParser with SignalParser with DescriptionHolder with SingleConditionParser with
  SingleCaseParser with ExpressionGroupParser with BasicParser with MiscParser  {

  implicit val creator = CreationFactory.Hardware

  val name:String



  /** Splits the statements into groups */
  /*def transform = {
    val newStatements = this.allStatements.map(x => if (x != null) x.create)
    this.statements.clear
    this.statements.appendAll(newStatements)
  }
  */



    /** Create the statements for this module */
  protected def createStatements:List[SimpleSegment] = this.statements.toList.map(_.create).flatMap(_.createVector)
  /** Create the instances for this module */
  protected def createInstances:List[NewEntityInstance[_]] = List()

  /** Create a module provider from this module */
  def createModule:ModuleProvider = {
    //this.transform
    val mod = ModuleProvider(name,
        null,
        this.signals.toList.map(x => x.asInstanceOf[SignalTrait]),
        this.createStatements,
        this.createInstances,
        List() /*this.extraFilesInternal.toList*/)
    mod.description = this.description
    mod
  }

}

object ModuleParser {

}