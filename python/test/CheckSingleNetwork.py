import matplotlib.pyplot as plt
import struct
import numpy
import utils.FloatUtil as fu
import utils.PlotUtil as pu
import sys

base = "/home/andy/projects/NeuralHDL/tests/simple/data/"

def check(error,threshold) :
    if (threshold is not None) :
        if (error > threshold):
            print("Error Greater than Threshold " + str(error) + " > " + str(threshold))
            sys.exit(1)

def test(args) :
    err0 = pu.handleStage(base,0,args.plot)
    errors = [err0]
    if (args.plot) :
        plt.show()
    [check(x,args.threshold) for x in errors]


#def test(args) :
#    error = fu.load_float_file(base + "rtl_error.hex")
#    bias = fu.load_float_file(base + "rtl_bias0.hex")
#    tap = fu.load_float_array_file(base + "rtl_tap0.hex")

#    output = fu.load_float_file(base + "rtl_out.hex")

#    if (args.plot) :
#        pu.plotStageResult(output, error, bias, tap, 1, "stage 0 ")
#        plt.show()

#    sq_err = numpy.sum(numpy.square(error)) / len(error)
#    if (args.threshold is not None) :
#        if (sq_err > args.threshold):
#            print("Error Greater than Threshold " + str(sq_err) + " > " + str(args.threshold))
#            sys.exit(1)








