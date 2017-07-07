//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_st0_st
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_st0_st(
  input                         clk,
  input                         first,
  input float_24_8              full_st0_st_bias,
  input float_24_8              full_st0_st_data,
  input full_st0_st_reg_t       full_st0_st_reg,
  input                         reset,
  input                         stage_error_back,
  input                         stage_error_first,
  input                         stage_error_mode,
  input full_st0_st_tap_typ_6   taps,
  input                         update_error_first,
  output reg            [191:0]  full_st0_st_tap_out,
  output float_24_8             full_st0_st_bias_adder,
  output float_24_8             full_st0_st_data_out,
  output float_24_8             full_st0_st_data_out_bias,
  output float_24_8             full_st0_st_data_out_pre);

// Parameters 



// Wires 

  float_24_8                    full_st0_st_adder;  // <1,0>
  float_24_8                    full_st0_st_bias_w0;  // <1,0>
  float_24_8                    full_st0_st_bias_w1;  // <1,0>
  float_24_8                    full_st0_st_bias_w2;  // <1,0>
  float_24_8                    full_st0_st_bias_w3;  // <1,0>
  float_24_8                    full_st0_st_bias_w4;  // <1,0>
  float_24_8                    full_st0_st_bias_w5;  // <1,0>
  wire                  [191:0]  full_st0_st_tap_out_int  ;  // <192,0>
  float_24_8                    input_data_w0;  // <1,0>
  float_24_8                    input_data_w1;  // <1,0>
  float_24_8                    input_data_w2;  // <1,0>
  float_24_8                    input_data_w3;  // <1,0>
  float_24_8                    input_data_w4;  // <1,0>
  float_24_8                    input_data_w5;  // <1,0>
  float_24_8                    neuron_temp_w0;  // <1,0>
  float_24_8                    neuron_temp_w1;  // <1,0>
  float_24_8                    neuron_temp_w2;  // <1,0>
  float_24_8                    neuron_temp_w3;  // <1,0>
  float_24_8                    neuron_temp_w4;  // <1,0>
  float_24_8                    neuron_temp_w5;  // <1,0>
  full_st0_st_tap_lat_typ_6     taps_select;  // <1,0>
  float_24_8                    wireOut_w0;  // <1,0>
  float_24_8                    wireOut_w1;  // <1,0>
  float_24_8                    wireOut_w2;  // <1,0>
  float_24_8                    wireOut_w3;  // <1,0>
  float_24_8                    wireOut_w4;  // <1,0>
  float_24_8                    wireOut_w5;  // <1,0>


