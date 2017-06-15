package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}
import com.simplifide.generate.signal.{Constant, FloatSignal, OpType, SignalTrait}
import com.simplifide.generate.test2.data.FloatData.FloatWrap
import com.simplifide.generate.util.PathUtilities
import org.nd4j.linalg.ops.transforms.Transforms

/**
  * Created by andy on 5/15/17.
  */
trait Sigmoid extends ComplexSegment {
  val dataOut:SignalTrait
  val dataIn:SignalTrait

  /** Defines the body in the block */

}

object Sigmoid {

  case class AlawFloat2(override val name:String,
                       dataOut:FloatSignal,
                       dataIn:FloatSignal)(implicit clk:ClockControl) extends Sigmoid {

    import com.simplifide.generate.doc.MdGenerator._
    override def document =

      s"""
This block contains a sigmoid nonlinear operation based on "Myers and Hutchinson" piecewise linear approximation.
There are a few slight differences in the operation to simplify things due to the floating point aspect but
there output error has similar properties with a maximum error of ~4.8%. This block is more naturally done using
fixed point which inherently is internally done in the internal shifters of this block


## Input/Output
* output ${dataOut.document}  : Output of the block
* input  ${dataIn.document}   : Input of the block

## Generator Code

The code used to generate this code is relatively complex

* [Code Generator](${PathUtilities.nueralPath}/Sigmoid.scala)
* [Verilog Output](../design/${name}.v)

"""


    val lut  = List(0.0, 0.0625/2, 0.125, 0.25, 0.75, 0.8725, 0.935, 1.0).map(x => FloatWrap(x.toFloat))
    val bias = 127;

    val exponent = List(0,bias-5,bias-3,bias-2,bias-1,bias-1,bias-1,bias)

      // FIXME : Get rid of asInstanceof with variable cleanup
    val intOut   = signal(dataOut.newSignal(appendName("_int"),OpType.Signal)).asInstanceOf[FloatSignal]


    // The exponent for the operation is the output exponent for the operation
    // It shifts down normally over the whole range except the bottom segment
    // For this segment the exponent needs to shift down as the data gets smaller

    /-("Exponent LUT")
    val wid = dataIn.man.fixed.width
    intOut.exp := $iff (dataIn.exp > bias + 2) $then (
      dataIn.sgn ? 0 :: exponent(7)
    ) $else_if (dataIn.exp === bias + 2) $then (
      $iff (dataIn.sgn) $then (
        $iff (~dataIn.man.bit(0)) $then (exponent(1)-1)
        $else_if (~dataIn.man.bit(-1)) $then (exponent(1)-2)
        $else_if (~dataIn.man.bit(-2)) $then (exponent(1)-3)
        $else_if (~dataIn.man.bit(-3)) $then (exponent(1)-4)
        $else 0
      )
      $else (exponent(6))
    ) $else_if (dataIn.exp === bias + 1) $then (
      dataIn.sgn ? (dataIn.man.bit(0) ? exponent(1) :: exponent(1)+1) :: exponent(5)
    ) $else_if (dataIn.exp === bias) $then (
      dataIn.sgn ? (exponent(2)) :: (exponent(5))
    ) $else_if (dataIn.exp < bias) $then (
      dataIn.sgn ? (exponent(3)) :: (exponent(4))
    )


    def sliceIn(x:Int, s:Boolean) = {
      val bias = if (s) 0 else (math.pow(2.0,x)-2).toInt
      val dat  = if (s) ~dataIn.man(-x) else dataIn.man(-x)

      if (x > 0) cat(H(bias,x),dat)
      else dat.create
    }

    def sliceIn2(x:Int, s:Boolean) = {
      val bias = if (s) ((math.pow(2.0,x+1)-2).toInt)
                 else 1
      val dat  = if (s) ~dataIn.man(-(x+1)) else dataIn.man(-(x+1))
      cat(H(bias,x+1),dat.create)
    }

    def upShift = cat((~dataIn.man(dataIn.man.width-1,0)).create,H(0,1))

    // The mantissa for this operation is just the input data shifted appropriately
    //
    // 1. For positive values the data is normal, For negative values it is inverted
    // 2. Over the range around zero there are more shift operations as the exponent changes rapidly

    /-("Mantissa LUT")
    intOut.man := $iff (dataIn.exp > bias + 2) $then (
      0
    ) $else_if (dataIn.exp === bias + 2) $then (
      dataIn.sgn ? 0 :: sliceIn(4,false)
    ) $else_if (dataIn.exp === bias + 1) $then (
      dataIn.sgn ? upShift :: sliceIn(3,false)
    ) $else_if (dataIn.exp === bias) $then (
      dataIn.sgn ? sliceIn(0,true) :: sliceIn(2,false)
    ) $else_if (dataIn.exp === bias-1) $then (
      dataIn.sgn ? sliceIn2(0,true) :: sliceIn2(1,false)
    ) $else_if (dataIn.exp === bias-2) $then (
      dataIn.sgn ? sliceIn2(1,true) :: sliceIn2(2,false) // Jere
    ) $else_if (dataIn.exp === bias-3) $then (
      dataIn.sgn ? sliceIn2(2,true):: sliceIn2(3,false)
    )$else (
      dataIn.sgn ? sliceIn2(3,true) :: sliceIn2(4,false)
    )

    intOut.sgn := 0

    // Temporary Bypass of Sigmoid operation
    //dataOut    := intOut $at clk
    dataOut    := dataIn $at clk

    override def createBody = {}
    override def inputs: Seq[SignalTrait] = dataIn :: clk.allSignals(INPUT)
    override def outputs:List[SignalTrait] = List(dataOut)


  }


}
