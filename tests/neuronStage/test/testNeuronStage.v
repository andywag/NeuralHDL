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

  reg                   [31:0]  biasIn_0_mem[0:49984];  // <32,0>
  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  dataIn_0_mem[0:49984];  // <32,0>
  reg                   [31:0]  rout1_fptr        ;  // <32,0>
  reg                   [31:0]  rout_fptr         ;  // <32,0>
  reg                   [31:0]  tapIn_0_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_10_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_11_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_12_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_13_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_14_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_15_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_16_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_17_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_18_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_19_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_1_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_20_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_21_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_22_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_23_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_24_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_25_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_26_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_27_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_28_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_29_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_2_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_30_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_31_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_3_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_4_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_5_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_6_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_7_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_8_mem[0:49984];  // <32,0>
  reg                   [31:0]  tapIn_9_mem[0:49984];  // <32,0>
  reg                           valid             ;  // <1,0>


// Other

  float_24_8                    biasIn_0;  // <1,0>
  float_24_8                    biasIn_0_0;  // <1,0>
  float_24_8                    biasIn_0_1;  // <1,0>
  float_24_8                    biasIn_0_10;  // <1,0>
  float_24_8                    biasIn_0_11;  // <1,0>
  float_24_8                    biasIn_0_12;  // <1,0>
  float_24_8                    biasIn_0_13;  // <1,0>
  float_24_8                    biasIn_0_14;  // <1,0>
  float_24_8                    biasIn_0_15;  // <1,0>
  float_24_8                    biasIn_0_16;  // <1,0>
  float_24_8                    biasIn_0_17;  // <1,0>
  float_24_8                    biasIn_0_18;  // <1,0>
  float_24_8                    biasIn_0_19;  // <1,0>
  float_24_8                    biasIn_0_2;  // <1,0>
  float_24_8                    biasIn_0_20;  // <1,0>
  float_24_8                    biasIn_0_21;  // <1,0>
  float_24_8                    biasIn_0_22;  // <1,0>
  float_24_8                    biasIn_0_23;  // <1,0>
  float_24_8                    biasIn_0_24;  // <1,0>
  float_24_8                    biasIn_0_25;  // <1,0>
  float_24_8                    biasIn_0_26;  // <1,0>
  float_24_8                    biasIn_0_27;  // <1,0>
  float_24_8                    biasIn_0_28;  // <1,0>
  float_24_8                    biasIn_0_29;  // <1,0>
  float_24_8                    biasIn_0_3;  // <1,0>
  float_24_8                    biasIn_0_30;  // <1,0>
  float_24_8                    biasIn_0_31;  // <1,0>
  float_24_8                    biasIn_0_4;  // <1,0>
  float_24_8                    biasIn_0_5;  // <1,0>
  float_24_8                    biasIn_0_6;  // <1,0>
  float_24_8                    biasIn_0_7;  // <1,0>
  float_24_8                    biasIn_0_8;  // <1,0>
  float_24_8                    biasIn_0_9;  // <1,0>
  float_24_8                    dataIn_0;  // <1,0>
  float_24_8                    dataOutPre_0;  // <1,0>
  float_24_8                    dataOut_0;  // <1,0>
  float_24_8                    tapIn_0;  // <1,0>
  float_24_8                    tapIn_1;  // <1,0>
  float_24_8                    tapIn_10;  // <1,0>
  float_24_8                    tapIn_11;  // <1,0>
  float_24_8                    tapIn_12;  // <1,0>
  float_24_8                    tapIn_13;  // <1,0>
  float_24_8                    tapIn_14;  // <1,0>
  float_24_8                    tapIn_15;  // <1,0>
  float_24_8                    tapIn_16;  // <1,0>
  float_24_8                    tapIn_17;  // <1,0>
  float_24_8                    tapIn_18;  // <1,0>
  float_24_8                    tapIn_19;  // <1,0>
  float_24_8                    tapIn_2;  // <1,0>
  float_24_8                    tapIn_20;  // <1,0>
  float_24_8                    tapIn_21;  // <1,0>
  float_24_8                    tapIn_22;  // <1,0>
  float_24_8                    tapIn_23;  // <1,0>
  float_24_8                    tapIn_24;  // <1,0>
  float_24_8                    tapIn_25;  // <1,0>
  float_24_8                    tapIn_26;  // <1,0>
  float_24_8                    tapIn_27;  // <1,0>
  float_24_8                    tapIn_28;  // <1,0>
  float_24_8                    tapIn_29;  // <1,0>
  float_24_8                    tapIn_3;  // <1,0>
  float_24_8                    tapIn_30;  // <1,0>
  float_24_8                    tapIn_31;  // <1,0>
  float_24_8                    tapIn_4;  // <1,0>
  float_24_8                    tapIn_5;  // <1,0>
  float_24_8                    tapIn_6;  // <1,0>
  float_24_8                    tapIn_7;  // <1,0>
  float_24_8                    tapIn_8;  // <1,0>
  float_24_8                    tapIn_9;  // <1,0>


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
    .tapIn_10(tapIn_10),
    .tapIn_11(tapIn_11),
    .tapIn_12(tapIn_12),
    .tapIn_13(tapIn_13),
    .tapIn_14(tapIn_14),
    .tapIn_15(tapIn_15),
    .tapIn_16(tapIn_16),
    .tapIn_17(tapIn_17),
    .tapIn_18(tapIn_18),
    .tapIn_19(tapIn_19),
    .tapIn_2(tapIn_2),
    .tapIn_20(tapIn_20),
    .tapIn_21(tapIn_21),
    .tapIn_22(tapIn_22),
    .tapIn_23(tapIn_23),
    .tapIn_24(tapIn_24),
    .tapIn_25(tapIn_25),
    .tapIn_26(tapIn_26),
    .tapIn_27(tapIn_27),
    .tapIn_28(tapIn_28),
    .tapIn_29(tapIn_29),
    .tapIn_3(tapIn_3),
    .tapIn_30(tapIn_30),
    .tapIn_31(tapIn_31),
    .tapIn_4(tapIn_4),
    .tapIn_5(tapIn_5),
    .tapIn_6(tapIn_6),
    .tapIn_7(tapIn_7),
    .tapIn_8(tapIn_8),
    .tapIn_9(tapIn_9),
    .valid(valid));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd50000)) begin
      $finish;
    end
  end
