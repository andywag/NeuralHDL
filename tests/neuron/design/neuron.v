//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       neuron
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module neuron(
  input float_24_8              bias,
  input                         clk,
  input float_24_8              data,
  input                         reset,
  input float_24_8              tap,
  output float_24_8             out);

// Parameters 



// Wires 

  wire          signed  [48:0]  out_abs_out       ;  // <49,0>
  wire          signed  [48:0]  out_add_out       ;  // <49,0>
  wire          signed  [25:0]  out_ain1          ;  // <26,0>
  wire          signed  [25:0]  out_ain2          ;  // <26,0>
  wire                  [7:0]   out_del           ;  // <8,0>
  wire                  [7:0]   out_exp           ;  // <8,0>
  wire          signed  [25:0]  out_nsh_in        ;  // <26,0>
  wire          signed  [48:0]  out_nsh_out       ;  // <49,0>
  wire          signed  [48:0]  out_rnd_bit       ;  // <49,0>
  wire                          out_sgn           ;  // <1,0>
  wire          signed  [48:0]  out_sh_in         ;  // <49,0>
  wire          signed  [25:0]  out_sh_in1        ;  // <26,0>
  wire          signed  [48:0]  out_sh_out        ;  // <49,0>
  wire                  [7:0]   out_shift         ;  // <8,0>
  float_24_8                    out_tap_data_out;  // <1,0>
  wire                  [23:0]  out_tap_data_out_man1;  // <24,0>
  wire                  [23:0]  out_tap_data_out_man2;  // <24,0>
  wire                  [47:0]  out_tap_data_out_res;  // <48,0>
  wire                  [47:0]  out_tap_data_out_res_im;  // <48,0>
  wire                  [1:0]   out_tap_data_out_sh;  // <2,0>


// Registers 

  float_24_8                    out_int;  // <1,0>
  float_24_8                    out_register;  // <1,0>
  float_24_8                    out_tap_data_out_int;  // <1,0>
  float_24_8                    out_tap_data_out_reg;  // <1,0>


// Other



