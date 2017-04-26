package com.simplifide.generate.blocks.statemachine2

import com.simplifide.generate.signal.{ParameterTrait, SignalTrait}
import com.simplifide.generate.blocks.basic.condition.{NewCaseStatement, ConditionStatement}
import com.simplifide.generate.blocks.basic.state.Always
import com.simplifide.generate.generator._
import com.simplifide.generate.blocks.basic.flop.{SimpleFlopList, ClockControl}
import com.simplifide.generate.blocks.statemachine2.StateMachine.Both


/**
  * StateMachine Machine Definition
  */

trait StateMachine extends ComplexSegment  {

  implicit val clk:ClockControl

  val states:List[State]
  val current:SignalTrait



}

object StateMachine {
  def apply(states:List[State],current:SignalTrait,next:SignalTrait = null)(implicit clk:ClockControl) = {
    if (next == null) new Impl(states,current)
    else new Both(states,current,next)
  }
    

  class Both(val states:List[State],
    val current:SignalTrait,
    val next:SignalTrait)(implicit val clk:ClockControl) extends StateMachine  {
    def createBody {
      // Create the transition related to a state
      def createState(state:State) = {
        def createIf(transition:Transition) = $ifo(transition.condition) $then  (this.next ::= transition.destination.parameter)
        // Creates the case condition
        val ifClauses = state.transitions.map(x => createIf(x))
        $cases(state.parameter) $then ( // Creates the state block
          if (ifClauses.length > 0) (ifClauses.reduceLeft(_+_) $else (this.next ::= this.current)).create else SimpleSegment.Empty
          )
      }

      this.signal(states.map(_.parameter)) // Append the parameters to this code segment
      // Create a Case ParserStatement containing the transitions
      /- ("Transition Statements")
      $always_body (
        this.current $match (
          states.map(createState(_))
          )
      )
      /- ("StateMachine Machine Body")
      $always_clk(clk) (this.current ::= this.next)

    }
  }
  
  class Impl(val states:List[State],val current:SignalTrait)(implicit val clk:ClockControl) extends StateMachine  {
    def createBody {
      // Create the transition related to a state
      def createState(state:State) = {
        def createIf(transition:Transition) = $ifo(transition.condition) $then  (this.current ::= transition.destination.parameter)
        // Creates the case condition
        val ifClauses = state.transitions.map(x => createIf(x))
        $cases(state.parameter) $then ( // Creates the state block
          if (ifClauses.length > 0) ifClauses.reduceLeft(_+_).create else SimpleSegment.Empty
          )
      }

      this.signal(states.map(_.parameter)) // Append the parameters to this code segment
      // Create a Case ParserStatement containing the transitions
      /- ("StateMachine Machine FunctionBody")
      $always_clk(clk) (
        this.current $match (
          states.map(createState(_))
          )
      )

    }
  }
  
  
}
  /*
  val states = model.groups.keys.toList.sortBy(_.index)
  val params:List[SignalTrait] = states.map(x => ParameterTrait.Decimal(x.name,x.index.toString)).toList



  def fsmStatememt:SimpleSegment = {
    def caseItem(state:StateMachine,transitions:List[StateMachine.Transition]) = {
      val tran = transitions.map(x => (OptionExpression2OptionSegment(x.expr),List(new SimpleStatement.Reg(this.next,BasicSegments.Identifier(x.destination.name)))))
      val condition = ConditionStatement(tran)
      NewCaseStatement.Item(BasicSegments.Identifier(state.name),condition)
    }

    val states = model.groups.map(x => (x._1,x._2)).toList.sortBy(x => x._1.index)
    val states2 = states.map(x => caseItem(x._1,x._2))
    val cas  = new NewCaseStatement(this.current,states2)
    new Always.Star(None,cas,List())
  }

  def actionStatement:SimpleSegment = {
    val expressions = states.map(x => NewCaseStatement.Item(BasicSegments.Identifier(x.name),BasicSegments.BeginEnd(x.expressions)))
    val cas    = new NewCaseStatement(this.next,expressions)
    new Always.Star(None,cas,List())
  }

  override def createCode(implicit writer:CodeWriter):SegmentReturn = {
    // Create the StateMachine Diagram
    val flop = SimpleFlopList.simple(clk,current,next) // StateMachine Flop
    SegmentReturn.combine(writer,List(flop,this.fsmStatememt),this.params)
  }
  */
