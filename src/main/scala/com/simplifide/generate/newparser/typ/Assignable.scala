package com.simplifide.generate.newparser.typ

import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct
import com.simplifide.generate.newparser.typ.Expressable.SeqExpressable
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.SignalInterface

/**
  * Created by andy on 5/12/17.
  */
trait Assignable[T] {
  val value: T

  //def :=[S](input: Expressable[S])(implicit scope: SegmentHolder): SimpleSegment
  //def ::=[S](input: Expressable[S]): SimpleSegment

  def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder): SimpleSegment
  def !::=[S](input: Expressable[S]): SimpleSegment

  def !->[S](input:Expressable[S])(implicit scope: SegmentHolder):SignalTrait = {
    !:=(input)
    value.asInstanceOf[SignalTrait] // FIXME : Temporary Hack
  }

}

object Assignable {
  implicit val creator = CreationFactory.Hardware

/*
  class AssignableMem(override val value: MemoryStruct) extends Assignable[MemoryStruct] {

    val eqs:(Assignable[_],Expressable[_],SegmentHolder)=>SimpleSegment = (x,y,z) => x.!:=(y)(z)
    val eq:(Assignable[_],Expressable[_])=>SimpleSegment = (x,y) => x !::= y

    def assign[S](input:Expressable[S], f:(Assignable[_],Expressable[_])=>SimpleSegment)(implicit scope: SegmentHolder) = {
      input match {
        case Expressable.SeqExpressable(va) => {
          va(0) match {
            case x:MemoryStruct =>
              f(new AssignableSeq(value.inputSignals ::: x.outputSignals),
                new SeqExpressable(x.inputSignals ::: value.outputSignals))
            case _ => ???
          }
        }
        case _              => ???
      }
    }

    // FIXME : Should convert := and ::= to same function
    // FIXME : Should make this a general operation for structure
    def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder) = assign(input,eqs(_,_,scope))
    def !::=[S](input: Expressable[S]) = assign(input,eq)(SegmentHolder.Impl)
  }
*/
  class AssignableSingle(override val value: Expression) extends Assignable[Expression] {

    def createStatement[S](input: Expressable[S]) = {
      val result = input match {
        case x: OutputAssignable[_] => x.createStatement(this)
        case _ => input.value match {
          case y:Expression       => ParserStatement(value,y)
          case y:Seq[Expression]  => ParserStatement(value,y(0))
          case _                  => ???
        }
      }
      result
    }

    def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder) = {
      val out = createStatement(input).create
      scope.assign(out)
      out
    }

    def !::=[S](input: Expressable[S]) = {
      val out = createStatement(input).create
      out
    }
  }

  class AssignableSeq(override val value: Seq[Expression]) extends Assignable[Seq[Expression]] {

    def createStatement[S](input: Expressable[S]) = {
      val result = input match {
        case x: OutputAssignable[_] => x.createStatement(this)
        case x: SeqExpressable => {
          val result = (value zip x.value).map(x => ParserStatement(x._1, x._2))
          BasicSegments.List(result.toList.map(_.create))
        }
      }
      result
    }

    def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder) = {
      val out = createStatement(input).create
      scope.assign(out)
      out
    }

    def !::=[S](input: Expressable[S]) = {
      val out = createStatement(input).create
      out
    }
  }

  class AssignableInterface(override val value:SignalInterface) extends Assignable[SignalInterface] {

    val eqs:(Assignable[_],Expressable[_],SegmentHolder)=>SimpleSegment = (x,y,z) => x.!:=(y)(z)
    val eq:(Assignable[_],Expressable[_])=>SimpleSegment = (x,y) => x !::= y

    def assign[S](input:Expressable[S], f:(Assignable[_],Expressable[_])=>SimpleSegment)(implicit scope: SegmentHolder) = {
      input match {
        case x:Expressable.ExpressableInterface => {
          f(new AssignableSeq(value.inputs.toList ::: x.value.outputs.toList),
            new SeqExpressable(x.value.inputs.toList ::: value.outputs.toList))
        }
        case _              => ???
      }
    }

    // FIXME : Should convert := and ::= to same function
    // FIXME : Should make this a general operation for structure
    def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder) = assign(input,eqs(_,_,scope))
    def !::=[S](input: Expressable[S]) = assign(input,eq)(SegmentHolder.Impl)
  }

}