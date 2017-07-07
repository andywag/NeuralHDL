//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_err_ctrl
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_err_ctrl(
  input                         clk,
  input float_24_8              expected,
  input                         expected_fst,
  input                 [31:0]  expected_int_rd_data,
  input                         expected_vld,
  input                         reset,
  input float_24_8              stage_1_data_out,
  input                         stage_1_data_out_fst,
  input                         stage_1_data_out_vld,
  input                         zctrl_rdy,
  output expected_int_32_6      expected_int,
  output                [31:0]  expected_int_wr_data,
  output                        expected_rdy,
  output                        stage_1_data_out_rdy,
  output float_24_8             zctrl,
  output                        zctrl_fst,
  output                        zctrl_vld);

// Parameters 



// Wires 

  float_24_8                    calc_in;  // <1,0>
  wire          signed  [48:0]  zctrl_abs_out     ;  // <49,0>
  wire          signed  [48:0]  zctrl_add_out     ;  // <49,0>
  wire          signed  [25:0]  zctrl_ain1        ;  // <26,0>
  wire          signed  [25:0]  zctrl_ain2        ;  // <26,0>
  wire                  [8:0]   zctrl_del         ;  // <9,0>
  wire                  [7:0]   zctrl_exp         ;  // <8,0>
  wire          signed  [25:0]  zctrl_nsh_in      ;  // <26,0>
  wire          signed  [48:0]  zctrl_nsh_out     ;  // <49,0>
  wire          signed  [48:0]  zctrl_rnd_bit     ;  // <49,0>
  wire                          zctrl_sgn         ;  // <1,0>
  wire          signed  [48:0]  zctrl_sh_in       ;  // <49,0>
  wire          signed  [25:0]  zctrl_sh_in1      ;  // <26,0>
  wire          signed  [48:0]  zctrl_sh_out      ;  // <49,0>
  wire                  [7:0]   zctrl_shift       ;  // <8,0>


// Registers 

  reg                   [6:0]   fifo_depth        ;  // <7,0>
  reg                   [5:0]   input_counter     ;  // <6,0>
  reg                   [5:0]   output_counter    ;  // <6,0>
  float_24_8                    stage_1_data_out_r1;  // <1,0>
  float_24_8                    stage_1_data_out_r2;  // <1,0>
  reg                           stage_1_data_out_vld_r1  ;  // <1,0>
  reg                           stage_1_data_out_vld_r2  ;  // <1,0>
  reg                           stage_1_data_out_vld_r3  ;  // <1,0>
  float_24_8                    zctrl_int;  // <1,0>
  float_24_8                    zctrl_register;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    stage_1_data_out_r1 <= 'd0;
    stage_1_data_out_r2 <= 'd0;
  end
  else begin
    stage_1_data_out_r1 <= stage_1_data_out;
    stage_1_data_out_r2 <= stage_1_data_out_r1;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_1_data_out_vld_r1 <= 'd0;
    stage_1_data_out_vld_r2 <= 'd0;
    stage_1_data_out_vld_r3 <= 'd0;
  end
  else begin
    stage_1_data_out_vld_r1 <= stage_1_data_out_vld;
    stage_1_data_out_vld_r2 <= stage_1_data_out_vld_r1;
    stage_1_data_out_vld_r3 <= stage_1_data_out_vld_r2;
  end
end
assign stage_1_data_out_rdy = 'd1;

