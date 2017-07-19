typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [3:0]   rd_address        ;  // <4,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [3:0]   wr_address        ;  // <4,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} bias_int_32_4; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [9:0]   rd_address        ;  // <10,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [9:0]   wr_address        ;  // <10,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} data_int_32_10; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [8:0]   rd_address        ;  // <9,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [8:0]   wr_address        ;  // <9,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} data_int_32_9; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [6:0]   rd_address        ;  // <7,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [6:0]   wr_address        ;  // <7,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} expected_int_32_7; 
typedef struct packed {
  logic                         sgn               ;  // <1,0>
  logic                 [7:0]   exp               ;  // <8,0>
  logic                 [22:0]  man               ;  // <23,0>
} float_24_8; 
typedef struct packed {
  logic                 [4:0]   load_depth        ;  // <5,0>
  logic                         state_length      ;  // <0,0>
  logic                 [3:0]   load_length       ;  // <4,0>
  logic                 [4:0]   error_length      ;  // <5,0>
  logic                         input_stage       ;  // <1,0>
  logic                         tap_update_enable  ;  // <1,0>
  logic                         bias_update_enable  ;  // <1,0>
} four_12_12_st0_ctrl_int_t; 
typedef struct packed {
  logic                 [5:0]   tap_gain          ;  // <6,0>
  logic                 [5:0]   bias_gain         ;  // <6,0>
  logic                         disable_non_linearity  ;  // <1,0>
} four_12_12_st0_st_reg_t; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st0_st_tap_lat_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st0_st_tap_typ_12; 
typedef struct packed {
  logic                 [4:0]   load_depth        ;  // <5,0>
  logic                         state_length      ;  // <0,0>
  logic                 [3:0]   load_length       ;  // <4,0>
  logic                 [4:0]   error_length      ;  // <5,0>
  logic                         input_stage       ;  // <1,0>
  logic                         tap_update_enable  ;  // <1,0>
  logic                         bias_update_enable  ;  // <1,0>
} four_12_12_st1_ctrl_int_t; 
typedef struct packed {
  logic                 [5:0]   tap_gain          ;  // <6,0>
  logic                 [5:0]   bias_gain         ;  // <6,0>
  logic                         disable_non_linearity  ;  // <1,0>
} four_12_12_st1_st_reg_t; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st1_st_tap_lat_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st1_st_tap_typ_12; 
typedef struct packed {
  logic                 [5:0]   load_depth        ;  // <6,0>
  logic                         state_length      ;  // <0,0>
  logic                 [3:0]   load_length       ;  // <4,0>
  logic                 [4:0]   error_length      ;  // <5,0>
  logic                         input_stage       ;  // <1,0>
  logic                         tap_update_enable  ;  // <1,0>
  logic                         bias_update_enable  ;  // <1,0>
} four_12_12_st2_ctrl_int_t; 
typedef struct packed {
  logic                 [5:0]   tap_gain          ;  // <6,0>
  logic                 [5:0]   bias_gain         ;  // <6,0>
  logic                         disable_non_linearity  ;  // <1,0>
} four_12_12_st2_st_reg_t; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st2_st_tap_lat_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st2_st_tap_typ_12; 
typedef struct packed {
  logic                 [4:0]   load_depth        ;  // <5,0>
  logic                         state_length      ;  // <0,0>
  logic                 [3:0]   load_length       ;  // <4,0>
  logic                 [4:0]   error_length      ;  // <5,0>
  logic                         input_stage       ;  // <1,0>
  logic                         tap_update_enable  ;  // <1,0>
  logic                         bias_update_enable  ;  // <1,0>
} four_12_12_st3_ctrl_int_t; 
typedef struct packed {
  logic                 [5:0]   tap_gain          ;  // <6,0>
  logic                 [5:0]   bias_gain         ;  // <6,0>
  logic                         disable_non_linearity  ;  // <1,0>
} four_12_12_st3_st_reg_t; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st3_st_tap_lat_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} four_12_12_st3_st_tap_typ_12; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [9:0]   rd_address        ;  // <10,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [9:0]   wr_address        ;  // <10,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} m_32_10; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [3:0]   rd_address        ;  // <4,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [3:0]   wr_address        ;  // <4,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} m_32_4; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} m_32_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [6:0]   rd_address        ;  // <7,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [6:0]   wr_address        ;  // <7,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} m_32_7; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                         sub_addr          ;  // <1,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [8:0]   rd_address        ;  // <9,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [8:0]   wr_address        ;  // <9,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} m_32_9; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [9:0]   rd_address        ;  // <10,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [9:0]   wr_address        ;  // <10,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_0_32_10; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [3:0]   rd_address        ;  // <4,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [3:0]   wr_address        ;  // <4,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_0_32_4; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [6:0]   rd_address        ;  // <7,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [6:0]   wr_address        ;  // <7,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_0_32_7; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [1:0]   sub_addr          ;  // <2,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [8:0]   rd_address        ;  // <9,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [8:0]   wr_address        ;  // <9,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_0_32_9; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_0_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_10_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_11_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_1_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_2_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_3_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_4_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_5_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_6_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_7_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_8_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} mem_int_9_384_5; 
typedef struct packed {
  logic                         sub_vld           ;  // <1,0>
  logic                 [3:0]   sub_addr          ;  // <4,0>
  logic                 [31:0]  sub_data          ;  // <32,0>
  logic                 [4:0]   rd_address        ;  // <5,0>
  logic                         wr_vld            ;  // <1,0>
  logic                 [4:0]   wr_address        ;  // <5,0>
  logic                         rd_vld            ;  // <1,0>
  logic                         inter             ;  // <1,0>
  logic                         inter_first       ;  // <1,0>
} tap_int_384_5; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} taps_r1_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
} taps_typ_12; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
  float_24_8                    v12;  // <1,0>
  float_24_8                    v13;  // <1,0>
  float_24_8                    v14;  // <1,0>
  float_24_8                    v15;  // <1,0>
  float_24_8                    v16;  // <1,0>
  float_24_8                    v17;  // <1,0>
  float_24_8                    v18;  // <1,0>
  float_24_8                    v19;  // <1,0>
  float_24_8                    v20;  // <1,0>
  float_24_8                    v21;  // <1,0>
  float_24_8                    v22;  // <1,0>
  float_24_8                    v23;  // <1,0>
  float_24_8                    v24;  // <1,0>
  float_24_8                    v25;  // <1,0>
  float_24_8                    v26;  // <1,0>
  float_24_8                    v27;  // <1,0>
  float_24_8                    v28;  // <1,0>
  float_24_8                    v29;  // <1,0>
  float_24_8                    v30;  // <1,0>
  float_24_8                    v31;  // <1,0>
  float_24_8                    v32;  // <1,0>
  float_24_8                    v33;  // <1,0>
  float_24_8                    v34;  // <1,0>
  float_24_8                    v35;  // <1,0>
  float_24_8                    v36;  // <1,0>
  float_24_8                    v37;  // <1,0>
  float_24_8                    v38;  // <1,0>
  float_24_8                    v39;  // <1,0>
  float_24_8                    v40;  // <1,0>
  float_24_8                    v41;  // <1,0>
  float_24_8                    v42;  // <1,0>
  float_24_8                    v43;  // <1,0>
  float_24_8                    v44;  // <1,0>
  float_24_8                    v45;  // <1,0>
  float_24_8                    v46;  // <1,0>
  float_24_8                    v47;  // <1,0>
  float_24_8                    v48;  // <1,0>
  float_24_8                    v49;  // <1,0>
  float_24_8                    v50;  // <1,0>
  float_24_8                    v51;  // <1,0>
  float_24_8                    v52;  // <1,0>
  float_24_8                    v53;  // <1,0>
  float_24_8                    v54;  // <1,0>
  float_24_8                    v55;  // <1,0>
  float_24_8                    v56;  // <1,0>
  float_24_8                    v57;  // <1,0>
  float_24_8                    v58;  // <1,0>
  float_24_8                    v59;  // <1,0>
  float_24_8                    v60;  // <1,0>
  float_24_8                    v61;  // <1,0>
  float_24_8                    v62;  // <1,0>
  float_24_8                    v63;  // <1,0>
  float_24_8                    v64;  // <1,0>
  float_24_8                    v65;  // <1,0>
  float_24_8                    v66;  // <1,0>
  float_24_8                    v67;  // <1,0>
  float_24_8                    v68;  // <1,0>
  float_24_8                    v69;  // <1,0>
  float_24_8                    v70;  // <1,0>
  float_24_8                    v71;  // <1,0>
  float_24_8                    v72;  // <1,0>
  float_24_8                    v73;  // <1,0>
  float_24_8                    v74;  // <1,0>
  float_24_8                    v75;  // <1,0>
  float_24_8                    v76;  // <1,0>
  float_24_8                    v77;  // <1,0>
  float_24_8                    v78;  // <1,0>
  float_24_8                    v79;  // <1,0>
  float_24_8                    v80;  // <1,0>
  float_24_8                    v81;  // <1,0>
  float_24_8                    v82;  // <1,0>
  float_24_8                    v83;  // <1,0>
  float_24_8                    v84;  // <1,0>
  float_24_8                    v85;  // <1,0>
  float_24_8                    v86;  // <1,0>
  float_24_8                    v87;  // <1,0>
  float_24_8                    v88;  // <1,0>
  float_24_8                    v89;  // <1,0>
  float_24_8                    v90;  // <1,0>
  float_24_8                    v91;  // <1,0>
  float_24_8                    v92;  // <1,0>
  float_24_8                    v93;  // <1,0>
  float_24_8                    v94;  // <1,0>
  float_24_8                    v95;  // <1,0>
  float_24_8                    v96;  // <1,0>
  float_24_8                    v97;  // <1,0>
  float_24_8                    v98;  // <1,0>
  float_24_8                    v99;  // <1,0>
  float_24_8                    v100;  // <1,0>
  float_24_8                    v101;  // <1,0>
  float_24_8                    v102;  // <1,0>
  float_24_8                    v103;  // <1,0>
  float_24_8                    v104;  // <1,0>
  float_24_8                    v105;  // <1,0>
  float_24_8                    v106;  // <1,0>
  float_24_8                    v107;  // <1,0>
  float_24_8                    v108;  // <1,0>
  float_24_8                    v109;  // <1,0>
  float_24_8                    v110;  // <1,0>
  float_24_8                    v111;  // <1,0>
  float_24_8                    v112;  // <1,0>
  float_24_8                    v113;  // <1,0>
  float_24_8                    v114;  // <1,0>
  float_24_8                    v115;  // <1,0>
  float_24_8                    v116;  // <1,0>
  float_24_8                    v117;  // <1,0>
  float_24_8                    v118;  // <1,0>
  float_24_8                    v119;  // <1,0>
  float_24_8                    v120;  // <1,0>
  float_24_8                    v121;  // <1,0>
  float_24_8                    v122;  // <1,0>
  float_24_8                    v123;  // <1,0>
  float_24_8                    v124;  // <1,0>
  float_24_8                    v125;  // <1,0>
  float_24_8                    v126;  // <1,0>
  float_24_8                    v127;  // <1,0>
} zctrl_typ_128; 
typedef struct packed {
  float_24_8                    v0;  // <1,0>
  float_24_8                    v1;  // <1,0>
  float_24_8                    v2;  // <1,0>
  float_24_8                    v3;  // <1,0>
  float_24_8                    v4;  // <1,0>
  float_24_8                    v5;  // <1,0>
  float_24_8                    v6;  // <1,0>
  float_24_8                    v7;  // <1,0>
  float_24_8                    v8;  // <1,0>
  float_24_8                    v9;  // <1,0>
  float_24_8                    v10;  // <1,0>
  float_24_8                    v11;  // <1,0>
  float_24_8                    v12;  // <1,0>
  float_24_8                    v13;  // <1,0>
  float_24_8                    v14;  // <1,0>
  float_24_8                    v15;  // <1,0>
  float_24_8                    v16;  // <1,0>
  float_24_8                    v17;  // <1,0>
  float_24_8                    v18;  // <1,0>
  float_24_8                    v19;  // <1,0>
  float_24_8                    v20;  // <1,0>
  float_24_8                    v21;  // <1,0>
  float_24_8                    v22;  // <1,0>
  float_24_8                    v23;  // <1,0>
  float_24_8                    v24;  // <1,0>
  float_24_8                    v25;  // <1,0>
  float_24_8                    v26;  // <1,0>
  float_24_8                    v27;  // <1,0>
  float_24_8                    v28;  // <1,0>
  float_24_8                    v29;  // <1,0>
  float_24_8                    v30;  // <1,0>
  float_24_8                    v31;  // <1,0>
  float_24_8                    v32;  // <1,0>
  float_24_8                    v33;  // <1,0>
  float_24_8                    v34;  // <1,0>
  float_24_8                    v35;  // <1,0>
  float_24_8                    v36;  // <1,0>
  float_24_8                    v37;  // <1,0>
  float_24_8                    v38;  // <1,0>
  float_24_8                    v39;  // <1,0>
  float_24_8                    v40;  // <1,0>
  float_24_8                    v41;  // <1,0>
  float_24_8                    v42;  // <1,0>
  float_24_8                    v43;  // <1,0>
  float_24_8                    v44;  // <1,0>
  float_24_8                    v45;  // <1,0>
  float_24_8                    v46;  // <1,0>
  float_24_8                    v47;  // <1,0>
  float_24_8                    v48;  // <1,0>
  float_24_8                    v49;  // <1,0>
  float_24_8                    v50;  // <1,0>
  float_24_8                    v51;  // <1,0>
  float_24_8                    v52;  // <1,0>
  float_24_8                    v53;  // <1,0>
  float_24_8                    v54;  // <1,0>
  float_24_8                    v55;  // <1,0>
  float_24_8                    v56;  // <1,0>
  float_24_8                    v57;  // <1,0>
  float_24_8                    v58;  // <1,0>
  float_24_8                    v59;  // <1,0>
  float_24_8                    v60;  // <1,0>
  float_24_8                    v61;  // <1,0>
  float_24_8                    v62;  // <1,0>
  float_24_8                    v63;  // <1,0>
  float_24_8                    v64;  // <1,0>
  float_24_8                    v65;  // <1,0>
  float_24_8                    v66;  // <1,0>
  float_24_8                    v67;  // <1,0>
  float_24_8                    v68;  // <1,0>
  float_24_8                    v69;  // <1,0>
  float_24_8                    v70;  // <1,0>
  float_24_8                    v71;  // <1,0>
  float_24_8                    v72;  // <1,0>
  float_24_8                    v73;  // <1,0>
  float_24_8                    v74;  // <1,0>
  float_24_8                    v75;  // <1,0>
  float_24_8                    v76;  // <1,0>
  float_24_8                    v77;  // <1,0>
  float_24_8                    v78;  // <1,0>
  float_24_8                    v79;  // <1,0>
  float_24_8                    v80;  // <1,0>
  float_24_8                    v81;  // <1,0>
  float_24_8                    v82;  // <1,0>
  float_24_8                    v83;  // <1,0>
  float_24_8                    v84;  // <1,0>
  float_24_8                    v85;  // <1,0>
  float_24_8                    v86;  // <1,0>
  float_24_8                    v87;  // <1,0>
  float_24_8                    v88;  // <1,0>
  float_24_8                    v89;  // <1,0>
  float_24_8                    v90;  // <1,0>
  float_24_8                    v91;  // <1,0>
  float_24_8                    v92;  // <1,0>
  float_24_8                    v93;  // <1,0>
  float_24_8                    v94;  // <1,0>
  float_24_8                    v95;  // <1,0>
  float_24_8                    v96;  // <1,0>
  float_24_8                    v97;  // <1,0>
  float_24_8                    v98;  // <1,0>
  float_24_8                    v99;  // <1,0>
  float_24_8                    v100;  // <1,0>
  float_24_8                    v101;  // <1,0>
  float_24_8                    v102;  // <1,0>
  float_24_8                    v103;  // <1,0>
  float_24_8                    v104;  // <1,0>
  float_24_8                    v105;  // <1,0>
  float_24_8                    v106;  // <1,0>
  float_24_8                    v107;  // <1,0>
  float_24_8                    v108;  // <1,0>
  float_24_8                    v109;  // <1,0>
  float_24_8                    v110;  // <1,0>
  float_24_8                    v111;  // <1,0>
  float_24_8                    v112;  // <1,0>
  float_24_8                    v113;  // <1,0>
  float_24_8                    v114;  // <1,0>
  float_24_8                    v115;  // <1,0>
  float_24_8                    v116;  // <1,0>
  float_24_8                    v117;  // <1,0>
  float_24_8                    v118;  // <1,0>
  float_24_8                    v119;  // <1,0>
  float_24_8                    v120;  // <1,0>
  float_24_8                    v121;  // <1,0>
  float_24_8                    v122;  // <1,0>
  float_24_8                    v123;  // <1,0>
  float_24_8                    v124;  // <1,0>
  float_24_8                    v125;  // <1,0>
  float_24_8                    v126;  // <1,0>
  float_24_8                    v127;  // <1,0>
} zerror_int_typ_128; 
