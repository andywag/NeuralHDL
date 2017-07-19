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
  input float_24_8              data_in,
  input                         reset,
  input float_24_8              taps,
  output float_24_8             data_out);

// Parameters 



// Wires 

  wire          signed  [48:0]  data_out_abs_out  ;  // <49,0>
  wire          signed  [48:0]  data_out_add_out  ;  // <49,0>
  wire          signed  [25:0]  data_out_ain1     ;  // <26,0>
  wire          signed  [25:0]  data_out_ain2     ;  // <26,0>
  wire                  [8:0]   data_out_del      ;  // <9,0>
  wire                  [7:0]   data_out_exp      ;  // <8,0>
  wire          signed  [25:0]  data_out_nsh_in   ;  // <26,0>
  wire          signed  [48:0]  data_out_nsh_out  ;  // <49,0>
  wire          signed  [48:0]  data_out_rnd_bit  ;  // <49,0>
  wire                          data_out_sgn      ;  // <1,0>
  wire          signed  [48:0]  data_out_sh_in    ;  // <49,0>
  wire          signed  [25:0]  data_out_sh_in1   ;  // <26,0>
  wire          signed  [48:0]  data_out_sh_out   ;  // <49,0>
  wire                  [7:0]   data_out_shift    ;  // <8,0>
  float_24_8                    data_out_tap_data_out;  // <1,0>
  wire                  [23:0]  data_out_tap_data_out_man1  ;  // <24,0>
  wire                  [23:0]  data_out_tap_data_out_man2  ;  // <24,0>
  wire                  [47:0]  data_out_tap_data_out_res  ;  // <48,0>
  wire                  [47:0]  data_out_tap_data_out_res_im  ;  // <48,0>
  wire                  [1:0]   data_out_tap_data_out_sh  ;  // <2,0>
  wire                  [7:0]   data_out_tap_data_out_tot_exp  ;  // <8,0>


// Registers 

  float_24_8                    data_out_int;  // <1,0>
  float_24_8                    data_out_register;  // <1,0>
  float_24_8                    data_out_tap_data_out_int;  // <1,0>
  float_24_8                    data_out_tap_data_out_reg;  // <1,0>


// Other



