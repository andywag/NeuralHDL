package com.simplifide.generate.html

import xml.Attribute._
import xml.Text._
import xml.{Attribute, Text, Null, Node}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 8/22/11
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */

object HtmlUtilities {

  def fullHtml(title:String,internal:List[Node]):Node = {
     <html>
      <head>
        <title>{title}</title>
      </head>
      <body>
        {internal}
      </body>
    </html>
  }
  
  def hyperlink(text:String, location:String) =
    <a>{text}</a> % Attribute(None, "href", Text(location), Null)

  def hypername(text:String, location:String) =
    <a>{text}</a> % Attribute(None, "name", Text(location), Null)

}