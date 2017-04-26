package com.simplifide.generate.blocks.basic.operator

import com.simplifide.generate.signal.{SignalTrait, FixedType}
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}

/**
 * Class which defines a bit select operation signal[top:bot]
 *
 * @constructor
 * @parameter signal Input Signal
 * @parameter top Top Bit of the Expression
 * @parameter bot Bottom Bit of the Expression
 * @parameter floor Floor of the Expression - Sets lowest level that the bottom bit can be set to
 *
 */

class Select(val signal:SimpleSegment,
             val top:Option[Int],
             val bot:Option[Int],
             val floor:Int = 0) extends SimpleSegment {

  def this(signal:SimpleSegment,top:Option[Int], bot:Option[Int]) {
    this(signal,top,bot,0)
  }
	
  override val fixed:FixedType = {
	  top match {
	 	  case None    => FixedType.unsigned(1,0)//new FixedType.Main(Signing.UnSigned,1,0)
	 	  case Some(x) => bot match {
	 	 	  case None    => FixedType.unsigned(1,0)
	 	 	  case Some(y) => FixedType.unsigned(x-y,0)
        case _     =>  FixedType.unsigned(1,0)
	 	  }
	  }

  }


  // TODO Clean up this code
  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    
    top match {
      case None => return writer.createCode(signal)// No Selection 
      case Some(x) => bot match {
          case None    => {
           val builder = new StringBuilder();
           builder.append(writer.createCode(signal).code)
           builder.append("[")
           builder.append(x.toString)
           builder.append("]")
           return SegmentReturn(builder.toString)
          }
          case Some(y) => 
            val builder = new StringBuilder();
            if (x < signal.fixed.width) { // No Sign Extension
               if (y < floor) { // Zero Pad
                 builder.append("{")
                 builder.append(writer.createCode(signal).code)
                 builder.append("[")
                 builder.append(x.toString)
                 builder.append(":"); builder.append(floor); builder.append("]")

                 builder.append(",")
                 builder.append((floor-y).toString)
                 builder.append("'d0")
                 builder.append("}")
               }
               else if (y >= 0) { // Truncate
                  builder.append(writer.createCode(signal).code)
                  builder.append("[")
                  builder.append(x.toString)
                  builder.append(":")
                  builder.append(y.toString)
                  builder.append("]")
               }
            }
            else { // Sign Extension
              builder.append("{")
              if (signal.fixed.isSigned) { // Signed Solution
                builder.append("{")
                builder.append((x-signal.fixed.width+1).toString)
                builder.append("{")
                builder.append(signal.name)
                builder.append("[")
                builder.append((signal.fixed.width-1).toString)
                builder.append("]")
                builder.append("}")
                builder.append("}")
              }
              else { // Unsigned Solution
                builder.append( (x-signal.fixed.width).toString)
                builder.append("'d0");
              }
              builder.append(",")
              builder.append(writer.createCode(signal).code)
              if (y < 0) { // Zero Pad
                 builder.append(",")
                 builder.append((-y).toString)
                 builder.append("'d0")
               }
               else if (y > 0) { // Truncate
                  //builder.append(appendSignal.createVerilogCode(context).code)
                  builder.append("[")
                  builder.append((signal.fixed.width-1).toString)
                  builder.append(":")
                  builder.append(y.toString)
                  builder.append("]")
               }
              builder.append("}")
            }
            SegmentReturn(builder.toString)
            
      }
    }
    
  }
  
}

/** Factory Methods to create a Select Block */
object Select {

  def apply(state:SignalTrait,top:Int):Select = new Select(state,Some(top),None)
  /** Factory Constructor for Select Block */
  def apply(state:SignalTrait,top:Int,bot:Int):Select = new Select(state,Some(top),Some(bot))
   /** Factory Constructor for Select Block base on range*/
  def apply(state:SignalTrait,range:Range):Select = new Select(state,Some(range.top),Some(range.bottom))

  /** Creates the Sign of the input signal */
  def sign(state:SignalTrait) = Select(state,state.fixed.width-1)

  class Range(val top:Int, val bottom:Int)

}

