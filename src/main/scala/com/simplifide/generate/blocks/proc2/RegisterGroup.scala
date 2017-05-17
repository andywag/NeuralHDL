package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.blocks.proc2.RegisterGroup.Impl
import com.simplifide.generate.html.Description

/**
 * Group of Registers
 */

trait RegisterGroup {
  /** Base Address for Group of Registers */
  val base:Int
  /** List of a group of addresses */
  val addressGroups:List[AddressGroup]
  /** Description of Register Group */
  val description:Description

  /** List of Addresses contained in this map*/
  final val addresses:List[AddressNew] = addressGroups.flatMap(_.addresses)
  /** List of signals contained in this group */
  val signals = addresses.flatMap(_.signals)
  /** Addresses which are offset by this register group */
  val realAddress = addresses.map(x => x.copy(address = base + x.address))


}

object RegisterGroup {

  class Impl(override val base:Int,
    override val addressGroups:List[AddressGroup],override val description:Description) extends RegisterGroup
  
}                                         