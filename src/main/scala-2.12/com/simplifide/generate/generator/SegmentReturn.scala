package com.simplifide.generate.generator

import scala.collection.mutable.ListBuffer
import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.util.StringOps

/**
 * Class which is used in the code construction process to return the generated code as well as possible errors
 * and other extra items which need to be generated
 *
 * @constructor
 * @parameter code The actual code which is generated
 * @parameter errors List of errors for the code
 * @parameter extra Extra statements which should be created before this statement
 * @parameter internal Extra internal signals which should be generated
 */
class SegmentReturn(val code:String,
                    val errors:List[InterfaceError],
                    val extra:List[SimpleSegment] = List[SimpleSegment](),
                    val internal:List[SignalTrait] = List[SignalTrait]()) {

  
  override def toString():String = code

  /** Combine two segment returns */
  def + (ret:SegmentReturn) = new SegmentReturn(this.code + ret.code,
        this.errors ::: ret.errors,
        ret.extra ::: this.extra ,
        this.internal ::: ret.internal)

  /** Combine two segment returns and apply an indent to the second method */
  def ++ (ret:SegmentReturn) = new SegmentReturn(this.code + StringOps.indentLines(ret.code,1),
        this.errors ::: ret.errors,
        this.extra ::: ret.extra,
        this.internal ::: ret.internal)

  /** Convenience method for adding a string to the current code */
  def + (ret:String) = new SegmentReturn(this.code + ret,this.errors,this.extra, this.internal)
  /** Convenience method for adding a new segment without having to call the writer.createCode method */
  def + (segment:SimpleSegment)(implicit writer:CodeWriter):SegmentReturn = this + writer.createCode(segment)
  /** Convenience method for adding a new segment without having to call the writer.createCode method */
  def ++ (segment:SimpleSegment)(implicit writer:CodeWriter):SegmentReturn = this ++ writer.createCode(segment)

  def extra(segments:List[SimpleSegment] = List(), variables:List[SignalTrait] = List()):SegmentReturn =
    this + new SegmentReturn("",List(),segments,variables)

}

object SegmentReturn {

  implicit def string2SegmentReturn(str:String):SegmentReturn = SegmentReturn(str)
  /** Creates a segment return which only contains code */
  def apply(code:String):SegmentReturn = new SegmentReturn(code,List())
  /** Creates a segment return based on an error */
  def apply(error:InterfaceError) = new SegmentReturn("",List(error))

  /** Combine the list of segments and internals */
  def combine(writer:CodeWriter,segments:List[SimpleSegment],internals:List[SignalTrait]):SegmentReturn  =
    combine(segments.map(writer.createCode(_)),List(),internals)
  /** Combine the list of SegmentReturns */
  def combine(segs:List[SegmentReturn], extra:List[InterfaceError], internals:List[SignalTrait] = List()):SegmentReturn =
     segs.reduceLeft(_+_) + new SegmentReturn("",extra,List(),internals)



}





 