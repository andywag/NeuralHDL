//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage(
  input                 [2:0]   bias_length,
  input                         clk,
  input                 [3:0]   load_depth,
  input                 [2:0]   load_length,
  input                         reset,
  input float_24_8              st_data,
  input                         st_data_fst,
  input                         st_data_out_pre_rdy,
  input                         st_data_out_rdy,
  input                         st_data_vld,
  input float_24_8              st_error,
  input                         st_error_fst,
  input                         st_error_vld,
  input                         state_length,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                [2:0]   bias_address,
  output float_24_8             st_data_out,
  output                        st_data_out_fst,
  output float_24_8             st_data_out_pre,
  output                        st_data_out_pre_fst,
  output                        st_data_out_pre_vld,
  output                        st_data_out_vld,
  output                        st_data_rdy,
  output                        st_error_rdy,
  output                [3:0]   tap_address,
  output                        tap_in_rdy);

// Parameters 



// Wires 

  bias_int_32_3                 bias_int;  // <1,0>
  wire                  [31:0]  bias_int_rd_data  ;  // <32,0>
  wire                  [31:0]  bias_int_wr_data  ;  // <32,0>
  data_int_32_6                 data_int;  // <1,0>
  wire                  [31:0]  data_int_rd_data  ;  // <32,0>
  wire                  [31:0]  data_int_wr_data  ;  // <32,0>
  wire                          first             ;  // <1,0>
  wire                          st_data_out_pre_rdy;  // <1,0>
  wire                          st_data_out_rdy   ;  // <1,0>
  float_24_8                    stage_st_bias;  // <1,0>
  float_24_8                    stage_st_data;  // <1,0>
  float_24_8                    stage_st_data_out;  // <1,0>
  float_24_8                    stage_st_data_out_pre;  // <1,0>
  tap_int_192_4                 tap_int;  // <1,0>
  wire                  [191:0] tap_int_rd_data   ;  // <192,0>
  wire                  [191:0] tap_int_wr_data   ;  // <192,0>
  taps_typ_6                    taps;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// stage_st
////////////////////////////////////////////////////////////////////////////////

stage_st stage_st (
    .clk(clk),
    .first(first),
    .reset(reset),
    .stage_st_bias(stage_st_bias),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(stage_st_data_out),
    .stage_st_data_out_bias(stage_st_data_out_bias),
    .stage_st_data_out_pre(stage_st_data_out_pre),
    .taps(taps));

////////////////////////////////////////////////////////////////////////////////
// stage_mem
////////////////////////////////////////////////////////////////////////////////

stage_mem stage_mem (
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .reset(reset),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data));

////////////////////////////////////////////////////////////////////////////////
// stage_ctrl
////////////////////////////////////////////////////////////////////////////////

stage_ctrl stage_ctrl (
    .bias_address(bias_address),
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .bias_length(bias_length),
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .first(first),
    .load_depth(load_depth),
    .load_length(load_length),
    .reset(reset),
    .st_data(st_data),
    .st_data_fst(st_data_fst),
    .st_data_out(st_data_out),
    .st_data_out_fst(st_data_out_fst),
    .st_data_out_pre(st_data_out_pre),
    .st_data_out_pre_fst(st_data_out_pre_fst),
    .st_data_out_pre_rdy(st_data_out_pre_rdy),
    .st_data_out_pre_vld(st_data_out_pre_vld),
    .st_data_out_rdy(st_data_out_rdy),
    .st_data_out_vld(st_data_out_vld),
    .st_data_rdy(st_data_rdy),
    .st_data_vld(st_data_vld),
    .st_error(st_error),
    .st_error_fst(st_error_fst),
    .st_error_rdy(st_error_rdy),
    .st_error_vld(st_error_vld),
    .stage_st_bias(stage_st_bias),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(stage_st_data_out),
    .stage_st_data_out_pre(stage_st_data_out_pre),
    .state_length(state_length),
    .tap_address(tap_address),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data),
    .taps(taps));

endmodule

