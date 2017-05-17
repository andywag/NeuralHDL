package com.simplifide.generate.generator

/**
  *
  */

class SplitSegment(val segments:List[SimpleSegment],val extra:List[SimpleSegment]) {



}

object SplitSegment {
  def apply(segments:List[SimpleSegment],extra:List[SimpleSegment]) = new SplitSegment(segments,extra)
}