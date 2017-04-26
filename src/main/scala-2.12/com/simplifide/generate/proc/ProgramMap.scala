package com.simplifide.generate.proc

/**
 * Map of control signals
 */
class ProgramMap(val baseInstruction:Instruction,
                 val programMap:Map[Controls,List[Controls.Value]],
                 val length:Int) {

  val instructions:List[Instruction] = {
    val values = programMap.values.flatten.groupBy(_.index)
    null
  }

}

object ProgramMap {

  def apply(baseInstruction:Instruction,controls:List[Controls.Value],length:Int) = {
    def orderAndZero(control:Controls,controlValues:List[Controls.Value]):List[Controls.Value] = {
      val controlsMap = controlValues.map(x => (x.index,x)).toMap  // Create a map of controls (index, controlValue)
      val fullControls = List.tabulate(length) ( x => controlsMap.getOrElse(x,Controls.Value(control,x,-1))) // Fill other controls with Zero
      fullControls.sortBy(_.index) // Order the Data
    }
    val cont = controls.groupBy(x => x.control)                 // Create a map of control signals
    val programMap = baseInstruction.controls.map(x => (x,orderAndZero(x,cont.getOrElse(x,List())))).toMap // Create a map of controls with values
    new ProgramMap(baseInstruction,programMap,length)
  }
}