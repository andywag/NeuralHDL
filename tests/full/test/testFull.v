//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testFull
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testFull(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                          expected_rdy      ;  // <1,0>
  wire                          full_st0_ctrl_data_fifo_data_ready;  // <1,0>
  wire                          full_st1_ctrl_data_fifo_data_ready;  // <1,0>
  wire                          load_finish       ;  // <1,0>
  wire                          st_data_out_fst   ;  // <1,0>
  wire                          st_data_out_pre_fst;  // <1,0>
  wire                          st_data_out_pre_vld;  // <1,0>
  wire                          st_data_out_vld   ;  // <1,0>
  wire                          st_data_rdy       ;  // <1,0>
  wire                          tap_in_rdy        ;  // <1,0>


// Registers 

  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  exp_rdy_count     ;  // <32,0>
  reg                           expected_fst      ;  // <1,0>
  reg                   [31:0]  expected_mem[0:36];  // <32,0>
  reg                           expected_vld      ;  // <1,0>
  reg                   [31:0]  in_rdy_count      ;  // <32,0>
  reg                   [31:0]  rtl_bias0_fptr    ;  // <32,0>
  reg                   [31:0]  rtl_bias1_fptr    ;  // <32,0>
  reg                   [31:0]  rtl_error0_fptr   ;  // <32,0>
  reg                   [31:0]  rtl_error1_fptr   ;  // <32,0>
  reg                   [31:0]  rtl_st0_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_st1_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_tap0_fptr     ;  // <32,0>
  reg                   [31:0]  rtl_tap1_fptr     ;  // <32,0>
  reg                           st_data_fst       ;  // <1,0>
  reg                   [31:0]  st_data_mem[0:36] ;  // <32,0>
  reg                           st_data_out_pre_rdy;  // <1,0>
  reg                           st_data_out_rdy   ;  // <1,0>
  reg                           st_data_vld       ;  // <1,0>
  reg                           tap_in_fst        ;  // <1,0>
  reg                           tap_in_vld        ;  // <1,0>


// Other

  float_24_8                    expected;  // <1,0>
  full_st0_ctrl_int_t           full_st0_ctrl_int;  // <1,0>
  full_st0_st_reg_t             full_st0_st_reg;  // <1,0>
  full_st1_ctrl_int_t           full_st1_ctrl_int;  // <1,0>
  full_st1_st_reg_t             full_st1_st_reg;  // <1,0>
  float_24_8                    st_data;  // <1,0>
  float_24_8                    st_data_out;  // <1,0>
  float_24_8                    st_data_out_pre;  // <1,0>
  float_24_8                    tap_in;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// full
////////////////////////////////////////////////////////////////////////////////

full full (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .full_st0_ctrl_data_fifo_data_ready(full_st0_ctrl_data_fifo_data_ready),
    .full_st0_ctrl_int(full_st0_ctrl_int),
    .full_st0_st_reg(full_st0_st_reg),
    .full_st1_ctrl_data_fifo_data_ready(full_st1_ctrl_data_fifo_data_ready),
    .full_st1_ctrl_int(full_st1_ctrl_int),
    .full_st1_st_reg(full_st1_st_reg),
    .load_finish(load_finish),
    .reset(reset),
    .st_data(st_data),
    .st_data_fst(st_data_fst),
    .st_data_out(st_data_out),
    .st_data_out_fst(st_data_out_fst),
    .st_data_out_pre(st_data_out_pre),
    .st_data_out_pre_fst(st_data_out_pre_fst),
    .st_data_out_pre_rdy(st_data_out_pre_rdy),
    .st_data_out_pre_vld(st_data_out_pre_vld),
    .st_data_out_rdy(st_data_out_rdy),
    .st_data_out_vld(st_data_out_vld),
    .st_data_rdy(st_data_rdy),
    .st_data_vld(st_data_vld),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd36864)) begin
      $finish;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    in_rdy_count <= 32'd0;
  end
  else if ((st_data_rdy & st_data_vld)) begin 
    if ((in_rdy_count == 'd35)) begin
      in_rdy_count <= 32'd0;
    end
    else begin
      in_rdy_count <= in_rdy_count[31:0] + 32'd1;
    end
  end
end
assign st_data_vld = 'd1;

// Load st_data
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/full/data/init_data.hex",st_data_mem);
end

assign st_data = in_rdy_count[31] ? 'd0 : st_data_mem[in_rdy_count];
always @(posedge clk) begin
  if (reset) begin
    exp_rdy_count <= 32'd0;
  end
  else if ((expected_rdy & expected_vld)) begin 
    if ((exp_rdy_count == 'd35)) begin
      exp_rdy_count <= 32'd0;
    end
    else begin
      exp_rdy_count <= exp_rdy_count[31:0] + 32'd1;
    end
  end
end
assign expected_vld = 'd1;

// Load expected
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/full/data/init_expected.hex",expected_mem);
end

assign expected = exp_rdy_count[31] ? 'd0 : expected_mem[exp_rdy_count];
assign full_st0_ctrl_int.tap_update_enable = 'd1;
assign full_st0_ctrl_int.bias_update_enable = 'd1;
assign full_st0_ctrl_int.input_stage = 'd1;
assign full_st0_st_reg.tap_gain = 6'd3;
assign full_st0_st_reg.bias_gain = 6'd3;
assign full_st0_st_reg.disable_non_linearity = 'd0;
assign full_st1_ctrl_int.tap_update_enable = 'd1;
assign full_st1_ctrl_int.bias_update_enable = 'd1;
assign full_st1_ctrl_int.input_stage = 'd0;
assign full_st1_st_reg.tap_gain = 6'd3;
assign full_st1_st_reg.bias_gain = 6'd3;
assign full_st1_st_reg.disable_non_linearity = 'd1;

// Store Store 
initial begin
  rtl_error0_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_error0.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.stage_0_error_vld & testFull.full.stage_0_error_rdy)) begin 
    $fdisplay(rtl_error0_fptr,"%h ",testFull.full.stage_0_error);
  end
end

// Store Store 
initial begin
  rtl_bias0_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_bias0.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.full_st0.bias_int.wr_vld & ~testFull.full.full_st0.bias_int.sub_vld)) begin 
    $fdisplay(rtl_bias0_fptr,"%h ",testFull.full.full_st0.bias_int_wr_data);
  end
