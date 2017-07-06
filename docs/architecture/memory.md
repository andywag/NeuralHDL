## Memory Layout 

The memory for each stage is split into 3 separate memories based on their size and frequency of use. 

1. Data Memory
1. Bias Memory
1. Tap and Error Memory

The memory size and depths have been calculated to keep the network fully utilized. The internal network parameters are somewhat obvious as they are directly related to the size of the network operation. The depths of the error and data are less obvious and directly effect the throughput of the network. This is due to the data being required for the tap updates so must be accessible when the error is fedback. 

### Data Memory
The data memory contains the input data to the stage. This information is required for both the feedforward operation of the stage and the error updates. 

1. The memory read requires one data sample per stage calculation. 
1. The depth of the memory is a function of the maximum error feedback time and throughput requirements

### Bias Memory

The bias memory contains the bias values from the network. 

1. The memory read requires one data sample per stage calculation. 
1. The depth of this memory is equivalent to the amount of neurons in the stage

### Tap and Error Memory
This is a parallel memory which has an output width which is a multiple of the number of MAC units used in the stage. The tap memory contains both the tap values for the stage as well as the error which simplifies the interface to the stage and reduces the memory requirements. 

1. The memory requires one parallel read (number of neurons width) for each operation of the neural stage
1. The depth of the memory is equivalent to the size of the taps plus the required error storage. 

The memory is assumed to have read/write access to individaul taps as well as the full set of parallel taps. This is used for 

1. Loading the Error into memory
2. Reading the Transposed Taps out for BackPropagation
