//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage_mem_data
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage_mem_data(
  input                         clk,
  input data_int_32_6           data_int,
  input                 [31:0]  data_int_wr_data,
  input                         reset,
  output                [31:0]  data_int_rd_data);

// Parameters 



// Wires 

  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// stage_mem_data_0
////////////////////////////////////////////////////////////////////////////////

memory_32_6 stage_mem_data_0 (
    .clk(clk),
    .m(data_int),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

assign write_0 = data_int_wr_data[31:0];
assign data_int_rd_data[31:0] = read_0;
endmodule

