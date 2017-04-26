package com.simplifide.generate.parser.items

import com.simplifide.generate.html.Description
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.items.InstanceParser.{PortConnection, InstanceBuilder}
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.{SegmentHolder, EntityParser}
import com.simplifide.generate.project._


/**
  *  Parser used to create an instance
  */

trait InstanceParser {

  self: EntityParser =>

  def $instance(entity:InstanceParser.InstanceBuilder, parent:PathProvider = PathProvider.Empty) = {
    val par = entity.createInstance(parent)
    this.instance(par)  // Append the instance to this block
  }
  
  implicit def SignalTrait2Port(signal:SignalTrait) = 
    new InstanceParser.Port(signal)
  implicit def Entity2InstanceBuilder(entity:NewEntity) =
    new InstanceBuilder(entity.name,entity,Connection.Default,Description.Empty)
  implicit def EntityParser2InstanceBuilder(entity:EntityParser) =
    new InstanceBuilder(entity.name,entity.createEntity,Connection.Default,Description.Empty)

}

object InstanceParser {


  class Port(val signal:SignalTrait)  {
    def --> (segment:SimpleSegment = ConnectionNew.EMPTY) = new PortConnection(signal,segment)

  }
  
  class PortConnection(val internal:SignalTrait, val external:SimpleSegment) {

    def createConnection =
      (internal.allSignalChildren.map(_.name) zip external.allChildren).map(x => new ConnectionNew.NameToSegment(x._1,x._2))

  }
  
  def apply(entity:NewEntity) = new InstanceBuilder(entity.name,entity,Connection.Default,Description.Empty)
  

  
  class InstanceBuilder(val name:String, val entity:NewEntity, val connection:Connection, val description:Description) {
    def copy(name:String = this.name, 
      entity:NewEntity = this.entity, 
      connection:Connection = this.connection,
      description:Description = this.description) = new InstanceBuilder(name,entity,connection,description) 
    
    def $named(name:String) = copy(name = name)
    def $ports(connections:PortConnection*) = {
      val newConnections = connections.toList.flatMap(_.createConnection)
      val connection = new ConnectionNew(newConnections)
      copy(connection = connection)
    }
    def $comment(value:String) = copy(description = new Description.Str(value))

    def createInstance(parent:PathProvider) = {
      NewEntityInstance(entity,name,connection,parent)
    }
  }
}
