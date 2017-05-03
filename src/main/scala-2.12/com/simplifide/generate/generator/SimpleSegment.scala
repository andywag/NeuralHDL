package com.simplifide.generate.generator

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.{SegmentHolder, ExpressionReturn}
import com.simplifide.generate.proc.{ControlHolder, Controls}
import com.simplifide.generate.proc.parser.ProcessorSegment
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.parser.factory.{CreationFactory}
import com.simplifide.generate.signal.{SignalSelect, OpType, SignalTrait, FixedType}
import com.simplifide.generate.parser.items.MiscParser


/**
 * Base trait for a code segment.
 */

trait SimpleSegment extends Expression with ControlHolder with AssignmentHolder  {

  implicit val creator = CreationFactory.Hardware
  
  def apply(fixed:FixedType):SimpleSegment = this
  // TODO Need a way to handle a slice
  def apply(index:(Int,Int)) = this

  def apply(width:MiscParser.Width) = this

  def apply(index:Int) = this
  /** Name of this Block */
  val name = ""
  /** Fixed type of the output from this segment*/
  val fixed:FixedType = FixedType.Simple

  val opType:OpType = OpType.Signal

  def appendName(iname:String) =
    (this.name.replace(".","_") + s"_$iname").replace("__","_")


  def isReg    = false
  
  /** Number of Children for this module. Used for array expansion */
  def numberOfChildren:Int = 0
  /** Returns the child at the input index */
  def child(index:Int):SimpleSegment = this
  /** Returns the child at the input index with the given output */
  def child(index:Int,output:SimpleSegment):SimpleSegment = this.child(index)
  /** Get a complete list of all children of this block */
  def children:List[SimpleSegment] = List.tabulate(numberOfChildren){x =>child(x)}
  /** Return a set of children based on the output */
  def children(output:SimpleSegment):List[SimpleSegment] = List.tabulate(numberOfChildren)(x => child(x,output.child(x)))
  /** All of the Children */
  def allChildren:List[SimpleSegment] = if (numberOfChildren == 0) List(this) else children.flatMap(x => x.allChildren)
  /** Create an assignment based on this segment */
  
  /** Return a sliced version of this segment */
  /** List of Extra Statements created from this statement */
  def extra:List[SimpleSegment] = List()
  /** Output of this code segment */
  def outputs:List[SignalTrait] = List()


  /** Create the simple segment */
  def create(implicit creator:CreationFactory):SimpleSegment = this
  /** Create Expression as a function of the output */
  def createOutput(output:SimpleSegment)(implicit creator:CreationFactory) = this.create

  /*override def createAssignment(output:SimpleSegment)(implicit creator:CreationFactory):SimpleSegment =
    new Statement.Reg(output,this.createOutput(output))
  */
  /** Convert the input segment to a set of vectors*/
  def createVector:List[SimpleSegment] = List(this)

  def createVectorSingle:SimpleSegment = {
    val vector = createVector
    if (vector.size == 1) vector(0) else BasicSegments.List(vector)
  }

  def createSplit:List[SimpleSegment] = List(this)

  def createSplitSingle:SimpleSegment = {
    val vector = createSplit
    if (vector.size == 1) vector(0) else BasicSegments.List(vector)
  }

  /** Split the segment into multiple statements if required */
  def createIndividualSplit(output:SimpleSegment,index:Int=0):(SimpleSegment,List[SimpleSegment]) = {
    (this, List())
  }
  /** Create a suboutput for intermediate terms - Only Supported for signals */
  def createSubOutput(index:Int):SimpleSegment = null
 


  //def createCode(writer:CodeWriter):SegmentReturn

  def createCode(implicit writer:CodeWriter):SegmentReturn
  def createCodeRoot(implicit writer:CodeWriter):SegmentReturn = createCode

  /** Combine this segment with the input segment */
  def ++ (segment:SimpleSegment):SimpleSegment = BasicSegments.List(List(this,segment))
  /** Combine this segment with the string */
  def ++ (segment:String):SimpleSegment        = this ++ new SimpleSegment.Code(segment)






  /** Methods to create code segments */
  def createVerilogCode(writer:CodeWriter):SegmentReturn     = createCode(writer)
  def createVhdlCode(writer:CodeWriter):SegmentReturn        = createCode(writer)
  def createFloatCode(writer:CodeWriter):SegmentReturn       = createCode(writer)
  def createFixedCode(writer:CodeWriter):SegmentReturn       = createCode(writer)
  def createHeaderCode(writer:CodeWriter):SegmentReturn      = createCode(writer)
}

object SimpleSegment {

  def maxChildren(ch:scala.List[SimpleSegment]):Int = {
    ch.map(x => x.numberOfChildren).reduceLeft(math.max(_,_))
  }

  class Code(val value:String) extends SimpleSegment{
    def createCode(implicit writer:CodeWriter):SegmentReturn =  new SegmentReturn(value,List())

  }

  class List(val segments:scala.List[SimpleSegment]) extends SimpleSegment{
    def createCode(implicit writer:CodeWriter):SegmentReturn = {
       val segs = segments.map(writer.createCode(_))
       segs.reduceLeft(_+_)
    }
  }
  
  object Empty extends SimpleSegment {
    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      SegmentReturn("")
    }
  }

  class Combo extends SimpleSegment {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      System.out.println("Error" + this + this.getClass)
      null
    }

  }

}

