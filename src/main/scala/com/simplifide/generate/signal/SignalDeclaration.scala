package com.simplifide.generate.signal

import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.html.Description
import com.simplifide.generate.signal.sv.Struct
import com.simplifide.generate.util.StringOps


/**
 * Class describing segment to create a signal declaration
 *
 * @constructor
 * @parameter signal Signal to be created
 *
 */
class SignalDeclaration(val signal:SignalTrait) extends SimpleSegment{


  // The basic verilog declaration
  def createSingle(signal:SignalTrait):SegmentReturn = {
    def declarationStruct(typ:String): String = {
      signal.opType match {
        case OpType.Input     => s"input $typ"
        case OpType.Output    => s"output $typ"
        case OpType.RegOutput => s"output reg $typ" // Only Works for Ansi Port Declarations
        case OpType.InOut     => s"inout $typ"
        case _                => typ
      }
    }
    val declaration: String = {
      signal.opType match {
        case OpType.Input => "input "
        case OpType.Output => "output "
        case OpType.RegOutput => "output reg " // Only Works for Ansi Port Declarations
        case OpType.InOut => "inout "
        //case OpType.Struct          => signal.asInstanceOf[Struct].typeName
        case OpType.Register => "reg " // Only Works for Ansi Port Declarations
        case OpType.Param => "parameter "
        case OpType.Logic => "logic "
        case _ => "wire "
      }
    }
    val width: String = {
      signal match {
        case x: Struct => ""
        case _ => if (signal.width > 1) "[" + (signal.width - 1) + ":0] "
        else " "
      }
    }
    val array: String = {
      if (signal.arrayLength > 0) "[0:" + signal.arrayLength + "]"
      else ""
    }
    val assignment: String = {
      signal match {
        case x: ParameterTrait => " = " + x.parameterAssignment
        case _ => ""
      }
    }
    val signed = {
      signal match {
        case x: Struct => if (x.isIo) x.typeName else ""
        case _ => if (signal.fixed.isSigned) "signed " else ""
      }
    }



    signal match {
      case x: Struct => {
        StringOps.formatLine(
          List(
            (declarationStruct(x.typeName), 2),
            (signal.name, 32)
          )
        )
      }
      case _ => {
        StringOps.formatLine(
          List(
            (declaration, 2),
            (signed, 16),
            (width, 24),
            (signal.name, 32),
            (array, 40),
            (assignment, 50)
          )
        )
      }
    }
  }

  private def createComment:SegmentReturn = {
    signal.description match {
      case Some(Description.Empty) => SegmentReturn(" // ") + signal.fixed.getDescription
      case Some(x)                 => SegmentReturn(" // ") + x.woXML
      case None                    => SegmentReturn(" // ") + signal.fixed.getDescription
    }
  }

  private def createItem(signal:SignalTrait):SegmentReturn = {
    createSingle(signal) + "; " + this.createComment + "\n"
  }

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val result = this.signal.allSignalChildren.map(createItem(_)).reduceLeft(_+_)
    result
  }



}

object SignalDeclaration {

  def apply(signal:SignalTrait):List[SignalDeclaration] =
    signal.allSignalChildren.map(x => new SignalDeclaration(x))

  def head(signal:SignalTrait):List[SignalDeclaration] =
    signal.allSignalChildren.map(x => new Head(x))

  class Head(signal:SignalTrait) extends SignalDeclaration(signal) {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val builder = new StringBuilder
      this.signal.allSignalChildren.foreach(x => builder.append(createSingle(x)))
      return SegmentReturn(builder.toString)
    }
  }


}