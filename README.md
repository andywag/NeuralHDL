# NeuralHDL

NeuralHDL is an internal DSL written in scala for hardware design geared towards neural networks. The goal of this project is to create (or expand) a [CAFFE](http://caffe.berkeleyvision.org/) like tool which supports generic neural network designs targetted towards generating configurable hardware. This project is an extension of an existing general purpose hardware DSL ([ScalaDL](https://github.com/andywag/ScalaDL) used in the design of a highly parallel optical modem which has architectural similarities to neural networks.

Detailed documentation and instructions for use can be found here :  

[Detailed Documentation](https://andywag.github.io/NeuralHDL/index.html)

The results of a simple example project can be seen below. 

[Two Stage Network Test Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/TwoStage.ipynb)

A simple stage in caffe which is a json description is shown below. 

```json
layer {
  name: "conv1"
  type: "Convolution"
  param { lr_mult: 1 }
  param { lr_mult: 2 }
  convolution_param {
    num_output: 20
   }
  bottom: "data"
  top: "conv1"
}
```

A similar description for a network stage for this tool is shown below. JSON if not CAFFE syntax could be easily implemented to support this structure but most like longer term a more descriptive DSL will be added.  

```scala
NeuralStageInfo((dataLength,outputLength), // # Inputs, # Outputs
    dataFill,                              // Fifo Depth for Input
    numberNeurons,                         // Number of MAC Units to Share
    errorFill,                             // Fifo Depth for Error Feedback
    outputFill)                            // Fifo Depth for expected values

```




