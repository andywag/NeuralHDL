package com.simplifide.generate.blocks.neural

import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.parser.EntityParser

/**
  * Created by andy on 6/3/17.
  */
class NeuralNetwork[T](val name:String,
                        info:NeuralStageInfo
                       )(implicit clk:ClockControl) extends EntityParser {

}
