//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       neuronStage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module neuronStage(
  input float_24_8              biasIn_0,
  input                         clk,
  input float_24_8              dataIn_0,
  input                         reset,
  input float_24_8              tapIn_0,
  input float_24_8              tapIn_1,
  input float_24_8              tapIn_2,
  input float_24_8              tapIn_3,
  input float_24_8              tapIn_4,
  input float_24_8              tapIn_5,
  input float_24_8              tapIn_6,
  input float_24_8              tapIn_7,
  input                         valid,
  output float_24_8             dataOutPre_0,
  output float_24_8             dataOut_0);

// Parameters 



// Wires 

  float_24_8                    biasIn_0_w0;  // <1,0>
  float_24_8                    biasIn_0_w1;  // <1,0>
  float_24_8                    biasIn_0_w2;  // <1,0>
  float_24_8                    biasIn_0_w3;  // <1,0>
  float_24_8                    biasIn_0_w4;  // <1,0>
  float_24_8                    biasIn_0_w5;  // <1,0>
  float_24_8                    biasIn_0_w6;  // <1,0>
  float_24_8                    biasIn_0_w7;  // <1,0>
  float_24_8                    wireOut_w0;  // <1,0>
  float_24_8                    wireOut_w1;  // <1,0>
  float_24_8                    wireOut_w2;  // <1,0>
  float_24_8                    wireOut_w3;  // <1,0>
  float_24_8                    wireOut_w4;  // <1,0>
  float_24_8                    wireOut_w5;  // <1,0>
  float_24_8                    wireOut_w6;  // <1,0>
  float_24_8                    wireOut_w7;  // <1,0>


// Registers 

  float_24_8                    biasIn_0_r1;  // <1,0>
  float_24_8                    biasIn_0_r2;  // <1,0>
  float_24_8                    biasIn_0_r3;  // <1,0>
  float_24_8                    biasIn_0_r4;  // <1,0>
  float_24_8                    biasIn_0_r5;  // <1,0>
  float_24_8                    biasIn_0_r6;  // <1,0>
  float_24_8                    biasIn_0_r7;  // <1,0>
  float_24_8                    biasIn_0_r8;  // <1,0>
  float_24_8                    outLine_w0;  // <1,0>
  float_24_8                    outLine_w1;  // <1,0>
  float_24_8                    outLine_w2;  // <1,0>
  float_24_8                    outLine_w3;  // <1,0>
  float_24_8                    outLine_w4;  // <1,0>
  float_24_8                    outLine_w5;  // <1,0>
  float_24_8                    outLine_w6;  // <1,0>
  float_24_8                    outLine_w7;  // <1,0>
  reg                           valid0            ;  // <1,0>
  reg                           valid1            ;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    biasIn_0_r1 <= 'd0;
    biasIn_0_r2 <= 'd0;
    biasIn_0_r3 <= 'd0;
    biasIn_0_r4 <= 'd0;
    biasIn_0_r5 <= 'd0;
    biasIn_0_r6 <= 'd0;
    biasIn_0_r7 <= 'd0;
    biasIn_0_r8 <= 'd0;
  end
  else begin
    biasIn_0_r1 <= biasIn_0;
    biasIn_0_r2 <= biasIn_0_r1;
    biasIn_0_r3 <= biasIn_0_r2;
    biasIn_0_r4 <= biasIn_0_r3;
    biasIn_0_r5 <= biasIn_0_r4;
    biasIn_0_r6 <= biasIn_0_r5;
    biasIn_0_r7 <= biasIn_0_r6;
    biasIn_0_r8 <= biasIn_0_r7;
  end
end
////////////////////////////////////////////////////////////////////////////////
// neuron0
////////////////////////////////////////////////////////////////////////////////

neuron neuron0 (
    .biasIn_0(biasIn_0_w0),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w0),
    .reset(reset),
    .tapIn_0(tapIn_0));

////////////////////////////////////////////////////////////////////////////////
// neuron1
////////////////////////////////////////////////////////////////////////////////

neuron neuron1 (
    .biasIn_0(biasIn_0_w1),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w1),
    .reset(reset),
    .tapIn_0(tapIn_1));

