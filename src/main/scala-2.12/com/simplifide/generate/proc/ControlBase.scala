package com.simplifide.generate.proc

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 11/18/11
 * Time: 10:05 AM
 * To change this template use File | Settings | File Templates.
 */

trait ControlBase extends ControlHolder{
  /** Name of the control holder */
  val name:String
  /** Create the set of controls for this holder */
  val createControls:List[Controls]



}