package com.simplifide.generate.blocks.basic.fixed

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.blocks.basic.flop.SimpleFlopList
import scala.collection.mutable.ListBuffer
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}
import scala.Some
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.Statement

// TODO Needs refactoring to use AdditionSegment2
/**
 * Class which defines an adder tree which consists of input signals multiplied by a constant value
 *
 * @constructor
 * @parameter name Name of the Block
 * @parameter output Output Signal
 * @parameter constants Values which make up the adder tree (Contains signal and constant)
 * @parameter internal Internal Width of Adder Tree
 * @parameter levels Levels between flops
 * @parameter flop True if internal flops are required
 **/
class AdderTree(override val name:String,
                val clk:ClockControl,
                val output:SignalTrait,
                val constants:List[AdderTree.Value],
                val internal:FixedType,
                val levels:Int = 0,
                val flop:Boolean = false) extends SimpleSegment{


  /** Method to handle the case when the input is the output */
  def createBypass(writer:CodeWriter,output:SignalTrait):SegmentReturn = {
		  return null;
  }

  def getFlopRows(depth:Int):List[Int] = {
    val incr = depth.toDouble/levels.toDouble

    val buf = new ListBuffer[Int]()
    val alev = if (flop) levels else levels-1 // Don't Flop if output not required
    for (i <- 0 until levels) {
      val row = math.round((i+1)*incr).toInt-1
      buf.append(row)
    }
    return buf.toList
  }



  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    // Create the nodes sorted by the scale factor
    var nodes = constants.flatMap(x => x.createNode).sortBy(e => -e.scale)
    // Calculate depth of tree
    val size_nodes = nodes.size.toDouble
    val div = math.log10(size_nodes)/math.log10(2.0)
    val depth = math.floor(div + 1.0).toInt
    // Create the Rows of the adder tree
    val rows = new ListBuffer[AdderTree.Row]
    val udepth = if (depth < levels) levels else depth
    val flops:List[Int] = getFlopRows(udepth)
    for (i <- 0 until udepth) {
      val fixed = if (i == udepth-1) output.fixed else internal
      val row = if (flops.contains(i)) new AdderTree.FlopRow(clk,name,nodes,fixed,udepth-i-1,i==0)
                else                   new AdderTree.NormalRow(name,nodes,fixed,udepth-i-1,  i==0)
      rows.append(row)
      nodes = row.outputNodes
    }
    val flop = new SimpleFlopList(None,clk, rows.toList.flatMap(x => x.flopSegments._1), rows.toList.flatMap(x => x.flopSegments._1))

    val outStatement =  new Statement.Assign(output,nodes(0).signal)

    return SegmentReturn.combine(writer, rows.toList ::: List(outStatement,flop),rows.toList.flatMap(x => x.signals))
  }

}

object AdderTree {

  def Value(constant:Constant,signal:SignalTrait) =
    new AdderTree.Value(constant,signal)


  class Value(val constant:Constant,val signal:SignalTrait) {
    def createNode:List[AdderTree.Node] =
      return constant.createCSD.map(x => new AdderTree.Node(signal,constant.fixed.fraction - x.value,x.negative))
  }

  class Node(val signal:SignalTrait,val scale:Int,val neg:Boolean)  {

    /** Return the quantized version of the data */
    private def fixedSelect(internal:FixedType, first:Boolean):SimpleSegment =
        if (first) new FixedSelect.Scale(signal,internal,scale) else signal

    /** First term for the addition */
    def firstTerm(internal:FixedType, first:Boolean):AdditionTerm =
      if (neg) return new AdditionTerm.SubTerm(fixedSelect(internal,first))
      else return new AdditionTerm.Empty(fixedSelect(internal,first))

    /** Second term for the addition */
    def secondTerm(internal:FixedType, first:Boolean):AdditionTerm =
      if (neg) return new AdditionTerm.SubTerm(fixedSelect(internal,first))
      else return new AdditionTerm.AddTerm(fixedSelect(internal,first))
  }
  
