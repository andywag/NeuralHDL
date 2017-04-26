package com.simplifide.generate.project

import com.simplifide.generate.language.ExtraFile
import com.simplifide.generate.html.{HtmlTable, Description, HtmlUtilities}
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.signal.{RegisterTrait, ArrayTrait, SignalTrait}


/*
 * Class used to generate an html description of the module
 *
 * @constructor
 * @parameter filename File name of the html file
 * @parameter module Module which defines this description
 */
/*
class ModuleHtmlGenerator(override val filename:String, val module:Entity) extends ExtraFile {

  val head        = <h1>{module.name} Description</h1>
  val description = module.description match {
    case None    => <p></p>
    case Some(x) => x.html
  }

  val signalTable = {
    def typ(signal:SignalTrait) = {
      signal match {
        case x:RegisterTrait[_] => "Register(" + x.length + ")"
        case x:ArrayTrait[_]    => "Array(" +  x.length + ")"
        case x:SignalTrait   => x.opType.toString
        case _ => ""
      }
    }
    def row(signal:SignalTrait):List[Description] = List(signal.name,
      signal.fixed.toString,
      typ(signal),
      signal.description.getOrElse(""))

    val internalHead:List[Description] = List("Name","Width","Type","Description")
    val body:List[List[Description]] = module.entitySignals.map(x => row(x))
    val htmlModel = new HtmlTable(internalHead,body,Some("StateMachine Description Table"))
    htmlModel.createTable
  }


  val contents = HtmlUtilities.fullHtml(filename + " Impl Description",List(head,description,signalTable)).toString()
}

object ModuleHtmlGenerator {
  
}
*/