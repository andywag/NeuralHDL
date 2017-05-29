package com.simplifide.generate.signal.sv

import com.simplifide.generate.signal.SignalTrait

/**
  * Created by andy on 5/27/17.
  */
trait SignalInterface {
  val name:String
  val inputs:Seq[SignalTrait]         = Seq()
  val outputs:Seq[SignalTrait]        = Seq()
  val interfaces:Seq[SignalInterface] = Seq()

  def allInputs:Seq[SignalTrait]  = inputs.toList ::: interfaces.flatMap(_.allInputs).toList
  def allOutputs:Seq[SignalTrait] = outputs.toList ::: interfaces.flatMap(_.allOutputs).toList


  // FIXME : Cut and pasted all over
  def appendName(iname:String) =
    (this.name.replace(".","_") + s"_$iname").replace("__","_")


  def signals:List[SignalTrait] = {
    allInputs.map(_.asInput).toList  ::: allOutputs.map(_.asOutput).toList
  }
  def reverse:List[SignalTrait]  = {
    allOutputs.map(_.asInput).toList ::: allInputs.map(_.asOutput).toList
  }

}

object SignalInterface {

}
