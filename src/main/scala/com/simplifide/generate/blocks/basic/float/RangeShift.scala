package com.simplifide.generate.blocks.basic.float

import com.simplifide.generate.blocks.basic.operator.UnaryOperator
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser.ConditionParser
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.{FloatSignal, OpType, SignalTrait}

/**
  * Created by andy on 4/28/17.
  */
class RangeShift(override val name:String, input:FloatSignal) extends SimpleSegment with ConditionParser{

  val searchRange:Int = 3

  val shift    = SignalTrait(this.appendName("sh"), OpType.Register, input.exp.fixed)
  val internal = input.copyStruct(input.appendName("int"),OpType.Struct)

  val uWidth = input.man.width


  val state0 = internal.sgn ::= input.sgn

  val state1 = shift ::= (
    $iff (input.man(uWidth-1)) $then (0)
      $else_if(input.man(uWidth-2)) $then {1}
      $else_if(input.man(uWidth-3)) $then {2}
    )

  val state2 = internal.exp ::= input.exp + shift
  val state3 = internal.man ::= (
    $iff (input.man(uWidth-1)) $then (input.man << 1)
      $else_if(input.man(uWidth-2)) $then {input.man << 2}
      $else_if(input.man(uWidth-3)) $then {input.man << 3}
    )
  override def createCode(implicit writer: CodeWriter): SegmentReturn = {
    writer.createCode(internal).extra(List(state0,state1,state2,state3).map(_.create),List(shift,internal))
  }
}



