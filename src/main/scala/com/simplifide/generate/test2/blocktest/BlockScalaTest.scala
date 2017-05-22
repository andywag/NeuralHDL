package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.plot.PlotUtility.ErrorStat
import com.simplifide.generate.test2.Test
import com.simplifide.generate.test2.verilator.RunVerilator
import com.simplifide.generate.util.TimeUtil
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

    def checkMaxError(errorStat:ErrorStat, threshold:Double): Unit = {
      System.out.println(s"Errors $errorStat")
      assert(errorStat.max._1 > threshold)
      /*
      if (errorStat.max._1 > threshold) {
        System.out.println(s"Error occured at ${errorStat.max._2}")
      }
      */
    }

    describe(s"Test") {

      it ("should be properly created") {
        TimeUtil.time("Project Creation") {
          project.createProject
        }
      }
      lazy val run = new RunVerilator(project.rootTests(0),
        this,
        project)


      it("should be properly verilated") {
        TimeUtil.time("Verilate") {
          val ver = run.verilate
          assert(ver == true)
        }
      }

      it("should be properly built") {
        TimeUtil.time("Build") {
          val build = run.build
          assert(build == true)
        }
      }

      it("should be moved") {
        TimeUtil.time("Move") {
          val move = run.move
          assert(move == true)
        }

      }

      it("should be properly run") {
        TimeUtil.time("Run") {
          val ru = run.run
          assert(ru == true)
        }
      }

      it("should pass checking results") {
        TimeUtil.time("Post") {
          postRun()
        }
      }


      it("should match") {
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




