//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testSimple
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testSimple(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                          expected_rdy      ;  // <1,0>
  wire                          load_finish       ;  // <1,0>
  wire                          simple_st0_ctrl_data_fifo_data_ready;  // <1,0>
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
  reg                   [31:0]  expected_mem[0:72];  // <32,0>
  reg                           expected_vld      ;  // <1,0>
  reg                   [31:0]  in_rdy_count      ;  // <32,0>
  reg                   [31:0]  rtl_bias_fptr     ;  // <32,0>
  reg                   [31:0]  rtl_error_fptr    ;  // <32,0>
  reg                   [31:0]  rtl_expected_fptr ;  // <32,0>
  reg                   [31:0]  rtl_in_fptr       ;  // <32,0>
  reg                   [31:0]  rtl_mem_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_out_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_pre_fptr      ;  // <32,0>
  reg                           st_data_fst       ;  // <1,0>
  reg                   [31:0]  st_data_mem[0:36] ;  // <32,0>
  reg                           st_data_out_pre_rdy;  // <1,0>
  reg                           st_data_out_rdy   ;  // <1,0>
  reg                           st_data_vld       ;  // <1,0>
  reg                           tap_in_fst        ;  // <1,0>
  reg                           tap_in_vld        ;  // <1,0>


// Other

  float_24_8                    expected;  // <1,0>
  simple_st0_ctrl_int_t         simple_st0_ctrl_int;  // <1,0>
  float_24_8                    st_data;  // <1,0>
  float_24_8                    st_data_out;  // <1,0>
  float_24_8                    st_data_out_pre;  // <1,0>
  float_24_8                    tap_in;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// simple
////////////////////////////////////////////////////////////////////////////////

simple simple (
    .clk(clk),
    .expected(expected),
    .expected_fst(expected_fst),
    .expected_rdy(expected_rdy),
    .expected_vld(expected_vld),
    .load_finish(load_finish),
    .reset(reset),
    .simple_st0_ctrl_data_fifo_data_ready(simple_st0_ctrl_data_fifo_data_ready),
    .simple_st0_ctrl_int(simple_st0_ctrl_int),
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
    if ((counter == 'd4608)) begin
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
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_data.hex",st_data_mem);
end

assign st_data = in_rdy_count[31] ? 'd0 : st_data_mem[in_rdy_count];
always @(posedge clk) begin
  if (reset) begin
    exp_rdy_count <= 32'd0;
  end
  else if ((expected_rdy & expected_vld)) begin 
    if ((exp_rdy_count == 'd71)) begin
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
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_expected.hex",expected_mem);
end

assign expected = exp_rdy_count[31] ? 'd0 : expected_mem[exp_rdy_count];

// Store Store st_data
initial begin
  rtl_in_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_in.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((st_data_rdy & st_data_vld)) begin 
    $fdisplay(rtl_in_fptr,"%h ",st_data);
  end
end

// Store Store st_data_out
initial begin
  rtl_out_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_out.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (st_data_out_vld) begin 
    $fdisplay(rtl_out_fptr,"%h ",st_data_out);
  end
end

// Store Store st_data_out_pre
initial begin
  rtl_pre_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_pre.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (st_data_out_pre_vld) begin 
    $fdisplay(rtl_pre_fptr,"%h ",st_data_out_pre);
  end
end

// Store Store 
initial begin
  rtl_error_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_error.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((testSimple.simple.simple_err.stage_0_error_vld & testSimple.simple.simple_err.stage_0_error_rdy)) begin 
    $fdisplay(rtl_error_fptr,"%h ",testSimple.simple.simple_err.stage_0_error);
  end
end

// Store Store 
initial begin
  rtl_bias_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_bias.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (testSimple.simple.simple_st0.bias_int.wr_vld) begin 
    $fdisplay(rtl_bias_fptr,"%h ",testSimple.simple.simple_st0.bias_int_wr_data);
  end
end

// Store Store expected
initial begin
  rtl_expected_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_expected.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if ((expected_vld & expected_rdy)) begin 
    $fdisplay(rtl_expected_fptr,"%h ",expected);
  end
end

// Store Store 
initial begin
  rtl_mem_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/simple/data/rtl_mem.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (testSimple.simple.simple_st0.data_int.rd_vld) begin 
    $fdisplay(rtl_mem_fptr,"%h ",testSimple.simple.simple_st0.data_int_rd_data);
  end
end
assign simple_st0_ctrl_int.error_length = 4'd11;
assign simple_st0_ctrl_int.load_length = 3'd5;
assign simple_st0_ctrl_int.load_depth = 3'd5;
assign simple_st0_ctrl_int.state_length = 'd1;

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

