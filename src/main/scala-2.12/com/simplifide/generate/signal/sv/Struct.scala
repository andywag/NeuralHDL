package com.simplifide.generate.signal.sv

import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.signal.{FixedType, OpType, SignalDeclaration, SignalTrait}

/**
  * Created by andy on 4/28/17.
  */
trait Struct extends SignalTrait {
  val packed:Boolean
  val typeName:String
  val signals:List[SignalTrait]

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case x:Struct => if (x.typeName.equals(this.typeName)) true else false
      case _ =>          false
    }
  }

  override lazy val width = {
    val w = signals.map(_.width).reduceLeft(_+_)
    w
  }

  def createName(input:String) = {
    if (name.length > 0) s"$name.$input" else input
  }

  /** Kind of a kludge need a better way to copy objects (shapeless maybe) */
  def copyStruct(n:String, o:OpType):SignalTrait



}

object Struct {

  class TypeDef(segment:SimpleSegment) extends SimpleSegment {
    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      SegmentReturn("typedef ") + writer.createCode(segment)
    }
  }

  class StructInternal(struct:Struct) extends SimpleSegment {
    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val ret:SegmentReturn = struct.signals.map(x => {
        val n = x.name.split("\\.")(1)
        new SignalDeclaration(x.newSignal(name = n, opType = OpType.Logic ))
      }
      ).map(x => writer.createCode(x)).reduceLeft(_+_)
      SegmentReturn("struct packed {\n") + ret+ SegmentReturn(s"} ${struct.typeName}; \n")
    }
  }

  class StructDeclaration(struct:Struct) extends SimpleSegment {
    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      writer.createCode(new TypeDef(new StructInternal(struct)))
    }
  }
}
