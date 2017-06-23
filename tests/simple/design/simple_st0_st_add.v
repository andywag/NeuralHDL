//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple_st0_st_add
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple_st0_st_add(
  input                         clk,
  input float_24_8              outLine_w0,
  input                         reset,
  input float_24_8              simple_st0_st_bias_r8,
  output float_24_8             simple_st0_st_adder);

// Parameters 



// Wires 

  wire          signed  [48:0]  simple_st0_st_adder_abs_out;  // <49,0>
  wire          signed  [48:0]  simple_st0_st_adder_add_out;  // <49,0>
  wire          signed  [25:0]  simple_st0_st_adder_ain1;  // <26,0>
  wire          signed  [25:0]  simple_st0_st_adder_ain2;  // <26,0>
  wire                  [8:0]   simple_st0_st_adder_del;  // <9,0>
  wire                  [7:0]   simple_st0_st_adder_exp;  // <8,0>
  wire          signed  [25:0]  simple_st0_st_adder_nsh_in;  // <26,0>
  wire          signed  [48:0]  simple_st0_st_adder_nsh_out;  // <49,0>
  wire          signed  [48:0]  simple_st0_st_adder_rnd_bit;  // <49,0>
  wire                          simple_st0_st_adder_sgn;  // <1,0>
  wire          signed  [48:0]  simple_st0_st_adder_sh_in;  // <49,0>
  wire          signed  [25:0]  simple_st0_st_adder_sh_in1;  // <26,0>
  wire          signed  [48:0]  simple_st0_st_adder_sh_out;  // <49,0>
  wire                  [7:0]   simple_st0_st_adder_shift;  // <8,0>


// Registers 

  float_24_8                    simple_st0_st_adder_int;  // <1,0>
  float_24_8                    simple_st0_st_adder_register;  // <1,0>


// Other



