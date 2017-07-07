//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple_err
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple_err(
  input                         clk,
  input float_24_8              expected,
  input                         expected_fst,
  input                         expected_vld,
  input                         reset,
  input float_24_8              stage_0_data_out,
  input                         stage_0_data_out_fst,
  input                         stage_0_data_out_vld,
  output                        expected_rdy,
  output                        stage_0_data_out_rdy,
  output float_24_8             stage_0_error,
  output                        stage_0_error_fst,
  output                        stage_0_error_rdy,
  output                        stage_0_error_vld);

// Parameters 



// Wires 

  expected_int_32_6             expected_int;  // <1,0>
  wire                  [31:0]  expected_int_rd_data  ;  // <32,0>
  wire                  [31:0]  expected_int_wr_data  ;  // <32,0>
  wire                          stage_0_error_rdy  ;  // <1,0>
  float_24_8                    zctrl;  // <1,0>
  wire                          zctrl_fst         ;  // <1,0>
  wire                          zctrl_rdy         ;  // <1,0>
  wire                          zctrl_vld         ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// simple_err_expect
////////////////////////////////////////////////////////////////////////////////

simple_err_expect simple_err_expect (
    .clk(clk),
    .expected_int(expected_int),
    .expected_int_rd_data(expected_int_rd_data),
    .expected_int_wr_data(expected_int_wr_data),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_err_ctrl
////////////////////////////////////////////////////////////////////////////////

simple_err_ctrl simple_err_ctrl (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_int(expected_int),
    .expected_int_rd_data(expected_int_rd_data),
    .expected_int_wr_data(expected_int_wr_data),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .reset(reset),
    .stage_0_data_out(stage_0_data_out),
    .stage_0_data_out_fst(stage_0_data_out_fst),
    .stage_0_data_out_rdy(stage_0_data_out_rdy),
    .stage_0_data_out_vld(stage_0_data_out_vld),
    .zctrl(zctrl),
    .zctrl_fst(zctrl_fst),
    .zctrl_rdy(zctrl_rdy),
    .zctrl_vld(zctrl_vld));

////////////////////////////////////////////////////////////////////////////////
// simple_err_fifo
////////////////////////////////////////////////////////////////////////////////

simple_err_fifo simple_err_fifo (
    .clk(clk),
    .reset(reset),
    .stage_0_error(stage_0_error),
    .stage_0_error_fst(stage_0_error_fst),
    .stage_0_error_rdy(stage_0_error_rdy),
    .stage_0_error_vld(stage_0_error_vld),
    .zctrl(zctrl),
    .zctrl_fst(zctrl_fst),
    .zctrl_rdy(zctrl_rdy),
    .zctrl_vld(zctrl_vld));

endmodule

