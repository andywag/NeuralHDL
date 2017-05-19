package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.test2.Test
import com.simplifide.generate.test2.verilator.RunVerilator
import org.scalatest.{FlatSpec, FlatSpecLike, FunSpec}

/**
  * Created by andy on 5/4/17.
  */
class BlockScalaTest extends FunSpec{
  self:BlockTestParser =>
/*
  val run = new RunVerilator(Project.tests(0),
    NeuralTest.Project.testCase,
    this.pr)
  */
  /// /project.cleanProject




  describe (s"Test") {
    it ("should be properly cleaned") {
      project.cleanProject
    }
    it ("should be properly created") {
      project.createProject
    }

    lazy val run = new RunVerilator(project.rootTests(0),
      this,
      project)

    it ("should be properly verilated") {
      assert (run.verilate == true)
    }

    it ("should be properly built")  {
      assert (run.build == true)
    }

    it ("should be moved")  {
      assert (run.move == true)
    }

    it ("should be properly run")  {
      assert (run.run == true)
    }

    it ("should pass checking results") {
      this.postRun()
    }


    it ("should match") {
      val res = this.compareAllResults()
      res.map(x => {
        assert(x.errors.size == 0)
        println(x.debugString)
      })
    }

  }




  //s"$blockName"should "be match " in {

  //}







}




