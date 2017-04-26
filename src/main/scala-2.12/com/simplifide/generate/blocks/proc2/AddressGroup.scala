package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.html.Description


/** Group of Addresses used for register generation */

trait AddressGroup {
  
  /** List of addresses which are contained in this group */
  def addresses:List[AddressNew]
  /** Copy operation (Delegates to AddressNew) */
  def copy(name:String = addresses(0).name,
    address:Int = addresses(0).address,
    registers:List[FullRegister] = addresses(0).registers,
    description:Description = addresses(0).description):AddressGroup

}

object AddressGroup {
  
  /** Factory Method for creating an address group */
  /*def apply(name:String,
    address:Int, 
    registers:List[FullRegister],
    description:Description = Description.Empty)(implicit processorBus:ProcessorBus) = {
    
    val register = registers(0)
    
    if (register.register.signal.fixed.width < processorBus.dataWidth) AddressNew(name,address,registers,description)   // Width less than single address
    else new Multiple(name,address,registers(0),description)                            // Width greater than single address
  }*/
    //new Impl(name,address,registers,description)
  
  /** Address for a register which contains multiple addresses */
  /*
  class Multiple(val name:String,
    val baseAddress:Int,
    val register:FullRegister,
    val description:Description = Description.Empty) extends AddressGroup {

    def copy(name:String = addresses(0).name,
        address:Int = addresses(0).address,
        registers:List[FullRegister] = addresses(0).registers,
        description:Description = addresses(0).description):AddressGroup = {
      new Multiple(name,address,registers(0),description)
    } 
    
    val addresses:List[AddressNew] = {
      List(AddressNew(name,baseAddress,List(register),description))
    }  
  
  }
  
  object Multiple {
    def apply(name:String,
      address:Int, 
      registers:List[FullRegister],
      description:Description = Description.Empty)(implicit processorBus:ProcessorBus) = {
      new Multiple(name,address,registers(0),description)
    }
  }
  */
  
}