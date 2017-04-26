package com.simplifide.generate.blocks.proc2

import com.simplifide.generate.html.Description
import com.simplifide.generate.signal.SignalTrait


/**
 * Register and the addresses
 */

trait FullRegister {
  /** Base Register */
  val register:RegisterNew
  /** Location of Register */
  val location:FullRegister.Location
  /** Description of Register */
  val description:Description
  /** Default Value */
  val defaultValue:String = "0"

  def copy(register:RegisterNew     = this.register,
    location:FullRegister.Location  = this.location,
    description:Description         = this.description,
    defaultValue:String             = this.defaultValue) = FullRegister(register,location,description,defaultValue)
  
  /** List of Signals included in this full register */
  val signals = List(register.signal)

  /** Width of this register */
  val width = register.signal.fixed.width

  /** Convenience method to access signal */
  val signal = register.signal
  /** Top Location of the Address */
  val writeStop  = location.stop-1
  /** Bottom Location of the Address */
  val writeStart = location.start
  /** Top Location of the Address */
  val readStop   = register.signal.fixed.width - 1
  /** Bottom Location of the Address */
  val readStart  = 0

  /** Attach a Location to this register */
  def at(address:Int,start:Int):FullRegister = 
    this.copy(location = new FullRegister.Location(address,start,start+this.register.signal.fixed.width)) 
  /** Attach a fileLocation to this register */
  def at(location:FullRegister.Location):FullRegister = 
    this.copy(location = location)

  /** Attach a comment to this register */
  def -- (description:Description)      = this.copy(description = description)
  def comment (description:Description) = this.copy(description = description)
  
  /** Attach a default value to this register */
  def default(defaultValue:String) = this.copy(defaultValue = defaultValue)

  /** Creates multiple registers for the case of a register which encompasses multiple addresses */
  def split(index:Int, busWidth:Int) = new FullRegister.Partial(register,location,index,busWidth,description)
                                                   
}

object FullRegister {

  def apply(register:RegisterNew):FullRegister =
    new FullRegister.Impl(register,FullRegister.NoLocation)
  
  def apply(register:RegisterNew,location:FullRegister.Location):FullRegister =
    new FullRegister.Impl(register,location)

  def apply(register:RegisterNew,
    location:FullRegister.Location,
    description:Description,
    defaultValue:String) = new FullRegister.Impl(register,location,defaultValue,description)
  
  class Impl(override val register:RegisterNew,
    override val location:FullRegister.Location,
    override val defaultValue:String = "0",
    override val description:Description = Description.Empty) extends FullRegister {
    
  }
  
  class Partial(override val register:RegisterNew,
    override val location:FullRegister.Location,
    val index:Int,
    val busWidth:Int,
    override val description:Description = Description.Empty) extends FullRegister {

    /** Width of this register */
    override val width = -1

    val total = math.ceil(register.signal.width.toDouble/busWidth.toDouble).toInt

    /** Top Location of the Address */
    override val writeStop  = if (index < total-1) busWidth - 1 else register.signal.width % busWidth
    /** Bottom Location of the Address */
    override val writeStart = 0
    /** Top Location of the Address */
    override val readStop   = index*busWidth + writeStop
    /** Bottom Location of the Address */
    override val readStart  = index*busWidth
    

  }
  
  
  
  class Location (val address:Int, val start:Int, val stop:Int)
  object NoLocation extends Location(0,0,0)

}