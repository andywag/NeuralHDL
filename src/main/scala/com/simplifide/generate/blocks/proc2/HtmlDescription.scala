package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.language.ExtraFile
import com.simplifide.generate.language.Conversions._
import com.simplifide.generate.signal.ParameterTrait

class HtmlDescription(override val filename:String,
                      val registerMap:RegisterMapNew) extends ExtraFile {

/*
  private def detailedAddress(address:AddressNew) = {
    def addressMap   = <h2>{HtmlUtilities.hypername(address.name,address.name)}</h2>
    def addressTable = {
      // Create the Read Type for the Table
      def table(x:FullRegister):List[Description] = {
        val read = x.register match {
          case y:RegisterNew.Read      => "R"
          case y:RegisterNew.Write     => "W"
          case y:RegisterNew.ReadWrite => "RW"
        }
        // Location Type of the Table
        val location = if (x.width == 1) x.writeStop.toString else "[" + x.writeStop +":" + x.writeStart + "]"
        List(x.register.signal.name,read,location,x.defaultValue,x.description)
      }
      val head:List[Description]                 = List("Register","Type","Location","Default","Description")
      val body:List[List[Description]]            = address.registers.map(table(_))
      new HtmlTable(head,body,Some("Register Description")).createTable
    }
    def addressDescription = {
      def detailedRegister(x:FullRegister) = <p><h3>{x.register.signal.name}</h3><p>{x.description.html}</p></p>
      address.registers.map(x => detailedRegister(x))
    }
    <p><p>{addressMap}</p><p>{addressTable}</p><p>{addressDescription}</p></p>
  }
  
  /** Creates the set of detailed addresses */
  private val detailedAddresses = {
    val addresses = {
      <p>{registerMap.realAddresses.map(x => detailedAddress(x))}</p>
    }
    <p><h1>Detailed Address Declarations</h1>{addresses}</p>
  }

  /** Creates a table of address descriptions */
  private val addressDescription = {
    val head = HtmlTable2.Row("Group","Address","Value","Description")
    val body = {
      def parameter (parameter:ParameterTrait) = {
        val parameterDescription = parameter.html
        HtmlTable2.Row(
          List(HtmlUtilities.hyperlink(parameter.name,"#" + parameter.name),
          <p>{parameter.displayString}</p>,
          parameterDescription))
      }
      registerMap.parameters.sortBy(_.value).map(parameter(_))
    }
    new HtmlTable2(head,body,Some("Address Description")).createTable
  }
  /** Header of this Document */
  val header = <p>This document describes the registers defined by this processor interface</p>
*/
  val contents = null//HtmlUtilities.fullHtml(filename + " Register Map Description",List(header,addressDescription,detailedAddresses)).toString()
  //val contents = HtmlUtilities.fullHtml(filename + " Register Map Description",List(addressDescription)).toString()

}