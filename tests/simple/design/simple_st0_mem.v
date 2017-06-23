//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple_st0_mem
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple_st0_mem(
  input bias_int_32_4           bias_int,
  input                 [31:0]  bias_int_wr_data,
  input                         clk,
  input data_int_32_6           data_int,
  input                 [31:0]  data_int_wr_data,
  input                         reset,
  input tap_int_192_4           tap_int,
  input                 [191:0] tap_int_wr_data,
  output                [31:0]  bias_int_rd_data,
  output                [31:0]  data_int_rd_data,
  output                [191:0] tap_int_rd_data);

// Parameters 



// Wires 



// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap
////////////////////////////////////////////////////////////////////////////////

simple_st0_mem_tap simple_st0_mem_tap (
    .clk(clk),
    .reset(reset),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_bias
////////////////////////////////////////////////////////////////////////////////

simple_st0_mem_bias simple_st0_mem_bias (
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .clk(clk),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_data
////////////////////////////////////////////////////////////////////////////////

simple_st0_mem_data simple_st0_mem_data (
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .reset(reset));

endmodule

