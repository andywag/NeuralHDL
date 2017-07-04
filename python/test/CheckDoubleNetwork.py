import matplotlib.pyplot as plt
import struct
import numpy
import utils.FloatUtil as fu
import utils.PlotUtil as pu
from scipy import signal
import sys

base = "/home/andy/projects/NeuralHDL/tests/full/data/"

def check(error,threshold) :
    if (threshold is not None) :
        if (error > threshold):
            print("Error Greater than Threshold " + str(error) + " > " + str(threshold))
            sys.exit(1)

def test(args) :
    err0 = pu.handleStage(base,0,args.plot)
    err1 = pu.handleStage(base,1,args.plot)
    errors = [err0,err1]
    if (args.plot) :
        plt.show()
    [check(x,args.threshold) for x in errors]








