typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [2:0]   rd_address        ;  // <3,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [2:0]   wr_address        ;  // <3,0>
  logic                         rd_vld            ;  // <1,0>
} bias_int_32_3; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [5:0]   rd_address        ;  // <6,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [5:0]   wr_address        ;  // <6,0>
  logic                         rd_vld            ;  // <1,0>
} data_int_32_6; 
typedef struct packed {
  logic                         sgn               ;  // <1,0>
  logic                 [7:0]   exp               ;  // <8,0>
  logic                 [22:0]  man               ;  // <23,0>
} float_24_8; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [2:0]   rd_address        ;  // <3,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [2:0]   wr_address        ;  // <3,0>
  logic                         rd_vld            ;  // <1,0>
} m_32_3; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [3:0]   rd_address        ;  // <4,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [3:0]   wr_address        ;  // <4,0>
  logic                         rd_vld            ;  // <1,0>
} m_32_4; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [5:0]   rd_address        ;  // <6,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [5:0]   wr_address        ;  // <6,0>
  logic                         rd_vld            ;  // <1,0>
} m_32_6; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
} stage_st_tap_typ_6; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [2:0]   sub_addr          ;  // <3,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [3:0]   rd_address        ;  // <4,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [3:0]   wr_address        ;  // <4,0>
  logic                         rd_vld            ;  // <1,0>
} tap_int_192_4; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
} taps_typ_6; 
