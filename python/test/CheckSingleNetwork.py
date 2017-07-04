import matplotlib.pyplot as plt
import struct
import numpy
import utils.FloatUtil as fu
import utils.PlotUtil as pu
import sys

base = "/home/andy/projects/NeuralHDL/tests/simple/data/"

def test(args) :
    input = fu.load_float_file(base + "rtl_in.hex")
    error = fu.load_float_file(base + "rtl_error.hex")
    bias = fu.load_float_file(base + "rtl_bias.hex")
    output = fu.load_float_file(base + "rtl_out.hex")
    pre = fu.load_float_file(base + "rtl_pre.hex")
    tap = fu.load_float_array_file(base + "rtl_tap.hex")

    if (args.plot) :
        pu.plotStageResult(output, error, bias, tap, 1, "stage 0 ")
        plt.show()

    sq_err = numpy.sum(numpy.square(error)) / len(error)
    if (args.threshold is not None) :
        if (sq_err > args.threshold):
            print("Error Greater than Threshold " + str(sq_err) + " > " + str(args.threshold))
            sys.exit(1)








