# NeuralHDL

NeuralHDL is an internal DSL in scala for hardware design geared towards neural networks.
The goal of this project will be to create (or expand) existing tool sets like [DeepLearningForJ](https://deeplearning4j.org/) or [CAFFE](http://caffe.berkeleyvision.org/) to directly generate and interface to hardware designs. This project is based on an existing general purpose hardware DSL ([ScalaDL](https://github.com/andywag/ScalaDL))
which was used in the design of a highly parallel optical modem which has architectural similarities to neural networks.

The relatively simple structure of neural networks is a good fit for DSL used to generate the structures which
is already done in tools like CAFFE for software solutions. This design technique is also a good fit (if not better)
for hardware designs which is the purpose of this project. The purpose of this tool will be to either expand or
be an alternative for CAFFE like languages for hardware designs.

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
A similar description for a stage is shown below for a stage description for this tool. A more descriptive DSL is in
development but this structure will describe the parameters for a fully connected stage.

```scala
NeuralStageInfo((dataLength,outputLength), // # Inputs, # Outputs
    dataLength,                            // # Inputs
    dataFill,                              // Fifo Depth for Input
    numberNeurons,                         // Number of MAC Units to Share
    errorFill,                             // Fifo Depth for Error Feedback
    outputFill)                            // Fifo Depth for expected values

```

Detailed documentation describing usign the tool and results can be found at the link below. 

[Detailed Documentation](https://andywag.github.io/NeuralHDL/index.html)

An example project can be seen below which shows the basic operation and performance of a simple network. 

[Two Stage Network Test Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/TwoStage.ipynb)


