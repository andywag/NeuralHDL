package com.simplifide.generate.blocks.basic.flop

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import collection.mutable.{LinkedHashMap, ListBuffer}
import com.simplifide.generate.generator._
import scala.Some
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.parser.ExpressionReturn
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.signal.{NewConstant, Constant, SignalTrait}

/** Simple Flop */
class SimpleFlopList(val name1:Option[String],
                 val head:ClockControl,
                 val reset:List[SimpleFlopList.Segment],
                 val enable:List[SimpleFlopList.Segment]) extends SimpleSegment {
  
  /** Creates a reset list for this flop. */
  private val resetList:SimpleSegment = new BasicSegments.ListSegment(reset)

  /** Creates an enable list for this flop. */
  private val enableList:SimpleSegment = new BasicSegments.ListSegment(enable)

  /** Convenience Operator to combine two flops together */
  def + (in:SimpleFlopList):SimpleFlopList =
     return new SimpleFlopList(this.name1,this.head,this.reset ::: in.reset,this.enable ::: in.enable )

  /*
  override def split:List[SimpleSegment] = {
    val flop = new SimpleFlop(name1,head,resetList,enableList)
    return flop.split
  }
  */

  /** Should no longer be called as this delegates to simple flop */
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val flop = new SimpleFlop(name1,head,resetList,enableList)
    return writer.createCode(flop)
  }
  


}

object SimpleFlopList {

  def simple(clk:ClockControl,out:SignalTrait,in:SignalTrait) = {
    new SimpleFlopList(None,clk,List(new Segment(out,None)),List(new Segment(out,Some(in))))
  }

  /** Convenience class */
  class Segment(val out:SimpleSegment,val in:Option[SimpleSegment]) extends SimpleSegment {
    override def numberOfChildren:Int           = out.numberOfChildren
    override def child(index:Int):SimpleSegment = {
      new Segment(out.child(index), if (in == None) None else Some(in.get.child(index)))
    }

    /*
    override def split:List[SimpleSegment] = {
      def busSplit:List[SimpleFlopList.Segment] = {
        if (this.out.numberOfChildren > 0) {  // If this is a vector create the vector before splitting
          val outChildren = this.out.allChildren
          return this.in match {              // TODO can probably be compressed into a simpler answer
            case Some(x) => (outChildren zip x.allChildren).map(x => new SimpleFlopList.Segment(x._1,Some(x._2)))
            case None    =>  outChildren.map(x => new SimpleFlopList.Segment(x,None))
          }
        }
        return List(this)
      }
      def segment(state:SimpleFlopList.Segment):Statement.Reg = {
         state.in match {
            case Some(x) => new Statement.Reg(state.out,x)
            case None    => new Statement.Reg(state.out,Constant(0,state.out.fixed.width))
         }
      }
      busSplit.flatMap(x => segment(x).split)

    }
    */
    /** Should not be used anymore as split should return regular statemetns */
    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      if (this.numberOfChildren == 0) {
         val assign = this.in match {
            case Some(x) => new Statement.Reg(this.out,x)
            case None    => new Statement.Reg(this.out,NewConstant(0,this.out.fixed))
         }
         return writer.createCode(assign)
      }
      return allChildren.map(writer.createCode(_)).reduceLeft(_+_)
      //return SegmentReturn.combine(writer,allChildren,List())
    }

    def getResetSegment:Segment = new Segment(out,None)
  }



}



