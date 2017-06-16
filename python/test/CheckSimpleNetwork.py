import matplotlib.pyplot as plt
import struct
import numpy
import utils.FloatUtil as fu

base = "/home/andy/projects/NeuralHDL/tests/simple/data/"

error = fu.load_float_file(base + "rtl_error.hex")
bias  = fu.load_float_file(base + "rtl_bias.hex")

#path = "/home/andy/projects/NeuralHDL/tests/simple/data/rtl_error.hex"
#path_bias = "/home/andy/projects/NeuralHDL/tests/simple/data/rtl_bias.hex"
#file = open(path,'r')
#fbias = open(path_bias,'r')

#result = [fu.hex_to_float(x.strip()) for x in file]
#bias   = [fu.hex_to_float(x.strip()) for x in fbias]

tresult = numpy.array(error)
size    = int(len(tresult)/6)
shift  = numpy.square(numpy.reshape(tresult,(size,6)))
vshift = numpy.sum(shift,1)

#[print(x) for x in result[12:len(result):6]]


plt.plot(vshift)
plt.ylabel('mse')
plt.show()

plt.plot(tresult)
plt.ylabel('error')
plt.show()

plt.plot(bias[12:len(bias):6])
plt.ylabel('bias')
plt.show()

