//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage_st
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage_st(
  input                         clk,
  input                         first,
  input                         reset,
  input float_24_8              stage_st_bias,
  input float_24_8              stage_st_data,
  input stage_st_tap_typ_6      taps,
  output float_24_8             stage_st_data_out,
  output float_24_8             stage_st_data_out_bias,
  output float_24_8             stage_st_data_out_pre);

// Parameters 



// Wires 

  float_24_8                    stage_st_adder;  // <1,0>
  float_24_8                    stage_st_bias_w0;  // <1,0>
  float_24_8                    stage_st_bias_w1;  // <1,0>
  float_24_8                    stage_st_bias_w2;  // <1,0>
  float_24_8                    stage_st_bias_w3;  // <1,0>
  float_24_8                    stage_st_bias_w4;  // <1,0>
  float_24_8                    stage_st_bias_w5;  // <1,0>
  float_24_8                    wireOut_w0;  // <1,0>
  float_24_8                    wireOut_w1;  // <1,0>
  float_24_8                    wireOut_w2;  // <1,0>
  float_24_8                    wireOut_w3;  // <1,0>
  float_24_8                    wireOut_w4;  // <1,0>
  float_24_8                    wireOut_w5;  // <1,0>


// Registers 

  reg                           first0            ;  // <1,0>
  reg                           first1            ;  // <1,0>
  float_24_8                    outLine_w0;  // <1,0>
  float_24_8                    outLine_w1;  // <1,0>
  float_24_8                    outLine_w2;  // <1,0>
  float_24_8                    outLine_w3;  // <1,0>
  float_24_8                    outLine_w4;  // <1,0>
  float_24_8                    outLine_w5;  // <1,0>


// Other



////////////////////////////////////////////////////////////////////////////////
// neuron0
////////////////////////////////////////////////////////////////////////////////

neuron neuron0 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w0),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w0),
    .stage_st_tap(taps.v0));

////////////////////////////////////////////////////////////////////////////////
// neuron1
////////////////////////////////////////////////////////////////////////////////

neuron neuron1 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w1),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w1),
    .stage_st_tap(taps.v1));

////////////////////////////////////////////////////////////////////////////////
// neuron2
////////////////////////////////////////////////////////////////////////////////

neuron neuron2 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w2),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w2),
    .stage_st_tap(taps.v2));

////////////////////////////////////////////////////////////////////////////////
// neuron3
////////////////////////////////////////////////////////////////////////////////

neuron neuron3 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w3),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w3),
    .stage_st_tap(taps.v3));

////////////////////////////////////////////////////////////////////////////////
// neuron4
////////////////////////////////////////////////////////////////////////////////

neuron neuron4 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w4),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w4),
    .stage_st_tap(taps.v4));

////////////////////////////////////////////////////////////////////////////////
// neuron5
////////////////////////////////////////////////////////////////////////////////

neuron neuron5 (
    .clk(clk),
    .reset(reset),
    .stage_st_bias(stage_st_bias_w5),
    .stage_st_data(stage_st_data),
    .stage_st_data_out(wireOut_w5),
    .stage_st_tap(taps.v5));

////////////////////////////////////////////////////////////////////////////////
// stage_st_add
////////////////////////////////////////////////////////////////////////////////

stage_st_add stage_st_add (
    .clk(clk),
    .outLine_w0(outLine_w0),
    .reset(reset),
    .stage_st_adder(stage_st_adder),
    .stage_st_bias(stage_st_bias));

////////////////////////////////////////////////////////////////////////////////
// sigmoid
////////////////////////////////////////////////////////////////////////////////

sigmoid sigmoid (
    .clk(clk),
    .reset(reset),
    .stage_st_data_out(stage_st_data_out),
    .stage_st_data_out_pre(stage_st_adder));


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

assign stage_st_bias_w0 = first0 ? 'd0 : wireOut_w0;
assign stage_st_bias_w1 = first0 ? 'd0 : wireOut_w1;
assign stage_st_bias_w2 = first0 ? 'd0 : wireOut_w2;
assign stage_st_bias_w3 = first0 ? 'd0 : wireOut_w3;
assign stage_st_bias_w4 = first0 ? 'd0 : wireOut_w4;
assign stage_st_bias_w5 = first0 ? 'd0 : wireOut_w5;

// Create the output Delay Line

always @(posedge clk) begin
  if (reset) begin
    outLine_w0 <= 'd0;
    outLine_w1 <= 'd0;
    outLine_w2 <= 'd0;
    outLine_w3 <= 'd0;
    outLine_w4 <= 'd0;
    outLine_w5 <= 'd0;
  end
  else begin
    if (first0) begin
      outLine_w0 <= wireOut_w0;
      outLine_w1 <= wireOut_w1;
      outLine_w2 <= wireOut_w2;
      outLine_w3 <= wireOut_w3;
      outLine_w4 <= wireOut_w4;
      outLine_w5 <= wireOut_w5;
    end
    else begin
      outLine_w0 <= outLine_w1;
      outLine_w1 <= outLine_w2;
      outLine_w2 <= outLine_w3;
      outLine_w3 <= outLine_w4;
      outLine_w4 <= outLine_w5;
    end
  end
end

// Assign the outputs
assign stage_st_data_out_pre = outLine_w0;
assign stage_st_data_out_bias = stage_st_adder;
endmodule