// Registers 

  float_24_8                    bias_add_delay_w0;  // <1,0>
  float_24_8                    bias_add_delay_w1;  // <1,0>
  float_24_8                    bias_add_delay_w2;  // <1,0>
  float_24_8                    bias_add_delay_w3;  // <1,0>
  float_24_8                    bias_add_delay_w4;  // <1,0>
  float_24_8                    bias_add_delay_w5;  // <1,0>
  float_24_8                    bias_add_input_w0;  // <1,0>
  float_24_8                    bias_add_input_w1;  // <1,0>
  float_24_8                    bias_add_input_w2;  // <1,0>
  float_24_8                    bias_add_input_w3;  // <1,0>
  float_24_8                    bias_add_input_w4;  // <1,0>
  float_24_8                    bias_add_input_w5;  // <1,0>
  reg                           first0            ;  // <1,0>
  reg                           first1            ;  // <1,0>
  float_24_8                    full_st0_st_bias_r1;  // <1,0>
  float_24_8                    full_st0_st_bias_r2;  // <1,0>
  float_24_8                    full_st0_st_bias_r3;  // <1,0>
  float_24_8                    full_st0_st_bias_r4;  // <1,0>
  float_24_8                    full_st0_st_bias_r5;  // <1,0>
  float_24_8                    full_st0_st_bias_r6;  // <1,0>
  float_24_8                    full_st0_st_bias_r7;  // <1,0>
  float_24_8                    full_st0_st_bias_r8;  // <1,0>
  float_24_8                    in_line_w0;  // <1,0>
  float_24_8                    in_line_w1;  // <1,0>
  float_24_8                    in_line_w2;  // <1,0>
  float_24_8                    in_line_w3;  // <1,0>
  float_24_8                    in_line_w4;  // <1,0>
  float_24_8                    in_line_w5;  // <1,0>
  float_24_8                    out_line_w0;  // <1,0>
  float_24_8                    out_line_w1;  // <1,0>
  float_24_8                    out_line_w2;  // <1,0>
  float_24_8                    out_line_w3;  // <1,0>
  float_24_8                    out_line_w4;  // <1,0>
  float_24_8                    out_line_w5;  // <1,0>
  reg                           stage_error_back_r1  ;  // <1,0>
  reg                           stage_error_back_r2  ;  // <1,0>
  reg                           stage_error_back_r3  ;  // <1,0>
  reg                           stage_error_back_r4  ;  // <1,0>
  reg                           stage_error_back_r5  ;  // <1,0>
  reg                           stage_error_back_r6  ;  // <1,0>
  reg                           stage_error_back_r7  ;  // <1,0>
  reg                           stage_error_back_r8  ;  // <1,0>
  reg                           stage_error_back_r9  ;  // <1,0>
  reg                           stage_error_mode_r1  ;  // <1,0>
  full_st0_st_tap_lat_typ_6     taps_conv;  // <1,0>
  full_st0_st_tap_lat_typ_6     taps_lat;  // <1,0>
  full_st0_st_tap_lat_typ_6     taps_lat1;  // <1,0>
  taps_r1_typ_6                 taps_r1;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    full_st0_st_bias_r1 <= 'd0;
    full_st0_st_bias_r2 <= 'd0;
    full_st0_st_bias_r3 <= 'd0;
    full_st0_st_bias_r4 <= 'd0;
    full_st0_st_bias_r5 <= 'd0;
    full_st0_st_bias_r6 <= 'd0;
    full_st0_st_bias_r7 <= 'd0;
    full_st0_st_bias_r8 <= 'd0;
  end
  else begin
    full_st0_st_bias_r1 <= full_st0_st_bias;
    full_st0_st_bias_r2 <= full_st0_st_bias_r1;
    full_st0_st_bias_r3 <= full_st0_st_bias_r2;
    full_st0_st_bias_r4 <= full_st0_st_bias_r3;
    full_st0_st_bias_r5 <= full_st0_st_bias_r4;
    full_st0_st_bias_r6 <= full_st0_st_bias_r5;
    full_st0_st_bias_r7 <= full_st0_st_bias_r6;
    full_st0_st_bias_r8 <= full_st0_st_bias_r7;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_error_back_r1 <= 'd0;
    stage_error_back_r2 <= 'd0;
    stage_error_back_r3 <= 'd0;
    stage_error_back_r4 <= 'd0;
    stage_error_back_r5 <= 'd0;
    stage_error_back_r6 <= 'd0;
    stage_error_back_r7 <= 'd0;
    stage_error_back_r8 <= 'd0;
    stage_error_back_r9 <= 'd0;
  end
  else begin
    stage_error_back_r1 <= stage_error_back;
    stage_error_back_r2 <= stage_error_back_r1;
    stage_error_back_r3 <= stage_error_back_r2;
    stage_error_back_r4 <= stage_error_back_r3;
    stage_error_back_r5 <= stage_error_back_r4;
    stage_error_back_r6 <= stage_error_back_r5;
    stage_error_back_r7 <= stage_error_back_r6;
    stage_error_back_r8 <= stage_error_back_r7;
    stage_error_back_r9 <= stage_error_back_r8;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_error_mode_r1 <= 'd0;
  end
  else begin
    stage_error_mode_r1 <= stage_error_mode;
  end
end
always @(posedge clk) begin
  if (reset) begin
    taps_r1 <= 'd0;
  end
  else begin
    taps_r1 <= taps;
  end
end
////////////////////////////////////////////////////////////////////////////////
// neuron0
////////////////////////////////////////////////////////////////////////////////

neuron neuron0 (
    .bias(full_st0_st_bias_w0),
    .clk(clk),
    .data_in(input_data_w0),
    .data_out(wireOut_w0),
    .reset(reset),
    .taps(taps_select.v0));

////////////////////////////////////////////////////////////////////////////////
// neuron1
////////////////////////////////////////////////////////////////////////////////

