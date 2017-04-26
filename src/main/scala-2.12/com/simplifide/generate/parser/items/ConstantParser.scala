package com.simplifide.generate.parser.items

import com.simplifide.generate.signal.{FixedType, NewConstant}


/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 5/31/12
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */

trait ConstantParser {

  def unsigned(width:Int) = if (width == -1) FixedType.Simple else FixedType.unsigned(width,0)
  def signed(width:Int)   = if (width == -1) FixedType.SimpleSigned else FixedType.signed(width,0)


  def B(value:Long,width:Int = -1) = new NewConstant.NewLong(value,unsigned(width),NewConstant.BINARY)
  def O(value:Long,width:Int = -1) = new NewConstant.NewLong(value,unsigned(width),NewConstant.OCTAL)
  def D(value:Long,width:Int = -1) = new NewConstant.NewLong(value,unsigned(width),NewConstant.DECIMAL)
  def H(value:Long,width:Int = -1) = new NewConstant.NewLong(value,unsigned(width),NewConstant.HEX)

  def SB(value:Long,width:Int = -1) = new NewConstant.NewLong(value,signed(width),NewConstant.BINARY)
  def SO(value:Long,width:Int = -1) = new NewConstant.NewLong(value,signed(width),NewConstant.OCTAL)
  def SD(value:Long,width:Int = -1) = new NewConstant.NewLong(value,signed(width),NewConstant.DECIMAL)
  def SH(value:Long,width:Int = -1) = new NewConstant.NewLong(value,signed(width),NewConstant.HEX)

  
}
