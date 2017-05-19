package com.simplifide.generate.test2.blocktest

import com.simplifide.generate.test2.Test
import com.simplifide.generate.test2.verilator.RunVerilator
import org.scalatest.{FlatSpec, FlatSpecLike}

/**
  * Created by andy on 5/4/17.
  */
class BlockScalaTest extends FlatSpec{
  self:BlockTestParser =>
/*
  val run = new RunVerilator(Project.tests(0),
    NeuralTest.Project.testCase,
    this.pr)
  */
  /// /project.cleanProject


  def createAndRun = {
    val project1 = new BlockProject(this)
    project1.cleanProject
    project1.createProject
    val run = new RunVerilator(test, this, project)
    run.verilate
    run.build
    run.move
    run.run
  }


  lazy val project = this.create
  lazy val test    = project.tests(0)
  lazy val run = new RunVerilator(test,
    this,
    project)




  s"$blockName" should "be properly verilated" in {
    assert (run.verilate == true)
  }

  it should "be properly built" in {
    assert (run.build == true)
  }

  it should "be moved " in {
    assert (run.move == true)
  }

  it should "be properly run" in {
    assert (run.run == true)
    this.postRun()
  }


  it should "Match " in {
    val res = this.compareAllResults()
    res.map(x => {
        assert(x.errors.size == 0)
        println(x.debugString)
    })
  }
  //s"$blockName"should "be match " in {

  //}







}




