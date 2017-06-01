//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       neural_stage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module neural_stage(
  input                         clk,
  input                         first,
  input float_24_8              neural_stage_bias,
  input float_24_8              neural_stage_data,
  input                         reset,
  input neural_stage_tap_typ_16 taps,
  output float_24_8             neural_stage_data_out,
  output float_24_8             neural_stage_data_out_bias,
  output float_24_8             neural_stage_data_out_pre);

// Parameters 



// Wires 

  float_24_8                    neural_stage_adder;  // <1,0>
  float_24_8                    neural_stage_bias_w0;  // <1,0>
  float_24_8                    neural_stage_bias_w1;  // <1,0>
  float_24_8                    neural_stage_bias_w10;  // <1,0>
  float_24_8                    neural_stage_bias_w11;  // <1,0>
  float_24_8                    neural_stage_bias_w12;  // <1,0>
  float_24_8                    neural_stage_bias_w13;  // <1,0>
  float_24_8                    neural_stage_bias_w14;  // <1,0>
  float_24_8                    neural_stage_bias_w15;  // <1,0>
  float_24_8                    neural_stage_bias_w2;  // <1,0>
  float_24_8                    neural_stage_bias_w3;  // <1,0>
  float_24_8                    neural_stage_bias_w4;  // <1,0>
  float_24_8                    neural_stage_bias_w5;  // <1,0>
  float_24_8                    neural_stage_bias_w6;  // <1,0>
  float_24_8                    neural_stage_bias_w7;  // <1,0>
  float_24_8                    neural_stage_bias_w8;  // <1,0>
  float_24_8                    neural_stage_bias_w9;  // <1,0>
  float_24_8                    wireOut_w0;  // <1,0>
  float_24_8                    wireOut_w1;  // <1,0>
  float_24_8                    wireOut_w10;  // <1,0>
  float_24_8                    wireOut_w11;  // <1,0>
  float_24_8                    wireOut_w12;  // <1,0>
  float_24_8                    wireOut_w13;  // <1,0>
  float_24_8                    wireOut_w14;  // <1,0>
  float_24_8                    wireOut_w15;  // <1,0>
  float_24_8                    wireOut_w2;  // <1,0>
  float_24_8                    wireOut_w3;  // <1,0>
  float_24_8                    wireOut_w4;  // <1,0>
  float_24_8                    wireOut_w5;  // <1,0>
  float_24_8                    wireOut_w6;  // <1,0>
  float_24_8                    wireOut_w7;  // <1,0>
  float_24_8                    wireOut_w8;  // <1,0>
  float_24_8                    wireOut_w9;  // <1,0>


// Registers 

  reg                           first0            ;  // <1,0>
  reg                           first1            ;  // <1,0>
  float_24_8                    outLine_w0;  // <1,0>
  float_24_8                    outLine_w1;  // <1,0>
  float_24_8                    outLine_w10;  // <1,0>
  float_24_8                    outLine_w11;  // <1,0>
  float_24_8                    outLine_w12;  // <1,0>
  float_24_8                    outLine_w13;  // <1,0>
  float_24_8                    outLine_w14;  // <1,0>
  float_24_8                    outLine_w15;  // <1,0>
  float_24_8                    outLine_w2;  // <1,0>
  float_24_8                    outLine_w3;  // <1,0>
  float_24_8                    outLine_w4;  // <1,0>
  float_24_8                    outLine_w5;  // <1,0>
  float_24_8                    outLine_w6;  // <1,0>
  float_24_8                    outLine_w7;  // <1,0>
  float_24_8                    outLine_w8;  // <1,0>
  float_24_8                    outLine_w9;  // <1,0>


// Other



////////////////////////////////////////////////////////////////////////////////
// neuron0
////////////////////////////////////////////////////////////////////////////////

neuron neuron0 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w0),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w0),
    .neural_stage_tap(taps.v0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron1
////////////////////////////////////////////////////////////////////////////////

neuron neuron1 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w1),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w1),
    .neural_stage_tap(taps.v1),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron2
////////////////////////////////////////////////////////////////////////////////

neuron neuron2 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w2),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w2),
    .neural_stage_tap(taps.v2),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron3
////////////////////////////////////////////////////////////////////////////////

neuron neuron3 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w3),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w3),
    .neural_stage_tap(taps.v3),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron4
////////////////////////////////////////////////////////////////////////////////

neuron neuron4 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w4),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w4),
    .neural_stage_tap(taps.v4),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron5