assign simple_st0_st_adder_del = {0'd0,outLine_w0.exp} - {0'd0,simple_st0_st_bias_r8.exp};
assign simple_st0_st_adder_shift = simple_st0_st_adder_del[8] ? -simple_st0_st_adder_del : simple_st0_st_adder_del;
assign simple_st0_st_adder_sgn = simple_st0_st_adder_del[8];
assign simple_st0_st_adder_exp = simple_st0_st_adder_del[8] ? simple_st0_st_bias_r8.exp : outLine_w0.exp;
assign simple_st0_st_adder_ain1 = outLine_w0.sgn ? -{1'd0,1'd0,1'd1,outLine_w0.man} : {1'd0,1'd0,1'd1,outLine_w0.man};
assign simple_st0_st_adder_ain2 = simple_st0_st_bias_r8.sgn ? -{1'd0,1'd0,1'd1,simple_st0_st_bias_r8.man} : {1'd0,1'd0,1'd1,simple_st0_st_bias_r8.man};
assign simple_st0_st_adder_nsh_in = simple_st0_st_adder_sgn ? simple_st0_st_adder_ain2 : simple_st0_st_adder_ain1;
assign simple_st0_st_adder_sh_in1 = simple_st0_st_adder_sgn ? simple_st0_st_adder_ain1 : simple_st0_st_adder_ain2;
assign simple_st0_st_adder_sh_in = {simple_st0_st_adder_sh_in1,23'd0};
assign simple_st0_st_adder_sh_out = (simple_st0_st_adder_sh_in >>> simple_st0_st_adder_shift);
assign simple_st0_st_adder_rnd_bit = (49'd8388608 >> simple_st0_st_adder_shift);
assign simple_st0_st_adder_nsh_out = {simple_st0_st_adder_nsh_in,23'd0};
assign simple_st0_st_adder_add_out = simple_st0_st_adder_sh_out[48:0] + simple_st0_st_adder_nsh_out[48:0];
assign simple_st0_st_adder_abs_out = simple_st0_st_adder_add_out[48] ? -simple_st0_st_adder_add_out : simple_st0_st_adder_add_out;
always @* begin
  simple_st0_st_adder_int.sgn <= simple_st0_st_adder_add_out[48];
  if ((simple_st0_st_adder_exp < 'd10)) begin
    simple_st0_st_adder_int.exp <= 8'd0;
    simple_st0_st_adder_int.man <= 23'd0;
  end
  else if (simple_st0_st_adder_abs_out[47]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd1;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[46:24] + (simple_st0_st_adder_abs_out[23] & (|simple_st0_st_adder_abs_out[22:0] | simple_st0_st_adder_abs_out[24]));
  end
  else if (simple_st0_st_adder_abs_out[46]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd2;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[45:23] + (simple_st0_st_adder_abs_out[22] & (|simple_st0_st_adder_abs_out[21:0] | simple_st0_st_adder_abs_out[23]));
  end
  else if (simple_st0_st_adder_abs_out[45]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd3;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[44:22] + (simple_st0_st_adder_abs_out[21] & (|simple_st0_st_adder_abs_out[20:0] | simple_st0_st_adder_abs_out[22]));
  end
  else if (simple_st0_st_adder_abs_out[44]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd4;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[43:21] + (simple_st0_st_adder_abs_out[20] & (|simple_st0_st_adder_abs_out[19:0] | simple_st0_st_adder_abs_out[21]));
  end
  else if (simple_st0_st_adder_abs_out[43]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd5;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[42:20] + (simple_st0_st_adder_abs_out[19] & (|simple_st0_st_adder_abs_out[18:0] | simple_st0_st_adder_abs_out[20]));
  end
  else if (simple_st0_st_adder_abs_out[42]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd6;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[41:19] + (simple_st0_st_adder_abs_out[18] & (|simple_st0_st_adder_abs_out[17:0] | simple_st0_st_adder_abs_out[19]));
  end
  else if (simple_st0_st_adder_abs_out[41]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd7;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[40:18] + (simple_st0_st_adder_abs_out[17] & (|simple_st0_st_adder_abs_out[16:0] | simple_st0_st_adder_abs_out[18]));
  end
  else if (simple_st0_st_adder_abs_out[40]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd8;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[39:17] + (simple_st0_st_adder_abs_out[16] & (|simple_st0_st_adder_abs_out[15:0] | simple_st0_st_adder_abs_out[17]));
  end
  else if (simple_st0_st_adder_abs_out[39]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd9;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[38:16] + (simple_st0_st_adder_abs_out[15] & (|simple_st0_st_adder_abs_out[14:0] | simple_st0_st_adder_abs_out[16]));
  end
  else if (simple_st0_st_adder_abs_out[38]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd10;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[37:15] + (simple_st0_st_adder_abs_out[14] & (|simple_st0_st_adder_abs_out[13:0] | simple_st0_st_adder_abs_out[15]));
  end
  else if (simple_st0_st_adder_abs_out[37]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd11;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[36:14] + (simple_st0_st_adder_abs_out[13] & (|simple_st0_st_adder_abs_out[12:0] | simple_st0_st_adder_abs_out[14]));
  end
  else if (simple_st0_st_adder_abs_out[36]) begin 
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd2 - 8'd12;
    simple_st0_st_adder_int.man <= simple_st0_st_adder_abs_out[35:13] + (simple_st0_st_adder_abs_out[12] & (|simple_st0_st_adder_abs_out[11:0] | simple_st0_st_adder_abs_out[13]));
  end
  else begin
    simple_st0_st_adder_int.exp <= simple_st0_st_adder_exp[7:0] + 8'd1 - 8'd12;
    simple_st0_st_adder_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    simple_st0_st_adder_register <= 'd0;
  end
  else begin
    simple_st0_st_adder_register <= simple_st0_st_adder_int;
  end
end
assign simple_st0_st_adder = simple_st0_st_adder_register;
endmodule

