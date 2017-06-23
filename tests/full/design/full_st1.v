//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_st1
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_st1(
  input                         clk,
  input full_st1_ctrl_int_t     full_st1_ctrl_int,
  input                         reset,
  input float_24_8              stage_1_data,
  input                         stage_1_data_fst,
  input                         stage_1_data_out_pre_rdy,
  input                         stage_1_data_out_rdy,
  input                         stage_1_data_vld,
  input float_24_8              stage_1_error,
  input                         stage_1_error_fst,
  input                         stage_1_error_vld,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                        full_st1_ctrl_data_fifo_data_ready,
  output                        load_finish,
  output float_24_8             stage_1_data_out,
  output                        stage_1_data_out_fst,
  output float_24_8             stage_1_data_out_pre,
  output                        stage_1_data_out_pre_fst,
  output                        stage_1_data_out_pre_vld,
  output                        stage_1_data_out_vld,
  output                        stage_1_data_rdy,
  output                        stage_1_error_rdy,
  output                        tap_in_rdy);

// Parameters 



// Wires 

  bias_int_32_3                 bias_int;  // <1,0>
  wire                  [31:0]  bias_int_rd_data  ;  // <32,0>
  wire                  [31:0]  bias_int_wr_data  ;  // <32,0>
  data_int_32_7                 data_int;  // <1,0>
  wire                  [31:0]  data_int_rd_data  ;  // <32,0>
  wire                  [31:0]  data_int_wr_data  ;  // <32,0>
  wire                          first             ;  // <1,0>
  float_24_8                    full_st1_st_bias;  // <1,0>
  float_24_8                    full_st1_st_data;  // <1,0>
  float_24_8                    full_st1_st_data_out;  // <1,0>
  float_24_8                    full_st1_st_data_out_bias;  // <1,0>
  float_24_8                    full_st1_st_data_out_pre;  // <1,0>
  wire                  [191:0] full_st1_st_tap_out;  // <192,0>
  wire                          stage_error_first ;  // <1,0>
  wire                          stage_error_mode  ;  // <1,0>
  tap_int_192_4                 tap_int;  // <1,0>
  wire                  [191:0] tap_int_rd_data   ;  // <192,0>
  wire                  [191:0] tap_int_wr_data   ;  // <192,0>
  taps_typ_6                    taps;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// full_st1_st
////////////////////////////////////////////////////////////////////////////////

full_st1_st full_st1_st (
    .clk(clk),
    .first(first),
    .full_st1_st_bias(full_st1_st_bias),
    .full_st1_st_bias_adder(full_st1_st_bias_adder),
    .full_st1_st_data(full_st1_st_data),
    .full_st1_st_data_out(full_st1_st_data_out),
    .full_st1_st_data_out_bias(full_st1_st_data_out_bias),
    .full_st1_st_data_out_pre(full_st1_st_data_out_pre),
    .full_st1_st_tap_out(full_st1_st_tap_out),
    .reset(reset),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .taps(taps));

////////////////////////////////////////////////////////////////////////////////
// full_st1_mem
////////////////////////////////////////////////////////////////////////////////

full_st1_mem full_st1_mem (
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
// full_st1_ctrl
////////////////////////////////////////////////////////////////////////////////

full_st1_ctrl full_st1_ctrl (
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .first(first),
    .full_st1_ctrl_data_fifo_data_ready(full_st1_ctrl_data_fifo_data_ready),
    .full_st1_ctrl_int(full_st1_ctrl_int),
    .full_st1_st_bias(full_st1_st_bias),
    .full_st1_st_data(full_st1_st_data),
    .full_st1_st_data_out(full_st1_st_data_out),
    .full_st1_st_data_out_bias(full_st1_st_data_out_bias),
    .full_st1_st_data_out_pre(full_st1_st_data_out_pre),
    .full_st1_st_tap_out(full_st1_st_tap_out),
    .load_finish(load_finish),
    .reset(reset),
    .stage_1_data(stage_1_data),
    .stage_1_data_fst(stage_1_data_fst),
    .stage_1_data_out(stage_1_data_out),
    .stage_1_data_out_fst(stage_1_data_out_fst),
    .stage_1_data_out_pre(stage_1_data_out_pre),
    .stage_1_data_out_pre_fst(stage_1_data_out_pre_fst),
    .stage_1_data_out_pre_vld(stage_1_data_out_pre_vld),
    .stage_1_data_out_vld(stage_1_data_out_vld),
    .stage_1_data_rdy(stage_1_data_rdy),
    .stage_1_data_vld(stage_1_data_vld),
    .stage_1_error(stage_1_error),
    .stage_1_error_fst(stage_1_error_fst),
    .stage_1_error_rdy(stage_1_error_rdy),
    .stage_1_error_vld(stage_1_error_vld),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data),
    .taps(taps));

endmodule