////////////////////////////////////////////////////////////////////////////////
// neuron2
////////////////////////////////////////////////////////////////////////////////

neuron neuron2 (
    .biasIn_0(biasIn_0_w2),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w2),
    .reset(reset),
    .tapIn_0(tapIn_2));

////////////////////////////////////////////////////////////////////////////////
// neuron3
////////////////////////////////////////////////////////////////////////////////

neuron neuron3 (
    .biasIn_0(biasIn_0_w3),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w3),
    .reset(reset),
    .tapIn_0(tapIn_3));

////////////////////////////////////////////////////////////////////////////////
// neuron4
////////////////////////////////////////////////////////////////////////////////

neuron neuron4 (
    .biasIn_0(biasIn_0_w4),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w4),
    .reset(reset),
    .tapIn_0(tapIn_4));

////////////////////////////////////////////////////////////////////////////////
// neuron5
////////////////////////////////////////////////////////////////////////////////

neuron neuron5 (
    .biasIn_0(biasIn_0_w5),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w5),
    .reset(reset),
    .tapIn_0(tapIn_5));

////////////////////////////////////////////////////////////////////////////////
// neuron6
////////////////////////////////////////////////////////////////////////////////

neuron neuron6 (
    .biasIn_0(biasIn_0_w6),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w6),
    .reset(reset),
    .tapIn_0(tapIn_6));

////////////////////////////////////////////////////////////////////////////////
// neuron7
////////////////////////////////////////////////////////////////////////////////

neuron neuron7 (
    .biasIn_0(biasIn_0_w7),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w7),
    .reset(reset),
    .tapIn_0(tapIn_7));

////////////////////////////////////////////////////////////////////////////////
// sigmoid
////////////////////////////////////////////////////////////////////////////////

sigmoid sigmoid (
    .clk(clk),
    .dataOutPre_0(outLine_w0),
    .dataOut_0(dataOut_0),
    .reset(reset));


// Delay the Input Valid
always @(posedge clk) begin
  if (reset) begin
    valid0 <= 'd0;
    valid1 <= 'd0;
  end
  else begin
    valid0 <= valid;
    valid1 <= valid0;
  end
end

// Select the inputs to the Neuron

assign biasIn_0_w0 = valid0 ? biasIn_0_r7 : wireOut_w0;
assign biasIn_0_w1 = valid0 ? biasIn_0_r6 : wireOut_w1;
assign biasIn_0_w2 = valid0 ? biasIn_0_r5 : wireOut_w2;
assign biasIn_0_w3 = valid0 ? biasIn_0_r4 : wireOut_w3;
assign biasIn_0_w4 = valid0 ? biasIn_0_r3 : wireOut_w4;
assign biasIn_0_w5 = valid0 ? biasIn_0_r2 : wireOut_w5;
assign biasIn_0_w6 = valid0 ? biasIn_0_r1 : wireOut_w6;
assign biasIn_0_w7 = valid0 ? biasIn_0 : wireOut_w7;

// Create the output Delay Line

always @(posedge clk) begin
  if (reset) begin
    outLine_w0 <= 'd0;
    outLine_w1 <= 'd0;
    outLine_w2 <= 'd0;
    outLine_w3 <= 'd0;
    outLine_w4 <= 'd0;
    outLine_w5 <= 'd0;
    outLine_w6 <= 'd0;
    outLine_w7 <= 'd0;
  end
  else begin
    if (valid0) begin
      outLine_w0 <= wireOut_w0;
      outLine_w1 <= wireOut_w1;
      outLine_w2 <= wireOut_w2;
      outLine_w3 <= wireOut_w3;
      outLine_w4 <= wireOut_w4;
      outLine_w5 <= wireOut_w5;
      outLine_w6 <= wireOut_w6;
      outLine_w7 <= wireOut_w7;
    end
    else begin
      outLine_w0 <= outLine_w1;
      outLine_w1 <= outLine_w2;
      outLine_w2 <= outLine_w3;
      outLine_w3 <= outLine_w4;
      outLine_w4 <= outLine_w5;
      outLine_w5 <= outLine_w6;
      outLine_w6 <= outLine_w7;
    end
  end
end

// Assign the outputs
assign dataOutPre_0 = outLine_w0;
endmodule

