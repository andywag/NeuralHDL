package com.simplifide.generate.test2

import com.simplifide.generate.parser.EntityParser
import java.io.File
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.test2.WaveformCreator.{Item, ModuleGroup, DividerGroup, SignalGroup}
import com.simplifide.generate.project.NewEntity

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */

trait WaveformCreator {

  val path:String
  val entity:NewEntity

  private def createWave(signal:SignalTrait)  = new WaveformCreator.Wave(signal)
  private def createGroup(signal:SignalTrait) = new SignalGroup(signal.name,signal.children.map(createSignal(_)))
  private def createSignal(signal:SignalTrait):Item = if (signal.numberOfChildren > 0) createGroup(signal) else createWave(signal)
  
  def handleGrouped(prefix:String, signals:List[(SignalTrait,Array[String])]):List[Item] = {
    def pruneString(signal:(SignalTrait,Array[String])) = {
      if (signal._2.length < 2) signal else (signal._1,signal._2.slice(1,signal._2.length))
    }
    if (signals.size == 1) List(new WaveformCreator.Wave(signals(0)._1))
    else {   
      val pruned = signals.map(x => pruneString(x))
      val grouped = pruned.groupBy(x => x._2(0))
      List(new WaveformCreator.SignalGroup(prefix,grouped.flatMap(x => handleGrouped(x._1,x._2)).toList))
    }
  }

  def nameGrouping(signals:List[SignalTrait]):List[Item] = {
    val split = signals.map(x => (x, x.name.split("_")))
    val group = split.groupBy(x => x._2(0))
    group.toList.sortBy(_._1).flatMap(x => handleGrouped(x._1,x._2)).toList
  }
  
  val body = {
    val signals = SignalTrait.uniqueSignals(entity.allSignals.flatMap(_.allSignalChildren)).sortBy(_.name)//if (entity.base == None) entity.signals else entity.base.get.signals.toList
   // System.out.println(nameGrouping(signals).map(_.debug("")).foldLeft("")(_+_))
    new ModuleGroup(entity.name,nameGrouping(signals))
  }

  
}

object WaveformCreator {
  
  class Item(val name:String) {
    def debug(indent:String) = indent + name + "\n"
  }
  
  class Group(name:String, val children:List[Item]) extends Item(name)  {
    override def debug(indent:String) = indent + name + "\n" + children.map(x => x.debug(indent + "   ")).foldLeft("")(_+_)
  }
  class ModuleGroup(name:String, children:List[Item]) extends Group(name,children)
  class SignalGroup(name:String, children:List[Item]) extends Group(name,children)
  class DividerGroup(name:String, children:List[Item]) extends Group(name,children)
  class Wave(val signal:SignalTrait) extends Item(signal.name)
  
  
}