end

// Store Store 
initial begin
  rtl_tap0_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_tap0.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.full_st0.tap_int.wr_vld & ~testFull.full.full_st0.tap_int.sub_vld)) begin 
    $fdisplay(rtl_tap0_fptr,"%h ",testFull.full.full_st0.tap_int_wr_data);
  end
end

// Store Store 
initial begin
  rtl_st0_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_st0.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.stage_0_data_out_vld & testFull.full.stage_0_data_out_rdy)) begin 
    $fdisplay(rtl_st0_fptr,"%h ",testFull.full.stage_0_data_out);
  end
end

// Store Store 
initial begin
  rtl_error1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_error1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.stage_1_error_vld & testFull.full.stage_1_error_rdy)) begin 
    $fdisplay(rtl_error1_fptr,"%h ",testFull.full.stage_1_error);
  end
end

// Store Store 
initial begin
  rtl_bias1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_bias1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.full_st1.bias_int.wr_vld & ~testFull.full.full_st1.bias_int.sub_vld)) begin 
    $fdisplay(rtl_bias1_fptr,"%h ",testFull.full.full_st1.bias_int_wr_data);
  end
end

// Store Store 
initial begin
  rtl_tap1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_tap1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.full_st1.tap_int.wr_vld & ~testFull.full.full_st1.tap_int.sub_vld)) begin 
    $fdisplay(rtl_tap1_fptr,"%h ",testFull.full.full_st1.tap_int_wr_data);
  end
end

// Store Store 
initial begin
  rtl_st1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/full/data/rtl_st1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testFull.full.stage_0_data_out_vld & testFull.full.stage_0_data_out_rdy)) begin 
    $fdisplay(rtl_st1_fptr,"%h ",testFull.full.stage_0_data_out);
  end
end
assign st_data_out_rdy = 'd1;
assign full_st0_ctrl_int.load_length = 3'd5;
assign full_st0_ctrl_int.load_depth = 3'd5;
assign full_st0_ctrl_int.state_length = 'd1;
assign full_st0_ctrl_int.error_length = 4'd11;
assign full_st0_ctrl_int.input_stage = 'd1;
assign full_st1_ctrl_int.load_length = 4'd11;
assign full_st1_ctrl_int.load_depth = 3'd5;
assign full_st1_ctrl_int.state_length = 'd1;
assign full_st1_ctrl_int.error_length = 4'd5;

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

