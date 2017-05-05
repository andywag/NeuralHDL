package com.simplifide.generate.signal

import com.simplifide.generate.generator.{CodeWriter, SegmentReturn, SimpleSegment}
import com.simplifide.generate.parser.model.Clock
import com.simplifide.generate.html.Description
import com.simplifide.generate.language.DescriptionHolder
import com.simplifide.generate.parser.SegmentHolder
import com.simplifide.generate.proc.Controls
import com.simplifide.generate.blocks.basic.operator.{Operators, Select}

import collection.mutable.ListBuffer
import com.simplifide.generate.proc.parser.ProcessorSegment
import com.simplifide.generate.blocks.basic.memory.Memory
import com.simplifide.generate.blocks.basic.fixed.{FixedOperations, FixedSelect}
import com.simplifide.generate.parser.items.MiscParser
import com.simplifide.generate.signal.sv.Struct
//import org.scalatest._

/**
 * Trait describing a signal
 */

trait SignalTrait extends SimpleSegment with DescriptionHolder with Controls with SignalCreator {

  override val name:String
  /** Fixed type of signal */
  val fixed:FixedType
  /** Length of array if this is an array */
  val arrayLength = 0

  /** Method which an indexing of a variable from a clk. Called from the parser x[n-k] */
  def apply(clk:Clock):SimpleSegment = if (clk.delay == 0) this else child(clk.delay)
  /** Returns this variable indexed by the input signal */
  def apply(signal:SignalTrait) = Operators.Slice(this,signal)
  /** Method for indexing a variable. Called from teh parser x[n] */
  override def apply(index:Int):SignalTrait =
    if (this.numberOfChildren > 0) child(index).asInstanceOf[SignalTrait] else new SignalSelect(this,index,index)
  /** Creates a slice of a signal */
  override def apply(index:(Int,Int)) = new SignalSelect(this,index._1,index._2)
  override def apply(width:MiscParser.Width) =  new SignalSelect(this,width.top,width.bottom)

  override def apply(fixed:FixedType):SimpleSegment = FixedSelect(this,fixed)
  /** Creates a New Signal (Virtual Constructor) */
  def newSignal(name:String = this.name,
    opType:OpType = this.opType,
    fix:FixedType = this.fixed):SignalTrait

  override def toString = name

  def createArray(iName:String, len:Int, opType:OpType = OpType.Register) = {
    SignalTrait(appendName(iName),opType,FixedType.unsigned(width,0),len)
  }

  def createSignal:SignalTrait = this
  def createSignal(opType:OpType):SignalTrait = this.copy(optype = opType)
  def createSignal(name:String, opType:OpType):SignalTrait = this.copy(nam = name,optype = opType)

  override def outputs = this.allSignalChildren

  /** Convenience method for specifying the width */
  lazy val width = fixed.width

  def connect(signal:SignalTrait):Map[SignalTrait,SignalTrait] = Map(this -> signal)

  /** Compares this signal to the input signal. True if same type and name*/
  def generalEquals(signal:SimpleSegment):Boolean = {
    if (signal.isInstanceOf[SignalTrait]) (this.baseSignal.name == signal.asInstanceOf[SignalTrait].baseSignal.name)
    else false
  }

  def baseSignal = this



  override def createSubOutput(index:Int):SignalTrait = SignalTrait(name + "_" + index, opType, fixed)


  /** Changes the type for a testbench addition */
  def changeTestType:SignalTrait = {
    this match {
      case x:Struct => x.copyStruct(this.name,OpType.Struct)
      case _        => SignalTrait(this.name,this.opType.testType,this.fixed)
    }
  }
  /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  def changeType(typ:OpType):SignalTrait = SignalTrait(this.name,typ,this.fixed)
  /** Reverses the connection for this block */
  def reverseType:SignalTrait = SignalTrait(this.name,this.opType.reverseType,this.fixed)

  def asInput  = changeType(OpType.Input)
  def asOutput = changeType(OpType.Output)
  def asRegOut = changeType(OpType.RegOutput)

