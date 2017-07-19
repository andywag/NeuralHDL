# NeuralHDL

NeuralHDL is an internal DSL written in scala for hardware design geared towards neural networks. The goal of this project is to create (or expand) a [CAFFE](http://caffe.berkeleyvision.org/) like tool which supports generic neural network designs targetted towards generating configurable hardware. This project is an extension of an existing general purpose hardware DSL ([ScalaDL](https://github.com/andywag/ScalaDL) used in the design of a highly parallel optical modem which has architectural similarities to neural networks.

Detailed documentation and instructions for use can be found here :  

[Detailed Documentation](https://andywag.github.io/NeuralHDL/index.html)

The results of a simple example project can be seen below. 

[Two Stage Network Test Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/TwoStage.ipynb)
[Multi Stage Network Test Results](https://github.com/andywag/NeuralHDL/blob/master/docs/results/MultiStage.ipynb)


# Status - Next Steps

This project was started to assess the feasability of porting an existing harwdware flow to neural networks and to do a basic study on the hardware architecture and potential solutions. At this point in time those goals have been met. This toolset can generate a configurable fully connected neural network with some limitations. This is a small subset of the possible configuration but the infrastructure is in place to easily generalize to more complicated architectures. 

There are many possible improvements but even configurable hardware architectures differ depending on the requirements of the problem. At this point, there is an effort to clean up some of the control bugs limiting the configurability and study the convergence properties, but the next step would require an actual application. If anyone has an interest/application which this toolset would fit into please contact me to discuss. 
 
