//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_err
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_err(
  input                         clk,
  input float_24_8              expected,
  input                         expected_fst,
  input                         expected_vld,
  input                         reset,
  input float_24_8              stage_1_data_out,
  input                         stage_1_data_out_fst,
  input                         stage_1_data_out_vld,
  output                        expected_rdy,
  output                        stage_1_data_out_rdy,
  output float_24_8             stage_1_error,
  output                        stage_1_error_fst,
  output                        stage_1_error_rdy,
  output                        stage_1_error_vld);

// Parameters 



// Wires 

  expected_int_32_6             expected_int;  // <1,0>
  wire                  [31:0]  expected_int_rd_data  ;  // <32,0>
  wire                  [31:0]  expected_int_wr_data  ;  // <32,0>
  wire                          stage_1_error_rdy  ;  // <1,0>
  float_24_8                    zctrl;  // <1,0>
  wire                          zctrl_fst         ;  // <1,0>
  wire                          zctrl_rdy         ;  // <1,0>
  wire                          zctrl_vld         ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// full_err_expect
////////////////////////////////////////////////////////////////////////////////

full_err_expect full_err_expect (
    .clk(clk),
    .expected_int(expected_int),
    .expected_int_rd_data(expected_int_rd_data),
    .expected_int_wr_data(expected_int_wr_data),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// full_err_ctrl
////////////////////////////////////////////////////////////////////////////////

full_err_ctrl full_err_ctrl (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_int(expected_int),
    .expected_int_rd_data(expected_int_rd_data),
    .expected_int_wr_data(expected_int_wr_data),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .reset(reset),
    .stage_1_data_out(stage_1_data_out),
    .stage_1_data_out_fst(stage_1_data_out_fst),
    .stage_1_data_out_rdy(stage_1_data_out_rdy),
    .stage_1_data_out_vld(stage_1_data_out_vld),
    .zctrl(zctrl),
    .zctrl_fst(zctrl_fst),
    .zctrl_rdy(zctrl_rdy),
    .zctrl_vld(zctrl_vld));

////////////////////////////////////////////////////////////////////////////////
// full_err_fifo
////////////////////////////////////////////////////////////////////////////////

full_err_fifo full_err_fifo (
    .clk(clk),
    .reset(reset),
    .stage_1_error(stage_1_error),
    .stage_1_error_fst(stage_1_error_fst),
    .stage_1_error_rdy(stage_1_error_rdy),
    .stage_1_error_vld(stage_1_error_vld),
    .zctrl(zctrl),
    .zctrl_fst(zctrl_fst),
    .zctrl_rdy(zctrl_rdy),
    .zctrl_vld(zctrl_vld));

endmodule