assign data_out_tap_data_out_man1 = {1'd1,data_in.man};
assign data_out_tap_data_out_man2 = {1'd1,taps.man};
assign data_out_tap_data_out_res_im = data_out_tap_data_out_man1 * data_out_tap_data_out_man2;
assign data_out_tap_data_out_res = data_out_tap_data_out_res_im;
assign data_out_tap_data_out_tot_exp = data_in.exp[7:0] + taps.exp[7:0];
always @* begin
  data_out_tap_data_out_int.sgn <= (data_in.sgn ^ taps.sgn);
  if ((data_out_tap_data_out_tot_exp <= 'd128)) begin
    assign data_out_tap_data_out_sh = 2'd0;
    data_out_tap_data_out_int.sgn <= 'd0;
    data_out_tap_data_out_int.exp <= 8'd0;
    data_out_tap_data_out_int.man <= 23'd0;
  end
  else if (data_out_tap_data_out_res[47]) begin 
    assign data_out_tap_data_out_sh = 2'd0;
    data_out_tap_data_out_int.exp <= data_in.exp[7:0] + taps.exp[7:0] - 8'd126;
    data_out_tap_data_out_int.man <= data_out_tap_data_out_res[46:24] + data_out_tap_data_out_res[23];
  end
  else if (data_out_tap_data_out_res[46]) begin 
    assign data_out_tap_data_out_sh = 2'd1;
    data_out_tap_data_out_int.exp <= data_in.exp[7:0] + taps.exp[7:0] - 8'd127;
    data_out_tap_data_out_int.man <= data_out_tap_data_out_res[45:23] + data_out_tap_data_out_res[22];
  end
  else if (data_out_tap_data_out_res[45]) begin 
    assign data_out_tap_data_out_sh = 2'd2;
    data_out_tap_data_out_int.exp <= data_in.exp[7:0] + taps.exp[7:0] - 8'd128;
    data_out_tap_data_out_int.man <= data_out_tap_data_out_res[44:22] + data_out_tap_data_out_res[21];
  end
end
always @(posedge clk) begin
  if (reset) begin
    data_out_tap_data_out_reg <= 'd0;
  end
  else begin
    data_out_tap_data_out_reg <= data_out_tap_data_out_int;
  end
end
assign data_out_tap_data_out = data_out_tap_data_out_reg;
assign data_out_del = {0'd0,data_out_tap_data_out.exp} - {0'd0,bias.exp};
assign data_out_shift = data_out_del[8] ? -data_out_del : data_out_del;
assign data_out_sgn = data_out_del[8];
assign data_out_exp = data_out_del[8] ? bias.exp : data_out_tap_data_out.exp;
assign data_out_ain1 = data_out_tap_data_out.sgn ? -{1'd0,1'd0,1'd1,data_out_tap_data_out.man} : {1'd0,1'd0,1'd1,data_out_tap_data_out.man};
assign data_out_ain2 = bias.sgn ? -{1'd0,1'd0,1'd1,bias.man} : {1'd0,1'd0,1'd1,bias.man};
assign data_out_nsh_in = data_out_sgn ? data_out_ain2 : data_out_ain1;
assign data_out_sh_in1 = data_out_sgn ? data_out_ain1 : data_out_ain2;
assign data_out_sh_in = {data_out_sh_in1,23'd0};
assign data_out_sh_out = (data_out_sh_in >>> data_out_shift);
assign data_out_rnd_bit = (49'd8388608 >> data_out_shift);
assign data_out_nsh_out = {data_out_nsh_in,23'd0};
assign data_out_add_out = data_out_sh_out[48:0] + data_out_nsh_out[48:0];
assign data_out_abs_out = data_out_add_out[48] ? -data_out_add_out : data_out_add_out;
always @* begin
  data_out_int.sgn <= data_out_add_out[48];
  if ((data_out_exp < 'd10)) begin
    data_out_int.exp <= 8'd0;
    data_out_int.man <= 23'd0;
  end
  else if (data_out_abs_out[47]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd1;
    data_out_int.man <= data_out_abs_out[46:24] + (data_out_abs_out[23] & (|data_out_abs_out[22:0] | data_out_abs_out[24]));
  end
  else if (data_out_abs_out[46]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd2;
    data_out_int.man <= data_out_abs_out[45:23] + (data_out_abs_out[22] & (|data_out_abs_out[21:0] | data_out_abs_out[23]));
  end
  else if (data_out_abs_out[45]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd3;
    data_out_int.man <= data_out_abs_out[44:22] + (data_out_abs_out[21] & (|data_out_abs_out[20:0] | data_out_abs_out[22]));
  end
  else if (data_out_abs_out[44]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd4;
    data_out_int.man <= data_out_abs_out[43:21] + (data_out_abs_out[20] & (|data_out_abs_out[19:0] | data_out_abs_out[21]));
  end
  else if (data_out_abs_out[43]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd5;
    data_out_int.man <= data_out_abs_out[42:20] + (data_out_abs_out[19] & (|data_out_abs_out[18:0] | data_out_abs_out[20]));
  end
  else if (data_out_abs_out[42]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd6;
    data_out_int.man <= data_out_abs_out[41:19] + (data_out_abs_out[18] & (|data_out_abs_out[17:0] | data_out_abs_out[19]));
  end
  else if (data_out_abs_out[41]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd7;
    data_out_int.man <= data_out_abs_out[40:18] + (data_out_abs_out[17] & (|data_out_abs_out[16:0] | data_out_abs_out[18]));
  end
  else if (data_out_abs_out[40]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd8;
    data_out_int.man <= data_out_abs_out[39:17] + (data_out_abs_out[16] & (|data_out_abs_out[15:0] | data_out_abs_out[17]));
  end
  else if (data_out_abs_out[39]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd9;
    data_out_int.man <= data_out_abs_out[38:16] + (data_out_abs_out[15] & (|data_out_abs_out[14:0] | data_out_abs_out[16]));
  end
  else if (data_out_abs_out[38]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd10;
    data_out_int.man <= data_out_abs_out[37:15] + (data_out_abs_out[14] & (|data_out_abs_out[13:0] | data_out_abs_out[15]));
  end
  else if (data_out_abs_out[37]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd11;
    data_out_int.man <= data_out_abs_out[36:14] + (data_out_abs_out[13] & (|data_out_abs_out[12:0] | data_out_abs_out[14]));
  end
  else if (data_out_abs_out[36]) begin 
    data_out_int.exp <= data_out_exp[7:0] + 8'd2 - 8'd12;
    data_out_int.man <= data_out_abs_out[35:13] + (data_out_abs_out[12] & (|data_out_abs_out[11:0] | data_out_abs_out[13]));
  end
  else begin
    data_out_int.exp <= data_out_exp[7:0] + 8'd1 - 8'd12;
    data_out_int.man <= 23'd0;
  end
end
always @(posedge clk) begin
  if (reset) begin
    data_out_register <= 'd0;
  end
  else begin
    data_out_register <= data_out_int;
  end
end
assign data_out = data_out_register;
endmodule

