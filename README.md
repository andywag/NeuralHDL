# ScalaHDL

ScalaHDL is an internal DSL for hardware design based on Scala. It is the culmination of years of struggle with 
creating parameterizable hardware designs with languages which are not specifically tailored to the problem. 

## Issues with Current Design Paradigms

1. RTL Languages (Verilog/VHDL) support hardware structure and parallelism well but are not well suited towards parameterization
2. HLS Languages (Currently C++) supports parameterization to a degree but does not directly map hardware structures
which makes the exact translations and mappings unclear

ScalaHDL solves these issues by using an RTL like syntax embedded inside a modern design language (scala) which allows for both. 

1. Creating Efficient hardware designs using the verilog like embedded syntax 
2. Allow parameterization using the constructs of the modern language. 

## Getting Started

## Examples
