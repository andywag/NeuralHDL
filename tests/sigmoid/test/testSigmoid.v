//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testSigmoid
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------
`timescale 10ns/1ns


`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testSigmoid(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [30:0]  d_index           ;  // <31,0>


// Registers 

  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  data_mem[0:1000000];  // <32,0>
  reg                   [31:0]  rout_fptr         ;  // <32,0>


// Other

  float_24_8                    data;  // <1,0>
  float_24_8                    out;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// sigmoid
////////////////////////////////////////////////////////////////////////////////

sigmoid sigmoid (
    .clk(clk),
    .data(data),
    .out(out),
    .reset(reset));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd1000000)) begin
      $finish;
    end
  end
end

// Load data
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/sigmoid/data/data.hex",data_mem);
end

assign data = data_mem[counter];

// Store Store out
initial begin
  rout_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/sigmoid/data/rout.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout_fptr,"%h ",out);
  end
end

// Counter to Index Test
always @(posedge clk) begin
  if (reset) begin
    counter <= 32'd0;
  end
  else begin
    counter <= counter[31:0] + 32'd1;
  end
end

// Initial Statement
initial begin
  
end


// DUT
endmodule

