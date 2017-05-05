package com.simplifide.generate.html


//import xml.{ Elem}
import com.simplifide.generate.language.DescriptionHolder
//import org.shapeless._
/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 8/22/11
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */

trait Description {
  val string:String
  //val html:xml.Elem
  val woXML:String

  
  def shortString:String = if (string.length > 64) string.substring(0,63)    + "..." else string
  //def shortHtml:xml.Elem = if (string.length > 64) <p>string.substring(0,63) + "..."</p> else html
  
  def --:[T <: DescriptionHolder] (holder:T):T = {
    holder.description = Some(this)
    holder
  }

}

object Description {
  def apply(str:String) = new Str(str)
  //def apply(elem:Elem)   = new Html(elem)

  /*
  class Html(val elem:Elem) extends Description {
    val html   = elem
    val string = html.toString
    val woXML  = elem.text
  }
  */

  class Str(val str:String) extends Description {
    //val html = <p>{str}</p>
    val string = str
    val woXML = str
  }
  
  object Empty extends Str("Not Defined")
    

}