//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st1_mem_bias
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st1_mem_bias(
  input bias_int_32_4           bias_int,
  input                 [31:0]  bias_int_wr_data,
  input                         clk,
  input                         reset,
  output                [31:0]  bias_int_rd_data);

// Parameters 



// Wires 

  mem_int_0_32_4                mem_int_0;  // <1,0>
  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>
  wire                          write_sub         ;  // <1,1>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// four_12_12_st1_mem_bias_0
////////////////////////////////////////////////////////////////////////////////

memory_32_4 four_12_12_st1_mem_bias_0 (
    .clk(clk),
    .m(mem_int_0),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

assign write_0 = bias_int_wr_data[31:0];
assign mem_int_0 = bias_int;
assign bias_int_rd_data[31:0] = read_0;
endmodule

