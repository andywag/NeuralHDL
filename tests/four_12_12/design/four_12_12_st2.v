//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st2
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st2(
  input                         clk,
  input four_12_12_st2_ctrl_int_t four_12_12_st2_ctrl_int,
  input four_12_12_st2_st_reg_t four_12_12_st2_st_reg,
  input                         reset,
  input float_24_8              stage_2_data,
  input                         stage_2_data_fst,
  input                         stage_2_data_out_pre_rdy,
  input                         stage_2_data_out_rdy,
  input                         stage_2_data_vld,
  input float_24_8              stage_2_error,
  input                         stage_2_error_fst,
  input                         stage_2_error_out_rdy,
  input                         stage_2_error_vld,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                        four_12_12_st2_ctrl_data_fifo_data_ready,
  output                        load_finish,
  output float_24_8             stage_2_data_out,
  output                        stage_2_data_out_fst,
  output float_24_8             stage_2_data_out_pre,
  output                        stage_2_data_out_pre_fst,
  output                        stage_2_data_out_pre_vld,
  output                        stage_2_data_out_vld,
  output                        stage_2_data_rdy,
  output float_24_8             stage_2_error_out,
  output                        stage_2_error_out_fst,
  output                        stage_2_error_out_vld,
  output                        stage_2_error_rdy,
  output                        tap_in_rdy);

// Parameters 



// Wires 

  bias_int_32_4                 bias_int;  // <1,0>
  wire                  [31:0]  bias_int_rd_data  ;  // <32,0>
  wire                  [31:0]  bias_int_wr_data  ;  // <32,0>
  data_int_32_10                data_int;  // <1,0>
  wire                  [31:0]  data_int_rd_data  ;  // <32,0>
  wire                  [31:0]  data_int_wr_data  ;  // <32,0>
  wire                          first             ;  // <1,0>
  float_24_8                    four_12_12_st2_st_bias;  // <1,0>
  float_24_8                    four_12_12_st2_st_data;  // <1,0>
  float_24_8                    four_12_12_st2_st_data_out;  // <1,0>
  float_24_8                    four_12_12_st2_st_data_out_bias;  // <1,0>
  float_24_8                    four_12_12_st2_st_data_out_pre;  // <1,0>
  wire                  [383:0]  four_12_12_st2_st_tap_out  ;  // <384,0>
  wire                          stage_2_error_out_rdy  ;  // <1,0>
  wire                          stage_error_back  ;  // <1,0>
  wire                          stage_error_first  ;  // <1,0>
  wire                          stage_error_mode  ;  // <1,0>
  tap_int_384_5                 tap_int;  // <1,0>
  wire                  [383:0]  tap_int_rd_data  ;  // <384,0>
  wire                  [383:0]  tap_int_wr_data  ;  // <384,0>
  taps_typ_12                   taps;  // <1,0>
  wire                          update_error_first  ;  // <1,0>
  float_24_8                    zerror_int;  // <1,0>
  wire                          zerror_int_fst    ;  // <1,0>
  wire                          zerror_int_vld    ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// four_12_12_st2_st
////////////////////////////////////////////////////////////////////////////////

four_12_12_st2_st four_12_12_st2_st (
    .clk(clk),
    .first(first),
    .four_12_12_st2_st_bias(four_12_12_st2_st_bias),
    .four_12_12_st2_st_bias_adder(four_12_12_st2_st_bias_adder),
    .four_12_12_st2_st_data(four_12_12_st2_st_data),
    .four_12_12_st2_st_data_out(four_12_12_st2_st_data_out),
    .four_12_12_st2_st_data_out_bias(four_12_12_st2_st_data_out_bias),
    .four_12_12_st2_st_data_out_pre(four_12_12_st2_st_data_out_pre),
    .four_12_12_st2_st_reg(four_12_12_st2_st_reg),
    .four_12_12_st2_st_tap_out(four_12_12_st2_st_tap_out),
    .reset(reset),
    .stage_error_back(stage_error_back),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .taps(taps),
    .update_error_first(update_error_first));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st2_mem
////////////////////////////////////////////////////////////////////////////////

four_12_12_st2_mem four_12_12_st2_mem (
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
// four_12_12_st2_ctrl
////////////////////////////////////////////////////////////////////////////////

four_12_12_st2_ctrl four_12_12_st2_ctrl (
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .first(first),
    .four_12_12_st2_ctrl_data_fifo_data_ready(four_12_12_st2_ctrl_data_fifo_data_ready),
    .four_12_12_st2_ctrl_int(four_12_12_st2_ctrl_int),
    .four_12_12_st2_st_bias(four_12_12_st2_st_bias),
    .four_12_12_st2_st_data(four_12_12_st2_st_data),
    .four_12_12_st2_st_data_out(four_12_12_st2_st_data_out),
    .four_12_12_st2_st_data_out_bias(four_12_12_st2_st_data_out_bias),
    .four_12_12_st2_st_data_out_pre(four_12_12_st2_st_data_out_pre),
    .four_12_12_st2_st_tap_out(four_12_12_st2_st_tap_out),
    .load_finish(load_finish),
    .reset(reset),
    .stage_2_data(stage_2_data),
    .stage_2_data_fst(stage_2_data_fst),
    .stage_2_data_out(stage_2_data_out),
    .stage_2_data_out_fst(stage_2_data_out_fst),
    .stage_2_data_out_pre(stage_2_data_out_pre),
    .stage_2_data_out_pre_fst(stage_2_data_out_pre_fst),
    .stage_2_data_out_pre_vld(stage_2_data_out_pre_vld),
    .stage_2_data_out_vld(stage_2_data_out_vld),
    .stage_2_data_rdy(stage_2_data_rdy),
    .stage_2_data_vld(stage_2_data_vld),
    .stage_2_error(stage_2_error),
    .stage_2_error_fst(stage_2_error_fst),
    .stage_2_error_out(stage_2_error_out),
    .stage_2_error_out_fst(stage_2_error_out_fst),
    .stage_2_error_out_vld(stage_2_error_out_vld),
    .stage_2_error_rdy(stage_2_error_rdy),
    .stage_2_error_vld(stage_2_error_vld),
    .stage_error_back(stage_error_back),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data),
    .taps(taps),
    .update_error_first(update_error_first),
    .zerror_int(zerror_int),
    .zerror_int_fst(zerror_int_fst),
    .zerror_int_vld(zerror_int_vld));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st2_fifo
////////////////////////////////////////////////////////////////////////////////

four_12_12_st2_fifo four_12_12_st2_fifo (
    .clk(clk),
    .reset(reset),
    .stage_2_error_out(stage_2_error_out),
    .stage_2_error_out_fst(stage_2_error_out_fst),
    .stage_2_error_out_rdy(stage_2_error_out_rdy),
    .stage_2_error_out_vld(stage_2_error_out_vld),
    .zerror_int(zerror_int),
    .zerror_int_fst(zerror_int_fst),
    .zerror_int_rdy(zerror_int_rdy),
    .zerror_int_vld(zerror_int_vld));

endmodule

