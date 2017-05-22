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
  input float_24_8              tapIn_10,
  input float_24_8              tapIn_11,
  input float_24_8              tapIn_12,
  input float_24_8              tapIn_13,
  input float_24_8              tapIn_14,
  input float_24_8              tapIn_15,
  input float_24_8              tapIn_16,
  input float_24_8              tapIn_17,
  input float_24_8              tapIn_18,
  input float_24_8              tapIn_19,
  input float_24_8              tapIn_2,
  input float_24_8              tapIn_20,
  input float_24_8              tapIn_21,
  input float_24_8              tapIn_22,
  input float_24_8              tapIn_23,
  input float_24_8              tapIn_24,
  input float_24_8              tapIn_25,
  input float_24_8              tapIn_26,
  input float_24_8              tapIn_27,
  input float_24_8              tapIn_28,
  input float_24_8              tapIn_29,
  input float_24_8              tapIn_3,
  input float_24_8              tapIn_30,
  input float_24_8              tapIn_31,
  input float_24_8              tapIn_4,
  input float_24_8              tapIn_5,
  input float_24_8              tapIn_6,
  input float_24_8              tapIn_7,
  input float_24_8              tapIn_8,
  input float_24_8              tapIn_9,
  input                         valid,
  output float_24_8             dataOutPre_0,
  output float_24_8             dataOut_0);

// Parameters 



// Wires 

  float_24_8                    biasIn_0_w0;  // <1,0>
  float_24_8                    biasIn_0_w1;  // <1,0>
  float_24_8                    biasIn_0_w10;  // <1,0>
  float_24_8                    biasIn_0_w11;  // <1,0>
  float_24_8                    biasIn_0_w12;  // <1,0>
  float_24_8                    biasIn_0_w13;  // <1,0>
  float_24_8                    biasIn_0_w14;  // <1,0>
  float_24_8                    biasIn_0_w15;  // <1,0>
  float_24_8                    biasIn_0_w16;  // <1,0>
  float_24_8                    biasIn_0_w17;  // <1,0>
  float_24_8                    biasIn_0_w18;  // <1,0>
  float_24_8                    biasIn_0_w19;  // <1,0>
  float_24_8                    biasIn_0_w2;  // <1,0>
  float_24_8                    biasIn_0_w20;  // <1,0>
  float_24_8                    biasIn_0_w21;  // <1,0>
  float_24_8                    biasIn_0_w22;  // <1,0>
  float_24_8                    biasIn_0_w23;  // <1,0>
  float_24_8                    biasIn_0_w24;  // <1,0>
  float_24_8                    biasIn_0_w25;  // <1,0>
  float_24_8                    biasIn_0_w26;  // <1,0>
  float_24_8                    biasIn_0_w27;  // <1,0>
  float_24_8                    biasIn_0_w28;  // <1,0>
  float_24_8                    biasIn_0_w29;  // <1,0>
  float_24_8                    biasIn_0_w3;  // <1,0>
  float_24_8                    biasIn_0_w30;  // <1,0>
  float_24_8                    biasIn_0_w31;  // <1,0>
  float_24_8                    biasIn_0_w4;  // <1,0>
  float_24_8                    biasIn_0_w5;  // <1,0>
  float_24_8                    biasIn_0_w6;  // <1,0>
  float_24_8                    biasIn_0_w7;  // <1,0>
  float_24_8                    biasIn_0_w8;  // <1,0>
  float_24_8                    biasIn_0_w9;  // <1,0>
  float_24_8                    wireOut_w0;  // <1,0>
  float_24_8                    wireOut_w1;  // <1,0>
  float_24_8                    wireOut_w10;  // <1,0>
  float_24_8                    wireOut_w11;  // <1,0>
  float_24_8                    wireOut_w12;  // <1,0>
  float_24_8                    wireOut_w13;  // <1,0>
  float_24_8                    wireOut_w14;  // <1,0>
  float_24_8                    wireOut_w15;  // <1,0>
  float_24_8                    wireOut_w16;  // <1,0>
  float_24_8                    wireOut_w17;  // <1,0>
  float_24_8                    wireOut_w18;  // <1,0>
  float_24_8                    wireOut_w19;  // <1,0>
  float_24_8                    wireOut_w2;  // <1,0>
  float_24_8                    wireOut_w20;  // <1,0>
  float_24_8                    wireOut_w21;  // <1,0>
  float_24_8                    wireOut_w22;  // <1,0>
  float_24_8                    wireOut_w23;  // <1,0>
  float_24_8                    wireOut_w24;  // <1,0>
  float_24_8                    wireOut_w25;  // <1,0>
  float_24_8                    wireOut_w26;  // <1,0>
  float_24_8                    wireOut_w27;  // <1,0>
  float_24_8                    wireOut_w28;  // <1,0>
  float_24_8                    wireOut_w29;  // <1,0>
  float_24_8                    wireOut_w3;  // <1,0>
  float_24_8                    wireOut_w30;  // <1,0>
  float_24_8                    wireOut_w31;  // <1,0>
  float_24_8                    wireOut_w4;  // <1,0>
  float_24_8                    wireOut_w5;  // <1,0>
  float_24_8                    wireOut_w6;  // <1,0>
  float_24_8                    wireOut_w7;  // <1,0>
  float_24_8                    wireOut_w8;  // <1,0>
  float_24_8                    wireOut_w9;  // <1,0>


