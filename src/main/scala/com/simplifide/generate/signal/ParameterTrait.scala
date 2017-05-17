package com.simplifide.generate.signal

import com.simplifide.generate.html.Description


/**
 * Verilog parameter
 */
trait ParameterTrait extends SignalTrait {
  
  def copyParameter(
    name:String        = this.name,
    stringValue:String = this.stringValue, 
    fixed:FixedType    = this.fixed,
    internalDescription:Description = this.internalDescription):ParameterTrait
  /** Value of the parameter */
  val stringValue:String
  /** Fixed type of the parameter */
  override val fixed:FixedType = FixedType.Simple
  /** Operating type of the parameter */
  override val opType = OpType.Param
  /** Description for the parameter */
  val internalDescription:Description

  /** Assignment creation value */
  val parameterAssignment:String
  /** Display Value */
  val displayString:String
  /** Value of this parameter */
  val value:Long
  

  def newSignal(name:String = this.name,
    opType:OpType = this.opType,
    fix:FixedType = this.fixed):SignalTrait = this.copyParameter(name = name, fixed = fixed)

}

/**
 * Factory method for creating a verilog parameter
 */
object ParameterTrait {
  
  def Decimal(name:String, stringValue:String, fixed:FixedType=FixedType.Simple, description:Description=Description.Empty) = 
    new Decimal(name,stringValue,fixed,description)

  def Hex(name:String, stringValue:String, fixed:FixedType=FixedType.Simple, description:Description=Description.Empty) =
    new Hex(name,stringValue,fixed,description)
  
  class Decimal(override val name:String, 
                override val stringValue:String, 
                override val fixed:FixedType = FixedType.Simple,
                override val internalDescription:Description = Description.Empty) extends ParameterTrait {

    def copyParameter(name:String = this.name,
      stringValue:String = this.stringValue,
      fixed:FixedType = this.fixed,
      internalDescription:Description = this.internalDescription):ParameterTrait =
      new Decimal(name,stringValue,fixed,internalDescription)
    
    override val parameterAssignment = fixed  match {
      case FixedType.Simple => "'d" + stringValue
      case _                  => fixed.width + "'d" + stringValue
    }

    override val displayString = "0x" + stringValue
    override val value:Long = java.lang.Long.parseLong(stringValue,10)

  }
  
  class Hex(override val name:String, 
            override val stringValue:String,
            override val fixed:FixedType = FixedType.Simple,
            override val internalDescription:Description = Description.Empty) extends ParameterTrait {

    def copyParameter(name:String = this.name,
      stringValue:String = this.stringValue,
      fixed:FixedType = this.fixed,
      internalDescription:Description = this.internalDescription):ParameterTrait =
      new Hex(name,stringValue,fixed,internalDescription)

    override val parameterAssignment = fixed match {
      case FixedType.Simple => "'h" + stringValue
      case _                  => fixed.width + "'h" + stringValue
    }

    override val displayString = stringValue
    // TODO Not Correct only uses integer
    override val value:Long = java.lang.Long.parseLong(stringValue,16)

    
  }

  
  
  
  
}