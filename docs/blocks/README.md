# Building Blocks

The blocks for this design are written in an embedded DSL inside scala. The purpose of this language is to
allow fine grained control of the hardware generation while allowing the power of a high level language to
allow parameterization and simulation control. The examples shown in this section will detail some the syntax
of the language used. This is is an internal portion of the design which is not meant to be exposed to the end
user unless they want to extend the generated cores.

The concept of this DSL was to use a verilog like syntax embedded in a more powerful higher level language so in general the syntax should look verilog at the lowest level. Some of the example code in the section uses more abstract code but at the lowest level there is most likely a verilog like statement. 



