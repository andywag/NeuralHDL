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
  input float_24_8              data_in,
  input                         data_in_fst,
  input                         data_in_vld,
  input                         data_out_pre_rdy,
  input                         data_out_rdy,
  input                 [3:0]   load_depth,
  input                 [2:0]   load_length,
  input                         reset,
  input                         state_length,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                [2:0]   bias_address,
  output                        data_in_rdy,
  output float_24_8             data_out,
  output                        data_out_fst,
  output float_24_8             data_out_pre,
  output                        data_out_pre_fst,
  output                        data_out_pre_vld,
  output                        data_out_vld,
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
  wire                          data_out_pre_rdy  ;  // <1,0>
  wire                          data_out_rdy      ;  // <1,0>
  wire                          first             ;  // <1,0>
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
    .data_in(data_in),
    .data_in_fst(data_in_fst),
    .data_in_rdy(data_in_rdy),
    .data_in_vld(data_in_vld),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .data_out(data_out),
    .data_out_fst(data_out_fst),
    .data_out_pre(data_out_pre),
    .data_out_pre_fst(data_out_pre_fst),
    .data_out_pre_rdy(data_out_pre_rdy),
    .data_out_pre_vld(data_out_pre_vld),
    .data_out_rdy(data_out_rdy),
    .data_out_vld(data_out_vld),
    .first(first),
    .load_depth(load_depth),
    .load_length(load_length),
    .reset(reset),
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

