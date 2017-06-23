//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_st0_st_add
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_st0_st_add(
  input                         clk,
  input float_24_8              full_st0_st_bias_r8,
  input float_24_8              outLine_w0,
  input                         reset,
  output float_24_8             full_st0_st_adder);

// Parameters 



// Wires 

  wire          signed  [48:0]  full_st0_st_adder_abs_out;  // <49,0>
  wire          signed  [48:0]  full_st0_st_adder_add_out;  // <49,0>
  wire          signed  [25:0]  full_st0_st_adder_ain1;  // <26,0>
  wire          signed  [25:0]  full_st0_st_adder_ain2;  // <26,0>
  wire                  [8:0]   full_st0_st_adder_del;  // <9,0>
  wire                  [7:0]   full_st0_st_adder_exp;  // <8,0>
  wire          signed  [25:0]  full_st0_st_adder_nsh_in;  // <26,0>
  wire          signed  [48:0]  full_st0_st_adder_nsh_out;  // <49,0>
  wire          signed  [48:0]  full_st0_st_adder_rnd_bit;  // <49,0>
  wire                          full_st0_st_adder_sgn;  // <1,0>
  wire          signed  [48:0]  full_st0_st_adder_sh_in;  // <49,0>
  wire          signed  [25:0]  full_st0_st_adder_sh_in1;  // <26,0>
  wire          signed  [48:0]  full_st0_st_adder_sh_out;  // <49,0>
  wire                  [7:0]   full_st0_st_adder_shift;  // <8,0>


// Registers 

  float_24_8                    full_st0_st_adder_int;  // <1,0>
  float_24_8                    full_st0_st_adder_register;  // <1,0>


// Other



assign full_st0_st_adder_del = {0'd0,outLine_w0.exp} - {0'd0,full_st0_st_bias_r8.exp};
assign full_st0_st_adder_shift = full_st0_st_adder_del[8] ? -full_st0_st_adder_del : full_st0_st_adder_del;
assign full_st0_st_adder_sgn = full_st0_st_adder_del[8];
assign full_st0_st_adder_exp = full_st0_st_adder_del[8] ? full_st0_st_bias_r8.exp : outLine_w0.exp;
assign full_st0_st_adder_ain1 = outLine_w0.sgn ? -{1'd0,1'd0,1'd1,outLine_w0.man} : {1'd0,1'd0,1'd1,outLine_w0.man};
assign full_st0_st_adder_ain2 = full_st0_st_bias_r8.sgn ? -{1'd0,1'd0,1'd1,full_st0_st_bias_r8.man} : {1'd0,1'd0,1'd1,full_st0_st_bias_r8.man};
assign full_st0_st_adder_nsh_in = full_st0_st_adder_sgn ? full_st0_st_adder_ain2 : full_st0_st_adder_ain1;
assign full_st0_st_adder_sh_in1 = full_st0_st_adder_sgn ? full_st0_st_adder_ain1 : full_st0_st_adder_ain2;
assign full_st0_st_adder_sh_in = {full_st0_st_adder_sh_in1,23'd0};
assign full_st0_st_adder_sh_out = (full_st0_st_adder_sh_in >>> full_st0_st_adder_shift);
assign full_st0_st_adder_rnd_bit = (49'd8388608 >> full_st0_st_adder_shift);
assign full_st0_st_adder_nsh_out = {full_st0_st_adder_nsh_in,23'd0};
assign full_st0_st_adder_add_out = full_st0_st_adder_sh_out[48:0] + full_st0_st_adder_nsh_out[48:0];
assign full_st0_st_adder_abs_out = full_st0_st_adder_add_out[48] ? -full_st0_st_adder_add_out : full_st0_st_adder_add_out;
always @* begin
  full_st0_st_adder_int.sgn <= full_st0_st_adder_add_out[48];
  if ((full_st0_st_adder_exp < 'd10)) begin
    full_st0_st_adder_int.exp <= 8'd0;
    full_st0_st_adder_int.man <= 23'd0;
  end
  else if (full_st0_st_adder_abs_out[47]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd1;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[46:24] + (full_st0_st_adder_abs_out[23] & (|full_st0_st_adder_abs_out[22:0] | full_st0_st_adder_abs_out[24]));
  end
  else if (full_st0_st_adder_abs_out[46]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd2;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[45:23] + (full_st0_st_adder_abs_out[22] & (|full_st0_st_adder_abs_out[21:0] | full_st0_st_adder_abs_out[23]));
  end
  else if (full_st0_st_adder_abs_out[45]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd3;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[44:22] + (full_st0_st_adder_abs_out[21] & (|full_st0_st_adder_abs_out[20:0] | full_st0_st_adder_abs_out[22]));
  end
  else if (full_st0_st_adder_abs_out[44]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd4;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[43:21] + (full_st0_st_adder_abs_out[20] & (|full_st0_st_adder_abs_out[19:0] | full_st0_st_adder_abs_out[21]));
  end
  else if (full_st0_st_adder_abs_out[43]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd5;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[42:20] + (full_st0_st_adder_abs_out[19] & (|full_st0_st_adder_abs_out[18:0] | full_st0_st_adder_abs_out[20]));
  end
  else if (full_st0_st_adder_abs_out[42]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd6;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[41:19] + (full_st0_st_adder_abs_out[18] & (|full_st0_st_adder_abs_out[17:0] | full_st0_st_adder_abs_out[19]));
  end
  else if (full_st0_st_adder_abs_out[41]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd7;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[40:18] + (full_st0_st_adder_abs_out[17] & (|full_st0_st_adder_abs_out[16:0] | full_st0_st_adder_abs_out[18]));
  end
  else if (full_st0_st_adder_abs_out[40]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd8;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[39:17] + (full_st0_st_adder_abs_out[16] & (|full_st0_st_adder_abs_out[15:0] | full_st0_st_adder_abs_out[17]));
  end
  else if (full_st0_st_adder_abs_out[39]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd9;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[38:16] + (full_st0_st_adder_abs_out[15] & (|full_st0_st_adder_abs_out[14:0] | full_st0_st_adder_abs_out[16]));
  end
  else if (full_st0_st_adder_abs_out[38]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd10;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[37:15] + (full_st0_st_adder_abs_out[14] & (|full_st0_st_adder_abs_out[13:0] | full_st0_st_adder_abs_out[15]));
  end
  else if (full_st0_st_adder_abs_out[37]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd11;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[36:14] + (full_st0_st_adder_abs_out[13] & (|full_st0_st_adder_abs_out[12:0] | full_st0_st_adder_abs_out[14]));
  end
  else if (full_st0_st_adder_abs_out[36]) begin 
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd2 - 8'd12;
    full_st0_st_adder_int.man <= full_st0_st_adder_abs_out[35:13] + (full_st0_st_adder_abs_out[12] & (|full_st0_st_adder_abs_out[11:0] | full_st0_st_adder_abs_out[13]));
  end
  else begin
    full_st0_st_adder_int.exp <= full_st0_st_adder_exp[7:0] + 8'd1 - 8'd12;
    full_st0_st_adder_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    full_st0_st_adder_register <= 'd0;
  end
  else begin
    full_st0_st_adder_register <= full_st0_st_adder_int;
  end
end
assign full_st0_st_adder = full_st0_st_adder_register;
endmodule

