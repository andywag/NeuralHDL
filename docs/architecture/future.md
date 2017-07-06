# Future Directions for Architecture

The architecture used for this document is just one implementation possible using this toolset. Certain non-optimal choices were made which could/should be improved in future versions.

## Floating Point : 

Floating point was chosen to ease quantization issues but ended up leading to more issues than it solved. The quantization issues for this development were relatively small and almost all were related to the choice of floating point. The generator is built to use arbitrary precisions but this code hasn't been tested with fixed point. 

## Memory Packing

The memories indexing is based on a power of 2 for operations which is inneficient in terms of space. This was done in the devlopment to ease debugging but should be compressed in future revisions. 
 



