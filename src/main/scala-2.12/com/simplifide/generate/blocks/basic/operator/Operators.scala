package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.signal.OpType

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 5/29/11
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */

class Operators {

}

/**
 * Classes which are used for other operations
 */
object Operators {

  /** Concatenation Operator for verilog {a,b,c} */
  def Concat(states:List[SimpleSegment]) = new Concat(states)
  /** Parenthesis Operator for ( {in} )*/
  def Paren(in:SimpleSegment)            = new Paren(in)
  /** Parenthesis Operator for ( {in} )*/
  def Tick(in:SimpleSegment,op:String)   = new Tick(in,op)
  /** Slice Operation in[slice] */
  def Slice(in:SimpleSegment,slice:SimpleSegment) = new Slice(in,slice)

  /** Concatenation Operator for verilog {a,b,c} */
  class Concat(val segments:List[SimpleSegment]) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      def createSegments = segments.map(writer.createCode(_)).zipWithIndex.map(x => (if (x._2 == 0) x._1 else SegmentReturn(",") + x._1 )).reduceLeft(_+_)
      SegmentReturn("{") + createSegments + SegmentReturn("}")
    }
  }
  def Concat(segments:SimpleSegment*):Concat = Concat(segments.toList)

  /** Parenthesis Operator for ( {in} )*/
  class Paren(val in:SimpleSegment) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn  =
      SegmentReturn("(") + writer.createCode(in) + ")"
  }

  /** Tick Operator for ( in'op )*/
  class Tick(in:SimpleSegment,op:String) extends SimpleSegment{
    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      writer.createCode(in) + "'" + op
  }

  /** Slice Operation in[slice] */
  class Slice(in:SimpleSegment,slice:SimpleSegment) extends SimpleSegment{
    override def createCode(implicit writer:CodeWriter):SegmentReturn =
      writer.createCode(in) + "[" + writer.createCode(slice) + "]"

  }
  
  class Reverse(val input:SimpleSegment) extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val segments = List.tabulate(input.fixed.width)(x => input(x))
      new Concat(segments).createCode
    }
  }


}