package com.simplifide.generate.proc

import com.simplifide.generate.html.{Description, HtmlTable}
import com.simplifide.generate.util.FileOps
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/2/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */

class ControlHTML(val programMap:ProgramMap) {

  def createTable(location:String) = {
    val table = new HtmlTable(programMap.programMap.keys.toList.map(x => Description(x.toString)),
      programMap.programMap.values.toList.map(x => x.map(y => Description(y.value.toString))).transpose)
    FileOps.createFile(new File(location),table.htmlTable.toString())

  }
}

object ControlHTML {

}