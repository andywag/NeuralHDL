//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testNeuronStage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------
`timescale 10ns/1ns


`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testNeuronStage(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [30:0]  d_index           ;  // <31,0>


// Registers 

  reg                   [31:0]  biasIn_0_mem[0:1000];  // <32,0>
  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  dataIn_0_mem[0:1000];  // <32,0>
  reg                   [31:0]  rout1_fptr        ;  // <32,0>
  reg                   [31:0]  rout_fptr         ;  // <32,0>
  reg                   [31:0]  tapIn_0_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_1_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_2_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_3_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_4_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_5_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_6_mem[0:8000];  // <32,0>
  reg                   [31:0]  tapIn_7_mem[0:8000];  // <32,0>
  reg                           valid             ;  // <1,0>


// Other

  float_24_8                    biasIn_0;  // <1,0>
  float_24_8                    biasIn_0_0;  // <1,0>
  float_24_8                    biasIn_0_1;  // <1,0>
  float_24_8                    biasIn_0_2;  // <1,0>
  float_24_8                    biasIn_0_3;  // <1,0>
  float_24_8                    biasIn_0_4;  // <1,0>
  float_24_8                    biasIn_0_5;  // <1,0>
  float_24_8                    biasIn_0_6;  // <1,0>
  float_24_8                    biasIn_0_7;  // <1,0>
  float_24_8                    dataIn_0;  // <1,0>
  float_24_8                    dataOutPre_0;  // <1,0>
  float_24_8                    dataOut_0;  // <1,0>
  float_24_8                    tapIn_0;  // <1,0>
  float_24_8                    tapIn_1;  // <1,0>
  float_24_8                    tapIn_2;  // <1,0>
  float_24_8                    tapIn_3;  // <1,0>
  float_24_8                    tapIn_4;  // <1,0>
  float_24_8                    tapIn_5;  // <1,0>
  float_24_8                    tapIn_6;  // <1,0>
  float_24_8                    tapIn_7;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// neuronStage
////////////////////////////////////////////////////////////////////////////////

neuronStage neuronStage (
    .biasIn_0(biasIn_0),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOutPre_0(dataOutPre_0),
    .dataOut_0(dataOut_0),
    .reset(reset),
    .tapIn_0(tapIn_0),
    .tapIn_1(tapIn_1),
    .tapIn_2(tapIn_2),
    .tapIn_3(tapIn_3),
    .tapIn_4(tapIn_4),
    .tapIn_5(tapIn_5),
    .tapIn_6(tapIn_6),
    .tapIn_7(tapIn_7),
    .valid(valid));


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

// Create a valid pulse
assign valid = (counter[2:0] == 'd6);

// Delay the bias to line up the data
assign d_index = counter[30:0] - 31'd6;

// Load dataIn_0
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/data.hex",dataIn_0_mem);
end

assign dataIn_0 = dataIn_0_mem[d_index];

// Load tapIn_0
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap0.hex",tapIn_0_mem);
end

assign tapIn_0 = tapIn_0_mem[d_index];

// Load tapIn_1
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap1.hex",tapIn_1_mem);
end

assign tapIn_1 = tapIn_1_mem[d_index];

// Load tapIn_2
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap2.hex",tapIn_2_mem);
end

assign tapIn_2 = tapIn_2_mem[d_index];

// Load tapIn_3
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap3.hex",tapIn_3_mem);
end

assign tapIn_3 = tapIn_3_mem[d_index];

// Load tapIn_4
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap4.hex",tapIn_4_mem);
end

assign tapIn_4 = tapIn_4_mem[d_index];

// Load tapIn_5
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap5.hex",tapIn_5_mem);
end

assign tapIn_5 = tapIn_5_mem[d_index];

// Load tapIn_6
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap6.hex",tapIn_6_mem);
end

assign tapIn_6 = tapIn_6_mem[d_index];

// Load tapIn_7
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/tap7.hex",tapIn_7_mem);
end

assign tapIn_7 = tapIn_7_mem[d_index];

// Load biasIn_0
initial begin
  $readmemh("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/bias.hex",biasIn_0_mem);
end

assign biasIn_0 = biasIn_0_mem[counter];

// Store Store dataOutPre_0
initial begin
  rout_fptr = $fopen("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/rout.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout_fptr,"%h ",dataOutPre_0);
  end
end

// Store Store dataOut_0
initial begin
  rout1_fptr = $fopen("/home/andy/IdeaProjects/NeuralHDL/tests/neuronStage/data/rout1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout1_fptr,"%h ",dataOut_0);
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

