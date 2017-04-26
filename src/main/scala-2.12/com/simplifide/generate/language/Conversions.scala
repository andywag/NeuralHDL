package com.simplifide.generate.language

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.LanguageFactory.ExpressionConversion
import scala.{Some, None}
import com.simplifide.generate.html.Description
import com.simplifide.generate.parameter.Parameter
import com.simplifide.generate.parser.model._
import com.simplifide.generate.signal._

class Conversions {

}

/**
 * Object containing convenience conversions
 */
object Conversions {

  /*
  implicit def Signal2SignalTrait(signal:Signal):SignalTrait = {
    if (signal.isInstanceOf[SignalTrait]) signal.asInstanceOf[SignalTrait]
    else null
  }
  */

  /*
  implicit def OptionExpression2OptionSegment(expression:Option[Expression]):Option[SimpleSegment] = {
    expression match {
      case Some(x) => Some(Expression2Segment(x))
      case _       => None
    }
  }
  */

  // TODO This should be simply handled by expression create which doesn't work
  /*implicit def Expression2Segment(expression:Expression):SimpleSegment = {
    //expression.create
    if (expression.isInstanceOf[SimpleSegment]) expression.asInstanceOf[SimpleSegment]
    else new ExpressionConversion(expression)
  }
  */

  implicit def Clock2FlopControl(clock:Clock):ClockControl = {
    if (clock.isInstanceOf[ClockControl]) clock.asInstanceOf[ClockControl]
    else ClockControl()
  }

  /*
  implicit def OptionModelFixed2Fixed(fixed:Option[Model.Fixed]):Option[FixedType] = {
    fixed match {
      case None    => None
      case Some(x) => if (x.isInstanceOf[FixedType]) Some(x.asInstanceOf[FixedType])
                      else if (x == Model.NoFixed) Some(FixedType.Simple)
                      else Some(FixedType.signed(x.width,x.fraction))
    }
  }


  implicit def ModelFixed2Fixed(x:Model.Fixed):FixedType = {
    if (x.isInstanceOf[FixedType]) (x.asInstanceOf[FixedType])
    else if (x == Model.NoFixed) (FixedType.Simple)
    else (FixedType.signed(x.width,x.fraction))
  }


  implicit def SignalType2OpType(op:SignalType):OpType = {
    if (op.isInstanceOf[OpType]) op.asInstanceOf[OpType]
    else OpType.Signal
  }
  */

  implicit def Double2Expression(value:Double):NewConstant = NewConstant(value)
  //implicit def Int2Expression(value:Int):Constant = Constant(value.toDouble)

  implicit def ListExpression2ListSegment(expressions:List[Expression]) = {
    expressions.map(_.asInstanceOf[SimpleSegment])
  }

  // Description Conversions
  implicit def String2Description(str:String) = Description(str)
  implicit def Xml2Description(str:xml.Elem) = Description(str)

  // Parameter Conversions
  implicit def Parameter2Value[T](parameter:Parameter[T]):T = parameter.get
  implicit def Value2Parameter[T](value:T):Parameter[T] = Parameter[T]("",value)




}