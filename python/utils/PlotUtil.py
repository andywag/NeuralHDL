import matplotlib.pyplot as plt
import struct
import numpy
from scipy import signal
import utils.FloatUtil as fu

def plotStageResult(data,err,bi,ta,fig, pre) :
    er0s = signal.lfilter(numpy.ones(6), 1, numpy.square(err))

    plt.figure(fig)
    plt.subplot(231)
    plt.plot(data)
    plt.title(pre + "output")
    plt.subplot(232)
    plt.plot(bi)
    plt.title(pre + "bias")
    plt.subplot(233)
    plt.plot(numpy.squeeze(ta))
    plt.title(pre + "taps")
    plt.subplot(223)
    plt.plot(err)
    plt.title(pre + "error")
    plt.subplot(224)
    plt.plot(er0s)
    plt.title(pre + "square error")

def handleStage(base, index, plot) :
    st0 = fu.load_float_file(base + "rtl_st" + str(index) + ".hex")
    er0 = fu.load_float_file(base + "rtl_error" + str(index) + ".hex")
    bi0 = fu.load_float_file(base + "rtl_bias" + str(index) + ".hex")
    ta0 = fu.load_float_array_file(base + "rtl_tap" + str(index) + ".hex")
    sq_err = numpy.sum(numpy.square(er0))/len(er0)
    print ("Square Error " + str(index) + " " + str(sq_err))
    if (plot) :
        plotStageResult(st0,er0,bi0,ta0,index,"Stage " + str(index))
    return sq_err