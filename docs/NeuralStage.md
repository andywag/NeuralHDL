# Neural Stage 

This block contains a set of Neurons and is designed in a way to support the 3 basic Network operations using the same structure 
driving the inputs slightly differently. The different algorithms are all Matrix operations so the structure is designed to handle the 3 
operations with slightly different orderings. 


## Feedforward operation

For feedforward the operation is a simple matrix multiplication with the equation shown below : 

y=Hx : Need formatting

A block diagram of the operation is shown below which contains the feedforward operation only. 

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

The block contains N MAC units which are shared for the operations. The data is ordered in a way to share the accumulator and limit memory accesses. The access is shown in the table below for a matrix with K MAC units and N total operations. 

| Type          | 0       | 1      | K      | K+1     | N       |
| ------------- |:-------:| ------:| ------:| -------:| -------:|
| Tap (Vector)  | T0      |   T1   |   TK   | T(K+1)  | TN      |
| Data          | D0      |   D1   |   DK   | D0      | DK      |
| Bias          | B0      |   B1   |   BK   | B0      | BK      |

The advantage of the ordering above is that the data is accessed linearily to minimize access and the accumulator is always used to update the outputs avoiding the use of an adder tree. 

## Error Update operation

The error update calculation uses the same structure as above with a slightly different input configuration to the MAC units and some addition glue logic to handle this operation. The equation for this operation is shown below : 

e = HTe : TBD Fix Equation

The block diagram of the operation is shown below. 

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
    TapDelay-->BiasAdd
    BiasAdd-->Output
    
    The operation shown above is very similar to the feedforward operation with the following differences 
    1. The tap input is the error. This is made more efficient by storing the tap and error in the same memory
    2. The output of the block is parallel and is written to the tap memory
    3. The bias update is not shown but is done in a similar fashion as the tap update
