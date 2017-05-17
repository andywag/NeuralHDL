package com.simplifide.generate.blocks.test

import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.blocks.basic.misc.Lut
import com.simplifide.generate.blocks.basic.operator.{BinaryOperator, AbsoluteValue}
import com.simplifide.generate.blocks.basic.Statement
import com.simplifide.generate.generator.{InternalSignalDeclaration, BasicSegments, SimpleSegment}
import com.simplifide.generate.signal.{NewConstant, Constant, OpType, SignalTrait}
import com.simplifide.generate.parser.factory.CreationFactory

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 9/29/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */



trait CompareTable extends SimpleSegment.Combo {

  val output:SignalTrait
  val condition:SimpleSegment
  val values:List[SimpleSegment]
  val start:Int
  val finish:Int
  val threshold:Int = 0 

  override def create(implicit creator:CreationFactory) = {
    val cop = output.newSignal(output.name + "_comp",OpType.Register)
    val err = output.newSignal(output.name + "_err", OpType.Signal)
    val abs = output.newSignal(output.name + "_abs", OpType.Signal)
    val ind = SignalTrait(output.name + "_ind", OpType.Signal)

    val internal   = InternalSignalDeclaration(cop,err,abs,ind)
    val lut        = Lut(cop,condition,values)
    val delta      = new Statement.Declaration(err,(output - cop).asInstanceOf[SimpleSegment],List(cop,err,abs,ind))
    val absValue   = new Statement.Declaration(abs,new AbsoluteValue(err))
    val absInd     = new Statement.Declaration(ind, BinaryOperator.GT(abs,NewConstant(threshold,abs.fixed)))
    BasicSegments.List(internal,lut,delta,absValue,absInd).create
  }


}

object CompareTable {

  def apply(output:SignalTrait,
    condition:SimpleSegment,
    values:List[SimpleSegment],
    start:Int,
    finish:Int,
    threshold:Int = 0) = new CompareTable.Impl(output,condition,values,start,finish,threshold)

  def apply(output:SignalTrait,
    condition:SimpleSegment,
    creator:Int=>SimpleSegment,
    start:Int,
    finish:Int) = new CompareTable.Impl(output,condition,List.tabulate(finish)(creator(_)),start,finish,0)
  
  class Impl(
    override val output:SignalTrait,
    override val condition:SimpleSegment,
    override val values:List[SimpleSegment],
    override val start:Int,
    override val finish:Int,
    override val threshold:Int = 0 ) extends CompareTable

}