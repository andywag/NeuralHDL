//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12(
  input                         clk,
  input float_24_8              expected,
  input                         expected_fst,
  input                         expected_vld,
  input four_12_12_st0_ctrl_int_t four_12_12_st0_ctrl_int,
  input four_12_12_st0_st_reg_t four_12_12_st0_st_reg,
  input four_12_12_st1_ctrl_int_t four_12_12_st1_ctrl_int,
  input four_12_12_st1_st_reg_t four_12_12_st1_st_reg,
  input four_12_12_st2_ctrl_int_t four_12_12_st2_ctrl_int,
  input four_12_12_st2_st_reg_t four_12_12_st2_st_reg,
  input four_12_12_st3_ctrl_int_t four_12_12_st3_ctrl_int,
  input four_12_12_st3_st_reg_t four_12_12_st3_st_reg,
  input                         reset,
  input float_24_8              st_data,
  input                         st_data_fst,
  input                         st_data_out_pre_rdy,
  input                         st_data_out_rdy,
  input                         st_data_vld,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  output                        expected_rdy,
  output                        four_12_12_st0_ctrl_data_fifo_data_ready,
  output                        four_12_12_st1_ctrl_data_fifo_data_ready,
  output                        four_12_12_st2_ctrl_data_fifo_data_ready,
  output                        four_12_12_st3_ctrl_data_fifo_data_ready,
  output                        load_finish,
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
  float_24_8                    stage_2_data;  // <1,0>
  wire                          stage_2_data_fst  ;  // <1,0>
  float_24_8                    stage_2_data_out;  // <1,0>
  wire                          stage_2_data_out_fst  ;  // <1,0>
  float_24_8                    stage_2_data_out_pre;  // <1,0>
  wire                          stage_2_data_out_pre_fst  ;  // <1,0>
  wire                          stage_2_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_2_data_out_pre_vld  ;  // <1,0>
  wire                          stage_2_data_out_rdy  ;  // <1,0>
  wire                          stage_2_data_out_vld  ;  // <1,0>
  wire                          stage_2_data_rdy  ;  // <1,0>
  wire                          stage_2_data_vld  ;  // <1,0>
  float_24_8                    stage_2_error;  // <1,0>
  wire                          stage_2_error_fst  ;  // <1,0>
  float_24_8                    stage_2_error_out;  // <1,0>
  wire                          stage_2_error_out_fst  ;  // <1,0>
  wire                          stage_2_error_out_rdy  ;  // <1,0>
  wire                          stage_2_error_out_vld  ;  // <1,0>
  wire                          stage_2_error_rdy  ;  // <1,0>
  wire                          stage_2_error_vld  ;  // <1,0>
  float_24_8                    stage_3_data;  // <1,0>
  wire                          stage_3_data_fst  ;  // <1,0>
  float_24_8                    stage_3_data_out;  // <1,0>
  wire                          stage_3_data_out_fst  ;  // <1,0>
  float_24_8                    stage_3_data_out_pre;  // <1,0>
  wire                          stage_3_data_out_pre_fst  ;  // <1,0>
  wire                          stage_3_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_3_data_out_pre_vld  ;  // <1,0>
  wire                          stage_3_data_out_rdy  ;  // <1,0>
  wire                          stage_3_data_out_vld  ;  // <1,0>
  wire                          stage_3_data_rdy  ;  // <1,0>
  wire                          stage_3_data_vld  ;  // <1,0>
  float_24_8                    stage_3_error;  // <1,0>
  wire                          stage_3_error_fst  ;  // <1,0>
  float_24_8                    stage_3_error_out;  // <1,0>
  wire                          stage_3_error_out_fst  ;  // <1,0>
  wire                          stage_3_error_out_rdy  ;  // <1,0>
  wire                          stage_3_error_out_vld  ;  // <1,0>
  wire                          stage_3_error_rdy  ;  // <1,0>
  wire                          stage_3_error_vld  ;  // <1,0>
  float_24_8                    stage_4_data;  // <1,0>
  wire                          stage_4_data_fst  ;  // <1,0>
  float_24_8                    stage_4_data_out;  // <1,0>
  wire                          stage_4_data_out_fst  ;  // <1,0>
  float_24_8                    stage_4_data_out_pre;  // <1,0>
  wire                          stage_4_data_out_pre_fst  ;  // <1,0>
  wire                          stage_4_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_4_data_out_pre_vld  ;  // <1,0>
  wire                          stage_4_data_out_rdy  ;  // <1,0>
  wire                          stage_4_data_out_vld  ;  // <1,0>
  wire                          stage_4_data_rdy  ;  // <1,0>
  wire                          stage_4_data_vld  ;  // <1,0>
  float_24_8                    stage_4_error;  // <1,0>
  wire                          stage_4_error_fst  ;  // <1,0>
  float_24_8                    stage_4_error_out;  // <1,0>
  wire                          stage_4_error_out_fst  ;  // <1,0>
  wire                          stage_4_error_out_rdy  ;  // <1,0>
  wire                          stage_4_error_out_vld  ;  // <1,0>
  wire                          stage_4_error_rdy  ;  // <1,0>
  wire                          stage_4_error_vld  ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// four_12_12_st0
