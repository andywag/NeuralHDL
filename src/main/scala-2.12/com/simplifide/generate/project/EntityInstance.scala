package com.simplifide.generate.project

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.signal.SignalTrait


/**
 * Segment which is used to create a module instantiation based on the child Entity
 */
/*
trait EntityInstance[T <: Entity] extends SimpleSegment{

  val entity:T
  val connection:PortConnection = PortConnection.Default

  import entity._

  override def toString = name + "(" + entity + ")"



  // TODO Need to add signal name conversion
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      def createSignals:SegmentReturn = {
        def createSignal(signal:SignalTrait, index:Int):SegmentReturn =
          (if (index != 0) ",\n    " else "\n    ") +  "." + signal.name + "(" + connection.connect(signal).name +")"
        val allSignals = entity.entitySignals.flatMap(_.allSignalChildren).filter(x => (x.isInput || x.isOutput))
        allSignals.zipWithIndex.map(x => createSignal(x._1,x._2)).foldLeft(SegmentReturn(""))(_ + _)
      }
      val out = SegmentReturn(entity.name) + " " + this.name + " (" + createSignals + ");\n\n"
      out
  }

  /** Returns a list of all signals as seen at the enclosing module */
  def allSignals = entity.entitySignals.flatMap(_.allSignalChildren).map(connection.connect(_))

  /** Pass which connects the inputs to the entity instances */
  def inputPass:NewEntityInstance[Entity]  =
    NewEntityInstance(this.entity.inputPass,this.name,this.connection)

  /** Pass which connects the outputs to the entity instances */
  def outputPass(outputs:Option[List[SignalTrait]]):NewEntityInstance[Entity] =
    NewEntityInstance(this.entity.outputPass(outputs),this.name,this.connection)

}

/** Factory method for creating an instance */
object EntityInstance {
  def apply[T <: Entity](entity:T) = new Impl(entity,entity.name,PortConnection.Default)
  def apply[T <: Entity](entity:T, name:String) = new Impl(entity,name,PortConnection.Default)
  def apply[T <: Entity](entity:T, connection:PortConnection) = new Impl(entity,entity.name,connection)
  def apply[T <: Entity](entity:T, name:String,connection:PortConnection) = new Impl(entity,name,connection)

  class Impl[T <: Entity](override val entity:T,
             override val name:String,
             override val connection:PortConnection) extends NewEntityInstance[T]




}
*/