end

// Create a valid pulse
assign valid = (counter[4:0] == 'd30);

// Delay the bias to line up the data
assign d_index = counter[30:0] - 31'd30;

// Load dataIn_0
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/data.hex",dataIn_0_mem);
end

assign dataIn_0 = dataIn_0_mem[d_index];

// Load tapIn_0
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap0.hex",tapIn_0_mem);
end

assign tapIn_0 = tapIn_0_mem[d_index];

// Load tapIn_1
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap1.hex",tapIn_1_mem);
end

assign tapIn_1 = tapIn_1_mem[d_index];

// Load tapIn_2
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap2.hex",tapIn_2_mem);
end

assign tapIn_2 = tapIn_2_mem[d_index];

// Load tapIn_3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap3.hex",tapIn_3_mem);
end

assign tapIn_3 = tapIn_3_mem[d_index];

// Load tapIn_4
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap4.hex",tapIn_4_mem);
end

assign tapIn_4 = tapIn_4_mem[d_index];

// Load tapIn_5
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap5.hex",tapIn_5_mem);
end

assign tapIn_5 = tapIn_5_mem[d_index];

// Load tapIn_6
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap6.hex",tapIn_6_mem);
end

assign tapIn_6 = tapIn_6_mem[d_index];

// Load tapIn_7
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap7.hex",tapIn_7_mem);
end

assign tapIn_7 = tapIn_7_mem[d_index];

// Load tapIn_8
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap8.hex",tapIn_8_mem);
end

assign tapIn_8 = tapIn_8_mem[d_index];

// Load tapIn_9
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap9.hex",tapIn_9_mem);
end

assign tapIn_9 = tapIn_9_mem[d_index];

// Load tapIn_10
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap10.hex",tapIn_10_mem);
end

assign tapIn_10 = tapIn_10_mem[d_index];

// Load tapIn_11
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap11.hex",tapIn_11_mem);
end

assign tapIn_11 = tapIn_11_mem[d_index];

// Load tapIn_12
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap12.hex",tapIn_12_mem);
end

assign tapIn_12 = tapIn_12_mem[d_index];

// Load tapIn_13
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap13.hex",tapIn_13_mem);
end

assign tapIn_13 = tapIn_13_mem[d_index];

// Load tapIn_14
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap14.hex",tapIn_14_mem);
end

assign tapIn_14 = tapIn_14_mem[d_index];

// Load tapIn_15
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap15.hex",tapIn_15_mem);
end

assign tapIn_15 = tapIn_15_mem[d_index];

// Load tapIn_16
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap16.hex",tapIn_16_mem);
end

assign tapIn_16 = tapIn_16_mem[d_index];

// Load tapIn_17
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap17.hex",tapIn_17_mem);
end

assign tapIn_17 = tapIn_17_mem[d_index];

// Load tapIn_18
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap18.hex",tapIn_18_mem);
end

assign tapIn_18 = tapIn_18_mem[d_index];

// Load tapIn_19
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap19.hex",tapIn_19_mem);
end

assign tapIn_19 = tapIn_19_mem[d_index];

// Load tapIn_20
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap20.hex",tapIn_20_mem);
end

assign tapIn_20 = tapIn_20_mem[d_index];

// Load tapIn_21
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap21.hex",tapIn_21_mem);
end

assign tapIn_21 = tapIn_21_mem[d_index];

// Load tapIn_22
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap22.hex",tapIn_22_mem);
end

assign tapIn_22 = tapIn_22_mem[d_index];

// Load tapIn_23
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap23.hex",tapIn_23_mem);
end

assign tapIn_23 = tapIn_23_mem[d_index];

// Load tapIn_24
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap24.hex",tapIn_24_mem);
end

assign tapIn_24 = tapIn_24_mem[d_index];

// Load tapIn_25
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap25.hex",tapIn_25_mem);
end

assign tapIn_25 = tapIn_25_mem[d_index];

// Load tapIn_26
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap26.hex",tapIn_26_mem);
end

assign tapIn_26 = tapIn_26_mem[d_index];

// Load tapIn_27
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap27.hex",tapIn_27_mem);
end

assign tapIn_27 = tapIn_27_mem[d_index];

// Load tapIn_28
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap28.hex",tapIn_28_mem);
end

assign tapIn_28 = tapIn_28_mem[d_index];

// Load tapIn_29
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap29.hex",tapIn_29_mem);
end

assign tapIn_29 = tapIn_29_mem[d_index];

// Load tapIn_30
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap30.hex",tapIn_30_mem);
end

assign tapIn_30 = tapIn_30_mem[d_index];

// Load tapIn_31
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/tap31.hex",tapIn_31_mem);
end

assign tapIn_31 = tapIn_31_mem[d_index];

// Load biasIn_0
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neuronStage/data/bias.hex",biasIn_0_mem);
end

assign biasIn_0 = biasIn_0_mem[counter];

// Store Store dataOutPre_0
initial begin
  rout_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/neuronStage/data/rout.hex","w");
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
  rout1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/neuronStage/data/rout1.hex","w");
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

