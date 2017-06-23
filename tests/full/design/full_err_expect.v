//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_err_expect
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_err_expect(
  input                         clk,
  input expected_int_32_6       expected_int,
  input                 [31:0]  expected_int_wr_data,
  input                         reset,
  output                [31:0]  expected_int_rd_data);

// Parameters 



// Wires 

  mem_int_0_32_6                mem_int_0;  // <1,0>
  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>
  wire                          write_sub         ;  // <1,1>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// full_err_expect_0
////////////////////////////////////////////////////////////////////////////////

memory_32_6 full_err_expect_0 (
    .clk(clk),
    .m(mem_int_0),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

assign write_0 = expected_int_wr_data[31:0];
assign mem_int_0 = expected_int;
assign expected_int_rd_data[31:0] = read_0;
endmodule

