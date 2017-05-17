package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.generator.ComplexSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl

/*
 *  Implementation of the address decoder
 */
trait AddressDecoderNew {
  val registerMap:RegisterMapNew
  val bus:ProcessorBus

}

object AddressDecoderNew {

  /**
   * Read mux implementation
   */
  class Read(override val registerMap:RegisterMapNew, override val bus:ProcessorBus)  extends ComplexSegment with AddressDecoderNew {

    private def createAddress(address:AddressNew) = {
      def individual(register:FullRegister) =
        bus.readData((register.writeStop,register.writeStart)) ::= register.signal((register.readStop,register.readStart))
      val results = address.registers.filter(_.register.isRead)
      $cases(address.parameter) $then (results.map(individual(_)))
    }

    def createBody {
      $always_body(
        bus.readAddress $match (
          registerMap.realAddresses.map(createAddress(_))
        )
      )
    }
  }

  /*
   * Write mux implementation
   */
  class Write(override val registerMap:RegisterMapNew, override val bus:ProcessorBus)(implicit clk:ClockControl) extends ComplexSegment with AddressDecoderNew {

    private def createAddress(address:AddressNew) = {
      def individual(register:FullRegister) =
        register.signal((register.readStop,register.readStart)) ::= bus.writeData((register.writeStop,register.writeStart))
      $cases(address.parameter) $then (address.registers.filter(_.register.isWrite).map(individual(_)))
    }
    
    def createBody {
      $always_clk(clk) (
        bus.writeAddress $match (
          registerMap.realAddresses.map(x => createAddress(x))
        )
      )

    }
  }

  
}