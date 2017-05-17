package com.simplifide.generate.blocks.proc2.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.proc2._
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.{OpType, SignalTrait, FixedType}
import com.simplifide.generate.html.Description


/** Parser used to create a processor interface */
trait RegisterParser extends RegisterParser.Builder{

  /** Clock control required for this operation */
  implicit val clk:ClockControl
  /** Groups of addresses */
  val groups       = new ListBuffer[RegisterGroup]()
  /** Bus which is required for this processor interface */
  implicit val processorBus:ProcessorBus

  /** Creates a set of addresses defined in a group */
  def registerGroup(baseAddress:Int)(section:RegisterModel.GroupGenerator) =
    groups.append(section.createGroup(baseAddress))
  /** Creates a set of addresses defined in a group */
  def registerGroupComment(baseAddress:Int)(section:RegisterModel.GroupGenerator)(description:Description = Description.Empty) =
    groups.append(section.createGroup(baseAddress))
  
  /** Creates an address definition */
  //def address(baseAddress:Int, section:RegisterModel.Section) =
  //  new RegisterModel.AddressSection(baseAddress,List())


  lazy val registerMap  = new RegisterMapNew(groups.toList)
  lazy val readDecoder  = new AddressDecoderNew.Read(registerMap,processorBus)
  lazy val writeDecoder = new AddressDecoderNew.Write(registerMap,processorBus)

}

object RegisterParser {
  trait Builder {
    /** Create a section */
    def sectionOpen(register:RegisterNew):RegisterSection  = RegisterSection(FullRegister(register))
    /** Creates a read register for the processor interface */
    def read(name:String, width:Int)       = sectionOpen(new RegisterNew.Read(SignalTrait(name,OpType.Input,FixedType.unsigned(width,0))))
    def read(signal:SignalTrait)           = sectionOpen(new RegisterNew.Read(signal))
    /** Creates a write register for the processor interface */
    def write(name:String, width:Int)      = sectionOpen(new RegisterNew.Write(SignalTrait(name,OpType.RegOutput,FixedType.unsigned(width,0))))
    def write(signal:SignalTrait)          = sectionOpen(new RegisterNew.Write(signal))
    /** Creates a read-write register for the processor interface */
    def readWrite(name:String, width:Int)  = sectionOpen(new RegisterNew.ReadWrite(SignalTrait(name,OpType.RegOutput,FixedType.unsigned(width,0))))
    def readWrite(signal:SignalTrait)      = sectionOpen(new RegisterNew.ReadWrite(signal))

    private def addressSection(address:Int,group:AddressGroup)(implicit processorBus:ProcessorBus) = AddressSection(address,List(),group)
    /** Define an address for this map */
    def address(section:RegisterSection)(implicit processorBus:ProcessorBus) =
      addressSection(0,AddressNew("",0,section.allRegisters))
    /** Define an address with only the fileLocation */
    def address(location:Int)(implicit processorBus:ProcessorBus) =
      addressSection(location,AddressNew("",location,List()))
    /** Creation of Address */
    def address(name:String, location:Int)(implicit processorBus:ProcessorBus) =
      addressSection(location,AddressNew(name,location,List()))
    /*
    /** Creates a new address */
    def multiple(fileLocation:Int)(implicit processorBus:ProcessorBus) =
      addressSection(fileLocation,AddressGroup.Multiple("",fileLocation,List()))
    /** Creates a new address */
    def multiple(name:String,fileLocation:Int)(implicit processorBus:ProcessorBus) =
      addressSection(fileLocation,AddressGroup.Multiple(name,fileLocation,List()))
    */


    /** Define a multi address */






  }
}