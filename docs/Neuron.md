# Neuron

The neuron is the basic hardware block for the design. It is actually really a MAC unit rather than a full neuron. The reason for the partioning
is that the bias addition and non-linearity is done at a different period than the multiply operation so is done at a higher level to allow
for sharing. 

A diagram of the structure is shown below  : 

```mermaid
graph LR
    Add(+)
    Add2(+)
    Mult(*)

    subgraph MAC
    Mult-->Add
    Data-->Mult
    Tap-->Mult
    Delay(Delay)-->Add
    end
 ```
  
 ## Example Code

The code to generate this block is relatively straightforward. It simply contains a multiplier and an adder. The
appropriate adder is selected by the type of the input. *The use of times and use of plus rather than */+ is due
to a recent change in the way these operations are handled and is temporary.*

```scala

internalSignal := dataIn times taps
dataOut        := internalSignal plus bias

```

## Reference Code

* [Code Generator](../src/main/scala/com/simplifide/generate/blocks/neural/Neuron.scala)
* [Verilog Output](../tests/neuron/design/neuron.v)