  def allSignalChildren:List[SignalTrait] = this.allChildren.map(_.asInstanceOf[SignalTrait])
  /** Create Slice is used for creating the variables in an array whereas slice is
    * used to get the variables. There may be a subtle difference between the 2 methods. Creation of the slice is called
    * when creating the children slice is called on the actual exp
    */
  def createSlice(index:Int, prefix:String = ""):SignalTrait = {
    val cop = this.newSignal(this.name + "_" + prefix + index)
    cop.description = this.description
    cop
  }
  /** Creates the subsignal associated with this vector index */
  def slice(index:Int):SimpleSegment  = this
  /** Returns all of the children associated with this vector. This method only works on the vector portion
    * of the operation */
  override def children:List[SignalTrait] = List()
  /** Create a list of appendSignal declarations for this appendSignal. This will expand the vector into a larger set of signals */
  def copy(nam:String=this.name,optype:OpType=this.opType,fix:FixedType=fixed):SignalTrait = {
    val cop = newSignal(nam,optype,fix)
    cop.description = this.description
    cop
  }

  def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn(name)

  /** Convenience Operation to find the sign of the signal */
  def sign:SimpleSegment = Select.sign(this)


 
  /** Convenience Method for specifying whether the signal is an input or output */
  def isIo = this.isInput | this.isOutput | (this.opType == OpType.InOut)
  /** Method which defines if the signal is an input  */
  def isInput  = opType.isInput//(opType == OpType.Input)
  /** Method which defines if the signal is an output */
  def isOutput = opType.isOutput
  /** Method which defines if the signal is an input  */
  def isParameter  = opType.isParameter
  /** Method which defines if the signal is an output */
  def isWire = opType.isWire

  /** Method which defines if the signal is an output */
  override def isReg = opType.isReg



  /** TODO : Copy of Control Match ... */
  override def createControl(actual:SimpleSegment,statements:ProcessorSegment,index:Int):List[Controls.Value] = {

    this.assignment match {
      case None    => return actual.createControl(null,null,index)
      case Some(x) => x.createControl(actual,statements,index)
    }
  }


  override def controlMatch(actual:SimpleSegment,statements:ProcessorSegment):Boolean = {
    if (actual.isInstanceOf[SignalTrait]) return this.name == actual.name

    val state = statements.getStatement(this)
    state match {
      case None    => false
      case Some(x) => x.input.controlMatch(actual,statements)
    }

  }

  

}
/**
 * Factory methods for creating new signals
 */
object SignalTrait {

  def apply(name:String,optype:OpType = OpType.Signal, fixed:FixedType=FixedType.Simple,depth:Int=0) = new Signal(name,optype,fixed,depth)

  /**
   * Default implementation of SignalTrait
   */
  class Signal(override val name:String,
    override val opType:OpType,
    override val fixed:FixedType,
    override val arrayLength:Int = 0) extends SignalTrait {


    override def newSignal(name:String = this.name,
      opType:OpType = this.opType,
      fixed:FixedType = this.fixed):SignalTrait = new Signal(name,opType,fixed)

    override def slice(index:Int):SimpleSegment = {
      if (this.numberOfChildren == 0) {
        Select(this,index,index)
      }             // Kind of a kludge shouldn't be required
      else new Signal(name + "_" + index,opType,fixed)
    }
  }
  
  class SignalPath(val path:String, val base:SignalTrait) extends SignalTrait {
    override def createCode(implicit writer:CodeWriter):SegmentReturn = SegmentReturn(path + "." + base.name)
    def newSignal(name:String = this.name,
                  opType:OpType = this.opType,
                  fix:FixedType = this.fixed):SignalTrait = this

  }



  /** Convenience method for creating a unique set of signals */
  def uniqueSignals(signals:List[SignalTrait]):List[SignalTrait] = {
      val sortedSignals = signals.sortBy(_.name)
      val builder = new ListBuffer[SignalTrait]()
      for (signal <- sortedSignals) {
        if (builder.length == 0) builder.append(signal)
        else if (!signal.name.equalsIgnoreCase(builder(builder.length-1).name)) {
          builder.append(signal)
        }
      }
      builder.toList
    }




}
