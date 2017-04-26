package com.simplifide.generate.blocks.proc2

/**
 *   Register Map containing all of the registers in this design
 */
class RegisterMapNew(val groups:List[RegisterGroup]) {

  /** Complete list of addresses which are contained in these register groups */
  val addresses = groups.flatMap(x => x.addresses).sortBy(x => x.address)
  /** List of signals contained in these addresses */
  val signals = addresses.flatMap(_.signals)
  /** List of parameters */
  val parameters   = groups.flatMap(_.addresses).map(_.parameter)


  /** Addresses which are converted based on this group address */
  val realAddresses = groups.flatMap(_.realAddress).sortBy(_.address)

}

object RegisterMapNew {

}