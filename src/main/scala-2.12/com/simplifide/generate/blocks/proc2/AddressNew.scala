package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.html.Description
import com.simplifide.generate.blocks.proc2.AddressNew.Impl
import com.simplifide.generate.signal.{ ParameterTrait}


/**
 * Trait which defines an address containing the fileLocation of the address as well as the individual items which
 * are contained in this address
 */
trait AddressNew extends AddressGroup {
  /** Name of the Address */
  val name:String
  /** Location of address in the map */
  val address:Int
  /** List of Registers included in this address */
  val registers:List[FullRegister]
  /** Append a register to the current address */
  val description:Description
  /** Processor Bus used for creation of the final registers*/
  implicit val processorBus:ProcessorBus
  /** Create a copy of this address */
  def copy(name:String = this.name,
    address:Int = this.address,
    registers:List[FullRegister]=this.registers,
    description:Description=this.description):AddressNew
  /** Parameter defined by this address */
  val parameter = ParameterTrait.Hex(name,address.toHexString, processorBus.writeAddress.fixed,this.description)
  /** Signals defined for this address */
  val signals = registers.flatMap(_.signals)
  /** Attach a comment to this address */
  def comment(description:Description) = new AddressNew.Impl(name,address,registers,description)

  override def addresses = {
    val register = registers(0)
    if (processorBus.dataWidth >= register.width) List(this)
    else {
      val numberAddresses = math.ceil(register.width.toDouble / processorBus.dataWidth.toDouble).toInt
      List.tabulate(numberAddresses)(x => this.copy(name + "_" + x, address + x, List(register.split(x,processorBus.dataWidth)), description))
    }
  }

}

/** Factory methods for creating an address */
object AddressNew {


  /** Creation of an address as well as a list of items located at this address */
  def apply(name:String,
    address:Int,
    registers:List[FullRegister],
    description:Description = Description.Empty)(implicit processorBus:ProcessorBus) =
      new Impl(name,address,registers,description)

  /** Default Implementation of AddressNew */
  class Impl(override val name:String,
             override val address:Int,
             override val registers:List[FullRegister],
             override val description:Description = Description.Empty)(implicit val processorBus:ProcessorBus) extends AddressNew  {
    
    def copy(name:String = this.name,address:Int = this.address, registers:List[FullRegister]=this.registers, description:Description=this.description) =
        new Impl(name,address,registers,description)

    
  }
  



}