////////////////////////////////////////////////////////////////////////////////

neuron neuron5 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w5),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w5),
    .neural_stage_tap(taps.v5),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron6
////////////////////////////////////////////////////////////////////////////////

neuron neuron6 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w6),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w6),
    .neural_stage_tap(taps.v6),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron7
////////////////////////////////////////////////////////////////////////////////

neuron neuron7 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w7),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w7),
    .neural_stage_tap(taps.v7),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron8
////////////////////////////////////////////////////////////////////////////////

neuron neuron8 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w8),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w8),
    .neural_stage_tap(taps.v8),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron9
////////////////////////////////////////////////////////////////////////////////

neuron neuron9 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w9),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w9),
    .neural_stage_tap(taps.v9),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron10
////////////////////////////////////////////////////////////////////////////////

neuron neuron10 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w10),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w10),
    .neural_stage_tap(taps.v10),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron11
////////////////////////////////////////////////////////////////////////////////

neuron neuron11 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w11),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w11),
    .neural_stage_tap(taps.v11),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron12
////////////////////////////////////////////////////////////////////////////////

neuron neuron12 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w12),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w12),
    .neural_stage_tap(taps.v12),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron13
////////////////////////////////////////////////////////////////////////////////

neuron neuron13 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w13),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w13),
    .neural_stage_tap(taps.v13),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron14
////////////////////////////////////////////////////////////////////////////////

neuron neuron14 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w14),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w14),
    .neural_stage_tap(taps.v14),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neuron15
////////////////////////////////////////////////////////////////////////////////

neuron neuron15 (
    .clk(clk),
    .neural_stage_bias(neural_stage_bias_w15),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(wireOut_w15),
    .neural_stage_tap(taps.v15),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// neural_stage_add
////////////////////////////////////////////////////////////////////////////////

neural_stage_add neural_stage_add (
    .clk(clk),
    .neural_stage_adder(neural_stage_adder),
    .neural_stage_bias(neural_stage_bias),
    .outLine_w0(outLine_w0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// sigmoid
////////////////////////////////////////////////////////////////////////////////

sigmoid sigmoid (
    .clk(clk),
    .neural_stage_data_out(neural_stage_data_out),
    .neural_stage_data_out_pre(neural_stage_adder),
    .reset(reset));


// Delay the Input Valid
always @(posedge clk) begin
  if (reset) begin
    first0 <= 'd0;
    first1 <= 'd0;
  end
  else begin
    first0 <= first;
    first1 <= first0;
  end
end

// Select the inputs to the Neuron

assign neural_stage_bias_w0 = first0 ? 'd0 : wireOut_w0;
assign neural_stage_bias_w1 = first0 ? 'd0 : wireOut_w1;
assign neural_stage_bias_w2 = first0 ? 'd0 : wireOut_w2;
assign neural_stage_bias_w3 = first0 ? 'd0 : wireOut_w3;
assign neural_stage_bias_w4 = first0 ? 'd0 : wireOut_w4;
assign neural_stage_bias_w5 = first0 ? 'd0 : wireOut_w5;
assign neural_stage_bias_w6 = first0 ? 'd0 : wireOut_w6;
assign neural_stage_bias_w7 = first0 ? 'd0 : wireOut_w7;
assign neural_stage_bias_w8 = first0 ? 'd0 : wireOut_w8;
assign neural_stage_bias_w9 = first0 ? 'd0 : wireOut_w9;
assign neural_stage_bias_w10 = first0 ? 'd0 : wireOut_w10;
assign neural_stage_bias_w11 = first0 ? 'd0 : wireOut_w11;
assign neural_stage_bias_w12 = first0 ? 'd0 : wireOut_w12;
assign neural_stage_bias_w13 = first0 ? 'd0 : wireOut_w13;
assign neural_stage_bias_w14 = first0 ? 'd0 : wireOut_w14;
assign neural_stage_bias_w15 = first0 ? 'd0 : wireOut_w15;

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
    outLine_w2 <= 'd0;
    outLine_w3 <= 'd0;
    outLine_w4 <= 'd0;
    outLine_w5 <= 'd0;
    outLine_w6 <= 'd0;
    outLine_w7 <= 'd0;
    outLine_w8 <= 'd0;
    outLine_w9 <= 'd0;
  end
  else begin
    if (first0) begin
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
    end
  end
end

// Assign the outputs
assign neural_stage_data_out_pre = outLine_w0;
assign neural_stage_data_out_bias = neural_stage_adder;
endmodule

