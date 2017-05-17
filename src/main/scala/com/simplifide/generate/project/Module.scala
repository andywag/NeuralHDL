package com.simplifide.generate.project

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.ModuleParser
import com.simplifide.generate.parser.factory.{CreationFactory}


// TODO remove the processor_interface and state_machine
/**
 * Class which defines a module which contains signals and statements
 */
class Module(override val name:String)(implicit clk:ClockControl) extends ModuleParser   {
  //implicit val creator:CreationFactory = HardwareCreationFactory


  /** Create the statements for this module */
  //protected def createStatements:List[SimpleSegment] = this.statements.toList.map(x => x.asInstanceOf[SimpleSegment])
  /** Create the instances for this module */
  //protected def createInstances:List[NewEntityInstance] = List()

  /** Create a module provider from this module */
  /*def createModule:ModuleProvider = {
    this.transform
    val mod = ModuleProvider(name,
        this,
        this.signals.toList.map(x => x.asInstanceOf[SignalTrait]),
        this.createStatements,
        this.createInstances,
        this.extraFiles.toList)
    mod.description = this.description
    mod
  } */

}

object Module {

}