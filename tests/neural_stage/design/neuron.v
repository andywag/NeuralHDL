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
  input                         clk,
  input float_24_8              neural_stage_bias,
  input float_24_8              neural_stage_data,
  input float_24_8              neural_stage_tap,
  input                         reset,
  output float_24_8             neural_stage_data_out);

// Parameters 



// Wires 

  wire          signed  [48:0]  neural_stage_data_out_abs_out;  // <49,0>
  wire          signed  [48:0]  neural_stage_data_out_add_out;  // <49,0>
  wire          signed  [25:0]  neural_stage_data_out_ain1;  // <26,0>
  wire          signed  [25:0]  neural_stage_data_out_ain2;  // <26,0>
  wire                  [8:0]   neural_stage_data_out_del;  // <9,0>
  wire                  [7:0]   neural_stage_data_out_exp;  // <8,0>
  wire          signed  [25:0]  neural_stage_data_out_nsh_in;  // <26,0>
  wire          signed  [48:0]  neural_stage_data_out_nsh_out;  // <49,0>
  wire          signed  [48:0]  neural_stage_data_out_rnd_bit;  // <49,0>
  wire                          neural_stage_data_out_sgn;  // <1,0>
  wire          signed  [48:0]  neural_stage_data_out_sh_in;  // <49,0>
  wire          signed  [25:0]  neural_stage_data_out_sh_in1;  // <26,0>
  wire          signed  [48:0]  neural_stage_data_out_sh_out;  // <49,0>
  wire                  [7:0]   neural_stage_data_out_shift;  // <8,0>
  float_24_8                    neural_stage_data_out_tap_data_out;  // <1,0>
  wire                  [23:0]  neural_stage_data_out_tap_data_out_man1;  // <24,0>
  wire                  [23:0]  neural_stage_data_out_tap_data_out_man2;  // <24,0>
  wire                  [47:0]  neural_stage_data_out_tap_data_out_res;  // <48,0>
  wire                  [47:0]  neural_stage_data_out_tap_data_out_res_im;  // <48,0>
  wire                  [1:0]   neural_stage_data_out_tap_data_out_sh;  // <2,0>
  wire                  [7:0]   neural_stage_data_out_tap_data_out_tot_exp;  // <8,0>


// Registers 

  float_24_8                    neural_stage_data_out_int;  // <1,0>
  float_24_8                    neural_stage_data_out_register;  // <1,0>
  float_24_8                    neural_stage_data_out_tap_data_out_int;  // <1,0>
  float_24_8                    neural_stage_data_out_tap_data_out_reg;  // <1,0>


// Other



