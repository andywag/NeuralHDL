//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st1_st_bias_add
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st1_st_bias_add(
  input float_24_8              bias_add_delay_w0,
  input                         clk,
  input float_24_8              four_12_12_st1_st_bias_r1,
  input                         reset,
  output float_24_8             four_12_12_st1_st_bias_adder);

// Parameters 



// Wires 

  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_abs_out  ;  // <49,0>
  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_add_out  ;  // <49,0>
  wire          signed  [25:0]  four_12_12_st1_st_bias_adder_ain1  ;  // <26,0>
  wire          signed  [25:0]  four_12_12_st1_st_bias_adder_ain2  ;  // <26,0>
  wire                  [8:0]   four_12_12_st1_st_bias_adder_del  ;  // <9,0>
  wire                  [7:0]   four_12_12_st1_st_bias_adder_exp  ;  // <8,0>
  wire          signed  [25:0]  four_12_12_st1_st_bias_adder_nsh_in  ;  // <26,0>
  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_nsh_out  ;  // <49,0>
  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_rnd_bit  ;  // <49,0>
  wire                          four_12_12_st1_st_bias_adder_sgn  ;  // <1,0>
  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_sh_in  ;  // <49,0>
  wire          signed  [25:0]  four_12_12_st1_st_bias_adder_sh_in1  ;  // <26,0>
  wire          signed  [48:0]  four_12_12_st1_st_bias_adder_sh_out  ;  // <49,0>
  wire                  [7:0]   four_12_12_st1_st_bias_adder_shift  ;  // <8,0>


// Registers 

  float_24_8                    four_12_12_st1_st_bias_adder_int;  // <1,0>
  float_24_8                    four_12_12_st1_st_bias_adder_register;  // <1,0>


// Other



