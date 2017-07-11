# Memory Layout 

The memory for each stage is split into 3 separate memories based on their size and frequency of use. Different memory architectures can be selected depending on the frequency of use and properites of the memories. This architecture assumes a memory running at the same rate as the neural stage and a dual port memory.

1. Data Memory
1. Bias Memory
1. Tap and Error Memory

The memory size of the model is fixed based on the size of the network, but the depths for the data and error require careful consideration to keep the network fully utilized. The main constraint for the network is the input data and error must be available at the same time to properly update the taps. This requires the data to be stored inside the block until the error is available or will halt the operations. 

## Data Memory
The data memory contains the input data to the stage. This information is required for both the feedforward operation of the stage and the error updates. 

1. The memory read requires one data sample per stage calculation. 
1. The depth of the memory is a function of the maximum error feedback time and throughput requirements

| Address       | Data         |
| ------------- |:------------:| 
| 0             | Data0        |  
| 1             | Data1        |  
| K             | DataK        | 
| K+1           | 0            | 
| ...           | 0            | 
| N             | Data(K+1)    |
| ...           | ...          |

The current memory is stored inefficiently using the scheme above where K is the length of the input data and N is th

| Address | 0    | 1    | K    | K+1 | ... | N      | ... | 
| Data    | D(0) | D(1) | D(K) | 0   | 0   | D(K+1) | ... |


## Bias Memory

The bias memory contains the bias values from the network. 

1. The memory read requires one data sample per stage calculation. 
1. The depth of this memory is equivalent to the amount of neurons in the stage

The use of the bias term occurs at a later time than the data operation so there is the potential to delay the data with the read and write addresses. The effectiveness of this is limited somewhat by the bias update and bias use having different pipeline delays. 

## Tap and Error Memory
This is a parallel memory which has an output width which is a multiple of the number of MAC units used in the stage. The tap memory contains both the tap values for the stage as well as the error which simplifies the interface to the stage and reduces the memory requirements. 

1. The memory requires one parallel read (number of neurons width) for each operation of the neural stage
1. The depth of the memory is equivalent to the size of the taps plus the required error storage. 

The memory is assumed to have read/write access to individaul taps as well as the full set of parallel taps. This is used for 

1. Loading the Error into memory
2. Reading the Transposed Taps out for BackPropagation
