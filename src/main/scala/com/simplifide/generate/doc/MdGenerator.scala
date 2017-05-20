package com.simplifide.generate.doc

import java.io.File

import com.simplifide.generate.signal.SignalTrait
import com.simplifide.generate.signal.sv.Struct
import com.simplifide.generate.test2.Test
import com.simplifide.generate.util.FileOps

/**
  * Created by andy on 5/18/17.
  */
trait MdGenerator[T] {
  def document:String
  def create(file:String) = {
    FileOps.createFile(new File(file),document)
  }
}

object MdGenerator {
  implicit class MdSignal(val signal:SignalTrait) extends MdGenerator[SignalTrait] {
    def document:String = {
      signal match {
        case x:Struct => s"${x.name}      - ${x.typeName} "
        case _        => s"${signal.name} - ${signal.fixed} "
      }
    }
  }

  implicit class MdTest(val test:Test) extends MdGenerator[Test] {
    override def document: String =
      s"""
# ${test.testBench.name}

${test.testBench.base.get.document}

        """
  }

}
