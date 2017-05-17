package com.simplifide.generate.signal

/**
  * Created by andy on 5/8/17.
  */
trait IoType {

}

object IoType {
  case object IoInput   extends IoType
  case object IoOutput  extends IoType
  case object IoNone    extends IoType
}
