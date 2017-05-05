package com.simplifide.generate.proc

import collection.mutable.ListBuffer
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.model.{Expression, Clock}
import com.simplifide.generate.parser.{BaseParser, ObjectFactory, ModuleParser}
import com.simplifide.generate.blocks.basic.memory.Memory.MemoryBus
import parser.{ProcessorStatement, SignalAssign, ProcessorSegment}
import com.simplifide.generate.project.{NewEntity,  Module}
import com.simplifide.generate.parser.factory.{CreationFactory}
import com.simplifide.generate.util.InternalLogger

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/19/11
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */

class ProcProgram(val entity:NewEntity,
                  val length:Int) extends ModuleParser {

  //implicit val creator:CreationFactory = HardwareCreationFactory

  implicit val base = this

  override val name:String = "program"
  /** ProcStatement statements for this program */
  val signalAssigns = new ListBuffer[ProcessorStatement]()
  /** List of controls contained in the program */
  val controls = null // entity.controls
  /** Instruction associated with the control */
  val instruction = Instruction(controls)


  /** Create a complete map of the control signals */
  def controlMap:List[Controls] = {
     null
  }

  /*
  def baseInstruction:Instruction = {
    val controlValues = this.signalAssigns.zipWithIndex.flatMap(x => x._1.createControls(x._2)).toList
    val controls      = controlValues.map(_.control)
    controlValues.foreach(x => System.out.println(x.index + " => Control" + x.control + " -> " + x.value))
    Instruction(controls)
  }
  */

  /** Parse the Source File and find a list of Controls */
  def parse:ProgramMap = {
    // Create the outputs for a single statement
    val controlValues = this.signalAssigns.zipWithIndex.flatMap(x => x._1.createControls(x._2)).toList
    val controls      = controlValues.map(_.control)

    controlValues.foreach(x => InternalLogger.debug(x.index + " => Control" + x.control + " -> " + x.value))

    val instruction   = Instruction(controls)
    val programMap    = ProgramMap(instruction,controlValues,this.length)

    new ControlHTML(programMap).createTable("C:\\designs2\\Generator\\test\\com\\simplifide\\scala2\\test\\procgen\\proc_output\\doc\\table.html")
    //val controls = this.signalAssigns.toList.flatMap(x => x.parseControls(this.entity))
    //ProgramMap(this.instruction,controls,length)
    programMap
  }

  /** Convenience Conversion to and From Signal */
  implicit def Signal2SignalAssign(signal:SignalTrait):SignalAssign = new SignalAssign(signal,-1,None)
  implicit def SignalAssign2Signal(signal:SignalAssign):SignalTrait = signal.signal
  /** Convenience methods for converting to signal holders */
  //implicit def Memory2MemoryAssign(memory:MemoryBus):MemoryAssign = new MemoryAssign(memory)


}

object ProcProgram {




}