// Create the controls for the fifo
assign expected_rdy = (fifo_depth < 'd35);
always @(posedge clk) begin
  if (reset) begin
    fifo_depth <= 7'd0;
  end
  else begin
    if (((expected_rdy & expected_vld) & stage_1_data_out_vld)) begin
      fifo_depth <= fifo_depth;
    end
    else if (stage_1_data_out_vld) begin 
      fifo_depth <= fifo_depth[6:0] - 7'd1;
    end
    else if ((expected_rdy & expected_vld)) begin 
      fifo_depth <= fifo_depth[6:0] + 7'd1;
    end
  end
end

// Data Input Counter and Control
always @(posedge clk) begin
  if (reset) begin
    input_counter <= 6'd0;
  end
  else if ((expected_rdy & expected_vld)) begin 
    if ((input_counter == 'd35)) begin
      input_counter <= 6'd0;
    end
    else begin
      input_counter <= input_counter[5:0] + 6'd1;
    end
  end
end
assign expected_int.wr_vld = (expected_rdy & expected_vld);
assign expected_int.wr_address = input_counter;
assign expected_int_wr_data = expected;

// Data Output Counter and Control
always @(posedge clk) begin
  if (reset) begin
    output_counter <= 6'd0;
  end
  else if (stage_1_data_out_vld) begin 
    if ((output_counter == 'd35)) begin
      output_counter <= 6'd0;
    end
    else begin
      output_counter <= output_counter[5:0] + 6'd1;
    end
  end
end
assign expected_int.rd_vld = stage_1_data_out_vld;
assign expected_int.rd_address = output_counter;
assign zctrl_vld = stage_1_data_out_vld_r3;

// Actual Error Calculation
assign calc_in = expected_int_rd_data;
assign zctrl_del = {0'd0,calc_in.exp} - {0'd0,stage_1_data_out_r2.exp};
assign zctrl_shift = zctrl_del[8] ? -zctrl_del : zctrl_del;
assign zctrl_sgn = zctrl_del[8];
assign zctrl_exp = zctrl_del[8] ? stage_1_data_out_r2.exp : calc_in.exp;
assign zctrl_ain1 = calc_in.sgn ? -{1'd0,1'd0,1'd1,calc_in.man} : {1'd0,1'd0,1'd1,calc_in.man};
assign zctrl_ain2 = stage_1_data_out_r2.sgn ? {1'd0,1'd0,1'd1,stage_1_data_out_r2.man} : -{1'd0,1'd0,1'd1,stage_1_data_out_r2.man};
assign zctrl_nsh_in = zctrl_sgn ? zctrl_ain2 : zctrl_ain1;
assign zctrl_sh_in1 = zctrl_sgn ? zctrl_ain1 : zctrl_ain2;
assign zctrl_sh_in = {zctrl_sh_in1,23'd0};
assign zctrl_sh_out = (zctrl_sh_in >>> zctrl_shift);
assign zctrl_rnd_bit = (49'd8388608 >> zctrl_shift);
assign zctrl_nsh_out = {zctrl_nsh_in,23'd0};
assign zctrl_add_out = zctrl_sh_out[48:0] + zctrl_nsh_out[48:0];
assign zctrl_abs_out = zctrl_add_out[48] ? -zctrl_add_out : zctrl_add_out;
always @* begin
  zctrl_int.sgn <= zctrl_add_out[48];
  if ((zctrl_exp < 'd10)) begin
    zctrl_int.exp <= 8'd0;
    zctrl_int.man <= 23'd0;
  end
  else if (zctrl_abs_out[47]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd1;
    zctrl_int.man <= zctrl_abs_out[46:24] + (zctrl_abs_out[23] & (|zctrl_abs_out[22:0] | zctrl_abs_out[24]));
  end
  else if (zctrl_abs_out[46]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd2;
    zctrl_int.man <= zctrl_abs_out[45:23] + (zctrl_abs_out[22] & (|zctrl_abs_out[21:0] | zctrl_abs_out[23]));
  end
  else if (zctrl_abs_out[45]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd3;
    zctrl_int.man <= zctrl_abs_out[44:22] + (zctrl_abs_out[21] & (|zctrl_abs_out[20:0] | zctrl_abs_out[22]));
  end
  else if (zctrl_abs_out[44]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd4;
    zctrl_int.man <= zctrl_abs_out[43:21] + (zctrl_abs_out[20] & (|zctrl_abs_out[19:0] | zctrl_abs_out[21]));
  end
  else if (zctrl_abs_out[43]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd5;
    zctrl_int.man <= zctrl_abs_out[42:20] + (zctrl_abs_out[19] & (|zctrl_abs_out[18:0] | zctrl_abs_out[20]));
  end
  else if (zctrl_abs_out[42]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd6;
    zctrl_int.man <= zctrl_abs_out[41:19] + (zctrl_abs_out[18] & (|zctrl_abs_out[17:0] | zctrl_abs_out[19]));
  end
  else if (zctrl_abs_out[41]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd7;
    zctrl_int.man <= zctrl_abs_out[40:18] + (zctrl_abs_out[17] & (|zctrl_abs_out[16:0] | zctrl_abs_out[18]));
  end
  else if (zctrl_abs_out[40]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd8;
    zctrl_int.man <= zctrl_abs_out[39:17] + (zctrl_abs_out[16] & (|zctrl_abs_out[15:0] | zctrl_abs_out[17]));
  end
  else if (zctrl_abs_out[39]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd9;
    zctrl_int.man <= zctrl_abs_out[38:16] + (zctrl_abs_out[15] & (|zctrl_abs_out[14:0] | zctrl_abs_out[16]));
  end
  else if (zctrl_abs_out[38]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd10;
    zctrl_int.man <= zctrl_abs_out[37:15] + (zctrl_abs_out[14] & (|zctrl_abs_out[13:0] | zctrl_abs_out[15]));
  end
  else if (zctrl_abs_out[37]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd11;
    zctrl_int.man <= zctrl_abs_out[36:14] + (zctrl_abs_out[13] & (|zctrl_abs_out[12:0] | zctrl_abs_out[14]));
  end
  else if (zctrl_abs_out[36]) begin 
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd2 - 8'd12;
    zctrl_int.man <= zctrl_abs_out[35:13] + (zctrl_abs_out[12] & (|zctrl_abs_out[11:0] | zctrl_abs_out[13]));
  end
  else begin
    zctrl_int.exp <= zctrl_exp[7:0] + 8'd1 - 8'd12;
    zctrl_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    zctrl_register <= 'd0;
  end
  else begin
    zctrl_register <= zctrl_int;
  end
end
assign zctrl = zctrl_register;
endmodule