assign neural_stage_data_out_tap_data_out_man1 = {1'd1,neural_stage_data.man};
assign neural_stage_data_out_tap_data_out_man2 = {1'd1,neural_stage_tap.man};
assign neural_stage_data_out_tap_data_out_res_im = neural_stage_data_out_tap_data_out_man1 * neural_stage_data_out_tap_data_out_man2;
assign neural_stage_data_out_tap_data_out_res = neural_stage_data_out_tap_data_out_res_im;
assign neural_stage_data_out_tap_data_out_tot_exp = neural_stage_data.exp[7:0] + neural_stage_tap.exp[7:0];
always @* begin
  neural_stage_data_out_tap_data_out_int.sgn <= (neural_stage_data.sgn ^ neural_stage_tap.sgn);
  if ((neural_stage_data_out_tap_data_out_tot_exp <= 'd128)) begin
    assign neural_stage_data_out_tap_data_out_sh = 2'd0;
    neural_stage_data_out_tap_data_out_int.sgn <= 'd0;
    neural_stage_data_out_tap_data_out_int.exp <= 8'd0;
    neural_stage_data_out_tap_data_out_int.man <= 23'd0;
  end
  else if (neural_stage_data_out_tap_data_out_res[47]) begin 
    assign neural_stage_data_out_tap_data_out_sh = 2'd0;
    neural_stage_data_out_tap_data_out_int.exp <= neural_stage_data.exp[7:0] + neural_stage_tap.exp[7:0] - 8'd126;
    neural_stage_data_out_tap_data_out_int.man <= neural_stage_data_out_tap_data_out_res[46:24] + neural_stage_data_out_tap_data_out_res[23];
  end
  else if (neural_stage_data_out_tap_data_out_res[46]) begin 
    assign neural_stage_data_out_tap_data_out_sh = 2'd1;
    neural_stage_data_out_tap_data_out_int.exp <= neural_stage_data.exp[7:0] + neural_stage_tap.exp[7:0] - 8'd127;
    neural_stage_data_out_tap_data_out_int.man <= neural_stage_data_out_tap_data_out_res[45:23] + neural_stage_data_out_tap_data_out_res[22];
  end
  else if (neural_stage_data_out_tap_data_out_res[45]) begin 
    assign neural_stage_data_out_tap_data_out_sh = 2'd2;
    neural_stage_data_out_tap_data_out_int.exp <= neural_stage_data.exp[7:0] + neural_stage_tap.exp[7:0] - 8'd128;
    neural_stage_data_out_tap_data_out_int.man <= neural_stage_data_out_tap_data_out_res[44:22] + neural_stage_data_out_tap_data_out_res[21];
  end
end
always @(posedge clk) begin
  if (reset) begin
    neural_stage_data_out_tap_data_out_reg <= 'd0;
  end
  else begin
    neural_stage_data_out_tap_data_out_reg <= neural_stage_data_out_tap_data_out_int;
  end
end
assign neural_stage_data_out_tap_data_out = neural_stage_data_out_tap_data_out_reg;
assign neural_stage_data_out_del = {0'd0,neural_stage_data_out_tap_data_out.exp} - {0'd0,neural_stage_bias.exp};
assign neural_stage_data_out_shift = neural_stage_data_out_del[8] ? -neural_stage_data_out_del : neural_stage_data_out_del;
assign neural_stage_data_out_sgn = neural_stage_data_out_del[8];
assign neural_stage_data_out_exp = neural_stage_data_out_del[8] ? neural_stage_bias.exp : neural_stage_data_out_tap_data_out.exp;
assign neural_stage_data_out_ain1 = neural_stage_data_out_tap_data_out.sgn ? -{1'd0,1'd0,1'd1,neural_stage_data_out_tap_data_out.man} : {1'd0,1'd0,1'd1,neural_stage_data_out_tap_data_out.man};
assign neural_stage_data_out_ain2 = neural_stage_bias.sgn ? -{1'd0,1'd0,1'd1,neural_stage_bias.man} : {1'd0,1'd0,1'd1,neural_stage_bias.man};
assign neural_stage_data_out_nsh_in = neural_stage_data_out_sgn ? neural_stage_data_out_ain2 : neural_stage_data_out_ain1;
assign neural_stage_data_out_sh_in1 = neural_stage_data_out_sgn ? neural_stage_data_out_ain1 : neural_stage_data_out_ain2;
assign neural_stage_data_out_sh_in = {neural_stage_data_out_sh_in1,23'd0};
assign neural_stage_data_out_sh_out = (neural_stage_data_out_sh_in >>> neural_stage_data_out_shift);
assign neural_stage_data_out_rnd_bit = (49'd8388608 >> neural_stage_data_out_shift);
assign neural_stage_data_out_nsh_out = {neural_stage_data_out_nsh_in,23'd0};
assign neural_stage_data_out_add_out = neural_stage_data_out_sh_out[48:0] + neural_stage_data_out_nsh_out[48:0];
assign neural_stage_data_out_abs_out = neural_stage_data_out_add_out[48] ? -neural_stage_data_out_add_out : neural_stage_data_out_add_out;
always @* begin
  neural_stage_data_out_int.sgn <= neural_stage_data_out_add_out[48];
  if ((neural_stage_data_out_exp < 'd10)) begin
    neural_stage_data_out_int.exp <= 8'd0;
    neural_stage_data_out_int.man <= 23'd0;
  end
  else if (neural_stage_data_out_abs_out[47]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd1;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[46:24] + (neural_stage_data_out_abs_out[23] & (|neural_stage_data_out_abs_out[22:0] | neural_stage_data_out_abs_out[24]));
  end
  else if (neural_stage_data_out_abs_out[46]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd2;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[45:23] + (neural_stage_data_out_abs_out[22] & (|neural_stage_data_out_abs_out[21:0] | neural_stage_data_out_abs_out[23]));
  end
  else if (neural_stage_data_out_abs_out[45]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd3;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[44:22] + (neural_stage_data_out_abs_out[21] & (|neural_stage_data_out_abs_out[20:0] | neural_stage_data_out_abs_out[22]));
  end
  else if (neural_stage_data_out_abs_out[44]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd4;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[43:21] + (neural_stage_data_out_abs_out[20] & (|neural_stage_data_out_abs_out[19:0] | neural_stage_data_out_abs_out[21]));
  end
  else if (neural_stage_data_out_abs_out[43]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd5;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[42:20] + (neural_stage_data_out_abs_out[19] & (|neural_stage_data_out_abs_out[18:0] | neural_stage_data_out_abs_out[20]));
  end
  else if (neural_stage_data_out_abs_out[42]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd6;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[41:19] + (neural_stage_data_out_abs_out[18] & (|neural_stage_data_out_abs_out[17:0] | neural_stage_data_out_abs_out[19]));
  end
  else if (neural_stage_data_out_abs_out[41]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd7;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[40:18] + (neural_stage_data_out_abs_out[17] & (|neural_stage_data_out_abs_out[16:0] | neural_stage_data_out_abs_out[18]));
  end
  else if (neural_stage_data_out_abs_out[40]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd8;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[39:17] + (neural_stage_data_out_abs_out[16] & (|neural_stage_data_out_abs_out[15:0] | neural_stage_data_out_abs_out[17]));
  end
  else if (neural_stage_data_out_abs_out[39]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd9;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[38:16] + (neural_stage_data_out_abs_out[15] & (|neural_stage_data_out_abs_out[14:0] | neural_stage_data_out_abs_out[16]));
  end
  else if (neural_stage_data_out_abs_out[38]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd10;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[37:15] + (neural_stage_data_out_abs_out[14] & (|neural_stage_data_out_abs_out[13:0] | neural_stage_data_out_abs_out[15]));
  end
  else if (neural_stage_data_out_abs_out[37]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd11;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[36:14] + (neural_stage_data_out_abs_out[13] & (|neural_stage_data_out_abs_out[12:0] | neural_stage_data_out_abs_out[14]));
  end
  else if (neural_stage_data_out_abs_out[36]) begin 
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd2 - 8'd12;
    neural_stage_data_out_int.man <= neural_stage_data_out_abs_out[35:13] + (neural_stage_data_out_abs_out[12] & (|neural_stage_data_out_abs_out[11:0] | neural_stage_data_out_abs_out[13]));
  end
  else begin
    neural_stage_data_out_int.exp <= neural_stage_data_out_exp[7:0] + 8'd1 - 8'd12;
    neural_stage_data_out_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    neural_stage_data_out_register <= 'd0;
  end
  else begin
    neural_stage_data_out_register <= neural_stage_data_out_int;
  end
end
assign neural_stage_data_out = neural_stage_data_out_register;
endmodule