// Registers 

  float_24_8                    biasIn_0_r1;  // <1,0>
  float_24_8                    biasIn_0_r10;  // <1,0>
  float_24_8                    biasIn_0_r11;  // <1,0>
  float_24_8                    biasIn_0_r12;  // <1,0>
  float_24_8                    biasIn_0_r13;  // <1,0>
  float_24_8                    biasIn_0_r14;  // <1,0>
  float_24_8                    biasIn_0_r15;  // <1,0>
  float_24_8                    biasIn_0_r16;  // <1,0>
  float_24_8                    biasIn_0_r17;  // <1,0>
  float_24_8                    biasIn_0_r18;  // <1,0>
  float_24_8                    biasIn_0_r19;  // <1,0>
  float_24_8                    biasIn_0_r2;  // <1,0>
  float_24_8                    biasIn_0_r20;  // <1,0>
  float_24_8                    biasIn_0_r21;  // <1,0>
  float_24_8                    biasIn_0_r22;  // <1,0>
  float_24_8                    biasIn_0_r23;  // <1,0>
  float_24_8                    biasIn_0_r24;  // <1,0>
  float_24_8                    biasIn_0_r25;  // <1,0>
  float_24_8                    biasIn_0_r26;  // <1,0>
  float_24_8                    biasIn_0_r27;  // <1,0>
  float_24_8                    biasIn_0_r28;  // <1,0>
  float_24_8                    biasIn_0_r29;  // <1,0>
  float_24_8                    biasIn_0_r3;  // <1,0>
  float_24_8                    biasIn_0_r30;  // <1,0>
  float_24_8                    biasIn_0_r31;  // <1,0>
  float_24_8                    biasIn_0_r32;  // <1,0>
  float_24_8                    biasIn_0_r4;  // <1,0>
  float_24_8                    biasIn_0_r5;  // <1,0>
  float_24_8                    biasIn_0_r6;  // <1,0>
  float_24_8                    biasIn_0_r7;  // <1,0>
  float_24_8                    biasIn_0_r8;  // <1,0>
  float_24_8                    biasIn_0_r9;  // <1,0>
  float_24_8                    outLine_w0;  // <1,0>
  float_24_8                    outLine_w1;  // <1,0>
  float_24_8                    outLine_w10;  // <1,0>
  float_24_8                    outLine_w11;  // <1,0>
  float_24_8                    outLine_w12;  // <1,0>
  float_24_8                    outLine_w13;  // <1,0>
  float_24_8                    outLine_w14;  // <1,0>
  float_24_8                    outLine_w15;  // <1,0>
  float_24_8                    outLine_w16;  // <1,0>
  float_24_8                    outLine_w17;  // <1,0>
  float_24_8                    outLine_w18;  // <1,0>
  float_24_8                    outLine_w19;  // <1,0>
  float_24_8                    outLine_w2;  // <1,0>
  float_24_8                    outLine_w20;  // <1,0>
  float_24_8                    outLine_w21;  // <1,0>
  float_24_8                    outLine_w22;  // <1,0>
  float_24_8                    outLine_w23;  // <1,0>
  float_24_8                    outLine_w24;  // <1,0>
  float_24_8                    outLine_w25;  // <1,0>
  float_24_8                    outLine_w26;  // <1,0>
  float_24_8                    outLine_w27;  // <1,0>
  float_24_8                    outLine_w28;  // <1,0>
  float_24_8                    outLine_w29;  // <1,0>
  float_24_8                    outLine_w3;  // <1,0>
  float_24_8                    outLine_w30;  // <1,0>
  float_24_8                    outLine_w31;  // <1,0>
  float_24_8                    outLine_w4;  // <1,0>
  float_24_8                    outLine_w5;  // <1,0>
  float_24_8                    outLine_w6;  // <1,0>
  float_24_8                    outLine_w7;  // <1,0>
  float_24_8                    outLine_w8;  // <1,0>
  float_24_8                    outLine_w9;  // <1,0>
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
    biasIn_0_r9 <= 'd0;
    biasIn_0_r10 <= 'd0;
    biasIn_0_r11 <= 'd0;
    biasIn_0_r12 <= 'd0;
    biasIn_0_r13 <= 'd0;
    biasIn_0_r14 <= 'd0;
    biasIn_0_r15 <= 'd0;
    biasIn_0_r16 <= 'd0;
    biasIn_0_r17 <= 'd0;
    biasIn_0_r18 <= 'd0;
    biasIn_0_r19 <= 'd0;
    biasIn_0_r20 <= 'd0;
    biasIn_0_r21 <= 'd0;
    biasIn_0_r22 <= 'd0;
    biasIn_0_r23 <= 'd0;
    biasIn_0_r24 <= 'd0;
    biasIn_0_r25 <= 'd0;
    biasIn_0_r26 <= 'd0;
    biasIn_0_r27 <= 'd0;
    biasIn_0_r28 <= 'd0;
    biasIn_0_r29 <= 'd0;
    biasIn_0_r30 <= 'd0;
    biasIn_0_r31 <= 'd0;
    biasIn_0_r32 <= 'd0;
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
    biasIn_0_r9 <= biasIn_0_r8;
    biasIn_0_r10 <= biasIn_0_r9;
    biasIn_0_r11 <= biasIn_0_r10;
    biasIn_0_r12 <= biasIn_0_r11;
    biasIn_0_r13 <= biasIn_0_r12;
    biasIn_0_r14 <= biasIn_0_r13;
    biasIn_0_r15 <= biasIn_0_r14;
    biasIn_0_r16 <= biasIn_0_r15;
    biasIn_0_r17 <= biasIn_0_r16;
    biasIn_0_r18 <= biasIn_0_r17;
    biasIn_0_r19 <= biasIn_0_r18;
    biasIn_0_r20 <= biasIn_0_r19;
    biasIn_0_r21 <= biasIn_0_r20;
    biasIn_0_r22 <= biasIn_0_r21;
    biasIn_0_r23 <= biasIn_0_r22;
    biasIn_0_r24 <= biasIn_0_r23;
    biasIn_0_r25 <= biasIn_0_r24;
    biasIn_0_r26 <= biasIn_0_r25;
    biasIn_0_r27 <= biasIn_0_r26;
    biasIn_0_r28 <= biasIn_0_r27;
    biasIn_0_r29 <= biasIn_0_r28;
    biasIn_0_r30 <= biasIn_0_r29;
    biasIn_0_r31 <= biasIn_0_r30;
    biasIn_0_r32 <= biasIn_0_r31;
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
// neuron8
////////////////////////////////////////////////////////////////////////////////

