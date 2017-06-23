//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple_st0_mem_data
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple_st0_mem_data(
  input                         clk,
  input data_int_32_6           data_int,
  input                 [31:0]  data_int_wr_data,
  input                         reset,
  output                [31:0]  data_int_rd_data);

// Parameters 



// Wires 

  mem_int_0_32_6                mem_int_0;  // <1,0>
  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>
  wire                          write_sub         ;  // <1,1>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_data_0
////////////////////////////////////////////////////////////////////////////////

memory_32_6 simple_st0_mem_data_0 (
    .clk(clk),
    .m(mem_int_0),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

assign write_0 = data_int_wr_data[31:0];
assign mem_int_0 = data_int;
assign data_int_rd_data[31:0] = read_0;
endmodule

