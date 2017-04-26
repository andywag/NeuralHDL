package com.simplifide.generate.blocks.basic.state

import com.simplifide.generate.generator._

/**
 * Class which defines an always or process body
 *
 * @constructor
 * @parameter name1 Optional name of the block
 * @parameter body  FunctionBody inside the always body
 */



trait Always extends SimpleSegment{

  val internalName:Option[String]
  val body:SimpleSegment
  val senseList:Option[List[SimpleSegment]]
  
  def newAlways(name:Option[String]       = this.internalName,
    body:SimpleSegment                    = this.body,
    senseList:Option[List[SimpleSegment]] = this.senseList):Always = {
   
    this match {
      case x:Always.Sensitivity => new Always.Sensitivity(internalName,body,senseList)
      case _ => new Always.Star(name,body,senseList)
    }
    
  }

  override def createSplit  = List(this.newAlways(body = body.createSplitSingle))
  override def createVector = List(this.newAlways(body = body.createVectorSingle))


  def extra(extraStatements:List[SimpleSegment])(implicit writer:CodeWriter):SegmentReturn = {
    if (extraStatements.size > 0) {
      val extraReturns = extraStatements.map(writer.createCode(_))
      extraReturns.reduceLeft[SegmentReturn](_+_)
    }
    else SegmentReturn("")

  }
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    def sense:SegmentReturn = {
      def internalSense = senseList.get.map(writer.createCode(_)).zipWithIndex.map(x => (if (x._2 == 0) x._1 else SegmentReturn(",") + x._1)).reduceLeft(_+_)
      this match {
        case x:Always.Sensitivity => {
          SegmentReturn("always @(") + internalSense + ") "
        }
        case _                    => SegmentReturn("always @* ")
      }
    }
    val bodyReturn = writer.createCode(body)
    extra(bodyReturn.extra) + sense + "begin\n" ++ bodyReturn + "end\n"

  }
  
  
}

object Always {

  def Sensitivity(body:List[SimpleSegment],senseList:List[SimpleSegment]):Sensitivity =
    new Sensitivity(None,BasicSegments.List(body),Some(senseList))

  /** Always Block with Sensitivity List (senseList) containing FunctionBody body */
  def Sensitivity(name:Option[String],body:SimpleSegment,senseList:List[SimpleSegment]):Sensitivity =
    new Sensitivity(name,body,Some(senseList))

  def Star(name:Option[String],body:SimpleSegment, senseList:List[SimpleSegment]):Star =
    new Star(name,body,Some(senseList))

  def Star(body:SimpleSegment):Star = new Star(None,body,None)

  def Star(segments:List[SimpleSegment]):Star =
    Star(None,BasicSegments.List(segments),List())

  /**
   * Always block which contains a sensitivity list
   * @constructor
   * @parameter senseList Sensitivity List for the always block
   */
  class Sensitivity(override val internalName:Option[String],
    override val body:SimpleSegment,
    override val senseList:Option[List[SimpleSegment]]) extends Always

  class Star(override val internalName:Option[String],
    override val body:SimpleSegment,
    override val senseList:Option[List[SimpleSegment]]) extends Always


}



