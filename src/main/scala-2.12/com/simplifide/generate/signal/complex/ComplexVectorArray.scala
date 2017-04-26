package com.simplifide.generate.signal.complex

import com.simplifide.generate.blocks.basic.operator.Select
import collection.mutable.ListBuffer
import com.simplifide.generate.signal._
import com.simplifide.generate.blocks.basic.misc.Comment
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 1/25/11
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */

/** Compressed Way to Deal with Complex Signals By Storing them in the same appendSignal */
/*
class ComplexVectorArray(override val name1:String,override val opType:OpType,val ifixed:FixedType,val len:Int)
  extends SignalTrait {


  override val fixed = FixedType.unsigned(2*ifixed.width*len,0)
  def newSignal(nam:String,optype:OpType,fix:FixedType):SignalTrait = this

  /*SignalNew(name1,opType,
                    new FixedType.Main(Signing.UnSigned,2*ifixed.width*len,0),VectorType.NoVector)  {
   */

  //override def getRealFixedType:FixedType = ifixed;

  //def createInternalSignal(opType:OpType):ComplexSignal =
  //    ComplexSignal.newComplex(name1+"_internal",opType,fixed,len)


  def getBottomIndex(index:Int):Int = {
    val opindex = len - index - 1
    val twid = 2*ifixed.width
    opindex*twid
  }

  def getRealSlice(index:Int):Select = {
    val bot = getBottomIndex(index)
    new Select(this,Some(bot+2*ifixed.width-1),Some(bot+ifixed.width))
  }

  def getImagSlice(index:Int):Select = {
    val bot = getBottomIndex(index)
    new Select(this,Some(bot+ifixed.width-1),Some(bot))
  }

  //override def copyWithType(op:OpType):ComplexVectorArray =
  //  new ComplexVectorArray(this.name1,op,ifixed,len)

  /** Get the indexes of the real appendSignal at this index */
  private def getRealIndexes(index:Int):(Int,Int) = {
    val bot = getBottomIndex(index)
    return (bot+2*ifixed.width-1,bot+ifixed.width)
  }
  /** Get the indexes of the real appendSignal at this index */
  private def getImagIndexes(index:Int):(Int,Int) = {
    val bot = getBottomIndex(index)
    return (bot+ifixed.width-1,bot)
  }

  /** Returns a select based on the slice of the real appendSignal */
  def getRealSliceSelect(index:Int,top:Int,bot:Int):Select =  {
    val indexes = getRealIndexes(index)
    val to =  indexes._1 - (this.ifixed.width-1 - top)
    val bo =  indexes._2 + bot
    new Select(this,Some(to),Some(bo), indexes._2)
  }

  /** Returns a select base on the slice of the imaginary appendSignal */
  def getImagSliceSelect(index:Int,top:Int,bot:Int):Select =  {
    val indexes = getImagIndexes(index)
    val to =  indexes._1 - (this.ifixed.width-1 - top)
    val bo =  indexes._2 + bot
    new Select(this,Some(to),Some(bo), indexes._2)
  }

}


object ComplexVectorArray {

   def newSignal(name1:String,opType:OpType,fixed:FixedType,len:Int):ComplexVectorArray =
      new ComplexVectorArray(name1,opType,fixed,len)



	 def bitReverse(value:Int,num:Int):Int = {
    // Kludgey way of handling this. Add a one to the top bit
    // AND the remove it to make sure all the bits are created with toBinaryString
    val sh  = value + math.pow(2.0,num).toInt
    val bits = sh.toBinaryString.substring(1)
    var nvalue:Int = 0;
    for (i <- 0 until num) {
      val bit = bits.substring(i,i+1)
      if (bit == "1") nvalue += math.pow(2.0,i).toInt
    }
    return nvalue
  }
	
  class ArrayToComplex(val array:ComplexVectorArray,val complex:ArrayTrait[ComplexSignal]) extends SimpleSegment {

    def createStatement(appendSignal:SignalTrait,select:Select):SimpleSegment =
        new SimpleStatement.Assign(appendSignal,select)

    /** Return the real appendSignal at the given index */
    def getRealSelect(index:Int):Select = {
      val com = complex.slice(index) // Get the Complex Number
      val wid = com.fixed.width                          // Width of the Complex Number
      val topR = array.fixed.width-1 - index*2*com.fixed.width  // Index of Select
      new Select(array,Some(topR),Some(topR - wid+1))
    }
    /** Return the imaginary appendSignal at the given index */
    def getImagSelect(index:Int):Select = {
      val com = complex.slice(index) // Get the Complex Number
      val wid = com.fixed.width                          // Width of the Complex Number
      val topR = array.fixed.width-1 - index*2*com.fixed.width  // Index of Select
      new Select(array,Some(topR-wid),Some(topR-2*wid+1))
    }

    def getAddress(index:Int):Int = index

    override def createCode(writer:CodeWriter):SegmentReturn = {
      val states = new ListBuffer[SimpleSegment]()
      states.append(new Comment.SingleLine("Convert the input to the individual complex signals"))

      for (i <- 0 until complex.numberOfChildren) {
        val com = complex.slice(i) // Get the Complex Number
        /*val wid = com.getFixedType.width                          // Width of the Complex Number
        val topR = array.getFixedType.width-1 - i*2*com.getFixedType.width  // Index of Select
        val selR = new Select(array,Some(topR),Some(topR - wid+1))
        val selI = new Select(array,Some(topR-wid),Some(topR-2*wid+1)) */
        val stR = this.createStatement(com.real,getRealSelect(getAddress(i)))
        val stI = this.createStatement(com.imag,getImagSelect(getAddress(i)))
        states.append(stR)
        states.append(stI)
      }
      SegmentReturn.combine(states.toList.map(x => writer.createCode(x)),List())
    }


  }

  class ComplexToArray(override val array:ComplexVectorArray,override val complex:ArrayTrait[ComplexSignal]) extends ArrayToComplex(array,complex) {
       override def createStatement(appendSignal:SignalTrait,select:Select):SimpleSegment =
          new SimpleStatement.Assign(select,appendSignal)
  }

    class ComplexToArrayBitReverse(override val array:ComplexVectorArray,override val complex:ArrayTrait[ComplexSignal]) extends ComplexToArray(array,complex) {
     override def getAddress(index:Int):Int = {
       val ulen = math.log10(array.len)/math.log10(2.0)
       ComplexVectorArray.bitReverse(index,ulen.toInt)
     }
    }


    class BitReverse(val out:ComplexVectorArray,val in:ComplexVectorArray) extends SimpleSegment {
      override def createCode(writer:CodeWriter):SegmentReturn = {
        val states = new ListBuffer[SimpleSegment]()
        states.append(new Comment.SingleLine("Convert the complex outputs to a single appendSignal"))

        for (i <- 0 until out.len) {
          val ulen = math.log10(in.len)/math.log10(2.0)
          val rev  = ComplexVectorArray.bitReverse(i,ulen.toInt)

          val stR = new SimpleStatement.Assign(out.getRealSlice(i),in.getRealSlice(rev))
          val stI = new SimpleStatement.Assign(out.getImagSlice(i),in.getImagSlice(rev))
          states.append(stR)
          states.append(stI)
        }
      SegmentReturn.combine(states.toList.map(x => writer.createCode(x)),List())
      }
    }



}   */