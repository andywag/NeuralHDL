//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage_st_add
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage_st_add(
  input                         clk,
  input float_24_8              outLine_w0,
  input                         reset,
  input float_24_8              stage_st_bias,
  output float_24_8             stage_st_adder);

// Parameters 



// Wires 

  wire          signed  [48:0]  stage_st_adder_abs_out;  // <49,0>
  wire          signed  [48:0]  stage_st_adder_add_out;  // <49,0>
  wire          signed  [25:0]  stage_st_adder_ain1;  // <26,0>
  wire          signed  [25:0]  stage_st_adder_ain2;  // <26,0>
  wire                  [8:0]   stage_st_adder_del;  // <9,0>
  wire                  [7:0]   stage_st_adder_exp;  // <8,0>
  wire          signed  [25:0]  stage_st_adder_nsh_in;  // <26,0>
  wire          signed  [48:0]  stage_st_adder_nsh_out;  // <49,0>
  wire          signed  [48:0]  stage_st_adder_rnd_bit;  // <49,0>
  wire                          stage_st_adder_sgn;  // <1,0>
  wire          signed  [48:0]  stage_st_adder_sh_in;  // <49,0>
  wire          signed  [25:0]  stage_st_adder_sh_in1;  // <26,0>
  wire          signed  [48:0]  stage_st_adder_sh_out;  // <49,0>
  wire                  [7:0]   stage_st_adder_shift;  // <8,0>


// Registers 

  float_24_8                    stage_st_adder_int;  // <1,0>
  float_24_8                    stage_st_adder_register;  // <1,0>


// Other



assign stage_st_adder_del = {0'd0,outLine_w0.exp} - {0'd0,stage_st_bias.exp};
assign stage_st_adder_shift = stage_st_adder_del[8] ? -stage_st_adder_del : stage_st_adder_del;
assign stage_st_adder_sgn = stage_st_adder_del[8];
assign stage_st_adder_exp = stage_st_adder_del[8] ? stage_st_bias.exp : outLine_w0.exp;
assign stage_st_adder_ain1 = outLine_w0.sgn ? -{1'd0,1'd0,1'd1,outLine_w0.man} : {1'd0,1'd0,1'd1,outLine_w0.man};
assign stage_st_adder_ain2 = stage_st_bias.sgn ? -{1'd0,1'd0,1'd1,stage_st_bias.man} : {1'd0,1'd0,1'd1,stage_st_bias.man};
assign stage_st_adder_nsh_in = stage_st_adder_sgn ? stage_st_adder_ain2 : stage_st_adder_ain1;
assign stage_st_adder_sh_in1 = stage_st_adder_sgn ? stage_st_adder_ain1 : stage_st_adder_ain2;
assign stage_st_adder_sh_in = {stage_st_adder_sh_in1,23'd0};
assign stage_st_adder_sh_out = (stage_st_adder_sh_in >>> stage_st_adder_shift);
assign stage_st_adder_rnd_bit = (49'd8388608 >> stage_st_adder_shift);
assign stage_st_adder_nsh_out = {stage_st_adder_nsh_in,23'd0};
assign stage_st_adder_add_out = stage_st_adder_sh_out[48:0] + stage_st_adder_nsh_out[48:0];
assign stage_st_adder_abs_out = stage_st_adder_add_out[48] ? -stage_st_adder_add_out : stage_st_adder_add_out;
always @* begin
  stage_st_adder_int.sgn <= stage_st_adder_add_out[48];
  if ((stage_st_adder_exp < 'd10)) begin
    stage_st_adder_int.exp <= 8'd0;
    stage_st_adder_int.man <= 23'd0;
  end
  else if (stage_st_adder_abs_out[47]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd1;
    stage_st_adder_int.man <= stage_st_adder_abs_out[46:24] + (stage_st_adder_abs_out[23] & (|stage_st_adder_abs_out[22:0] | stage_st_adder_abs_out[24]));
  end
  else if (stage_st_adder_abs_out[46]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd2;
    stage_st_adder_int.man <= stage_st_adder_abs_out[45:23] + (stage_st_adder_abs_out[22] & (|stage_st_adder_abs_out[21:0] | stage_st_adder_abs_out[23]));
  end
  else if (stage_st_adder_abs_out[45]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd3;
    stage_st_adder_int.man <= stage_st_adder_abs_out[44:22] + (stage_st_adder_abs_out[21] & (|stage_st_adder_abs_out[20:0] | stage_st_adder_abs_out[22]));
  end
  else if (stage_st_adder_abs_out[44]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd4;
    stage_st_adder_int.man <= stage_st_adder_abs_out[43:21] + (stage_st_adder_abs_out[20] & (|stage_st_adder_abs_out[19:0] | stage_st_adder_abs_out[21]));
  end
  else if (stage_st_adder_abs_out[43]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd5;
    stage_st_adder_int.man <= stage_st_adder_abs_out[42:20] + (stage_st_adder_abs_out[19] & (|stage_st_adder_abs_out[18:0] | stage_st_adder_abs_out[20]));
  end
  else if (stage_st_adder_abs_out[42]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd6;
    stage_st_adder_int.man <= stage_st_adder_abs_out[41:19] + (stage_st_adder_abs_out[18] & (|stage_st_adder_abs_out[17:0] | stage_st_adder_abs_out[19]));
  end
  else if (stage_st_adder_abs_out[41]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd7;
    stage_st_adder_int.man <= stage_st_adder_abs_out[40:18] + (stage_st_adder_abs_out[17] & (|stage_st_adder_abs_out[16:0] | stage_st_adder_abs_out[18]));
  end
  else if (stage_st_adder_abs_out[40]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd8;
    stage_st_adder_int.man <= stage_st_adder_abs_out[39:17] + (stage_st_adder_abs_out[16] & (|stage_st_adder_abs_out[15:0] | stage_st_adder_abs_out[17]));
  end
  else if (stage_st_adder_abs_out[39]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd9;
    stage_st_adder_int.man <= stage_st_adder_abs_out[38:16] + (stage_st_adder_abs_out[15] & (|stage_st_adder_abs_out[14:0] | stage_st_adder_abs_out[16]));
  end
  else if (stage_st_adder_abs_out[38]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd10;
    stage_st_adder_int.man <= stage_st_adder_abs_out[37:15] + (stage_st_adder_abs_out[14] & (|stage_st_adder_abs_out[13:0] | stage_st_adder_abs_out[15]));
  end
  else if (stage_st_adder_abs_out[37]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd11;
    stage_st_adder_int.man <= stage_st_adder_abs_out[36:14] + (stage_st_adder_abs_out[13] & (|stage_st_adder_abs_out[12:0] | stage_st_adder_abs_out[14]));
  end
  else if (stage_st_adder_abs_out[36]) begin 
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd2 - 8'd12;
    stage_st_adder_int.man <= stage_st_adder_abs_out[35:13] + (stage_st_adder_abs_out[12] & (|stage_st_adder_abs_out[11:0] | stage_st_adder_abs_out[13]));
  end
  else begin
    stage_st_adder_int.exp <= stage_st_adder_exp[7:0] + 8'd1 - 8'd12;
    stage_st_adder_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_st_adder_register <= 'd0;
  end
  else begin
    stage_st_adder_register <= stage_st_adder_int;
  end
end
assign stage_st_adder = stage_st_adder_register;
endmodule

