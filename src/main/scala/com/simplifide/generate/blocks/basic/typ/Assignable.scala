package com.simplifide.generate.blocks.basic.typ

import com.simplifide.generate.blocks.basic.newmemory.MemoryStruct
import com.simplifide.generate.blocks.basic.typ.Expressable.SeqExpressable
import com.simplifide.generate.generator.{BasicSegments, SimpleSegment}
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.factory.CreationFactory
import com.simplifide.generate.parser.model.Expression

/**
  * Created by andy on 5/12/17.
  */
trait Assignable[T] {
  val value: T

  def :=[S](input: Expressable[S])(implicit scope: SegmentHolder): SimpleSegment
  def ::=[S](input: Expressable[S]): SimpleSegment

  def !:=[S](input: Expressable[S])(implicit scope: SegmentHolder): SimpleSegment = := (input)
  def !::=[S](input: Expressable[S]): SimpleSegment = ::= (input)

}

object Assignable {
  implicit val creator = CreationFactory.Hardware


  class AssignableMem(override val value: MemoryStruct) extends Assignable[MemoryStruct] {

    // FIXME : Should convert := and ::= to same function
    // FIXME : Should make this a general operation for structure
    def :=[S](input: Expressable[S])(implicit scope: SegmentHolder) = {
      input match {
        case x:MemoryStruct => {
          new AssignableSeq(value.inputSignals ::: x.outputSignals) :=
            new SeqExpressable(x.inputSignals ::: value.outputSignals)
        }
        case _              => ???
      }

    }
    def ::=[S](input: Expressable[S]) = {
      input match {
        case x:MemoryStruct => {
          new AssignableSeq(value.inputSignals ::: x.outputSignals) ::=
            new SeqExpressable(x.inputSignals ::: value.outputSignals)
        }
        case _              => ???
      }
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

    def :=[S](input: Expressable[S])(implicit scope: SegmentHolder) = {
      val out = createStatement(input).create
      scope.assign(out)
      out
    }

    def ::=[S](input: Expressable[S]) = {
      val out = createStatement(input).create
      out
    }
  }
}