## Background

I have been been working on signal processing hardware design for the last 20 years and have spent all
of those years attempting to automate the design process with tools and code generators. Through these
years there have been many commercial tools attempting to solve this problem but all have  suffered from
the same inherent flaw in having tools do architecture. 

There are many parts of the design process that can be automated but architecture is an inherently
creative process requiring human intervention. Tools are good at low level optimization but suffer
when it comes to large processing. 

As a signal processing/hardware architect, I want a convenient way to describe the design without 
giving up control of how the architecture is mapped. Most tools including the current C++ 
HLS languages do not support an entry mechanism which gives architects the power/control to specify
their design. This approach works but tends to lead to inefficient and difficult to support designs.
I have yet to see a design which did not have issues in development and greater issues when the 
original designers left.

The alternate approach used here is an internal DSL which allows "architect/designer" to define his 
own abstraction to be able to completely specify his design in an abstract way. Other tools have been 
built following this approach (including a prior version of this tool) which have not had great 
adoption.

1. https://github.com/VeriScala/VeriScala
2. https://chisel.eecs.berkeley.edu/


The issue with these languages is that hardware designers in general have extremely limited software 
abilities and the initial design of the DSL can be challenging. The goal of this project is not to 
solve the general problem, but instead use it to create a tool set to enable simple designs to be 
specified and implemented by non-hardware savvy people. In general this approach would not be 
successful but should work well for a targetted solution. The machine learning engineer can work 
with a familiar language while at the same time the hardware designer can work with a familiar 
language.

## General HDL language issues

HDL languages have advantages and disadvantages. For structured designs like signal processing 
the disadvantages far outweigh the advantages so I have spent years attempting to automate the 
design of structured operations.

1. HDLs (Verilog-VHDL) are good at timing, low-level bit manipulation and control
2. HDLs are not good at parameterization and abstraction or automating structured designs

These two conflicting issues make automation problematic since it is beneficial to write control logic using an HDL but abstract more structured items using a higher level language. This led to various incarnations of hybrid design tools which only used partial generation leading to issues. Internal DSL solve this issues by allowing control over the syntax so the complete design can be included in the same language and structure


