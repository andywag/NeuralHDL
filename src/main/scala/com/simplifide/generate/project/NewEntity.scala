package com.simplifide.generate.project

import com.simplifide.generate.signal.{OpType, SignalTrait}
import com.simplifide.generate.language.ExtraFile
import com.simplifide.generate.parser.EntityParser
import com.simplifide.generate.generator.{SegmentReturn, ComplexSegment, SimpleSegment}
import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.func.HardwareFunction
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 2/4/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */

trait NewEntity extends PathProvider {
  
  val name:String
  val base:Option[EntityParser]
  
  /** List of signals which have not been expanded
   *  Currently of No Real Use since grouping is handled by "_"'s
   **/
  val nonExpandedSignals:List[SignalTrait]
  /** List of signals in the module */
  val signals:List[SignalTrait]
  /** Statements contained in this entity */
  val statements:List[SimpleSegment]
  /** Instances contained in this entity */
  val instances:List[NewEntityInstance[_]]
  /** List of functions contained in the module */
  val functions:List[HardwareFunction]
  /** Extra Files associated with this block */
  val files:List[ExtraFile]

  val location:Option[File]

  val head:List[SimpleSegment]

  def fileLocation(baseLocation:File) = location match {
    case Some(x) => x
    case None    => new File(baseLocation,name + ".v")
  }
  
  def newEntity(name:String              = this.name,
    base:Option[EntityParser]            = this.base,
    signals:List[SignalTrait]            = this.signals,
    statements:List[SimpleSegment]       = this.statements,
    instances:List[NewEntityInstance[_]] = this.instances,
    functions:List[HardwareFunction]     = this.functions,
    files:List[ExtraFile]                = this.files,
    nonExpandedSignals:List[SignalTrait] = this.nonExpandedSignals):NewEntity = NewEntity(name,base,signals,statements,instances,functions,files)

  // TODO Not sure why complex is treated seperately - Needs to be fixed
  def extraSignals = {
    val states = this.statements.flatMap(_.createVector).toList
    val complex = states.filter(x => x.isInstanceOf[ComplexSegment]).map(x => x.asInstanceOf[ComplexSegment])
    val internals = complex.flatMap(_.signals).filter(x => x.isInput || x.isOutput).toList
    val complexHolder = states.filter(x => x.isInstanceOf[ComplexSegment.Holder]).map(x => x.asInstanceOf[ComplexSegment.Holder])
    val internalsHolder = complexHolder.flatMap(_.signals).filter(x => x.isInput || x.isOutput).toList
    internals ::: internalsHolder ::: this.signals.filter(x => x.isInput || x.isOutput).toList
  }

  // Kind of a kludge as this requires creating the module 2 times
  def allSignals = signals ::: extraSignals ::: createModule.internalSignals

  def entities              = instances.map(_.entity.asInstanceOf[NewEntity])

  def children:List[NewEntity] = {
    NewEntity.uniqueEntity((entities ::: entities.flatMap(_.children)))
  }


  def writeModule(location:String):SegmentReturn = NewEntityGenerator(this).writeModule(location)

  def createModule:ModuleProvider = {
    ModuleProvider(name,null,this.signals ::: this.extraSignals,this.statements,this.instances,List())
  }

  def connect:NewEntity = {
    val inputPassRoot = this.inputPass
    inputPassRoot.outputPass(None)
  }
  
  private def containsOutput(signal:SignalTrait,outputs:List[SignalTrait]) = {
    def checkName(input:String, compare:String) = {
      if (input.equalsIgnoreCase(compare)) true
      else {
        val prefix = compare.split("\\.")
        input.startsWith(prefix(0))
      }
    }
    outputs.filter(x => checkName(signal.name,x.name)).size > 0
  }
  
  private def convertOutput(signal:SignalTrait) = if (signal.isReg) signal.asOutput else signal
  
