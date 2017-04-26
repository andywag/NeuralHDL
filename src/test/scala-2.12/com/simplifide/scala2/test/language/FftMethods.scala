package com.simplifide.scala2.test.language

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 9/6/11
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */

object FftMethods {
    def bitReverse(value:Int,num:Int):Int = {
      // Kludgy way of handling this. Add a one to the top bit
      // And the remove it to make sure all the bits are created with toBinaryString
      val sh  = value + math.pow(2.0,num).toInt
      val bits = sh.toBinaryString.substring(1)
      var nvalue:Int = 0;
      for (i <- 0 until num) {
        val bit = bits.substring(i,i+1)
        if (bit == "1") nvalue += math.pow(2.0,i).toInt
      }
      return nvalue
    }

    def getButterflyConstant(row:Int, column:Int, depth:Int):(Int,Int,Double) = {

      val length = math.pow(2.0,depth).toInt

      val stageDivisor           = math.pow(2.0, column).toInt                   // Stage Used to Calculate Butterfly Divisor
      val stageFFTSize           = length/stageDivisor                           // Size of Sub FFT

      val addressNorm     = 2*row % stageFFTSize                              // Remainder of AddressNew Normalized
      val baseNorm        = 2*row / stageFFTSize                              // Base of Normalized AddressNew
      val addressNorm1    = (2*row+1) % stageFFTSize                          // Remainder of AddressNew Normalized
      val baseNorm1       = (2*row+1) / stageFFTSize                          // Base of Normalized AddressNew

      val address0       = bitReverse(addressNorm ,depth - column) // Bit Reverse the AddressNew for Correct Calcuation
      val address1       = bitReverse(addressNorm1,depth - column) // Bit Reverse the AddressNew for Correct Calcuation
      val realAddress0    = stageFFTSize*baseNorm  + address0
      val realAddress1    = stageFFTSize*baseNorm1 + address1

      val baseAngle       = bitReverse(baseNorm,column)
      val angle           = baseAngle.toDouble/stageDivisor.toDouble

       (realAddress0,realAddress1,angle)
    }
  }