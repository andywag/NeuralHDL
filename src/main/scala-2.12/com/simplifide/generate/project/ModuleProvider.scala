package com.simplifide.generate.project

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import collection.immutable.List._
import com.simplifide.generate.signal.{RegisterTrait, SignalTrait, SignalDeclaration, OpType}
import com.simplifide.generate.language.{ DescriptionHolder, ExtraFile}
import com.simplifide.generate.util.StringOps


/** Trait describing an implementation of the body of a module */
trait ModuleProvider extends SimpleSegment  with DescriptionHolder {
  /** Impl Name */
  val name:String
  /** Signals Contained in this module */
  val signals:List[SignalTrait]
  /** Segments Associated with this module if it is a leaf*/
  val segments:List[SimpleSegment]
  /** New Instance Values associated with an Entity */
  val entityInstances:List[NewEntityInstance[_]]

  /** List of Extra Files associated with this module */
  val extraFiles:List[ExtraFile]
  /** Impl which this is based on */
  val module:Module

  override def toString = name


  /** Create the signal declarations for this module */
  private def createSignalDeclaration(signals:List[SignalTrait])(implicit writer:CodeWriter):String = 
    StringOps.accumulate(signals.flatMap(x => SignalDeclaration(x)).map(x => writer.createCode(x).code))


  /** Creates the flops for addresses defined in the module */
  private def createAutoFlops(writer:CodeWriter):String = {
    val registers = SignalTrait.uniqueSignals(this.signals).filter(x => x.isInstanceOf[RegisterTrait[_]]).map(x => x.asInstanceOf[RegisterTrait[_]])
    StringOps.accumulate(registers.map(x => writer.createCode(x.createFlop).code))
  }

  /** Create the segments for this module */
  private def createSegment(writer:CodeWriter,segment:SegmentReturn):String = {
    val builder = new StringBuilder
    builder.append(segment.code)
    return builder.toString
  }

  
  private def signalDeclarations(returns:List[SegmentReturn])(implicit writer:CodeWriter):String = {
    def typeDeclaration(typ:String, signals:List[SignalTrait]) = {
      "// " + typ + "\n\n" + createSignalDeclaration(signals) + "\n\n"
    }
    val internals = returns.flatMap(x => x.internal).filter(x => !x.isInput && !x.isOutput)
    val allSignals = SignalTrait.uniqueSignals(signals.flatMap(_.allSignalChildren).filter(x => x.opType.isSignal) ::: internals)
    typeDeclaration("Parameters ",allSignals.filter(_.isParameter)) + typeDeclaration("Wires ",allSignals.filter(_.isWire)) +
    typeDeclaration("Registers ",allSignals.filter(_.isReg))
  }

  def internalSignals(implicit writer:CodeWriter = CodeWriter.Verilog) = {
    val returns:List[SegmentReturn] = segments.map(x => writer.createCodeRoot(x)).filter(_ != null)
    returns.flatMap(x => x.internal).filter(x => !x.isInput && !x.isOutput)
  }

  def createCode(implicit writer:CodeWriter):SegmentReturn     = {
    //val builder = new StringBuilder()

    val returns:List[SegmentReturn] = segments.map(x => writer.createCodeRoot(x)).filter(_ != null)
    SegmentReturn(
      this.signalDeclarations(returns) +
      this.createAutoFlops(writer) +
      StringOps.accumulate(this.entityInstances.map(x => writer.createCode(x).code)) +
      StringOps.accumulate(returns.map(x => createSegment(writer,x)))
    )


  }

}

/**
 * Factory methods for Module Provider
 *
 */
object ModuleProvider {

  def apply(name:String,
            module:Module,
            signals :List[SignalTrait],
            segments:List[SimpleSegment],
            entityInstances:List[NewEntityInstance[_]],
            extra:List[ExtraFile] = List()) =
    new Impl(name,module,signals,segments,entityInstances,extra)

  class Impl(override val name:String,
               override val module:Module,
               override val signals:List[SignalTrait],
               override val segments:List[SimpleSegment],
               override val entityInstances:List[NewEntityInstance[_]],
               override val extraFiles:List[ExtraFile]) extends ModuleProvider

}