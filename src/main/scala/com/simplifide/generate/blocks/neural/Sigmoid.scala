package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.generator.{ComplexSegment, SimpleSegment}
import com.simplifide.generate.signal.{Constant, FloatSignal, OpType, SignalTrait}
import com.simplifide.generate.test2.data.FloatData.FloatWrap
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
                       dataIn:FloatSignal) extends Sigmoid {
    val ilut = List(-8.0, -4.0, -2.0, -1.0, 1.0, 2.0, 4.0, 8.0)

    val lut  = List(0.0, 0.0625, 0.125, 0.25, 0.75, 0.8725, 0.935, 1.0).map(x => FloatWrap(x.toFloat))
    //val shift = List(0,4,3,2,1,2,3,4)
    val shift = List(0,4,4,3,2,3,3,4)
    val bias = 127;

    val exponent = List(0,bias-4,bias-3,bias-2,bias-1,bias-1,bias-1,bias)

    val index    = signal(appendName("_index"),OpType.Signal,U(3,0))
    val expOut   = signal(dataIn.man.newSignal(appendName("_exp"),OpType.Signal))
    val tableOut = signal(dataIn.man.newSignal(appendName("_lut"),OpType.Signal))

    val shiftIn  = signal(dataIn.man.newSignal(appendName("_nluti"),OpType.Signal,dataIn.man.fixed+1))
    val shiftOut = signal(dataIn.man.newSignal(appendName("_nlut"),OpType.Signal))
    // FIXME : Get rid of asInstanceof with variable cleanup
    val intOut   = signal(dataOut.newSignal(appendName("_int"),OpType.Signal)).asInstanceOf[FloatSignal]


    val wid = dataIn.man.fixed.width
    intOut.exp := $iff (dataIn.exp > bias + 2) $then (
      dataIn.sgn ? 0 :: lut(7).constantExponent
    ) $else_if (dataIn.exp === bias + 2) $then (
      $iff (dataIn.sgn) $then (
        $iff (~dataIn.man(wid-1)) $then (lut(1).constantExponent)
        $else_if (~dataIn.man(wid-2)) $then (lut(1).constantExponent-1)
        $else_if (~dataIn.man(wid-3)) $then (lut(1).constantExponent-2)
        $else_if (~dataIn.man(wid-4)) $then (lut(1).constantExponent-3)
      )
      $else (lut(6).constantExponent)
    ) $else_if (dataIn.exp === bias + 1) $then (
      dataIn.sgn ? lut(1).constantExponent :: lut(5).constantExponent
    ) $else_if (dataIn.exp <= bias) $then (
      dataIn.sgn ? (lut(2).constantExponent) :: (lut(4).constantExponent)
    )


    def sliceIn(x:Int, s:Boolean) = {
      val bias = if (s) 1 else (math.pow(2.0,x)-2).toInt
      val dat  = if (s) ~dataIn.man(-x) else dataIn.man(-x)
      Operators.Concat(Constant.ConstantInt(bias,x),dat.create)
    }

    def sliceIn2(x:Int, s:Boolean) = {
      val bias = if (s) 1 else (math.pow(2.0,x-2)).toInt
      val dat  = if (s) ~dataIn.man(-x) else dataIn.man(-(x+1))
      Operators.Concat(Constant.ConstantInt(bias,x),dat.create)
    }

    intOut.man := $iff (dataIn.exp > bias + 2) $then (
      0
    ) $else_if (dataIn.exp === bias + 2) $then (
      dataIn.sgn ? 0 :: sliceIn(4,false)
    ) $else_if (dataIn.exp === bias + 1) $then (
      dataIn.sgn ? 0 :: sliceIn(3,false)
    ) $else_if (dataIn.exp === bias) $then (
      dataIn.sgn ?  0 :: sliceIn(2,false)
    ) $else_if (dataIn.exp === bias-1) $then (
      dataIn.sgn ? 0 :: sliceIn2(1,false)
    ) /*$else_if (dataIn.exp === bias-2) $then (
      dataIn.sgn ? 0 :: sliceIn(0,false)
    )*/
    intOut.sgn := 0

    dataOut    := intOut
    /** Defines the body in the block */
    override def createBody: Unit = {}



  }

  /** FIXME : Only support floating poitn for now */
  case class AlawFloat(override val name:String,
                       dataOut:FloatSignal,
                       dataIn:FloatSignal) extends Sigmoid {
    val ilut = List(-8.0, -4.0, -2.0, -1.0, 1.0, 2.0, 4.0, 8.0)

    val lut  = List(0.0, 0.0625, 0.12, 0.25, 0.75, 0.8725, 0.935, 1.0).map(x => FloatWrap(x.toFloat))
    //val shift = List(0,4,3,2,1,2,3,4)
    val shift = List(0,4,4,3,2,3,3,4)
    val bias = 127;

    val index    = signal(appendName("_index"),OpType.Signal,U(3,0))
    val expOut   = signal(dataIn.man.newSignal(appendName("_exp"),OpType.Signal))
    val tableOut = signal(dataIn.man.newSignal(appendName("_lut"),OpType.Signal))

    val shiftIn  = signal(dataIn.man.newSignal(appendName("_nluti"),OpType.Signal,dataIn.man.fixed+1))
    val shiftOut = signal(dataIn.man.newSignal(appendName("_nlut"),OpType.Signal))
    // FIXME : Get rid of asInstanceof with variable cleanup
    val intOut   = signal(dataOut.newSignal(appendName("_int"),OpType.Signal)).asInstanceOf[FloatSignal]


    index := $iff (dataIn.exp < bias) $then (
      3
    ) $else_if (dataIn.exp === bias) $then (
      dataIn.sgn ? 2 :: 4
    ) $else_if (dataIn.exp === bias+1) $then (
      dataIn.sgn ? 1 :: 5
    ) $else_if (dataIn.exp === bias+2) $then (
      dataIn.sgn ? 0 :: 6
    ) $else (
      dataIn.sgn ? 0 :: 7
    )

    /- ("LUT for the Sigmoid Mantissa")
    ->(Lut(tableOut,index,lut.map(x => x.constantMantissa)))

    /- ("LUT for the Sigmoid Exponent")
    def getExponent(x:Int):SimpleSegment =
      if (x == 3) (dataIn.sgn ? lut(x).constantExponent :: lut(x).constantExponent + 1).create
      else lut(x).constantExponent
    ->(Lut(expOut,index,lut.zipWithIndex.map(x => getExponent(x._2))))

    /- ("LUT for the Addition Portion of the Operation")
    shiftIn := Operators.Concat(Constant(0,U(1,0)),dataIn.man(dataIn.man.width-1,0))
    def getShift(x:Int):SimpleSegment =
      if (x == 0) Constant(0,U(shiftOut.fixed.width,0)) else (shiftIn >> (x)).create
    ->(Lut(shiftOut,index,shift.map(x => getShift(x))))

    /- ("Final Addition Stage for Sigmoid")
    intOut.man  := tableOut + shiftOut
    intOut.exp  := expOut
    intOut.sgn := 0

    dataOut    := intOut
    /** Defines the body in the block */
    override def createBody: Unit = {}



  }
}
