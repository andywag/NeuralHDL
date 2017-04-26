package com.simplifide.generate.project

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment, SegmentReturn}
import com.simplifide.generate.signal.{SignalTrait, OpType}
import com.simplifide.generate.proc.{Controls, ControlBase}
import com.simplifide.generate.parser.{EntityParser}
import com.simplifide.generate.language.ExtraFile
/*
/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/7/11
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class contains information about the modules hierarchy as well as global information used in creating
 * the modules. This class is also instrumental in creating the hierarchy for the design
 *
 * @parameter name Name of Entity
 * @parameter connectionName Name of the Connection for this block
 * @parameter converter Class which converts the signals for the connections
 *
 */
   // TODO Needs Code to be Cleaned up

trait Entity extends EntityParser {
  /** Name of the Entity */
  val name:String
  /** Clock for this entity */
  implicit val clk:ClockControl
  /** Signals Contained in this entity */

  /** Entity Instances */
  val instances:List[NewEntityInstance[_]] = List()

  /** Input/Output Signals for this Entity */
  //val entitySignals:List[SignalTrait] = List()
  def entitySignals:List[SignalTrait] = signals.toList

  /** Children of this Entity */
  lazy val entities:List[Entity] = instances.map(x => x.entity.asInstanceOf[Entity])
  ///** Segments contained in the module */
  //val extraFiles:List[ExtraFile] = List()
  lazy val extraFiles = extraFilesInternal.toList

  /** Real Entity used */
  val entity = this
  /** List of Input Signals in this Entity */
  def inputSignals:List[SignalTrait]  = entitySignals.filter(_.isInput)
  /** List of Output Signals in this Entity */
  def outputSignals:List[SignalTrait] = entitySignals.filter(_.isOutput)
  /** Complete List of Input and Output Signals */
  def ioSignals:List[SignalTrait]     = inputSignals ::: outputSignals
  /** Internal signals contained in this entity */
  val internalSignals:List[SignalTrait] = List()
  /** Internal signals contained in this entity */
  val internalStatements:List[SimpleSegment] = List()
  /** Create Recursive List of Children */
  def children:List[Entity] = (entities ::: entities.flatMap(x => x.children)).toSet.toList
  /** First pass for creating the connections for the entities. Attaches the module inputs */

  /** Write the entity and module to a file */
  def writeModule(fileLocation:String):SegmentReturn = null//EntityGenerator(entity).writeModule(fileLocation)

  /** Creates the body of this module. By default this attaches the instances associated with this entity */
  override def createModule:ModuleProvider = {
    /*val states = this.statements.flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    ModuleProvider(name,null,this.entitySignals ::: this.internalSignals,states,this.instances,List())
    */
    null
  }

  def extraSignals = {
    /*
    val states = this.statements.flatMap(_.split).toList.map(_.asInstanceOf[SimpleSegment])
    val complex = states.filter(x => x.isInstanceOf[ComplexSegment]).map(x => x.asInstanceOf[ComplexSegment])
    val internals = complex.flatMap(_.signals).filter(x => x.isInput || x.isOutput).toList
    val complexHolder = states.filter(x => x.isInstanceOf[ComplexSegment.Holder]).map(x => x.asInstanceOf[ComplexSegment.Holder])
    val internalsHolder = complexHolder.flatMap(_.signals).filter(x => x.isInput || x.isOutput).toList
    internals ::: internalsHolder ::: this.signals.filter(x => x.isInput || x.isOutput).toList
    */
    null
  }





  implicit def Instance2Entity[T <: Entity](instance:NewEntityInstance[T]):T = instance.entity
    //if (instance != null) instance.entity else null

  /**
    *  Create the signals in the branch module as a function of the inputs. This branch adds the inputs up the hierarchy
    *  which don't already exist as outputs from it's siblings. In the latter case a wire is appended
  **/

  private def containsOutput(signal:SignalTrait,outputs:List[SignalTrait]) =
        outputs.filter(_.name.equalsIgnoreCase(signal.name)).size > 0

  def inputPass:Entity = {
      // Construct the child instances
      val newInstances = instances.map(x => x.inputPass)
      // Convert the signals to the name for this module
      val signals =  newInstances.flatMap(_.allSignals)
      // Total signals in this module --- Shouldn't be required
      val allSignals = signals.flatMap(_.allSignalChildren)
      // Find a unique set of input signals
      val inputSignals = SignalTrait.uniqueSignals(allSignals.filter(_.isInput))
      // Find a unique set of outptu signals
      val outputSignals = SignalTrait.uniqueSignals(allSignals.filter(_.isOutput))
      // Creates a New Inputs when the Input is not Attached to An Output --- Should also be other signals
      val newInputs = inputSignals.filter(x => !containsOutput(x,outputSignals ::: this.signals.toList))
      // Creates a new set of wires when there is an input or output
      val newWires  = inputSignals.filter(x => containsOutput(x,outputSignals)).map(x => x.changeType(OpType.Signal))
      // Returns a new entity

      new ExpandedEntity(this,this.internalSignals ::: this.signals.toList,this.entitySignals ::: newInputs ::: newWires,newInstances,
        this.statements.map(x => x.create).toList ::: this.internalStatements)
    }
  /** Second pass for creating the connections for the entities. Filters the modules outputs */
  def outputPass(outputs:Option[List[SignalTrait]]):Entity = {
    def containsSignal(signal:SignalTrait) =
      outputs match {
        case Some(x) => x.filter(_.name.equalsIgnoreCase(signal.name)).size > 0
        case None    => false
      }
    // Creates the entities from the child modules
    val newInstances = this.instances.map(x => x.outputPass(outputs))
    // Finds the wires from this module
    val wires = this.entitySignals.flatMap(_.allSignalChildren).filter(x => !x.isOutput && !x.isInput)
    // Returns a list of all output signals from the child modules
    val outputSignals1 = newInstances.flatMap(_.allSignals).flatMap(_.allSignalChildren).filter(_.isOutput)
    // Removes the output signals from the total list of signasl
    val outputSignals  = outputSignals1.filter(x => !containsSignal(x)).filter(x => !containsOutput(x,wires))

    new ExpandedEntity(this,this.internalSignals ::: wires ::: this.signals.toList,this.entitySignals ::: outputSignals ,newInstances,
      this.statements.map(x => x.create).toList ::: this.internalStatements)
  }


}

object Entity {
  /** Root Entity for a project */
  class Root(name:String)(override implicit val clk:ClockControl) extends Branch(name) {

    val outputs:Option[List[SignalTrait]] = None
    def connect:Entity = {
      val inputPassRoot = this.inputPass
      inputPassRoot.outputPass(outputs)
    }
  }

  abstract class Leaf(override val name:String)(override implicit val clk:ClockControl) extends Entity {

  }

  class Branch(override val name:String)(override implicit val clk:ClockControl) extends Entity {




  }
}
*/