neuron neuron8 (
    .biasIn_0(biasIn_0_w8),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w8),
    .reset(reset),
    .tapIn_0(tapIn_8));

////////////////////////////////////////////////////////////////////////////////
// neuron9
////////////////////////////////////////////////////////////////////////////////

neuron neuron9 (
    .biasIn_0(biasIn_0_w9),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w9),
    .reset(reset),
    .tapIn_0(tapIn_9));

////////////////////////////////////////////////////////////////////////////////
// neuron10
////////////////////////////////////////////////////////////////////////////////

neuron neuron10 (
    .biasIn_0(biasIn_0_w10),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w10),
    .reset(reset),
    .tapIn_0(tapIn_10));

////////////////////////////////////////////////////////////////////////////////
// neuron11
////////////////////////////////////////////////////////////////////////////////

neuron neuron11 (
    .biasIn_0(biasIn_0_w11),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w11),
    .reset(reset),
    .tapIn_0(tapIn_11));

////////////////////////////////////////////////////////////////////////////////
// neuron12
////////////////////////////////////////////////////////////////////////////////

neuron neuron12 (
    .biasIn_0(biasIn_0_w12),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w12),
    .reset(reset),
    .tapIn_0(tapIn_12));

////////////////////////////////////////////////////////////////////////////////
// neuron13
////////////////////////////////////////////////////////////////////////////////

