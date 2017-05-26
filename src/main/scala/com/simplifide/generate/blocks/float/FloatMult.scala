package com.simplifide.generate.blocks.float

import com.simplifide.generate.blocks.basic.fixed.MultiplySegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.newparser.typ.{NumberType, TypeParser}
import com.simplifide.generate.generator.{CodeWriter, ComplexSegment, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.{ConditionParser, EntityParser, SignalParser}
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.signal._
import com.simplifide.generate.test2.blocktest.{BlockScalaTest, BlockTestParser}
import com.simplifide.generate.test2.data.DataGenParser.RANDOM

/**
  * Created by andy on 5/2/17.
  */
class FloatMult(override val name:String,
                val in1:FloatSignal,
                val in2:FloatSignal)(implicit clk:ClockControl) extends ComplexSegment
  with ConditionParser with SignalParser  {


  val mFixed = in1.man.fixed * in2.man.fixed

  val man1      = signal(appendName("man1"),OpType.Signal,U(in1.signalWidth+1,0))
  val man2      = signal(appendName("man2"),OpType.Signal,U(in2.signalWidth+1,0))
  val res       = signal(appendName("res") ,OpType.Signal,man1.fixed*man2.fixed)

  val shift     = signal(appendName("sh"),OpType.Signal,FixedType.unsigned(2,0))
  val internal  = signal(FloatSignal(appendName("int"),OpType.Register,in1.signalWidth, in1.expWidth))
  val reg       = signal(FloatSignal(appendName("reg"),OpType.Register,in1.signalWidth, in1.expWidth))

  val res1      = SignalTrait(appendName("_man1"),OpType.Signal,U(in1.signalWidth+1,0))

  man1 := Operators.Concat(List(Constant(1.0,U(1,0)),in1.man))
  man2 := Operators.Concat(List(Constant(1.0,U(1,0)),in2.man))
  res := man1*man2


  ->($always_star(
    internal.sgn ::= in1.sgn ^ in2.sgn,
    $iff (res(res.width-1)) $then (
      shift        ::= 0,
      internal.exp ::= in1.exp + in2.exp -126,
      internal.man ::= res(res.width-2,res.width-1-in1.signalWidth)+res(res.width-2-in1.signalWidth)
    )
      $else_if(res(res.width-2)) $then (
      shift        ::= 1,
      internal.exp ::= in1.exp + in2.exp - 127,
      internal.man ::= res(res.width-3,res.width-2-in1.signalWidth)+res(res.width-3-in1.signalWidth)
    )
      $else_if(res(res.width-3)) $then (
      shift        ::=2,
      internal.exp ::= in1.exp + in2.exp - 128,
      internal.man ::= res(res.width-4,res.width-3-in1.signalWidth)+res(res.width-4-in1.signalWidth)
    )
  ).create)
  reg := internal $at (clk)



  override def createOutput(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
    new FloatMult(output.name,in1,in2)


  //val mFixed = in1.mantissa.fixed * in2.mantissa.fixed
  //val internalMantissa:SignalTrait = SignalTrait(name + "_internal",OpType.Signal,mFixed)
  //val mult                         = internalMantissa := in1.mantissa * in2.mantissa

  override def createCode(implicit writer: CodeWriter): SegmentReturn = {

    return writer.createCode(reg).extra(statements.toList.map(_.create),signals.toList)
  }

  /** Defines the body in the block */
  override def createBody: Unit = {}
}



object FloatMult {




}
