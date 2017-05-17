package com.simplifide.generate.html



/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 8/22/11
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
/*
class HtmlTable2(val head:HtmlTable2.Row,
                 val body:List[HtmlTable2.Row],
                 val caption:Option[String] = None) {

  private def createHead(data:xml.Node) =
    <th><strong>{data}</strong></th>  % Attribute(None, "BGCOLOR", Text("#CCCCFF"), Null)

  private def createElement(data:xml.Node) = <td>{data}</td>


  private def createBody(row:HtmlTable2.Row) = {
    <tr>{row.data.map(createElement(_))}</tr>
  }


  def createTable = {
    val tab =
      <table>
        {if (caption != None) <caption>{caption.get}</caption>}
          <tr>
            {head.data.map(createHead(_))}
          </tr>
        {body.map(x => createBody(x))}
       </table>

    tab % Attribute(None, "border", Text("1"), Null)
  }

  def htmlTable = {
     <html>
      <head>
      </head>
      <body>
        {createTable}
      </body>
    </html>
  }

}

object HtmlTable2 {
  object Row {
    def apply(data:List[xml.Elem]) = new Row(data)
    def apply(data:Description*)  = new Row(data.map(x => {x.shortHtml}).toList)
  }
  class Row(val data:List[xml.Elem])
}
*/