neuron neuron13 (
    .biasIn_0(biasIn_0_w13),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w13),
    .reset(reset),
    .tapIn_0(tapIn_13));

////////////////////////////////////////////////////////////////////////////////
// neuron14
////////////////////////////////////////////////////////////////////////////////

neuron neuron14 (
    .biasIn_0(biasIn_0_w14),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w14),
    .reset(reset),
    .tapIn_0(tapIn_14));

////////////////////////////////////////////////////////////////////////////////
// neuron15
////////////////////////////////////////////////////////////////////////////////

neuron neuron15 (
    .biasIn_0(biasIn_0_w15),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w15),
    .reset(reset),
    .tapIn_0(tapIn_15));

////////////////////////////////////////////////////////////////////////////////
// neuron16
////////////////////////////////////////////////////////////////////////////////

neuron neuron16 (
    .biasIn_0(biasIn_0_w16),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w16),
    .reset(reset),
    .tapIn_0(tapIn_16));

////////////////////////////////////////////////////////////////////////////////
// neuron17
////////////////////////////////////////////////////////////////////////////////

neuron neuron17 (
    .biasIn_0(biasIn_0_w17),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w17),
    .reset(reset),
    .tapIn_0(tapIn_17));

////////////////////////////////////////////////////////////////////////////////
// neuron18
////////////////////////////////////////////////////////////////////////////////

neuron neuron18 (
    .biasIn_0(biasIn_0_w18),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w18),
    .reset(reset),
    .tapIn_0(tapIn_18));

////////////////////////////////////////////////////////////////////////////////
// neuron19
////////////////////////////////////////////////////////////////////////////////

neuron neuron19 (
    .biasIn_0(biasIn_0_w19),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w19),
    .reset(reset),
    .tapIn_0(tapIn_19));

////////////////////////////////////////////////////////////////////////////////
// neuron20
////////////////////////////////////////////////////////////////////////////////

neuron neuron20 (
    .biasIn_0(biasIn_0_w20),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w20),
    .reset(reset),
    .tapIn_0(tapIn_20));

////////////////////////////////////////////////////////////////////////////////
// neuron21
////////////////////////////////////////////////////////////////////////////////

neuron neuron21 (
    .biasIn_0(biasIn_0_w21),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w21),
    .reset(reset),
    .tapIn_0(tapIn_21));

////////////////////////////////////////////////////////////////////////////////
// neuron22
////////////////////////////////////////////////////////////////////////////////

neuron neuron22 (
    .biasIn_0(biasIn_0_w22),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w22),
    .reset(reset),
    .tapIn_0(tapIn_22));

////////////////////////////////////////////////////////////////////////////////
// neuron23
////////////////////////////////////////////////////////////////////////////////

neuron neuron23 (
    .biasIn_0(biasIn_0_w23),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w23),
    .reset(reset),
    .tapIn_0(tapIn_23));

////////////////////////////////////////////////////////////////////////////////
// neuron24
////////////////////////////////////////////////////////////////////////////////

neuron neuron24 (
    .biasIn_0(biasIn_0_w24),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w24),
    .reset(reset),
    .tapIn_0(tapIn_24));

////////////////////////////////////////////////////////////////////////////////
// neuron25
////////////////////////////////////////////////////////////////////////////////

neuron neuron25 (
    .biasIn_0(biasIn_0_w25),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w25),
    .reset(reset),
    .tapIn_0(tapIn_25));

////////////////////////////////////////////////////////////////////////////////
// neuron26
////////////////////////////////////////////////////////////////////////////////

neuron neuron26 (
    .biasIn_0(biasIn_0_w26),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w26),
    .reset(reset),
    .tapIn_0(tapIn_26));

////////////////////////////////////////////////////////////////////////////////
// neuron27
////////////////////////////////////////////////////////////////////////////////

