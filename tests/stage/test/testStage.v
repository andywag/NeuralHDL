//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testStage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testStage(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [2:0]   bias_address      ;  // <3,0>
  wire                          st_data_out_fst   ;  // <1,0>
  wire                          st_data_out_pre_fst;  // <1,0>
  wire                          st_data_out_pre_vld;  // <1,0>
  wire                          st_data_out_vld   ;  // <1,0>
  wire                          st_data_rdy       ;  // <1,0>
  wire          signed  [48:0]  st_error_abs_out  ;  // <49,0>
  wire          signed  [48:0]  st_error_add_out  ;  // <49,0>
  wire          signed  [25:0]  st_error_ain1     ;  // <26,0>
  wire          signed  [25:0]  st_error_ain2     ;  // <26,0>
  wire                  [8:0]   st_error_del      ;  // <9,0>
  wire                  [7:0]   st_error_exp      ;  // <8,0>
  wire          signed  [25:0]  st_error_nsh_in   ;  // <26,0>
  wire          signed  [48:0]  st_error_nsh_out  ;  // <49,0>
  wire                          st_error_rdy      ;  // <1,0>
  wire          signed  [48:0]  st_error_rnd_bit  ;  // <49,0>
  wire                          st_error_sgn      ;  // <1,0>
  wire          signed  [48:0]  st_error_sh_in    ;  // <49,0>
  wire          signed  [25:0]  st_error_sh_in1   ;  // <26,0>
  wire          signed  [48:0]  st_error_sh_out   ;  // <49,0>
  wire                  [7:0]   st_error_shift    ;  // <8,0>
  wire                  [3:0]   tap_address       ;  // <4,0>
  wire                          tap_in_rdy        ;  // <1,0>


// Registers 

  reg                   [2:0]   bias_length       ;  // <3,0>
  reg                   [31:0]  counter           ;  // <32,0>
  reg                   [31:0]  expected_memory   ;  // <32,0>
  reg                   [3:0]   load_depth        ;  // <4,0>
  reg                   [2:0]   load_length       ;  // <3,0>
  reg                   [31:0]  rtl_out_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_pre_fptr      ;  // <32,0>
  reg                           st_data_fst       ;  // <1,0>
  reg                   [31:0]  st_data_mem[0:156];  // <32,0>
  reg                           st_data_out_pre_rdy;  // <1,0>
  reg                           st_data_out_rdy   ;  // <1,0>
  reg                           st_data_vld       ;  // <1,0>
  reg                           st_error_fst      ;  // <1,0>
  float_24_8                    st_error_int;  // <1,0>
  float_24_8                    st_error_register;  // <1,0>
  reg                           st_error_vld      ;  // <1,0>
  reg                           state_length      ;  // <1,0>
  reg                           tap_in_fst        ;  // <1,0>
  reg                           tap_in_vld        ;  // <1,0>


// Other

  float_24_8                    expected;  // <1,0>
  float_24_8                    st_data;  // <1,0>
  float_24_8                    st_data_out;  // <1,0>
  float_24_8                    st_data_out_pre;  // <1,0>
  float_24_8                    st_error;  // <1,0>
  float_24_8                    tap_in;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// stage
////////////////////////////////////////////////////////////////////////////////

stage stage (
    .bias_address(bias_address),
    .bias_length(bias_length),
    .clk(clk),
    .load_depth(load_depth),
    .load_length(load_length),
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
    .st_error(st_error),
    .st_error_fst(st_error_fst),
    .st_error_rdy(st_error_rdy),
    .st_error_vld(st_error_vld),
    .state_length(state_length),
    .tap_address(tap_address),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd864)) begin
      $finish;
    end
  end
end
assign st_data_vld = 'd1;

// Load st_data
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_data.hex",st_data_mem);
end

assign st_data = counter[31] ? 'd0 : st_data_mem[counter];

// Store Store st_data_out
initial begin
  rtl_out_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/stage/data/rtl_out.hex","w");
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
  rtl_pre_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/stage/data/rtl_pre.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (st_data_out_pre_vld) begin 
    $fdisplay(rtl_pre_fptr,"%h ",st_data_out_pre);
  end
