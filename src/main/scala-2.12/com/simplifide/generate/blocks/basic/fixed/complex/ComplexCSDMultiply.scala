package com.simplifide.generate.blocks.basic.fixed.complex





/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
import com.simplifide.generate.blocks.basic.flop.ClockControl
import collection.mutable.ListBuffer
import com.simplifide.generate.signal.complex.{ComplexConstant, ComplexSignal}
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.signal.{Constant, SignalTrait, FixedType}
import com.simplifide.generate.blocks.basic.fixed.AdderTree

class ComplexCSDMultiply(override val name:String,
                         clk:ClockControl,
                         csd:ComplexConstant, 
                         signal:ComplexSignal,
                         output:ComplexSignal,
                         internal:FixedType,
                         delays:Int,
                         flop:Boolean = false) extends SimpleSegment{

  val realCSD    = csd.getRealConstant
  val imagCSD    = csd.getImagConstant
  val negImagCSD = csd.getNegativeImagConstant
  /** Creates a Comment which identifies what is happening with the CSD Multiplication */
  private def createCSDComment(constant:Constant):String = {
    val builder = new StringBuilder
    val csdR:List[Constant.CSD] = constant.createCSD
    builder.append(constant.value)
    builder.append("[")
    for (csd <- csdR) {
      builder.append(csd.debugString);
      builder.append(" ");
    }
    builder.append("]")

    return builder.toString
  }

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    val segments = new ListBuffer[SimpleSegment]()
    segments.append(new Comment.SingleLine("Complex Multiplication [" + realCSD.debugCSDString + "+ j" + imagCSD.debugCSDString + "]"))
    // Real Multiplication
    val realAdd  = List(new AdderTree.Value(realCSD,signal.real),new AdderTree.Value(negImagCSD,signal.imag))
    segments.append(new AdderTree(name+"_real",clk,output.real,realAdd,internal,delays,flop))
    // Imaginary Multiplication
    val imagAdd  = List(new AdderTree.Value(realCSD,signal.imag),new AdderTree.Value(imagCSD,signal.real))
    segments.append(new AdderTree(name+"_imag",clk,output.imag,imagAdd,internal,delays,flop))

    return SegmentReturn.combine(writer,segments.toList,List())
  }


  
}

*/