  /* Defines a row of the adder tree */
  abstract class Row(override val name:String,val nodes:List[AdderTree.Node],
                     val internal:FixedType,val level:Int,val first:Boolean) extends SimpleSegment {
    /** Returns the individual output appendSignal for this row */
    def outSignal(y:Int):SignalTrait =
      SignalTrait(name + "_" + this.level + "_" + y,OpType.Signal,internal)
    /** Returns the register appendSignal for this row */
    def regSignal(y:Int):SignalTrait =
     SignalTrait(name + "r_" + this.level + "_" + y,OpType.Register,internal)

    def flopSegments:(List[SimpleFlopList.Segment],List[SimpleFlopList.Segment]) = (List(),List())
    /** Create the adder tree for the row */
    def createAdder(writer:CodeWriter):SegmentReturn = {
      val adders = new ListBuffer[AdderTree.Segment]
      for (i <- 0 until nodes.size/2) {
        val add = new AdderTree.Segment(name + "_" + level + "_" + i,nodes(2*i),Some(nodes(2*i+1)),this.outSignal(i),internal,first)
        adders.append(add)
      }
      if (nodes.size % 2 == 1) adders.append(new AdderTree.Segment(name + "_" + nodes.size/2,nodes(nodes.size-1),None,this.outSignal(nodes.size/2),internal,first))
      val commentRet = writer.createCode(new Comment.SingleLine("Stage " + level + " Adders"))
      val addReturn =  adders.toList.map(x => x.createCode(writer))
      return SegmentReturn.combine(List(commentRet) ::: addReturn, List())
    }

    /**Get the output appendSignal associated with this row. Could be a register
       or a wire */
    def getRealOutputSignal(x:Int) = outSignal(x)
    /** Returns a list of output nodes */
    def outputNodes:List[AdderTree.Node] = {
      val onodes = new ListBuffer[AdderTree.Node]
      val len = math.ceil(nodes.size.toDouble/2.0).toInt
      val ulen = if (len > 0) len else 1
      for (i <- 0 until ulen) {
        val sig = getRealOutputSignal(i)
        onodes.append(new AdderTree.Node(sig,0,false))
      }
      return onodes.toList
    }

    def internalSignals(x:Int):List[SignalTrait] = List(outSignal(x))

    /** Returns a list of signals */
    def signals:List[SignalTrait] =
      List.tabulate(math.ceil(nodes.size.toDouble/2.0).toInt)(i => internalSignals(i)).flatMap(x => x)

  }
 
  
  class NormalRow(override val name:String,
                       override val nodes:List[AdderTree.Node],
                       override val internal:FixedType,
                       override val level:Int,
                       override val first:Boolean) extends Row(name,nodes,internal,level,first){
    
	  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      return createAdder(writer)
    }

  }
  
  class FlopRow(val clk:ClockControl,
                     override val name:String,
                     override val nodes:List[AdderTree.Node],
                     override val internal:FixedType,
                     override val level:Int,
                     override val first:Boolean) extends Row(name,nodes,internal,level,first){

    override def flopSegments:(List[SimpleFlopList.Segment],List[SimpleFlopList.Segment]) = {
      val res = new ListBuffer[SimpleFlopList.Segment]
      val ena = new ListBuffer[SimpleFlopList.Segment]
      for (i <- 0 until nodes.size/2) {
        res.append(new SimpleFlopList.Segment(regSignal(i),None))
        ena.append(new SimpleFlopList.Segment(regSignal(i),Some(outSignal(i))))
      }
      if (nodes.size % 2 == 1) {
        val index = math.ceil(nodes.size/2).toInt
        res.append(new SimpleFlopList.Segment(regSignal(index),None))
        ena.append(new SimpleFlopList.Segment(regSignal(index),Some(outSignal(index))))
      }
      //val flop = new SimpleFlopList(None,clk,res.toList,ena.toList)

      return (res.toList,ena.toList)

    }

    def createFlop(writer:CodeWriter):SegmentReturn = {
      val res = new ListBuffer[SimpleFlopList.Segment]
      val ena = new ListBuffer[SimpleFlopList.Segment]
      for (i <- 0 until nodes.size/2) {
        res.append(new SimpleFlopList.Segment(regSignal(i),None))
        ena.append(new SimpleFlopList.Segment(regSignal(i),Some(outSignal(i))))
      }
      if (nodes.size % 2 == 1) {
        val index = math.ceil(nodes.size/2).toInt
        res.append(new SimpleFlopList.Segment(regSignal(index),None))
        ena.append(new SimpleFlopList.Segment(regSignal(index),Some(outSignal(index))))
      }
      val flop = new SimpleFlopList(None,clk,res.toList,ena.toList)

      val commentRet = writer.createCode(new Comment.SingleLine("Stage " + level + " Registers"))
      val flopRet = writer.createCode(flop)
      return SegmentReturn.combine(List(commentRet) ::: List(flopRet),List())
   }
   override def getRealOutputSignal(x:Int) = regSignal(x)

   override def internalSignals(x:Int):List[SignalTrait] = List(regSignal(x),outSignal(x))

   override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      val code = List(createAdder(writer))
      return SegmentReturn.combine(code,List())
   }

  }

  /** Class which defines the main segment for adding the terms together
   *
   *  @constructor
   *  @param name Name of the Addition Segment
   *  @param in1 Input Signal
   *  @param in2 Optional second input signal
   *  @param out Output Signal
   *  @param internal Internal Width for the condition
   *  @param first First
   *
   **/
  class Segment(override val name:String,val in1:Node,val in2:Option[Node],
                val out:SignalTrait,val internal:FixedType,val first:Boolean) extends SimpleSegment {

    override def createCode(implicit writer:CodeWriter):SegmentReturn = {
      /*
      val seg:SimpleSegment = in2 match {
        case None    =>
          val addSegment = new AdditionSegment.RoundClip(name,List(in1.firstTerm(internal,first)),out.fixed,Some(internal))
          new Statement.Assign(out,addSegment)
        case Some(x) =>
          val addSegment = new AdditionSegment.RoundClip(name,List(in1.firstTerm(internal,first),x.secondTerm(internal,first)),out.fixed,Some(internal))
          new Statement.Assign(out,addSegment)
      }
      return writer.createCode(seg)
      */
      null
    }


  }
   
}



