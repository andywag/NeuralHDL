/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simplifide.generate.util

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Class which contains methods and utilities for dealing with the file system */
class FileOps {}


object FileOps {



  /** Create a directory at the file fileLocation file */
  def createDirectory(file:String) {
     createDirectory(new File(file))
  }

  /** Create a directory at the file fileLocation file */
  def createDirectory(file:java.io.File) {
	  if (!file.exists) file.mkdir
  }
  
  /** Create a directory at the fileLocation fileLocation if it doesn't already exist */
    def createDirectory(location:String,extra:Option[String]):File =  {
      val ufile = extra match {
        case None    => new File(location)
        case Some(x) => new File(location,x)
      }
      FileOps.createDirectory(ufile)
      ufile
    }
  
  /** Create a directory at the fileLocation fileLocation if it doesn't already exist */
  def createDirectory(location:java.io.File,extra:Option[String]):File =  {
    val ufile = extra match {
      case None    => location
      case Some(x) => new File(location,x)
    }
    FileOps.createDirectory(ufile)
    ufile
  }
  /** Create a new file in the input directory with the contents 'contents' */
  def createFile(file:java.io.File, contents:String) {
    val fil = new FileWriter(file)
    fil.write(contents.toString)
    fil.close()
    Log().info(s"Wrote File" + file)
    //System.out.println("Wrote File" + file)
    InternalLogger.debug(contents)
  }

  /** Create a new file with the directory from fileLocation with filename 'filename' and the contents 'contents' */
  def createFile(location:String,filename:String,contents:String)  {
    createFile(new File(new File(location),filename),contents)
  }



}
