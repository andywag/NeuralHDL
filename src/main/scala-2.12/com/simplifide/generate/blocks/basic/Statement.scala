package com.simplifide.generate.blocks.basic

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.parser.block.ParserStatement
import com.simplifide.generate.generator.SegmentReturn._
import com.simplifide.generate.parser.ExpressionReturn
import com.simplifide.generate.util.StringOps
import com.simplifide.generate.generator.{BasicSegments, CodeWriter, SegmentReturn, SimpleSegment}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 1/24/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * ProcStatement ParserStatement {output = input}
 *
 * @constructor
 * @parameter output Output of the ParserStatement
 * @parameter input Input of the ParserStatement
 * @parameter extraSignals Extra signals which can be attached to the statement to be added to the module later
 */

trait Statement extends SimpleSegment with ParserStatement  {

  val output:SimpleSegment
  val input:SimpleSegment
  val extraSignals:List[SignalTrait] = List()

  /** Output of this code segment */
  override def outputs:List[SignalTrait] = if (output == null) List() else output.outputs
  /** Attach the input to the output */


  override def toString = "assign " + output + " = " + input




  def copy(output:SimpleSegment = this.output,  input:SimpleSegment = this.input, extraSignals:List[SignalTrait] = this.extraSignals):Statement = {
    this match {
      case x:Statement.Assign          => new Statement.Assign(output,input,extraSignals)
      case x:Statement.Delay           => new Statement.Delay(output,input,x.delay,extraSignals)
      case x:Statement.Reg             => new Statement.Reg(output,input,extraSignals)
      case x:Statement.FunctionBody            => new Statement.FunctionBody(output,input,extraSignals)
      case x:Statement.Declaration     => new Statement.Declaration(output,input,extraSignals)
    }
  }






  /** Creates a new segment */
  private def returnSegment(outSegment:SegmentReturn,inSegment:SegmentReturn,root:Boolean):SegmentReturn = {
    def basic = outSegment + " <= " + inSegment + ";\n"
    def wire  = SegmentReturn("assign ") + outSegment + " = " + inSegment + ";\n"
    def reg   = SegmentReturn("always @* ") + outSegment + " <= " + inSegment + ";\n"
    def rootBase = if (output.isReg) reg else wire

    this match {
      case x:Statement.Assign      => {
        //if (root) rootBase else basic
        rootBase
      }
      case x:Statement.Reg         => {
        if (root) rootBase else basic
        //rootBase
      }
      case x:Statement.Delay       => SegmentReturn("assign #") + x.delay.toString + " "  + outSegment + " = " + inSegment + ";\n"
      case x:Statement.FunctionBody        => outSegment + " = " + inSegment + ";\n"
      case x:Statement.Declaration => "wire " + x.output.fixed.declaration + " " + x.output.name + " = " + inSegment + ";\n"
    }
  }
    
  
  override def createVector = {
    if (this.output.numberOfChildren <= 0) List(this)
    else (output.children zip input.children(output)).map(x => this.copy(x._1,x._2)).toList
  }

  /*
  override def createIndividualSplit(output:SimpleSegment, index:Int = 0) = {
    val split = input.createIndividualSplit(output,0)
    List(this.copy(this.output,split._1)) ::: split._2
  }
  */

  override def createSplit:List[SimpleSegment] = {
    val split = input.createIndividualSplit(output,0)
    List(this.copy(output,split._1)) ::: split._2
  }
    


  
  
  def createCode(root:Boolean)(implicit writer:CodeWriter):SegmentReturn = {
    val inC  = writer.createCode(input)
    val outC = writer.createCode(output)
    val ret =  returnSegment(outC,inC,root)
    // TODO convert the extra to actual statements
    //val state = inC.extra.map(_.asInstanceOf[Statement]).map(x => new Statement.Declaration(x.output,x.input))
    val results = inC.extra.map(x => writer.createCode(x)).foldLeft(SegmentReturn(""))(_+_)
    this match {
      case x:Statement.Reg => {
//        //ret.extra(state)
        results + ret
      }
      case _               => {
        //val code = StringOps.accumulate(state.map(writer.createCode(_).code))
        //(SegmentReturn( code) + ret.code).extra(List(),inC.internal)
        results + ret
      }
    }
  }

  override def createCode(implicit writer:CodeWriter):SegmentReturn = createCode(false)
  override def createCodeRoot(implicit writer:CodeWriter):SegmentReturn = createCode(true)


}

/** Methods and classes for creating statements */
object Statement {

  /** Assign ParserStatement used for a Wire */
  class Assign(override val output:SimpleSegment,
    override val input:SimpleSegment,
    override val extra:List[SignalTrait] = List()) extends Statement


  /** ParserStatement Used inside AlwaysBlock Block */
  class Reg(override val output:SimpleSegment,
    override val input:SimpleSegment,
    override val extra:List[SignalTrait] = List()) extends Statement


  /** ParserStatement used inside the body of a function or initial statement */
  class FunctionBody(override val output:SimpleSegment,
    override val input:SimpleSegment,
    override val extra:List[SignalTrait] = List()) extends Statement

  class Declaration(
    override val output:SimpleSegment,
    override val input:SimpleSegment,
    override val extra:List[SignalTrait] = List()) extends Statement

  /** Assign ParserStatement used for a Wire */
  class Delay(override val output:SimpleSegment,
               override val input:SimpleSegment,
               val delay:Int,
               override val extra:List[SignalTrait] = List()) extends Statement


}