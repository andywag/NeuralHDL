# NeuralHDL

NeuralHDL is an internal DSL in scala for hardware design geared towards neural networks. The goal of this project will be to create (or expand) existing tool sets like [DeepLearningForJ](https://deeplearning4j.org/) or [CAFFE](http://caffe.berkeleyvision.org/) to directly generate and interface to hardware designs. This project is based on an existing general purpose hardware DSL ([ScalaDL](https://github.com/andywag/ScalaDL)) which was used in the design of a highly parallel optical modem. The problem statement is different but the repeated structured architecture makes the tool well suited for the problem.  


It is the culmination of decades of automating signal processing design using HDL generators due to limitations in HDLs. The main purpose of this project is not to change the general workflow of ASIC teams or create a new language for hardware design. This is a difficult (potentially impossible problem) and is beyond the scope. 

## Background
Through these years there have been many commercial tools to solve this problem but have all suffered from the same flaw. 

1. Tools are not good at architecture

Abstraction should allow designers/architects a convenient way to describe their designs. Most tools including the current C++ HLS languages do not support an entry mechanism which gives architects the power/control to specify their design. This approach works but tends to lead to inefficient and difficult to support designs. 

The approach used with an internal DSL is to allow the "architect/designer" to define his own abstraction to be able to completely specify his design in an abstract way. Other tools have been built following this approach (including a prior version of this tool) which have not had great adoption. 

1. https://github.com/VeriScala/VeriScala
2. https://chisel.eecs.berkeley.edu/

The issue with these languages is that hardware designers in general have extremely limited software abilities and the initial design of the DSL can be challenging. The goal of this project is not to solve the general problem but to port an existing tool used in the development of a highly parallel optical receiver to another highly parallel architecture (neural networks). 

## Repeat

HDL languages have advantages and disadvantages. For structured designs like signal processing the disadvantages far outweigh the advantages so I have spent years attempting to automate the design of structured operations. 

1. HDLs (Verilog-VHDL) are good at timing, low-level bit manipulation and control
2. HDLs are not good at parameterization and abstraction or automating structured designs

These two conflicting issues make automation problematic since it is beneficial to write control logic using an HDL but abstract more structured items using a higher level language. This led to various incarnations of hybrid design tools which only used partial generation leading to issues. Internal DSL solve this issues by allowing control over the syntax so the complete design can be included in the same language and structure



## Issues with Current Design Paradigms

1. RTL Languages (Verilog/VHDL) support hardware structure and parallelism well but are not well suited towards parameterization
2. HLS Languages (Currently C++) supports parameterization to a degree but does not directly map hardware structures
which makes the exact translations and mappings unclear

ScalaHDL solves these issues by using an RTL like syntax embedded inside a modern design language (scala) which allows for both. 

1. Creating Efficient hardware designs using the verilog like embedded syntax 
2. Allow parameterization using the constructs of the modern language. 

## Getting Started

## Examples