////////////////////////////////////////////////////////////////////////////////

four_12_12_st0 four_12_12_st0 (
    .clk(clk),
    .four_12_12_st0_ctrl_data_fifo_data_ready(four_12_12_st0_ctrl_data_fifo_data_ready),
    .four_12_12_st0_ctrl_int(four_12_12_st0_ctrl_int),
    .four_12_12_st0_st_reg(four_12_12_st0_st_reg),
    .load_finish(load_finish),
    .reset(reset),
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
// four_12_12_st1
////////////////////////////////////////////////////////////////////////////////

four_12_12_st1 four_12_12_st1 (
    .clk(clk),
    .four_12_12_st1_ctrl_data_fifo_data_ready(four_12_12_st1_ctrl_data_fifo_data_ready),
    .four_12_12_st1_ctrl_int(four_12_12_st1_ctrl_int),
    .four_12_12_st1_st_reg(four_12_12_st1_st_reg),
    .load_finish(load_finish),
    .reset(reset),
    .stage_1_data(stage_1_data),
    .stage_1_data_fst(stage_1_data_fst),
    .stage_1_data_out(stage_1_data_out),
    .stage_1_data_out_fst(stage_1_data_out_fst),
    .stage_1_data_out_pre(stage_1_data_out_pre),
    .stage_1_data_out_pre_fst(stage_1_data_out_pre_fst),
    .stage_1_data_out_pre_rdy(stage_1_data_out_pre_rdy),
    .stage_1_data_out_pre_vld(stage_1_data_out_pre_vld),
    .stage_1_data_out_rdy(stage_1_data_out_rdy),
    .stage_1_data_out_vld(stage_1_data_out_vld),
    .stage_1_data_rdy(stage_1_data_rdy),
    .stage_1_data_vld(stage_1_data_vld),
    .stage_1_error(stage_1_error),
    .stage_1_error_fst(stage_1_error_fst),
    .stage_1_error_out(stage_1_error_out),
    .stage_1_error_out_fst(stage_1_error_out_fst),
    .stage_1_error_out_rdy(stage_1_error_out_rdy),
    .stage_1_error_out_vld(stage_1_error_out_vld),
    .stage_1_error_rdy(stage_1_error_rdy),
    .stage_1_error_vld(stage_1_error_vld),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st2
////////////////////////////////////////////////////////////////////////////////

four_12_12_st2 four_12_12_st2 (
    .clk(clk),
    .four_12_12_st2_ctrl_data_fifo_data_ready(four_12_12_st2_ctrl_data_fifo_data_ready),
    .four_12_12_st2_ctrl_int(four_12_12_st2_ctrl_int),
    .four_12_12_st2_st_reg(four_12_12_st2_st_reg),
    .load_finish(load_finish),
    .reset(reset),
    .stage_2_data(stage_2_data),
    .stage_2_data_fst(stage_2_data_fst),
    .stage_2_data_out(stage_2_data_out),
    .stage_2_data_out_fst(stage_2_data_out_fst),
    .stage_2_data_out_pre(stage_2_data_out_pre),
    .stage_2_data_out_pre_fst(stage_2_data_out_pre_fst),
    .stage_2_data_out_pre_rdy(stage_2_data_out_pre_rdy),
    .stage_2_data_out_pre_vld(stage_2_data_out_pre_vld),
    .stage_2_data_out_rdy(stage_2_data_out_rdy),
    .stage_2_data_out_vld(stage_2_data_out_vld),
    .stage_2_data_rdy(stage_2_data_rdy),
    .stage_2_data_vld(stage_2_data_vld),
    .stage_2_error(stage_2_error),
    .stage_2_error_fst(stage_2_error_fst),
    .stage_2_error_out(stage_2_error_out),
    .stage_2_error_out_fst(stage_2_error_out_fst),
    .stage_2_error_out_rdy(stage_2_error_out_rdy),
    .stage_2_error_out_vld(stage_2_error_out_vld),
    .stage_2_error_rdy(stage_2_error_rdy),
    .stage_2_error_vld(stage_2_error_vld),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3
////////////////////////////////////////////////////////////////////////////////

four_12_12_st3 four_12_12_st3 (
    .clk(clk),
    .four_12_12_st3_ctrl_data_fifo_data_ready(four_12_12_st3_ctrl_data_fifo_data_ready),
    .four_12_12_st3_ctrl_int(four_12_12_st3_ctrl_int),
    .four_12_12_st3_st_reg(four_12_12_st3_st_reg),
    .load_finish(load_finish),
    .reset(reset),
    .stage_3_data(stage_3_data),
    .stage_3_data_fst(stage_3_data_fst),
    .stage_3_data_out(stage_3_data_out),
    .stage_3_data_out_fst(stage_3_data_out_fst),
    .stage_3_data_out_pre(stage_3_data_out_pre),
    .stage_3_data_out_pre_fst(stage_3_data_out_pre_fst),
    .stage_3_data_out_pre_rdy(stage_3_data_out_pre_rdy),
    .stage_3_data_out_pre_vld(stage_3_data_out_pre_vld),
    .stage_3_data_out_rdy(stage_3_data_out_rdy),
    .stage_3_data_out_vld(stage_3_data_out_vld),
    .stage_3_data_rdy(stage_3_data_rdy),
    .stage_3_data_vld(stage_3_data_vld),
    .stage_3_error(stage_3_error),
    .stage_3_error_fst(stage_3_error_fst),
    .stage_3_error_out(stage_3_error_out),
    .stage_3_error_out_fst(stage_3_error_out_fst),
    .stage_3_error_out_rdy(stage_3_error_out_rdy),
    .stage_3_error_out_vld(stage_3_error_out_vld),
    .stage_3_error_rdy(stage_3_error_rdy),
    .stage_3_error_vld(stage_3_error_vld),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_err
////////////////////////////////////////////////////////////////////////////////

four_12_12_err four_12_12_err (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .reset(reset),
    .stage_3_data_out(stage_3_data_out),
    .stage_3_data_out_fst(stage_3_data_out_fst),
    .stage_3_data_out_rdy(stage_3_data_out_rdy),
    .stage_3_data_out_vld(stage_3_data_out_vld),
    .stage_3_error(stage_3_error),
    .stage_3_error_fst(stage_3_error_fst),
    .stage_3_error_rdy(stage_3_error_rdy),
    .stage_3_error_vld(stage_3_error_vld));

assign stage_0_data_vld = st_data_vld;
assign stage_0_data_fst = st_data_fst;
assign stage_0_data = st_data;
assign st_data_rdy = stage_0_data_rdy;
assign st_error_vld = stage_0_error_out_vld;
assign st_error_fst = stage_0_error_out_fst;
assign st_error = stage_0_error_out;
assign stage_0_error_out_rdy = st_error_rdy;
assign stage_1_data_vld = stage_0_data_out_vld;
assign stage_1_data_fst = stage_0_data_out_fst;
assign stage_1_data = stage_0_data_out;
assign stage_0_data_out_rdy = stage_1_data_rdy;
assign stage_0_error_vld = stage_1_error_out_vld;
assign stage_0_error_fst = stage_1_error_out_fst;
assign stage_0_error = stage_1_error_out;
assign stage_1_error_out_rdy = stage_0_error_rdy;
assign stage_2_data_vld = stage_1_data_out_vld;
assign stage_2_data_fst = stage_1_data_out_fst;
assign stage_2_data = stage_1_data_out;
assign stage_1_data_out_rdy = stage_2_data_rdy;
assign stage_1_error_vld = stage_2_error_out_vld;
assign stage_1_error_fst = stage_2_error_out_fst;
assign stage_1_error = stage_2_error_out;
assign stage_2_error_out_rdy = stage_1_error_rdy;
assign stage_3_data_vld = stage_2_data_out_vld;
assign stage_3_data_fst = stage_2_data_out_fst;
assign stage_3_data = stage_2_data_out;
assign stage_2_data_out_rdy = stage_3_data_rdy;
assign stage_2_error_vld = stage_3_error_out_vld;
assign stage_2_error_fst = stage_3_error_out_fst;
assign stage_2_error = stage_3_error_out;
assign stage_3_error_out_rdy = stage_2_error_rdy;

// Attach the output stage to the last block
assign st_data_out_vld = stage_3_data_out_vld;
assign st_data_out_fst = stage_3_data_out_fst;
assign st_data_out = stage_3_data_out;
assign stage_3_data_out_rdy = st_data_out_rdy;
assign st_data_out_pre_vld = stage_3_data_out_pre_vld;
assign st_data_out_pre_fst = stage_3_data_out_pre_fst;
assign st_data_out_pre = stage_3_data_out_pre;
assign stage_3_data_out_pre_rdy = st_data_out_pre_rdy;
endmodule