end
assign load_length = 3'd5;
assign load_depth = 4'd7;
assign state_length = 'd1;
assign bias_length = 3'd5;
assign load_depth = 4'd7;
assign st_error_del = {0'd0,expected.exp} - {0'd0,st_data_out.exp};
assign st_error_shift = st_error_del[8] ? -st_error_del : st_error_del;
assign st_error_sgn = st_error_del[8];
assign st_error_exp = st_error_del[8] ? st_data_out.exp : expected.exp;
assign st_error_ain1 = expected.sgn ? -{1'd0,1'd0,1'd1,expected.man} : {1'd0,1'd0,1'd1,expected.man};
assign st_error_ain2 = st_data_out.sgn ? {1'd0,1'd0,1'd1,st_data_out.man} : -{1'd0,1'd0,1'd1,st_data_out.man};
assign st_error_nsh_in = st_error_sgn ? st_error_ain2 : st_error_ain1;
assign st_error_sh_in1 = st_error_sgn ? st_error_ain1 : st_error_ain2;
assign st_error_sh_in = {st_error_sh_in1,23'd0};
assign st_error_sh_out = (st_error_sh_in >>> st_error_shift);
assign st_error_rnd_bit = (49'd8388608 >> st_error_shift);
assign st_error_nsh_out = {st_error_nsh_in,23'd0};
assign st_error_add_out = st_error_sh_out[48:0] + st_error_nsh_out[48:0];
assign st_error_abs_out = st_error_add_out[48] ? -st_error_add_out : st_error_add_out;
always @* begin
  st_error_int.sgn <= st_error_add_out[48];
  if ((st_error_exp < 'd10)) begin
    st_error_int.exp <= 8'd0;
    st_error_int.man <= 23'd0;
  end
  else if (st_error_abs_out[47]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd1;
    st_error_int.man <= st_error_abs_out[46:24] + (st_error_abs_out[23] & (|st_error_abs_out[22:0] | st_error_abs_out[24]));
  end
  else if (st_error_abs_out[46]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd2;
    st_error_int.man <= st_error_abs_out[45:23] + (st_error_abs_out[22] & (|st_error_abs_out[21:0] | st_error_abs_out[23]));
  end
  else if (st_error_abs_out[45]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd3;
    st_error_int.man <= st_error_abs_out[44:22] + (st_error_abs_out[21] & (|st_error_abs_out[20:0] | st_error_abs_out[22]));
  end
  else if (st_error_abs_out[44]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd4;
    st_error_int.man <= st_error_abs_out[43:21] + (st_error_abs_out[20] & (|st_error_abs_out[19:0] | st_error_abs_out[21]));
  end
  else if (st_error_abs_out[43]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd5;
    st_error_int.man <= st_error_abs_out[42:20] + (st_error_abs_out[19] & (|st_error_abs_out[18:0] | st_error_abs_out[20]));
  end
  else if (st_error_abs_out[42]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd6;
    st_error_int.man <= st_error_abs_out[41:19] + (st_error_abs_out[18] & (|st_error_abs_out[17:0] | st_error_abs_out[19]));
  end
  else if (st_error_abs_out[41]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd7;
    st_error_int.man <= st_error_abs_out[40:18] + (st_error_abs_out[17] & (|st_error_abs_out[16:0] | st_error_abs_out[18]));
  end
  else if (st_error_abs_out[40]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd8;
    st_error_int.man <= st_error_abs_out[39:17] + (st_error_abs_out[16] & (|st_error_abs_out[15:0] | st_error_abs_out[17]));
  end
  else if (st_error_abs_out[39]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd9;
    st_error_int.man <= st_error_abs_out[38:16] + (st_error_abs_out[15] & (|st_error_abs_out[14:0] | st_error_abs_out[16]));
  end
  else if (st_error_abs_out[38]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd10;
    st_error_int.man <= st_error_abs_out[37:15] + (st_error_abs_out[14] & (|st_error_abs_out[13:0] | st_error_abs_out[15]));
  end
  else if (st_error_abs_out[37]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd11;
    st_error_int.man <= st_error_abs_out[36:14] + (st_error_abs_out[13] & (|st_error_abs_out[12:0] | st_error_abs_out[14]));
  end
  else if (st_error_abs_out[36]) begin 
    st_error_int.exp <= st_error_exp[7:0] + 8'd2 - 8'd12;
    st_error_int.man <= st_error_abs_out[35:13] + (st_error_abs_out[12] & (|st_error_abs_out[11:0] | st_error_abs_out[13]));
  end
  else begin
    st_error_int.exp <= st_error_exp[7:0] + 8'd1 - 8'd12;
    st_error_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    st_error_register <= 'd0;
  end
  else begin
    st_error_register <= st_error_int;
  end
end
assign st_error = st_error_register;

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

