//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testNeuron
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------
`timescale 10ns/1ns


`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testNeuron(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [30:0]  d_index           ;  // <31,0>


// Registers 

  reg                   [31:0]  bias_mem[0:1000]  ;  // <32,0>
  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  data_mem[0:1000]  ;  // <32,0>
  reg                   [31:0]  rout_fptr         ;  // <32,0>
  reg                   [31:0]  tap_mem [0:1000]  ;  // <32,0>


// Other

  float_24_8                    bias;  // <1,0>
  float_24_8                    data;  // <1,0>
  float_24_8                    out;  // <1,0>
  float_24_8                    tap;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// neuron
////////////////////////////////////////////////////////////////////////////////

neuron neuron (
    .bias(bias),
    .clk(clk),
    .data(data),
    .out(out),
    .reset(reset),
    .tap(tap));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd1000)) begin
      $finish;
    end
  end
end

// Delay the bias to line up the data
assign d_index = counter[30:0] - 31'd1;

// Load data
initial begin
  $readmemh("/home/andy/IdeaProjects/ScalaHDL/tests/neuron/data/data.hex",data_mem);
end

assign data = data_mem[counter];

// Load tap
initial begin
  $readmemh("/home/andy/IdeaProjects/ScalaHDL/tests/neuron/data/tap.hex",tap_mem);
end

assign tap = tap_mem[counter];

// Load bias
initial begin
  $readmemh("/home/andy/IdeaProjects/ScalaHDL/tests/neuron/data/bias.hex",bias_mem);
end

assign bias = bias_mem[counter];

// Store Store out
initial begin
  rout_fptr = $fopen("/home/andy/IdeaProjects/ScalaHDL/tests/neuron/data/rout.hex","w");
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

