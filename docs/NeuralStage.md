# Neural Stage 

This block contains a set of Neurons and is designed in a way to support the 3 basic Network operations using the same structure 
driving the inputs slightly differently. The different algorithms are all Matrix operations so the structure is designed to handle the 3 
operations with slightly different orderings. 


## Feedforward operation

For feedforward the operation is a simple matrix multiplication with the equation shown below : 

graph LR
    Add(+)
    Mult(*)

    subgraph MACN
    Mult-->Add
    Data0-->Mult
    Tap-->Mult
    end

   subgraph MAC0
    Mult1(*)-->Add1(+)
    DataN-->Mult1
    Tap1-->Mult1
    end
    
    subgraph DelayLine
    Add-->Rn
    Add1-->R0
    end

    Data-->Data0
    Data-->DataN
    R0 -->BiasAdd(+)
    Bias-->BiasAdd
    BiasAdd-->Nonlinearity
    Nonlinearity-->Output
