//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       memory_32_5
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module memory_32_5(
  input                         clk,
  input m_32_5                  m,
  input                 [31:0]  m_wr_data,
  input                         reset,
  output                [31:0]  m_rd_data);

// Parameters 



// Wires 



// Registers 

  reg                   [31:0]  memory_32_5_memory [0:32] ;  // <32,0>
  reg                   [4:0]   memory_32_5_read_address  ;  // <5,0>


// Other




// Read Address Input Data
always @(posedge clk) begin
  if (reset) begin
    memory_32_5_read_address <= 5'd0;
  end
  else begin
    memory_32_5_read_address <= m.rd_address;
  end
end

// Write Data
always @(posedge clk) begin
  if (m.wr_vld) begin
    memory_32_5_memory[m.wr_address] <= m_wr_data;
  end
end

// Read Operation
always @(posedge clk) begin
  if (reset) begin
    m_rd_data <= 32'd0;
  end
  else begin
    m_rd_data <= memory_32_5_memory[memory_32_5_read_address];
  end
end
endmodule