  def inputPass:NewEntity = {
    // Construct the child instances
    val newInstances = this.instances.map(_.inputPass)
    // Convert the signals to the name for this module
    val localSignals =  newInstances.flatMap(_.allSignals)
    // Total signals in this module --- Shouldn't be required
    val allSignals = localSignals.flatMap(_.allSignalChildren)
    // Find a unique set of input signals
    val inputSignals = SignalTrait.uniqueSignals(allSignals.filter(_.isInput))
    // Find a unique set of output signals
    val outputSignals = SignalTrait.uniqueSignals(allSignals.filter(_.isOutput).map(convertOutput(_)))
    // Creates a New Inputs when the Input is not Attached to An Output --- Should also be other signals
    val newInputs = inputSignals.filter(x => !containsOutput(x,outputSignals ::: this.signals.toList))
    // Creates a new set of wires when there is an input or output
    val newWires  = inputSignals.filter(x => containsOutput(x,outputSignals)).map(x => x.changeType(OpType.Signal))
    // Create a set of new outputs for this module
    val newOutputs = outputSignals.filter(x => !containsOutput(x,inputSignals ::: this.signals.toList))
    // Returns a new entity
    this.newEntity(signals = this.signals ::: newInputs ::: newOutputs ::: newWires, instances = newInstances)
    
  }
  /** Second pass for creating the connections for the entities. Filters the modules outputs */
  def outputPass(outputs:Option[List[SignalTrait]]):NewEntity = {
    def containsSignal(signal:SignalTrait) =
      outputs match {
        case Some(x) => x.filter(_.name.equalsIgnoreCase(signal.name)).size > 0
        case None    => false
      }
    // Creates the entities from the child modules
    val newInstances = this.instances.map(x => x.outputPass(outputs))
    // Finds the wires from this module
    val wires = this.signals.flatMap(_.allSignalChildren).filter(x => !x.isOutput && !x.isInput)
    // Returns a list of all output signals from the child modules
    val outputSignals1 = newInstances.flatMap(_.allSignals).flatMap(_.allSignalChildren).filter(_.isOutput)
    // Removes the output signals from the total list of signasl
    val outputSignals  = outputSignals1.filter(x => !containsSignal(x)).filter(x => !containsOutput(x,wires))
    val realOutputs = outputSignals.map(x => if (x.isReg) x.copy(optype = OpType.Output) else x)

    this.newEntity(signals = this.signals ::: wires ::: realOutputs , instances = newInstances)
  }
  
  
}

object NewEntity {

  
  
  def apply(name:String, 
    base:Option[EntityParser],
    localSignals:List[SignalTrait],
    localStatements:List[SimpleSegment] ,
    localInstances:List[NewEntityInstance[_]] ,
    localFunctions:List[HardwareFunction],
    localFiles:List[ExtraFile],
    location:Option[File] = None,
    head:List[SimpleSegment] = List()) =
    new Impl(name,base,localSignals,localStatements,localInstances,localFunctions,localFiles,location,head,localSignals)
  

  
  case class Impl (
    val name:String, 
    override val base:Option[EntityParser],
    val signals:List[SignalTrait],
    val statements:List[SimpleSegment] ,
    val instances:List[NewEntityInstance[_]] ,
    val functions:List[HardwareFunction],
    val files:List[ExtraFile],
    val location:Option[File],
    val head:List[SimpleSegment],
    val nonExpandedSignals:List[SignalTrait]) extends NewEntity

  /** Convenience method for creating a unique set of entities.
   *
   * TODO Copy of Signal Trait
   * TODO Shouldn't be required. Only due to improper hierarchy
   */
  def uniqueEntity(signals:List[NewEntity]):List[NewEntity] = {
    val sortedSignals = signals.sortBy(_.name)
    val builder = new ListBuffer[NewEntity]()
    for (signal <- sortedSignals) {
      if (builder.length == 0) builder.append(signal)
      else if (!signal.name.equalsIgnoreCase(builder(builder.length-1).name)) {
        builder.append(signal)
      }
    }
    builder.toList
  }
    
}