assign out_tap_data_out_man1 = {1'd1,data.man};
assign out_tap_data_out_man2 = {1'd1,tap.man};
assign out_tap_data_out_res_im = out_tap_data_out_man1 * out_tap_data_out_man2;
assign out_tap_data_out_res = out_tap_data_out_res_im;
always @* begin
  out_tap_data_out_int.sgn <= (data.sgn ^ tap.sgn);
  if (out_tap_data_out_res[47]) begin
    assign out_tap_data_out_sh = 2'd0;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd126;
    out_tap_data_out_int.man <= out_tap_data_out_res[46:24] + out_tap_data_out_res[23];
  end
  else if (out_tap_data_out_res[46]) begin 
    assign out_tap_data_out_sh = 2'd1;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd127;
    out_tap_data_out_int.man <= out_tap_data_out_res[45:23] + out_tap_data_out_res[22];
  end
  else if (out_tap_data_out_res[45]) begin 
    assign out_tap_data_out_sh = 2'd2;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd128;
    out_tap_data_out_int.man <= out_tap_data_out_res[44:22] + out_tap_data_out_res[21];
  end
end
always @* begin
  out_tap_data_out_int.sgn <= (data.sgn ^ tap.sgn);
  if (out_tap_data_out_res[47]) begin
    assign out_tap_data_out_sh = 2'd0;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd126;
    out_tap_data_out_int.man <= out_tap_data_out_res[46:24] + out_tap_data_out_res[23];
  end
  else if (out_tap_data_out_res[46]) begin 
    assign out_tap_data_out_sh = 2'd1;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd127;
    out_tap_data_out_int.man <= out_tap_data_out_res[45:23] + out_tap_data_out_res[22];
  end
  else if (out_tap_data_out_res[45]) begin 
    assign out_tap_data_out_sh = 2'd2;
    out_tap_data_out_int.exp <= data.exp[7:0] + tap.exp[7:0] - 8'd128;
    out_tap_data_out_int.man <= out_tap_data_out_res[44:22] + out_tap_data_out_res[21];
  end
end
always @(posedge clk) begin
  if (reset) begin
    out_tap_data_out_reg <= 'd0;
  end
  else begin
    out_tap_data_out_reg <= out_tap_data_out_int;
  end
end
assign out_tap_data_out = out_tap_data_out_reg;
assign out_del = out_tap_data_out.exp[7:0] - bias.exp[7:0];
assign out_shift = out_del[7] ? -out_del : out_del;
assign out_sgn = out_del[7];
assign out_exp = out_del[7] ? bias.exp : out_tap_data_out.exp;
assign out_ain1 = out_tap_data_out.sgn ? -{1'd0,1'd0,1'd1,out_tap_data_out.man} : {1'd0,1'd0,1'd1,out_tap_data_out.man};
assign out_ain2 = bias.sgn ? -{1'd0,1'd0,1'd1,bias.man} : {1'd0,1'd0,1'd1,bias.man};
assign out_nsh_in = out_sgn ? out_ain2 : out_ain1;
assign out_sh_in1 = out_sgn ? out_ain1 : out_ain2;
assign out_sh_in = {out_sh_in1,23'd0};
assign out_sh_out = (out_sh_in >>> out_shift);
assign out_rnd_bit = (49'd8388608 >> out_shift);
assign out_nsh_out = {out_nsh_in,23'd0};
assign out_add_out = out_sh_out[48:0] + out_nsh_out[48:0];
assign out_abs_out = out_add_out[48] ? -out_add_out : out_add_out;
always @* begin
  out_int.sgn <= out_add_out[48];
  if (out_abs_out[47]) begin
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd1;
    out_int.man <= out_abs_out[46:24] + (out_abs_out[23] & (|out_abs_out[22:0] | out_abs_out[24]));
  end
  else if (out_abs_out[46]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd2;
    out_int.man <= out_abs_out[45:23] + (out_abs_out[22] & (|out_abs_out[21:0] | out_abs_out[23]));
  end
  else if (out_abs_out[45]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd3;
    out_int.man <= out_abs_out[44:22] + (out_abs_out[21] & (|out_abs_out[20:0] | out_abs_out[22]));
  end
  else if (out_abs_out[44]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd4;
    out_int.man <= out_abs_out[43:21] + (out_abs_out[20] & (|out_abs_out[19:0] | out_abs_out[21]));
  end
  else if (out_abs_out[43]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd5;
    out_int.man <= out_abs_out[42:20] + (out_abs_out[19] & (|out_abs_out[18:0] | out_abs_out[20]));
  end
  else if (out_abs_out[42]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd6;
    out_int.man <= out_abs_out[41:19] + (out_abs_out[18] & (|out_abs_out[17:0] | out_abs_out[19]));
  end
  else if (out_abs_out[41]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd7;
    out_int.man <= out_abs_out[40:18] + (out_abs_out[17] & (|out_abs_out[16:0] | out_abs_out[18]));
  end
  else if (out_abs_out[40]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd8;
    out_int.man <= out_abs_out[39:17] + (out_abs_out[16] & (|out_abs_out[15:0] | out_abs_out[17]));
  end
  else if (out_abs_out[39]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd9;
    out_int.man <= out_abs_out[38:16] + (out_abs_out[15] & (|out_abs_out[14:0] | out_abs_out[16]));
  end
  else if (out_abs_out[38]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd10;
    out_int.man <= out_abs_out[37:15] + (out_abs_out[14] & (|out_abs_out[13:0] | out_abs_out[15]));
  end
  else if (out_abs_out[37]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd11;
    out_int.man <= out_abs_out[36:14] + (out_abs_out[13] & (|out_abs_out[12:0] | out_abs_out[14]));
  end
  else if (out_abs_out[36]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd12;
    out_int.man <= out_abs_out[35:13] + (out_abs_out[12] & (|out_abs_out[11:0] | out_abs_out[13]));
  end
  else if (out_abs_out[35]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd13;
    out_int.man <= out_abs_out[34:12] + (out_abs_out[11] & (|out_abs_out[10:0] | out_abs_out[12]));
  end
  else if (out_abs_out[34]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd14;
    out_int.man <= out_abs_out[33:11] + (out_abs_out[10] & (|out_abs_out[9:0] | out_abs_out[11]));
  end
  else if (out_abs_out[33]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd15;
    out_int.man <= out_abs_out[32:10] + (out_abs_out[9] & (|out_abs_out[8:0] | out_abs_out[10]));
  end
  else if (out_abs_out[32]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd16;
    out_int.man <= out_abs_out[31:9] + (out_abs_out[8] & (|out_abs_out[7:0] | out_abs_out[9]));
  end
  else if (out_abs_out[31]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd17;
    out_int.man <= out_abs_out[30:8] + (out_abs_out[7] & (|out_abs_out[6:0] | out_abs_out[8]));
  end
  else if (out_abs_out[30]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd18;
    out_int.man <= out_abs_out[29:7] + (out_abs_out[6] & (|out_abs_out[5:0] | out_abs_out[7]));
  end
  else if (out_abs_out[29]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd19;
    out_int.man <= out_abs_out[28:6] + (out_abs_out[5] & (|out_abs_out[4:0] | out_abs_out[6]));
  end
  else if (out_abs_out[28]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd20;
    out_int.man <= out_abs_out[27:5] + (out_abs_out[4] & (|out_abs_out[3:0] | out_abs_out[5]));
  end
  else if (out_abs_out[27]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd21;
    out_int.man <= out_abs_out[26:4] + (out_abs_out[3] & (|out_abs_out[2:0] | out_abs_out[4]));
  end
  else if (out_abs_out[26]) begin 
    out_int.exp <= out_exp[7:0] + 8'd2 - 8'd22;
    out_int.man <= out_abs_out[25:3] + (out_abs_out[2] & (|out_abs_out[1:0] | out_abs_out[3]));
  end
end
always @(posedge clk) begin
  if (reset) begin
    out_register <= 'd0;
  end
  else begin
    out_register <= out_int;
  end
end
assign out = out_register;
endmodule

