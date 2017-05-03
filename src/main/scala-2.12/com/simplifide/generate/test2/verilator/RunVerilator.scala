package com.simplifide.generate.test2.verilator

import com.simplifide.generate.project.{NewEntity, Project}
import com.simplifide.generate.test2.{Test, TestEntityParser}
import com.simplifide.generate.util.{Logger, ProcessOps}

/**
  * Created by andy on 4/27/17.
  */
class RunVerilator(test:Test, testCase:TestEntityParser, project:Project) {

  val entity = testCase.createEntity

  val verilatorRoot = if (System.getenv("VERILATOR_ROOT") == null) "/home/andy/software/verilator" else System.getenv("VERILATOR_ROOT")

  val verilatorCommand = s"${verilatorRoot}/verilator_bin --trace --trace-structs -Wall -Wno-lint -Idesign -cc test/${entity.name}.v --exe test/${entity.name}.cpp"

  val makeCommand = s"make -f V${entity.name}.mk"

  val runCommand = s"./V${entity.name} "

  def verilate = {
    println (s"Running $verilatorCommand")
    ProcessOps.exec(verilatorCommand, Some(project.location))(ln => Logger.booleanMessage(ln))
  }
  def build =
    ProcessOps.exec(makeCommand,Some(s"${project.location}/obj_dir"))(ln => Logger.booleanMessage(ln))

  def run =
    ProcessOps.exec(runCommand,Some(s"${project.location}/obj_dir"))(ln => Logger.booleanMessage(ln))

  def buildOnly = {
    verilate
    build
  }

  def runAll = {
    //val ver1 = verilate
    if (verilate) {
      if (build) {
        if (run) {
          val res = testCase.compareAllResults()
          val errors = res.map(_.debugString).mkString("\n")
          System.out.println(errors)

        }
      }
    }
    //val bul1 = build
    //val run1 = run
    //run
  }




}
