package com.simplifide.generate.proc.blocks

import com.simplifide.generate.signal.{OpType, FixedType}
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.proc.Controls
import com.simplifide.generate.generator.{SimpleSegment, ComplexSegment}
import com.simplifide.generate.proc.parser.ProcessorSegment
import com.simplifide.generate.blocks.basic.fixed.{ AdditionSegment, MultiplySegment}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/8/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */

class MacNormal(val width:FixedType,
          val module:Boolean = false)(implicit clk:ClockControl) extends ComplexSegment {


  def apply(expression:SimpleSegment)  = new MacNormal.MacProgram(this,expression)

  val halfWidth = U(width.width/2,0)

  private val inputType = if (module) INPUT else REG
  private val inputWireType = if (module) INPUT else WIRE
  private val outputType = if (module) REGOUT else REG

  // INPUTS
  val X                      = signal("data_in0",inputType,width)
  val Y                      = signal("data_in1",inputType,width)
  val aluControl             = signal("alu_control",inputWireType,U(3,0))
  // OUTPUTS
  val Z                      = signal("data_out",outputType,width)
  // INTERNALS
  val multiplierOutput       = register("mult_out",OpType.Signal,width)(1)

  val adderIn0               = signal("adder_in0",REG,width)
  val adderIn1               = signal("adder_in1",REG,width)
  val adderOutput            = register("adder_out",WIRE,width)(1)




  override def createBody {

    /- ("Multiplier")
    multiplierOutput(0) := X * Y
    /- ("Adder Inputs")
    adderIn0 := Mux(aluControl(0),X,Delay(multiplierOutput,1))
    adderIn1 := Mux(aluControl(1),Y,Z)
    /- ("Adder ")
    adderOutput(0) := adderIn0 + adderIn1
    /- ("Block Output")
    Z := Mux(aluControl(2),Delay(multiplierOutput,1),Delay(adderOutput,1))


  }
}

object MacNormal {

  class MacProgram(val mac:MacNormal, val expression:SimpleSegment) extends SimpleSegment.Combo {

    private def handleInputs(multIn1:SimpleSegment,multIn2:SimpleSegment, index:Int):List[Controls.Value] =
          mac.X.createControl(multIn1,null,index) ::: mac.Y.createControl(multIn2,null,index)

    private def createControls(input1:Int, input2:Int, output:Int, index:Int) =
      List(Controls.Value(mac.aluControl,index,input1 + 2*input2 + 4*output))


    override def createControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = {
      expression match {
        /*case MultiplySegment(_,in1,in2,_,_) =>
          handleInputs(in1,in2,index) ::: createControls(0,0,0,index)
        case AdditionSegment2(_,in3,MultiplySegment(_,in1,in2,_,_),sign,_,_) =>
          handleInputs(in1,in2,index) ::: createControls (1,1,1,index)
        case AdditionSegment2(_,in1,in2,sign,_,_) =>
          handleInputs(in1,in2,index) ::: createControls(0,0,1,index)
        */
        case _ => List()
      }
    }

    override def outputDelay:Int = {
      expression match {
        //case AdditionSegment2(_,in1,in2,sign,_,_) => 1
        case _ => 2
      }
    }


  }

}