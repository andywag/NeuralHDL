import matplotlib.pyplot as plt
import struct
import numpy

def float_to_hex(f):
    return hex(struct.unpack('<I', struct.pack('<f', f))[0])

def hex_to_float(input) :
    return struct.unpack('!f',bytes.fromhex(input))

def load_float_file(path) :
    file = open(path,'r')
    result = [hex_to_float(x.strip()) for x in file]
    file.close
    return numpy.array(result)
