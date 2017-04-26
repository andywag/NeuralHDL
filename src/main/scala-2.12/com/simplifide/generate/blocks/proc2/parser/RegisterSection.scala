
package com.simplifide.generate.blocks.proc2.parser

import com.simplifide.generate.html.Description
import com.simplifide.generate.blocks.proc2.{RegisterNew, FullRegister}

/** Register Section */
trait RegisterSection extends RegisterParser.Builder with RegisterModel.GroupGenerator {

  /** List of Registers contained in this section */
  val registers:List[FullRegister]
  /** Active registers for this builder */
  val next:FullRegister

  /** Creates a new register section based on the inputs (Virtual Contructor) */
  def copy(registers:List[FullRegister] = this.registers, next:FullRegister = this.next):RegisterSection

  /** List of all of the addresses */
  val allRegisters = registers ::: List(next)

  /** Create an address which only defines the actual address */
  def at(address:Int):RegisterSection = this.at(address,0)
  /** Attach this register at this starting point */
  def start(start:Int):RegisterSection = this.at(0,start)
  /** Attach a fileLocation to the current register */
  def at(address:Int,start:Int):RegisterSection = {
    val location = new FullRegister.Location(address,start,start+next.register.signal.fixed.width)
    this.copy(next = next.at(location))
  }
  def default(defaultValue:String) = this.copy(next = next.copy(defaultValue = defaultValue))

  /** Attach a comment to the current register */
  def comment (description:Description) = {
    this.copy(next = next.copy(description = description))
  }

  override def sectionOpen(register:RegisterNew)  = this.copy(registers ::: List(next), FullRegister(register))



}

object RegisterSection {

  def apply(next:FullRegister) = new Impl(List(),next)
  def apply(registers:List[FullRegister], next:FullRegister) = new Impl(registers,next)
  /** Class which defines a builder working over a list of registers */
  class Impl(val registers:List[FullRegister], val next:FullRegister) extends RegisterSection {
    /** Default Copy Operation */
    def copy(registers:List[FullRegister] = this.registers, next:FullRegister = this.next) =
      new RegisterSection.Impl(registers,next)

    def createGroup(base:Int,comment:Description=Description.Empty) =
      null//RegisterGroup(base,registers ::: List(next))

    }

}