assign four_12_12_st1_st_bias_adder_del = {0'd0,bias_add_delay_w0.exp} - {0'd0,four_12_12_st1_st_bias_r1.exp};
assign four_12_12_st1_st_bias_adder_shift = four_12_12_st1_st_bias_adder_del[8] ? -four_12_12_st1_st_bias_adder_del : four_12_12_st1_st_bias_adder_del;
assign four_12_12_st1_st_bias_adder_sgn = four_12_12_st1_st_bias_adder_del[8];
assign four_12_12_st1_st_bias_adder_exp = four_12_12_st1_st_bias_adder_del[8] ? four_12_12_st1_st_bias_r1.exp : bias_add_delay_w0.exp;
assign four_12_12_st1_st_bias_adder_ain1 = bias_add_delay_w0.sgn ? -{1'd0,1'd0,1'd1,bias_add_delay_w0.man} : {1'd0,1'd0,1'd1,bias_add_delay_w0.man};
assign four_12_12_st1_st_bias_adder_ain2 = four_12_12_st1_st_bias_r1.sgn ? -{1'd0,1'd0,1'd1,four_12_12_st1_st_bias_r1.man} : {1'd0,1'd0,1'd1,four_12_12_st1_st_bias_r1.man};
assign four_12_12_st1_st_bias_adder_nsh_in = four_12_12_st1_st_bias_adder_sgn ? four_12_12_st1_st_bias_adder_ain2 : four_12_12_st1_st_bias_adder_ain1;
assign four_12_12_st1_st_bias_adder_sh_in1 = four_12_12_st1_st_bias_adder_sgn ? four_12_12_st1_st_bias_adder_ain1 : four_12_12_st1_st_bias_adder_ain2;
assign four_12_12_st1_st_bias_adder_sh_in = {four_12_12_st1_st_bias_adder_sh_in1,23'd0};
assign four_12_12_st1_st_bias_adder_sh_out = (four_12_12_st1_st_bias_adder_sh_in >>> four_12_12_st1_st_bias_adder_shift);
assign four_12_12_st1_st_bias_adder_rnd_bit = (49'd8388608 >> four_12_12_st1_st_bias_adder_shift);
assign four_12_12_st1_st_bias_adder_nsh_out = {four_12_12_st1_st_bias_adder_nsh_in,23'd0};
assign four_12_12_st1_st_bias_adder_add_out = four_12_12_st1_st_bias_adder_sh_out[48:0] + four_12_12_st1_st_bias_adder_nsh_out[48:0];
assign four_12_12_st1_st_bias_adder_abs_out = four_12_12_st1_st_bias_adder_add_out[48] ? -four_12_12_st1_st_bias_adder_add_out : four_12_12_st1_st_bias_adder_add_out;
always @* begin
  four_12_12_st1_st_bias_adder_int.sgn <= four_12_12_st1_st_bias_adder_add_out[48];
  if ((four_12_12_st1_st_bias_adder_exp < 'd10)) begin
    four_12_12_st1_st_bias_adder_int.exp <= 8'd0;
    four_12_12_st1_st_bias_adder_int.man <= 23'd0;
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[47]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd1;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[46:24] + (four_12_12_st1_st_bias_adder_abs_out[23] & (|four_12_12_st1_st_bias_adder_abs_out[22:0] | four_12_12_st1_st_bias_adder_abs_out[24]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[46]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd2;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[45:23] + (four_12_12_st1_st_bias_adder_abs_out[22] & (|four_12_12_st1_st_bias_adder_abs_out[21:0] | four_12_12_st1_st_bias_adder_abs_out[23]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[45]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd3;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[44:22] + (four_12_12_st1_st_bias_adder_abs_out[21] & (|four_12_12_st1_st_bias_adder_abs_out[20:0] | four_12_12_st1_st_bias_adder_abs_out[22]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[44]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd4;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[43:21] + (four_12_12_st1_st_bias_adder_abs_out[20] & (|four_12_12_st1_st_bias_adder_abs_out[19:0] | four_12_12_st1_st_bias_adder_abs_out[21]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[43]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd5;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[42:20] + (four_12_12_st1_st_bias_adder_abs_out[19] & (|four_12_12_st1_st_bias_adder_abs_out[18:0] | four_12_12_st1_st_bias_adder_abs_out[20]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[42]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd6;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[41:19] + (four_12_12_st1_st_bias_adder_abs_out[18] & (|four_12_12_st1_st_bias_adder_abs_out[17:0] | four_12_12_st1_st_bias_adder_abs_out[19]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[41]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd7;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[40:18] + (four_12_12_st1_st_bias_adder_abs_out[17] & (|four_12_12_st1_st_bias_adder_abs_out[16:0] | four_12_12_st1_st_bias_adder_abs_out[18]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[40]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd8;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[39:17] + (four_12_12_st1_st_bias_adder_abs_out[16] & (|four_12_12_st1_st_bias_adder_abs_out[15:0] | four_12_12_st1_st_bias_adder_abs_out[17]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[39]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd9;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[38:16] + (four_12_12_st1_st_bias_adder_abs_out[15] & (|four_12_12_st1_st_bias_adder_abs_out[14:0] | four_12_12_st1_st_bias_adder_abs_out[16]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[38]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd10;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[37:15] + (four_12_12_st1_st_bias_adder_abs_out[14] & (|four_12_12_st1_st_bias_adder_abs_out[13:0] | four_12_12_st1_st_bias_adder_abs_out[15]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[37]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd11;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[36:14] + (four_12_12_st1_st_bias_adder_abs_out[13] & (|four_12_12_st1_st_bias_adder_abs_out[12:0] | four_12_12_st1_st_bias_adder_abs_out[14]));
  end
  else if (four_12_12_st1_st_bias_adder_abs_out[36]) begin 
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd2 - 8'd12;
    four_12_12_st1_st_bias_adder_int.man <= four_12_12_st1_st_bias_adder_abs_out[35:13] + (four_12_12_st1_st_bias_adder_abs_out[12] & (|four_12_12_st1_st_bias_adder_abs_out[11:0] | four_12_12_st1_st_bias_adder_abs_out[13]));
  end
  else begin
    four_12_12_st1_st_bias_adder_int.exp <= four_12_12_st1_st_bias_adder_exp[7:0] + 8'd1 - 8'd12;
    four_12_12_st1_st_bias_adder_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    four_12_12_st1_st_bias_adder_register <= 'd0;
  end
  else begin
    four_12_12_st1_st_bias_adder_register <= four_12_12_st1_st_bias_adder_int;
  end
end
assign four_12_12_st1_st_bias_adder = four_12_12_st1_st_bias_adder_register;
endmodule

