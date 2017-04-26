package com.simplifide.generate.proc.blocks

import com.simplifide.generate.signal.{OpType, FixedType}
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.proc.Controls
import com.simplifide.generate.generator.{SimpleSegment, ComplexSegment}
import com.simplifide.generate.proc.parser.ProcessorSegment
import com.simplifide.generate.blocks.basic.fixed.MultiplySegment

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/8/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */

class Mac(val width:FixedType,
          val module:Boolean = false)(implicit clk:ClockControl) extends ComplexSegment {


  def apply(expression:SimpleSegment)  = new Mac.MacProgram(this,expression)

  val halfWidth = U(width.width/2,0)

  private val inputType = if (module) INPUT else WIRE
  private val inputRegType = if (module) INPUT else REG
  private val outputType = if (module) OUTPUT else WIRE

  // INPUTS
  val X                      = signal("data_in0",inputType,width)
  val Y                      = signal("data_in1",inputType,width)
  val multiplierControl0     = signal("mult_control_0",inputType,U(2,0))
  val multiplierControl1     = signal("mult_control_1",inputType,U(2,0))
  val adderControl0          = signal("adder_control_0",inputType,U(1,0))
  val adderControl1          = signal("adder_control_1",inputType,U(1,0))
  // OUTPUTS
  val Z                      = signal("data_out",outputType,width)
  // INTERNALS
  val halfInU0   = signal("half_in_high0",OpType.Signal,halfWidth)
  val halfInL0   = signal("half_in_low0",OpType.Signal,halfWidth)
  val halfInU1   = signal("half_in_high1",OpType.Signal,halfWidth)
  val halfInL1   = signal("half_in_low1",OpType.Signal,halfWidth)

  val multiplierIn0    = signal("mult_in0",REG,halfWidth)
  val multiplierIn1    = signal("mult_in1",REG,halfWidth)
  val multiplierOutput = register("mult_out",OpType.Signal,width)(1)

  val adderIn0    = signal("adder_in0",REG,halfWidth)
  val adderIn1    = signal("adder_in1",REG,halfWidth)
  val adderOutput = register("adder_out",WIRE,width)(1)


  override def createBody {
    /- ("Split the input into 2 signals")
    halfInU0 := X( (2*halfWidth.width-1) -> halfWidth.width )
    halfInL0 := X( (halfWidth.width-1) -> 0 )
    halfInU1 := Y( (2*halfWidth.width-1) -> halfWidth.width )
    halfInL1 := Y( (halfWidth.width-1) -> 0 )
    /- ("Multiplier Inputs")
    multiplierIn0 := Mux(multiplierControl0,halfInU0,halfInL0,halfInU1,halfInL1)
    multiplierIn1 := Mux(multiplierControl0,halfInU0,halfInL0,halfInU1,halfInL1)
    /- ("Multiplier")
    multiplierOutput(0) := multiplierIn0 * multiplierIn1
    /- ("Adder Inputs")
    adderIn0 := Mux(adderControl0,Delay(multiplierOutput,1),X)
    adderIn1 := Mux(adderControl1,Y,Z)
    /- ("Adder ")
    adderOutput(0) := adderIn0 + adderIn1
    /- ("Block Output")
    Z := Delay(adderOutput,1)


  }
}

object Mac {

  class MacProgram(val mac:Mac, val expression:SimpleSegment) extends SimpleSegment.Combo {
      override def createControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = {
        /*expression match {
          case MultiplySegment(_,in1,in2,_,_) => {
            in1.createControl(null,null,0) ::: in2.createControl (null,null,0)
          }
          case _ => List()
        }*/
        List()
      }
  }

}