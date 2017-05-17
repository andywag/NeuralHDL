
package com.simplifide.generate.blocks.proc2.parser

import com.simplifide.generate.blocks.proc2._
import com.simplifide.generate.html.Description
import com.simplifide.generate.blocks.proc2.parser.RegisterModel.GroupGenerator


/**
 * Builder used to create the address section for a processor interface
 **/

trait AddressSection extends GroupGenerator {
  /** Base Address for this section */
  val base:Int
  /** List of addresses inside this section */
  val addresses:List[AddressGroup]
  /** Current Address being Built */
  val next:AddressGroup
  /** Processor Bus */
  implicit val processorBus:ProcessorBus



  /** Copy operation (Virtual Constructor) */
  def copy(base:Int= this.base, addresses:List[AddressGroup]=this.addresses, next:AddressGroup=this.next):AddressSection

  /** Attach an address to the working address */
  def at(location:Int) = this.copy(next = next.copy(address = location))
  /** Attach a list of registers to the working address */
  def registers(regs:RegisterSection) = this.copy(next = next.copy(registers = regs.allRegisters))
  /** Attach an address for this section */
  def address(section:RegisterSection) =
    this.copy(addresses = this.addresses ::: List(next), next = AddressNew("",0,section.allRegisters))
  /** Creates a new address */
  def address(location:Int) =
    this.copy(addresses = this.addresses ::: List(next),next = AddressNew("",location,List()))
  /** Creates a new address */
  def address(name:String,location:Int) =
    this.copy(addresses = this.addresses ::: List(next),next = AddressNew(name,location,List()))

  /*
  /** Creates a new address */
  def multiple(fileLocation:Int) =
    this.copy(addresses = this.addresses ::: List(next),next = AddressGroup.Multiple("",fileLocation,List()))
  /** Creates a new address */
  def multiple(name:String,fileLocation:Int) =
    this.copy(addresses = this.addresses ::: List(next),next = AddressGroup.Multiple(name,fileLocation,List()))
  */
  /** Attach a comment to this address fileLocation */
  def comment(description:Description) = this.copy(next = next.copy(description = description))


  def createGroup(base:Int,comment:Description=Description.Empty) = new RegisterGroup.Impl(base,addresses ::: List(next),comment)



}

object AddressSection {

  def apply(base:Int, addresses:List[AddressGroup],next:AddressGroup)(implicit processorBus:ProcessorBus) =
    new AddressSection.Impl(base,addresses,next)

  /** Class which defines a builder working over a list of addresses */
    class Impl(override val base:Int,
      override val addresses:List[AddressGroup],
      override val next:AddressGroup)(implicit val processorBus:ProcessorBus) extends AddressSection {

      def copy(base:Int= this.base, addresses:List[AddressGroup]=this.addresses, next:AddressGroup=this.next) =
       new AddressSection.Impl(base,addresses,next)

   }
}