neuron neuron1 (
    .bias(full_st0_st_bias_w1),
    .clk(clk),
    .data_in(input_data_w1),
    .data_out(wireOut_w1),
    .reset(reset),
    .taps(taps_select.v1));

////////////////////////////////////////////////////////////////////////////////
// neuron2
////////////////////////////////////////////////////////////////////////////////

neuron neuron2 (
    .bias(full_st0_st_bias_w2),
    .clk(clk),
    .data_in(input_data_w2),
    .data_out(wireOut_w2),
    .reset(reset),
    .taps(taps_select.v2));

////////////////////////////////////////////////////////////////////////////////
// neuron3
////////////////////////////////////////////////////////////////////////////////

neuron neuron3 (
    .bias(full_st0_st_bias_w3),
    .clk(clk),
    .data_in(input_data_w3),
    .data_out(wireOut_w3),
    .reset(reset),
    .taps(taps_select.v3));

////////////////////////////////////////////////////////////////////////////////
// neuron4
////////////////////////////////////////////////////////////////////////////////

neuron neuron4 (
    .bias(full_st0_st_bias_w4),
    .clk(clk),
    .data_in(input_data_w4),
    .data_out(wireOut_w4),
    .reset(reset),
    .taps(taps_select.v4));

////////////////////////////////////////////////////////////////////////////////
// neuron5
////////////////////////////////////////////////////////////////////////////////

neuron neuron5 (
    .bias(full_st0_st_bias_w5),
    .clk(clk),
    .data_in(input_data_w5),
    .data_out(wireOut_w5),
    .reset(reset),
    .taps(taps_select.v5));

////////////////////////////////////////////////////////////////////////////////
// full_st0_st_add
////////////////////////////////////////////////////////////////////////////////

