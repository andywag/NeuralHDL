//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple(
  input                         clk,
  input float_24_8              expected,
  input                         expected_fst,
  input                         expected_vld,
  input                         reset,
  input simple_st0_ctrl_int_t   simple_st0_ctrl_int,
  input simple_st0_st_reg_t     simple_st0_st_reg,
  input float_24_8              st_data,
  input                         st_data_fst,
  input                         st_data_out_pre_rdy,
  input                         st_data_out_rdy,
  input                         st_data_vld,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                        expected_rdy,
  output                        load_finish,
  output                        simple_st0_ctrl_data_fifo_data_ready,
  output float_24_8             st_data_out,
  output                        st_data_out_fst,
  output float_24_8             st_data_out_pre,
  output                        st_data_out_pre_fst,
  output                        st_data_out_pre_vld,
  output                        st_data_out_vld,
  output                        st_data_rdy,
  output                        tap_in_rdy);

// Parameters 



// Wires 

  wire                          st_error_rdy      ;  // <1,0>
  float_24_8                    stage_0_data;  // <1,0>
  wire                          stage_0_data_fst  ;  // <1,0>
  float_24_8                    stage_0_data_out;  // <1,0>
  wire                          stage_0_data_out_fst  ;  // <1,0>
  float_24_8                    stage_0_data_out_pre;  // <1,0>
  wire                          stage_0_data_out_pre_fst  ;  // <1,0>
  wire                          stage_0_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_0_data_out_pre_vld  ;  // <1,0>
  wire                          stage_0_data_out_rdy  ;  // <1,0>
  wire                          stage_0_data_out_vld  ;  // <1,0>
  wire                          stage_0_data_rdy  ;  // <1,0>
  wire                          stage_0_data_vld  ;  // <1,0>
  float_24_8                    stage_0_error;  // <1,0>
  wire                          stage_0_error_fst  ;  // <1,0>
  float_24_8                    stage_0_error_out;  // <1,0>
  wire                          stage_0_error_out_fst  ;  // <1,0>
  wire                          stage_0_error_out_rdy  ;  // <1,0>
  wire                          stage_0_error_out_vld  ;  // <1,0>
  wire                          stage_0_error_rdy  ;  // <1,0>
  wire                          stage_0_error_vld  ;  // <1,0>
  float_24_8                    stage_1_data;  // <1,0>
  wire                          stage_1_data_fst  ;  // <1,0>
  float_24_8                    stage_1_data_out;  // <1,0>
  wire                          stage_1_data_out_fst  ;  // <1,0>
  float_24_8                    stage_1_data_out_pre;  // <1,0>
  wire                          stage_1_data_out_pre_fst  ;  // <1,0>
  wire                          stage_1_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_1_data_out_pre_vld  ;  // <1,0>
  wire                          stage_1_data_out_rdy  ;  // <1,0>
  wire                          stage_1_data_out_vld  ;  // <1,0>
  wire                          stage_1_data_rdy  ;  // <1,0>
  wire                          stage_1_data_vld  ;  // <1,0>
  float_24_8                    stage_1_error;  // <1,0>
  wire                          stage_1_error_fst  ;  // <1,0>
  float_24_8                    stage_1_error_out;  // <1,0>
  wire                          stage_1_error_out_fst  ;  // <1,0>
  wire                          stage_1_error_out_rdy  ;  // <1,0>
  wire                          stage_1_error_out_vld  ;  // <1,0>
  wire                          stage_1_error_rdy  ;  // <1,0>
  wire                          stage_1_error_vld  ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// simple_st0
////////////////////////////////////////////////////////////////////////////////

simple_st0 simple_st0 (
    .clk(clk),
    .load_finish(load_finish),
    .reset(reset),
    .simple_st0_ctrl_data_fifo_data_ready(simple_st0_ctrl_data_fifo_data_ready),
    .simple_st0_ctrl_int(simple_st0_ctrl_int),
    .simple_st0_st_reg(simple_st0_st_reg),
    .stage_0_data(stage_0_data),
    .stage_0_data_fst(stage_0_data_fst),
    .stage_0_data_out(stage_0_data_out),
    .stage_0_data_out_fst(stage_0_data_out_fst),
    .stage_0_data_out_pre(stage_0_data_out_pre),
    .stage_0_data_out_pre_fst(stage_0_data_out_pre_fst),
    .stage_0_data_out_pre_rdy(stage_0_data_out_pre_rdy),
    .stage_0_data_out_pre_vld(stage_0_data_out_pre_vld),
    .stage_0_data_out_rdy(stage_0_data_out_rdy),
    .stage_0_data_out_vld(stage_0_data_out_vld),
    .stage_0_data_rdy(stage_0_data_rdy),
    .stage_0_data_vld(stage_0_data_vld),
    .stage_0_error(stage_0_error),
    .stage_0_error_fst(stage_0_error_fst),
    .stage_0_error_out(stage_0_error_out),
    .stage_0_error_out_fst(stage_0_error_out_fst),
    .stage_0_error_out_rdy(stage_0_error_out_rdy),
    .stage_0_error_out_vld(stage_0_error_out_vld),
    .stage_0_error_rdy(stage_0_error_rdy),
    .stage_0_error_vld(stage_0_error_vld),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));

////////////////////////////////////////////////////////////////////////////////
// simple_err
////////////////////////////////////////////////////////////////////////////////

simple_err simple_err (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .reset(reset),
    .stage_0_data_out(stage_0_data_out),
    .stage_0_data_out_fst(stage_0_data_out_fst),
    .stage_0_data_out_rdy(stage_0_data_out_rdy),
    .stage_0_data_out_vld(stage_0_data_out_vld),
    .stage_0_error(stage_0_error),
    .stage_0_error_fst(stage_0_error_fst),
    .stage_0_error_rdy(stage_0_error_rdy),
    .stage_0_error_vld(stage_0_error_vld));

assign stage_0_data_vld = st_data_vld;
assign stage_0_data_fst = st_data_fst;
assign stage_0_data = st_data;
assign st_data_rdy = stage_0_data_rdy;
assign st_error_vld = stage_0_error_out_vld;
assign st_error_fst = stage_0_error_out_fst;
assign st_error = stage_0_error_out;
assign stage_0_error_out_rdy = st_error_rdy;

// Attach the output stage to the last block
assign st_data_out_vld = stage_0_data_out_vld;
assign st_data_out_fst = stage_0_data_out_fst;
assign st_data_out = stage_0_data_out;
assign stage_0_data_out_rdy = st_data_out_rdy;
assign st_data_out_pre_vld = stage_0_data_out_pre_vld;
assign st_data_out_pre_fst = stage_0_data_out_pre_fst;
assign st_data_out_pre = stage_0_data_out_pre;
assign stage_0_data_out_pre_rdy = st_data_out_pre_rdy;
endmodule

