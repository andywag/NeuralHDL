typedef struct packed {
  logic                         sgn               ;  // <1,0>
  logic                 [7:0]   exp               ;  // <8,0>
  logic                 [22:0]  man               ;  // <23,0>
} float_24_8; 
