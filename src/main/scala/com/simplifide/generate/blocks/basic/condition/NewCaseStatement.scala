package com.simplifide.generate.blocks.basic.condition

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.simplifide.generate.util.StringOps
import com.simplifide.generate.parser.model.Expression
import com.simplifide.generate.generator._


/** Case ParserStatement which doesn't include the head ie
 *  case (_)
 *
 *  endcase
 *
 *  @constructur
 *  @parameter condition Condition for the case statement
 *  @parameter statements List of statements for this case statement -- Should be NewCaseStatement.Item
 *
 **/
class NewCaseStatement(val condition:SimpleSegment, val statements:List[SimpleSegment]) extends SimpleSegment{

  override def outputs = statements.flatMap(_.outputs)

  /*
  override def createSplit = {
    statements.zipWithIndex.map(x => x._1.createIndividualSplit(null,x._2))
  }
  */
  
  override def createVector:List[SimpleSegment] =   
    List(new NewCaseStatement(condition,statements.flatMap(_.createVector)))
  
  
  def createCode(implicit writer:CodeWriter):SegmentReturn =  {
    def body = if (statements.size > 0) statements.map(x => writer.createCode(x)).reduceLeft(_+_)
      else SegmentReturn("")
    SegmentReturn("case(") + writer.createCode(condition) + ")\n" ++ body + "endcase\n"
  }


}


/** Factory Methods and classes used for the case construction */
object NewCaseStatement {

  /** Case ParserStatement Constructor */
  def apply(condition:SimpleSegment, statements:List[SimpleSegment]) = {
    new NewCaseStatement(condition,statements)
  }



  class Item(val condition:Option[SimpleSegment],result:SimpleSegment) extends SimpleSegment {

    override def outputs = result.outputs

    override def createVector:List[SimpleSegment] =
      List(new NewCaseStatement.Item(condition,result.createVectorSingle))


    def createCode(implicit writer:CodeWriter):SegmentReturn = {
      def conditionExpression = writer.createCode(condition.getOrElse(BasicSegments.Identifier("default")))

      val res = result match {
        case x:BasicSegments.ListSegment => new BasicSegments.BeginEnd(x.segments)
        case _                           => result
      }
      conditionExpression + " : " + writer.createCode(res)
    }



  }

  /** Factory Methods for Creating a case Item */
  object Item {
    
    def apply(condition:Option[Expression],result:Expression) = 
      condition match {
        case Some(x) => new Item(Some(x.asInstanceOf[SimpleSegment]),result.asInstanceOf[SimpleSegment])
        case None    => new Item(None, result.asInstanceOf[SimpleSegment])
      }
    /** Method to create a case item from an condition and a result */
    def apply(condition:SimpleSegment, result:SimpleSegment) = new Item(Some(condition), result)

  }

}

