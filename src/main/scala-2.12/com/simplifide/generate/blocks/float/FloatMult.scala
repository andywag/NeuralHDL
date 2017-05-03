package com.simplifide.generate.blocks.float

import com.simplifide.generate.blocks.basic.fixed.MultiplySegment
import com.simplifide.generate.blocks.basic.operator.Operators
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn}
import com.simplifide.generate.parser.{ConditionParser, SignalParser}
import com.simplifide.generate.signal._

/**
  * Created by andy on 5/2/17.
  */
class FloatMult(override val name:String,
                override val in1:FloatSignal,
                override val in2:FloatSignal) extends MultiplySegment
  with ConditionParser with SignalParser  {



  //val mFixed = in1.mantissa.fixed * in2.mantissa.fixed
  //val internalMantissa:SignalTrait = SignalTrait(name + "_internal",OpType.Signal,mFixed)
  //val mult                         = internalMantissa := in1.mantissa * in2.mantissa

  override def createCode(implicit writer: CodeWriter): SegmentReturn = {
    val mFixed = in1.man.fixed * in2.man.fixed

    val man1      = SignalTrait(appendName("man1"),OpType.Signal,U(in1.signalWidth+1,0))
    val man2      = SignalTrait(appendName("man2"),OpType.Signal,U(in2.signalWidth+1,0))
    val res       = SignalTrait(appendName("res") ,OpType.Signal,man1.fixed*man2.fixed)
    val shift     = SignalTrait(appendName("sh"),OpType.Signal,FixedType.unsigned(2,0))
    val internal  = FloatSignal(appendName("int"),OpType.Register,in1.signalWidth, in1.expWidth)

    val res1      = SignalTrait(appendName("_man1"),OpType.Signal,U(in1.signalWidth+1,0))

    val s0 = (man1 ::= Operators.Concat(List(Constant(1.0,U(1,0)),in1.man)))
    val s1 = (man2 ::= Operators.Concat(List(Constant(1.0,U(1,0)),in2.man)))
    val mult = (res ::= man1*man2)

    val fi = $always_star(
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
    ).create


    return writer.createCode(internal).extra(List(s0,s1,mult,fi).map(_.create),List(man1,man2,res,shift,internal))
  }
}

object FloatMult {


}