neuron neuron27 (
    .biasIn_0(biasIn_0_w27),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w27),
    .reset(reset),
    .tapIn_0(tapIn_27));

////////////////////////////////////////////////////////////////////////////////
// neuron28
////////////////////////////////////////////////////////////////////////////////

neuron neuron28 (
    .biasIn_0(biasIn_0_w28),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w28),
    .reset(reset),
    .tapIn_0(tapIn_28));

////////////////////////////////////////////////////////////////////////////////
// neuron29
////////////////////////////////////////////////////////////////////////////////

neuron neuron29 (
    .biasIn_0(biasIn_0_w29),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w29),
    .reset(reset),
    .tapIn_0(tapIn_29));

////////////////////////////////////////////////////////////////////////////////
// neuron30
////////////////////////////////////////////////////////////////////////////////

neuron neuron30 (
    .biasIn_0(biasIn_0_w30),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w30),
    .reset(reset),
    .tapIn_0(tapIn_30));

////////////////////////////////////////////////////////////////////////////////
// neuron31
////////////////////////////////////////////////////////////////////////////////

neuron neuron31 (
    .biasIn_0(biasIn_0_w31),
    .clk(clk),
    .dataIn_0(dataIn_0),
    .dataOut_0(wireOut_w31),
    .reset(reset),
    .tapIn_0(tapIn_31));

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

assign biasIn_0_w0 = valid0 ? biasIn_0_r31 : wireOut_w0;
assign biasIn_0_w1 = valid0 ? biasIn_0_r30 : wireOut_w1;
assign biasIn_0_w2 = valid0 ? biasIn_0_r29 : wireOut_w2;
assign biasIn_0_w3 = valid0 ? biasIn_0_r28 : wireOut_w3;
assign biasIn_0_w4 = valid0 ? biasIn_0_r27 : wireOut_w4;
assign biasIn_0_w5 = valid0 ? biasIn_0_r26 : wireOut_w5;
assign biasIn_0_w6 = valid0 ? biasIn_0_r25 : wireOut_w6;
assign biasIn_0_w7 = valid0 ? biasIn_0_r24 : wireOut_w7;
assign biasIn_0_w8 = valid0 ? biasIn_0_r23 : wireOut_w8;
assign biasIn_0_w9 = valid0 ? biasIn_0_r22 : wireOut_w9;
assign biasIn_0_w10 = valid0 ? biasIn_0_r21 : wireOut_w10;
assign biasIn_0_w11 = valid0 ? biasIn_0_r20 : wireOut_w11;
assign biasIn_0_w12 = valid0 ? biasIn_0_r19 : wireOut_w12;
assign biasIn_0_w13 = valid0 ? biasIn_0_r18 : wireOut_w13;
assign biasIn_0_w14 = valid0 ? biasIn_0_r17 : wireOut_w14;
assign biasIn_0_w15 = valid0 ? biasIn_0_r16 : wireOut_w15;
assign biasIn_0_w16 = valid0 ? biasIn_0_r15 : wireOut_w16;
assign biasIn_0_w17 = valid0 ? biasIn_0_r14 : wireOut_w17;
assign biasIn_0_w18 = valid0 ? biasIn_0_r13 : wireOut_w18;
assign biasIn_0_w19 = valid0 ? biasIn_0_r12 : wireOut_w19;
assign biasIn_0_w20 = valid0 ? biasIn_0_r11 : wireOut_w20;
assign biasIn_0_w21 = valid0 ? biasIn_0_r10 : wireOut_w21;
assign biasIn_0_w22 = valid0 ? biasIn_0_r9 : wireOut_w22;
assign biasIn_0_w23 = valid0 ? biasIn_0_r8 : wireOut_w23;
assign biasIn_0_w24 = valid0 ? biasIn_0_r7 : wireOut_w24;
assign biasIn_0_w25 = valid0 ? biasIn_0_r6 : wireOut_w25;
assign biasIn_0_w26 = valid0 ? biasIn_0_r5 : wireOut_w26;
assign biasIn_0_w27 = valid0 ? biasIn_0_r4 : wireOut_w27;
assign biasIn_0_w28 = valid0 ? biasIn_0_r3 : wireOut_w28;
assign biasIn_0_w29 = valid0 ? biasIn_0_r2 : wireOut_w29;
assign biasIn_0_w30 = valid0 ? biasIn_0_r1 : wireOut_w30;
assign biasIn_0_w31 = valid0 ? biasIn_0 : wireOut_w31;

