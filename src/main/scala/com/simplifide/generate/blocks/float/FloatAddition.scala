package com.simplifide.generate.blocks.float

import com.simplifide.generate.blocks.basic.condition.ConditionStatement
import com.simplifide.generate.blocks.basic.fixed.AdditionSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.operator.{Operators, UnaryOperator}
import com.simplifide.generate.generator.{CodeWriter, SegmentReturn}
import com.simplifide.generate.parser.{ConditionParser, EntityParser, SegmentHolder}
import com.simplifide.generate.parser.items.SingleConditionParser.IfStatement
import com.simplifide.generate.parser.model.BasicExpressions
import com.simplifide.generate.signal.{Constant, FloatSignal, OpType, SignalTrait}

/**
  * Created by andy on 5/2/17.
  */
class FloatAddition(override val name:String,
                    override val in1:FloatSignal,
                    override val in2:FloatSignal,
                    override val negative:Boolean)(implicit val clk:ClockControl) extends AdditionSegment with ConditionParser with SegmentHolder {

  val extraWidth = in1.signalWidth + 3
  val shiftWidth = in1.signalWidth + in1.signalWidth + 3

  val internalSig  = signal(FloatSignal(appendName("int"),OpType.Register,in1.signalWidth, in1.expWidth))

  val delta_exp       = signal(SignalTrait(appendName("del"),OpType.Signal,U(in1.expWidth,0)))
  val choose_exp       = signal(SignalTrait(appendName("exp"),OpType.Signal,U(in1.expWidth,0)))
  val sel_exp         = signal(SignalTrait(appendName("sgn"),OpType.Signal,U(1,0)))
  val shift           = signal(SignalTrait(appendName("shift"),OpType.Signal,U(in1.expWidth,0)))
  val add_in1         = signal(SignalTrait(appendName("ain1"),OpType.Signal,S(extraWidth,0)))
  val add_in2         = signal(SignalTrait(appendName("ain2"),OpType.Signal,S(extraWidth,0)))
  val shift_in1        = signal(SignalTrait(appendName("sh_in1"),OpType.Signal,S(extraWidth,0)))
  val shift_in        = signal(SignalTrait(appendName("sh_in"),OpType.Signal,S(shiftWidth,0)))

  val nshift_in       = signal(SignalTrait(appendName("nsh_in"),OpType.Signal,S(extraWidth,0)))
  val shift_out       = signal(SignalTrait(appendName("sh_out"),OpType.Signal,S(shiftWidth,0)))
  val nshift_out      = signal(SignalTrait(appendName("nsh_out"),OpType.Signal,S(shiftWidth,0)))
  val round_bit       = signal(SignalTrait(appendName("rnd_bit"),OpType.Signal,S(shiftWidth,0)))

  val add_out         = signal(SignalTrait(appendName("add_out"),OpType.Signal,S(shiftWidth,0)))
  val abs_out         = signal(SignalTrait(appendName("abs_out"),OpType.Signal,S(shiftWidth,0)))
  val reg             = signal(FloatSignal(appendName("register"),OpType.Register,in1.signalWidth, in1.expWidth))


  val ZERO = Constant(0.0,U(1,0))
  val ONE  = Constant(1.0,U(1,0))
  val PAD  = Constant(0.0,U(in1.signalWidth,0))
  val RND  = Constant(math.pow(2.0,in1.signalWidth),U(shiftWidth,0))

  def extend(in:SignalTrait) = Operators.Concat(ZERO,ZERO,ONE,in)

  (delta_exp   := in1.exp - in2.exp)
  (shift       := delta_exp.sign ? -delta_exp :: delta_exp)
  (sel_exp     := delta_exp.sign)
  (choose_exp := delta_exp.sign ? in2.exp :: in1.exp)
  (add_in1     := in1.sgn ? -extend(in1.man) :: extend(in1.man))
  (add_in2     := in2.sgn ? -extend(in2.man) :: extend(in2.man))
  (nshift_in   := sel_exp  ? add_in2 :: add_in1)
  shift_in1    := sel_exp  ? add_in1  :: add_in2
  shift_in     := Operators.Concat(shift_in1,PAD)
  shift_out   := shift_in >>> shift
  round_bit    := RND >> shift

  nshift_out  := Operators.Concat(nshift_in,PAD)
  // FIXME : Need to keep signs straight since reordered
  if (negative) add_out     := shift_out - nshift_out//+ shift_out(0)
  else          add_out     := shift_out + nshift_out// + shift_out(0)
  abs_out      := add_out.sign ? -add_out :: add_out


  def states(index:Int, inW:Int, outW:Int) = {

    val shiftValue = Constant(index-2+ inW-outW,U(5,0))
    val posValue   = Constant(1.0,U(5,0))


    val orLast = UnaryOperator.Or(abs_out(abs_out.width-1-index-outW-2,0))
    val round  = abs_out(abs_out.width-1-index-outW-1) & (orLast | abs_out(abs_out.width-1-index-outW))

    val first = internalSig.man ::= (abs_out(abs_out.width-2-index,abs_out.width-1-index-outW)) +
      round


    val other = internalSig.man ::= (abs_out(abs_out.width-2-index,abs_out.width-1-index-outW)) +
      round

    val command = if (index == 1) first else if (index == 2) other else other


    BasicExpressions.List(List(internalSig.exp ::= choose_exp + 2-index,
      command))
  }
  def condition(index:Int) = Some(abs_out(abs_out.width-index-1))

  val conds = List.tabulate(in1.signalWidth-1)(x => new IfStatement(condition(x+1),states(x+1,shiftWidth,in1.signalWidth)))
  val cconds = conds.zipWithIndex.map(x => x._1.createSegment(x._2))

  $always_star(
    internalSig.sgn ::= add_out.sign ,
    new ConditionStatement(cconds)
  )
  reg := internalSig $at (clk)


  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val temp = statements.toList.map(_.create)
    temp.foreach(x =>
      writer.createCode(x)
    )
    writer.createCode(reg).extra(statements.toList.map(_.create),signals.toList)
  }

}

object FloatAddition {




}