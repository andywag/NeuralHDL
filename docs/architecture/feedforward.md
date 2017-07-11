# Feedforward operation


The feedforward operation consists of a matrix multiplication, a vector bias addition and a non-linear operation. The operation has a serial input and output with parallel operations done using the MAC units.  

* equation TBD

A block diagram of the operation is shown below. The block contains N shared MAC units, a delay line, a bias addition followed by a non-linearity. 

```mermaid
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
```


## Data Flow

The data flow operates as follows : 

1. The input data is multiplied by the taps with the data being input at 1 sample per input 
2. The accumulator takes the previous results unless it is the first input sample
3. When the summation is complete the parallel outputs are latched into a delay line
4. The delay line is fed serially into the bias circuit. `This could/should be done using the accumulator`
5. The output of the bias addition block is fed into the nonlinearity and output serially

The access ordering is shown in the table below for an example which has K MAC units and N total inputs. The ordering is always done so that an output is completed without having to be stored back into memory. 

| Type          | 0       | 1      | K      | K+1     | N       |
| ------------- |:-------:| ------:| ------:| -------:| -------:|
| Tap (Vector)  | T0      |   T1   |   TK   | T(K+1)  | TN      |
| Data          | D0      |   D1   |   DK   | D0      | DK      |
| Bias          | B0      |   B1   |   BK   | B0      | BK      |

