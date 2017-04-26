package com.simplifide.generate.test2.isim

import com.simplifide.generate.parser.EntityParser
import java.io.File
import com.simplifide.generate.util.FileOps
import com.simplifide.generate.project.NewEntity
import com.simplifide.generate.test2.WaveformCreator

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 6/29/12
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */

class IsimWaveformCreator(val entity: NewEntity, val path: String) extends WaveformCreator {


  def createCode(item: WaveformCreator.Item, parent: Option[WaveformCreator.Item]): String = {
    val into = if (parent != None) " -into $" + parent.get.name else ""
    item match {
      case x: WaveformCreator.Wave =>
        "wave add " + path + x.signal.name + into + "\n"
      case x: WaveformCreator.Group =>
        "set " + x.name + " [group add " + x.name + into + "]\n" + x.children.map(createCode(_, Some(x))).foldLeft("")(_ + _)
      case _ => "ERROR"
    }
  }

  def createFile(file: java.io.File) = {

    FileOps.createFile(file, createCode(this.body, None))
  }


}