full_st0_st_add full_st0_st_add (
    .clk(clk),
    .full_st0_st_adder(full_st0_st_adder),
    .full_st0_st_bias_r8(full_st0_st_bias_r8),
    .out_line_w0(out_line_w0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// full_st0_st_bias_add
////////////////////////////////////////////////////////////////////////////////

full_st0_st_bias_add full_st0_st_bias_add (
    .bias_add_delay_w0(bias_add_delay_w0),
    .clk(clk),
    .full_st0_st_bias_adder(full_st0_st_bias_adder),
    .full_st0_st_bias_r1(full_st0_st_bias_r1),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// sigmoid
////////////////////////////////////////////////////////////////////////////////

sigmoid sigmoid (
    .bypass(full_st0_st_reg.disable_non_linearity),
    .clk(clk),
    .data_in(full_st0_st_adder),
    .data_out(full_st0_st_data_out),
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
always @(posedge clk) begin
  if (reset) begin
    in_line_w0 <= 'd0;
    in_line_w1 <= 'd0;
    in_line_w2 <= 'd0;
    in_line_w3 <= 'd0;
    in_line_w4 <= 'd0;
    in_line_w5 <= 'd0;
  end
  else begin
    if (first) begin
      in_line_w0 <= taps.v0;
      in_line_w1 <= taps.v1;
      in_line_w2 <= taps.v2;
      in_line_w3 <= taps.v3;
      in_line_w4 <= taps.v4;
      in_line_w5 <= taps.v5;
    end
    else begin
      in_line_w0 <= in_line_w1;
      in_line_w1 <= in_line_w2;
      in_line_w2 <= in_line_w3;
      in_line_w3 <= in_line_w4;
      in_line_w4 <= in_line_w5;
      in_line_w5 <= in_line_w0;
    end
  end
end
always @* taps_conv.v0.sgn <= taps.v0.sgn;
always @* taps_conv.v0.exp <= (taps.v0.exp > full_st0_st_reg.tap_gain) ? taps.v0.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v0.exp;
always @* taps_conv.v0.man <= taps.v0.man;
always @* taps_conv.v1.sgn <= taps.v1.sgn;
always @* taps_conv.v1.exp <= (taps.v1.exp > full_st0_st_reg.tap_gain) ? taps.v1.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v1.exp;
always @* taps_conv.v1.man <= taps.v1.man;
always @* taps_conv.v2.sgn <= taps.v2.sgn;
always @* taps_conv.v2.exp <= (taps.v2.exp > full_st0_st_reg.tap_gain) ? taps.v2.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v2.exp;
always @* taps_conv.v2.man <= taps.v2.man;
always @* taps_conv.v3.sgn <= taps.v3.sgn;
always @* taps_conv.v3.exp <= (taps.v3.exp > full_st0_st_reg.tap_gain) ? taps.v3.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v3.exp;
always @* taps_conv.v3.man <= taps.v3.man;
always @* taps_conv.v4.sgn <= taps.v4.sgn;
always @* taps_conv.v4.exp <= (taps.v4.exp > full_st0_st_reg.tap_gain) ? taps.v4.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v4.exp;
always @* taps_conv.v4.man <= taps.v4.man;
always @* taps_conv.v5.sgn <= taps.v5.sgn;
always @* taps_conv.v5.exp <= (taps.v5.exp > full_st0_st_reg.tap_gain) ? taps.v5.exp[7:0] - {1'd0,full_st0_st_reg.tap_gain} : taps.v5.exp;
always @* taps_conv.v5.man <= taps.v5.man;
always @(posedge clk) begin
  if (reset) begin
    taps_lat <= 'd0;
  end
  else if (stage_error_first) begin 
    taps_lat <= taps_conv;
  end
end
assign taps_select = (stage_error_mode & ~stage_error_back_r2) ? taps_lat : taps;

// Select the inputs to the Neuron
assign full_st0_st_bias_w0 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v0 : (first0 | update_error_first) ? 'd0 : wireOut_w0;
assign full_st0_st_bias_w1 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v1 : (first0 | update_error_first) ? 'd0 : wireOut_w1;
assign full_st0_st_bias_w2 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v2 : (first0 | update_error_first) ? 'd0 : wireOut_w2;
assign full_st0_st_bias_w3 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v3 : (first0 | update_error_first) ? 'd0 : wireOut_w3;
assign full_st0_st_bias_w4 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v4 : (first0 | update_error_first) ? 'd0 : wireOut_w4;
assign full_st0_st_bias_w5 = (stage_error_mode_r1 & ~stage_error_back_r2) ? taps_r1.v5 : (first0 | update_error_first) ? 'd0 : wireOut_w5;

// Create the output Delay Line

assign neuron_temp_w0 = wireOut_w0;
assign neuron_temp_w1 = wireOut_w1;
assign neuron_temp_w2 = wireOut_w2;
assign neuron_temp_w3 = wireOut_w3;
assign neuron_temp_w4 = wireOut_w4;
assign neuron_temp_w5 = wireOut_w5;
always @(posedge clk) begin
  if (reset) begin
    out_line_w0 <= 'd0;
    out_line_w1 <= 'd0;
    out_line_w2 <= 'd0;
    out_line_w3 <= 'd0;
    out_line_w4 <= 'd0;
    out_line_w5 <= 'd0;
  end
  else begin
    if ((first0 | update_error_first)) begin
      out_line_w0 <= neuron_temp_w0;
      out_line_w1 <= neuron_temp_w1;
      out_line_w2 <= neuron_temp_w2;
      out_line_w3 <= neuron_temp_w3;
      out_line_w4 <= neuron_temp_w4;
      out_line_w5 <= neuron_temp_w5;
    end
    else begin
      out_line_w0 <= out_line_w1;
      out_line_w1 <= out_line_w2;
      out_line_w2 <= out_line_w3;
      out_line_w3 <= out_line_w4;
      out_line_w4 <= out_line_w5;
    end
  end
end

// Create the bias update code
always @* taps_lat1.v0.sgn <= taps_lat.v0.sgn;
always @* taps_lat1.v0.man <= taps_lat.v0.man;
always @* taps_lat1.v0.exp <= (taps_lat.v0.exp > full_st0_st_reg.bias_gain) ? taps_lat.v0.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v0.exp;
always @* bias_add_input_w0 <= taps_lat1.v0;
always @* taps_lat1.v1.sgn <= taps_lat.v1.sgn;
always @* taps_lat1.v1.man <= taps_lat.v1.man;
always @* taps_lat1.v1.exp <= (taps_lat.v1.exp > full_st0_st_reg.bias_gain) ? taps_lat.v1.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v1.exp;
always @* bias_add_input_w1 <= taps_lat1.v1;
always @* taps_lat1.v2.sgn <= taps_lat.v2.sgn;
always @* taps_lat1.v2.man <= taps_lat.v2.man;
always @* taps_lat1.v2.exp <= (taps_lat.v2.exp > full_st0_st_reg.bias_gain) ? taps_lat.v2.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v2.exp;
always @* bias_add_input_w2 <= taps_lat1.v2;
always @* taps_lat1.v3.sgn <= taps_lat.v3.sgn;
always @* taps_lat1.v3.man <= taps_lat.v3.man;
always @* taps_lat1.v3.exp <= (taps_lat.v3.exp > full_st0_st_reg.bias_gain) ? taps_lat.v3.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v3.exp;
always @* bias_add_input_w3 <= taps_lat1.v3;
always @* taps_lat1.v4.sgn <= taps_lat.v4.sgn;
always @* taps_lat1.v4.man <= taps_lat.v4.man;
always @* taps_lat1.v4.exp <= (taps_lat.v4.exp > full_st0_st_reg.bias_gain) ? taps_lat.v4.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v4.exp;
always @* bias_add_input_w4 <= taps_lat1.v4;
always @* taps_lat1.v5.sgn <= taps_lat.v5.sgn;
always @* taps_lat1.v5.man <= taps_lat.v5.man;
always @* taps_lat1.v5.exp <= (taps_lat.v5.exp > full_st0_st_reg.bias_gain) ? taps_lat.v5.exp[7:0] - {1'd0,full_st0_st_reg.bias_gain} : taps_lat.v5.exp;
always @* bias_add_input_w5 <= taps_lat1.v5;
always @(posedge clk) begin
  if (reset) begin
    bias_add_delay_w0 <= 'd0;
    bias_add_delay_w1 <= 'd0;
    bias_add_delay_w2 <= 'd0;
    bias_add_delay_w3 <= 'd0;
    bias_add_delay_w4 <= 'd0;
    bias_add_delay_w5 <= 'd0;
  end
  else begin
    if (first0) begin
      bias_add_delay_w0 <= bias_add_input_w0;
      bias_add_delay_w1 <= bias_add_input_w1;
      bias_add_delay_w2 <= bias_add_input_w2;
      bias_add_delay_w3 <= bias_add_input_w3;
      bias_add_delay_w4 <= bias_add_input_w4;
      bias_add_delay_w5 <= bias_add_input_w5;
    end
    else begin
      bias_add_delay_w0 <= bias_add_delay_w1;
      bias_add_delay_w1 <= bias_add_delay_w2;
      bias_add_delay_w2 <= bias_add_delay_w3;
      bias_add_delay_w3 <= bias_add_delay_w4;
      bias_add_delay_w4 <= bias_add_delay_w5;
    end
  end
end
assign input_data_w0 = stage_error_back_r2 ? in_line_w0 : full_st0_st_data;
assign input_data_w1 = stage_error_back_r2 ? in_line_w1 : full_st0_st_data;
assign input_data_w2 = stage_error_back_r2 ? in_line_w2 : full_st0_st_data;
assign input_data_w3 = stage_error_back_r2 ? in_line_w3 : full_st0_st_data;
assign input_data_w4 = stage_error_back_r2 ? in_line_w4 : full_st0_st_data;
assign input_data_w5 = stage_error_back_r2 ? in_line_w5 : full_st0_st_data;

// Assign the outputs
assign full_st0_st_data_out_pre = out_line_w0;

// Assign the bias output
assign full_st0_st_data_out_bias = full_st0_st_bias_adder;

// Assign the tap outputs
assign full_st0_st_tap_out_int[31:0] = wireOut_w0;
assign full_st0_st_tap_out_int[63:32] = wireOut_w1;
assign full_st0_st_tap_out_int[95:64] = wireOut_w2;
assign full_st0_st_tap_out_int[127:96] = wireOut_w3;
assign full_st0_st_tap_out_int[159:128] = wireOut_w4;
assign full_st0_st_tap_out_int[191:160] = wireOut_w5;
always @(posedge clk) begin
  if (reset) begin
    full_st0_st_tap_out <= 192'd0;
  end
  else begin
    full_st0_st_tap_out <= full_st0_st_tap_out_int;
  end
end
endmodule

