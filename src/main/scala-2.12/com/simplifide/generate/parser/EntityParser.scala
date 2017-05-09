package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.language.ExtraFile
import com.simplifide.generate.project.{Connection, NewEntity, NewEntityInstance}
import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.signal.SignalTrait
import items.InstanceParser

/**
 * Parser which creates an entity
 */

trait EntityParser extends ModuleParser with FunctionHolder with InstanceParser {



  val location:Option[java.io.File] = None
  /** Set of extra files associated with this entity */
  protected val extraFilesInternal = new ListBuffer[ExtraFile]()
  /** Set of instances associated with this entity */
  protected val internalInstances = new ListBuffer[NewEntityInstance[_]]()
  /** Set of values included in the file which are defined above the entity */
  protected val headSegments = new ListBuffer[SimpleSegment]()


  /** Append a file to the list of files associated with this entity */
  def file(extraFile:ExtraFile) = {
    extraFilesInternal.append(extraFile)
    extraFile
  }
  /** Append an instance to the list of instances */
  def instance(newInstance:NewEntityInstance[_]) = {
    internalInstances.append(newInstance)
    newInstance
  }
  def instance(entity:EntityParser):NewEntityInstance[_] = {
    this.instance(entity.createEntity,entity.name)
  }

  /** Append a segment to the head */
  def head(segment:SimpleSegment) = {
    headSegments.append(segment)
    segment
  }



  def instance(entity:NewEntity,name:String,connections:Map[SignalTrait,SignalTrait]):NewEntityInstance[_] = {
   this.instance(entity,name,Connection.MapSignalConnection.name(connections))
  }
  
  def instance(entity:NewEntity,name:String,connection:Connection = Connection.Default) = {
    val newInstance = NewEntityInstance(entity,name,connection)
    internalInstances.append(newInstance)
    newInstance
  }

  /** Create the internal contents of this module */
  def createBody = {}
  
  def createEntity:NewEntity = {
    this.createBody
    val newStatements    = this.statements.toList.map(_.create)
    val vectorStatements = newStatements.flatMap(_.createVector)
    val splitStatements  = vectorStatements.flatMap(_.createSplit)
    NewEntity(this.name,Some(this),this.signals.toList,splitStatements,
      this.internalInstances.toList,this.createFunctions,this.extraFilesInternal.toList,
      location,headSegments.toList)
  }
  
}
