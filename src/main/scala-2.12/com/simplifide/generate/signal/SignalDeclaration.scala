package com.simplifide.generate.signal

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.html.Description
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
      val declaration:String = {
          signal.opType match {
            case OpType.Input           => "input "
            case OpType.Output          => "output "
            case OpType.RegOutput       => "output reg "     // Only Works for Ansi Port Declarations
            case OpType.Register        => "reg "     // Only Works for Ansi Port Declarations
            case OpType.Param           => "parameter "
            case OpType.InOut           => "inout "
            case _                      => "wire "
          }
        }
      val width:String = {
          if (signal.width > 1) "[" + (signal.width - 1) + ":0] "
          else " "
      }
      val array:String = {
          if (signal.arrayLength > 0)  "[0:" + signal.arrayLength + "]"
          else ""
        }
      val assignment:String = {
           signal match {
             case x:ParameterTrait => " = " + x.parameterAssignment
             case _                => ""
           }
      }
      val signed = if (signal.fixed.isSigned) "signed " else ""

      StringOps.formatLine(
        List(
          (declaration,2),
          (signed,16),
          (width,24),
          (signal.name,32),
          (array,40),
          (assignment,50)
        )
      )
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
    this.signal.allSignalChildren.map(createItem(_)).reduceLeft(_+_)
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