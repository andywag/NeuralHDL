package com.simplifide.generate.blocks.proc2.parser

import com.simplifide.generate.html.Description
import com.simplifide.generate.blocks.proc2.{AddressNew, RegisterGroup, RegisterNew, FullRegister}



class RegisterModel {

}

object RegisterModel {

  trait GroupGenerator {
    def createGroup(base:Int,comment:Description=Description.Empty):RegisterGroup
  }
}
 /*
  /** Class which defines a builder working over a list of registers */
  class Section(val registers:List[FullRegister], val next:FullRegister) extends RegisterParser.Builder with RegisterModel.GroupGenerator{

    /** List of all of the addresses */
    val allRegisters = registers ::: List(next)
    /** Default Copy Operation */
    def copy(registers:List[FullRegister] = this.registers, next:FullRegister = this.next) =
      new Section(registers,next)

    /** Create an address which only defines the actual address */
    def at(address:Int):Section = this.at(address,0)
    /** Attach this register at this starting point */
    def start(start:Int):Section = this.at(0,start)
    /** Attach a fileLocation to the current register */
    def at(address:Int,start:Int):Section = {
      val fileLocation = new FullRegister.Location(address,start,start+next.register.signal.fixed.width)
      this.copy(next = next.at(fileLocation))
    }


    /** Attach a comment to the current register */
    def comment (description:Description) = {
      this.copy(next = new FullRegister.Impl(next.register,next.fileLocation,description))
    }



    override def sectionOpen(register:RegisterNew)  = this.copy(registers ::: List(next), FullRegister(register))

    def createGroup(base:Int,comment:Description=Description.Empty) =
      RegisterGroup(base,registers ::: List(next))

  }
 
  
  object Section {
    def apply(next:FullRegister) = new Section(List(),next)
    def apply(registers:List[FullRegister],next:FullRegister) = new Section(registers,next)
  }

  /** Class which defines a builder working over a list of addresses */
  class AddressSection(val base:Int, addresses:List[AddressNew],next:AddressNew) extends GroupGenerator {
    
    def copy(base:Int= this.base, addresses:List[AddressNew]=this.addresses, next:AddressNew=this.next) = new AddressSection(base,addresses,next)

    /** Attach an address to the working address */
    def at(fileLocation:Int) = this.copy(next = next.copy(address = fileLocation))
    /** Attach a list of registers to the working address */
    def registers(regs:RegisterModel.Section) = this.copy(next = next.copy(registers = regs.allRegisters))

    def createGroup(base:Int,comment:Description=Description.Empty) = new RegisterGroup.Impl(base,addresses ::: List(next),comment)

    /** Attach an address for this section */
    def address(section:RegisterModel.Section) =
      this.copy(addresses = this.addresses ::: List(next), next = AddressNew(0,section.allRegisters))

    /** Creates a new address */
    def address(fileLocation:Int) =
      this.copy(addresses = this.addresses ::: List(next),next = AddressNew(fileLocation,List()))


    /** Attach a comment to this address fileLocation */
    def comment(description:Description) = this.copy(next = next.copy(description = description))

  }


  


  
}
*/