// Create the output Delay Line

always @(posedge clk) begin
  if (reset) begin
    outLine_w0 <= 'd0;
    outLine_w1 <= 'd0;
    outLine_w10 <= 'd0;
    outLine_w11 <= 'd0;
    outLine_w12 <= 'd0;
    outLine_w13 <= 'd0;
    outLine_w14 <= 'd0;
    outLine_w15 <= 'd0;
    outLine_w16 <= 'd0;
    outLine_w17 <= 'd0;
    outLine_w18 <= 'd0;
    outLine_w19 <= 'd0;
    outLine_w2 <= 'd0;
    outLine_w20 <= 'd0;
    outLine_w21 <= 'd0;
    outLine_w22 <= 'd0;
    outLine_w23 <= 'd0;
    outLine_w24 <= 'd0;
    outLine_w25 <= 'd0;
    outLine_w26 <= 'd0;
    outLine_w27 <= 'd0;
    outLine_w28 <= 'd0;
    outLine_w29 <= 'd0;
    outLine_w3 <= 'd0;
    outLine_w30 <= 'd0;
    outLine_w31 <= 'd0;
    outLine_w4 <= 'd0;
    outLine_w5 <= 'd0;
    outLine_w6 <= 'd0;
    outLine_w7 <= 'd0;
    outLine_w8 <= 'd0;
    outLine_w9 <= 'd0;
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
      outLine_w8 <= wireOut_w8;
      outLine_w9 <= wireOut_w9;
      outLine_w10 <= wireOut_w10;
      outLine_w11 <= wireOut_w11;
      outLine_w12 <= wireOut_w12;
      outLine_w13 <= wireOut_w13;
      outLine_w14 <= wireOut_w14;
      outLine_w15 <= wireOut_w15;
      outLine_w16 <= wireOut_w16;
      outLine_w17 <= wireOut_w17;
      outLine_w18 <= wireOut_w18;
      outLine_w19 <= wireOut_w19;
      outLine_w20 <= wireOut_w20;
      outLine_w21 <= wireOut_w21;
      outLine_w22 <= wireOut_w22;
      outLine_w23 <= wireOut_w23;
      outLine_w24 <= wireOut_w24;
      outLine_w25 <= wireOut_w25;
      outLine_w26 <= wireOut_w26;
      outLine_w27 <= wireOut_w27;
      outLine_w28 <= wireOut_w28;
      outLine_w29 <= wireOut_w29;
      outLine_w30 <= wireOut_w30;
      outLine_w31 <= wireOut_w31;
    end
    else begin
      outLine_w0 <= outLine_w1;
      outLine_w1 <= outLine_w2;
      outLine_w2 <= outLine_w3;
      outLine_w3 <= outLine_w4;
      outLine_w4 <= outLine_w5;
      outLine_w5 <= outLine_w6;
      outLine_w6 <= outLine_w7;
      outLine_w7 <= outLine_w8;
      outLine_w8 <= outLine_w9;
      outLine_w9 <= outLine_w10;
      outLine_w10 <= outLine_w11;
      outLine_w11 <= outLine_w12;
      outLine_w12 <= outLine_w13;
      outLine_w13 <= outLine_w14;
      outLine_w14 <= outLine_w15;
      outLine_w15 <= outLine_w16;
      outLine_w16 <= outLine_w17;
      outLine_w17 <= outLine_w18;
      outLine_w18 <= outLine_w19;
      outLine_w19 <= outLine_w20;
      outLine_w20 <= outLine_w21;
      outLine_w21 <= outLine_w22;
      outLine_w22 <= outLine_w23;
      outLine_w23 <= outLine_w24;
      outLine_w24 <= outLine_w25;
      outLine_w25 <= outLine_w26;
      outLine_w26 <= outLine_w27;
      outLine_w27 <= outLine_w28;
      outLine_w28 <= outLine_w29;
      outLine_w29 <= outLine_w30;
      outLine_w30 <= outLine_w31;
    end
  end
end

// Assign the outputs
assign dataOutPre_0 = outLine_w0;
endmodule

