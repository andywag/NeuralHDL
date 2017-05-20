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
  input float_24_8              biasIn_0,
  input                         clk,
  input float_24_8              dataIn_0,
  input                         reset,
  input float_24_8              tapIn_0,
  output float_24_8             dataOut_0);

// Parameters 



// Wires 

  wire          signed  [48:0]  dataOut_0_abs_out ;  // <49,0>
  wire          signed  [48:0]  dataOut_0_add_out ;  // <49,0>
  wire          signed  [25:0]  dataOut_0_ain1    ;  // <26,0>
  wire          signed  [25:0]  dataOut_0_ain2    ;  // <26,0>
  wire                  [7:0]   dataOut_0_del     ;  // <8,0>
  wire                  [7:0]   dataOut_0_exp     ;  // <8,0>
  wire          signed  [25:0]  dataOut_0_nsh_in  ;  // <26,0>
  wire          signed  [48:0]  dataOut_0_nsh_out ;  // <49,0>
  wire          signed  [48:0]  dataOut_0_rnd_bit ;  // <49,0>
  wire                          dataOut_0_sgn     ;  // <1,0>
  wire          signed  [48:0]  dataOut_0_sh_in   ;  // <49,0>
  wire          signed  [25:0]  dataOut_0_sh_in1  ;  // <26,0>
  wire          signed  [48:0]  dataOut_0_sh_out  ;  // <49,0>
  wire                  [7:0]   dataOut_0_shift   ;  // <8,0>
  float_24_8                    dataOut_0_tap_data_out;  // <1,0>
  wire                  [23:0]  dataOut_0_tap_data_out_man1;  // <24,0>
  wire                  [23:0]  dataOut_0_tap_data_out_man2;  // <24,0>
  wire                  [47:0]  dataOut_0_tap_data_out_res;  // <48,0>
  wire                  [47:0]  dataOut_0_tap_data_out_res_im;  // <48,0>
  wire                  [1:0]   dataOut_0_tap_data_out_sh;  // <2,0>


// Registers 

  float_24_8                    dataOut_0_int;  // <1,0>
  float_24_8                    dataOut_0_register;  // <1,0>
  float_24_8                    dataOut_0_tap_data_out_int;  // <1,0>
  float_24_8                    dataOut_0_tap_data_out_reg;  // <1,0>


// Other



assign dataOut_0_tap_data_out_man1 = {1'd1,dataIn_0.man};
assign dataOut_0_tap_data_out_man2 = {1'd1,tapIn_0.man};
assign dataOut_0_tap_data_out_res_im = dataOut_0_tap_data_out_man1 * dataOut_0_tap_data_out_man2;
assign dataOut_0_tap_data_out_res = dataOut_0_tap_data_out_res_im;
always @* begin
  dataOut_0_tap_data_out_int.sgn <= (dataIn_0.sgn ^ tapIn_0.sgn);
  if (dataOut_0_tap_data_out_res[47]) begin
    assign dataOut_0_tap_data_out_sh = 2'd0;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd126;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[46:24] + dataOut_0_tap_data_out_res[23];
  end
  else if (dataOut_0_tap_data_out_res[46]) begin 
    assign dataOut_0_tap_data_out_sh = 2'd1;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd127;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[45:23] + dataOut_0_tap_data_out_res[22];
  end
  else if (dataOut_0_tap_data_out_res[45]) begin 
    assign dataOut_0_tap_data_out_sh = 2'd2;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd128;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[44:22] + dataOut_0_tap_data_out_res[21];
  end
end
always @* begin
  dataOut_0_tap_data_out_int.sgn <= (dataIn_0.sgn ^ tapIn_0.sgn);
  if (dataOut_0_tap_data_out_res[47]) begin
    assign dataOut_0_tap_data_out_sh = 2'd0;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd126;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[46:24] + dataOut_0_tap_data_out_res[23];
  end
  else if (dataOut_0_tap_data_out_res[46]) begin 
    assign dataOut_0_tap_data_out_sh = 2'd1;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd127;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[45:23] + dataOut_0_tap_data_out_res[22];
  end
  else if (dataOut_0_tap_data_out_res[45]) begin 
    assign dataOut_0_tap_data_out_sh = 2'd2;
    dataOut_0_tap_data_out_int.exp <= dataIn_0.exp[7:0] + tapIn_0.exp[7:0] - 8'd128;
    dataOut_0_tap_data_out_int.man <= dataOut_0_tap_data_out_res[44:22] + dataOut_0_tap_data_out_res[21];
  end
end
always @(posedge clk) begin
  if (reset) begin
    dataOut_0_tap_data_out_reg <= 'd0;
  end
  else begin
    dataOut_0_tap_data_out_reg <= dataOut_0_tap_data_out_int;
  end
end
assign dataOut_0_tap_data_out = dataOut_0_tap_data_out_reg;
assign dataOut_0_del = dataOut_0_tap_data_out.exp[7:0] - biasIn_0.exp[7:0];
assign dataOut_0_shift = dataOut_0_del[7] ? -dataOut_0_del : dataOut_0_del;
assign dataOut_0_sgn = dataOut_0_del[7];
assign dataOut_0_exp = dataOut_0_del[7] ? biasIn_0.exp : dataOut_0_tap_data_out.exp;
assign dataOut_0_ain1 = dataOut_0_tap_data_out.sgn ? -{1'd0,1'd0,1'd1,dataOut_0_tap_data_out.man} : {1'd0,1'd0,1'd1,dataOut_0_tap_data_out.man};
assign dataOut_0_ain2 = biasIn_0.sgn ? -{1'd0,1'd0,1'd1,biasIn_0.man} : {1'd0,1'd0,1'd1,biasIn_0.man};
assign dataOut_0_nsh_in = dataOut_0_sgn ? dataOut_0_ain2 : dataOut_0_ain1;
assign dataOut_0_sh_in1 = dataOut_0_sgn ? dataOut_0_ain1 : dataOut_0_ain2;
assign dataOut_0_sh_in = {dataOut_0_sh_in1,23'd0};
assign dataOut_0_sh_out = (dataOut_0_sh_in >>> dataOut_0_shift);
assign dataOut_0_rnd_bit = (49'd8388608 >> dataOut_0_shift);
assign dataOut_0_nsh_out = {dataOut_0_nsh_in,23'd0};
assign dataOut_0_add_out = dataOut_0_sh_out[48:0] + dataOut_0_nsh_out[48:0];
assign dataOut_0_abs_out = dataOut_0_add_out[48] ? -dataOut_0_add_out : dataOut_0_add_out;
always @* begin
  dataOut_0_int.sgn <= dataOut_0_add_out[48];
  if (dataOut_0_abs_out[47]) begin
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd1;
    dataOut_0_int.man <= dataOut_0_abs_out[46:24] + (dataOut_0_abs_out[23] & (|dataOut_0_abs_out[22:0] | dataOut_0_abs_out[24]));
  end
  else if (dataOut_0_abs_out[46]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd2;
    dataOut_0_int.man <= dataOut_0_abs_out[45:23] + (dataOut_0_abs_out[22] & (|dataOut_0_abs_out[21:0] | dataOut_0_abs_out[23]));
  end
  else if (dataOut_0_abs_out[45]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd3;
    dataOut_0_int.man <= dataOut_0_abs_out[44:22] + (dataOut_0_abs_out[21] & (|dataOut_0_abs_out[20:0] | dataOut_0_abs_out[22]));
  end
  else if (dataOut_0_abs_out[44]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd4;
    dataOut_0_int.man <= dataOut_0_abs_out[43:21] + (dataOut_0_abs_out[20] & (|dataOut_0_abs_out[19:0] | dataOut_0_abs_out[21]));
  end
  else if (dataOut_0_abs_out[43]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd5;
    dataOut_0_int.man <= dataOut_0_abs_out[42:20] + (dataOut_0_abs_out[19] & (|dataOut_0_abs_out[18:0] | dataOut_0_abs_out[20]));
  end
  else if (dataOut_0_abs_out[42]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd6;
    dataOut_0_int.man <= dataOut_0_abs_out[41:19] + (dataOut_0_abs_out[18] & (|dataOut_0_abs_out[17:0] | dataOut_0_abs_out[19]));
  end
  else if (dataOut_0_abs_out[41]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd7;
    dataOut_0_int.man <= dataOut_0_abs_out[40:18] + (dataOut_0_abs_out[17] & (|dataOut_0_abs_out[16:0] | dataOut_0_abs_out[18]));
  end
  else if (dataOut_0_abs_out[40]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd8;
    dataOut_0_int.man <= dataOut_0_abs_out[39:17] + (dataOut_0_abs_out[16] & (|dataOut_0_abs_out[15:0] | dataOut_0_abs_out[17]));
  end
  else if (dataOut_0_abs_out[39]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd9;
    dataOut_0_int.man <= dataOut_0_abs_out[38:16] + (dataOut_0_abs_out[15] & (|dataOut_0_abs_out[14:0] | dataOut_0_abs_out[16]));
  end
  else if (dataOut_0_abs_out[38]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd10;
    dataOut_0_int.man <= dataOut_0_abs_out[37:15] + (dataOut_0_abs_out[14] & (|dataOut_0_abs_out[13:0] | dataOut_0_abs_out[15]));
  end
  else if (dataOut_0_abs_out[37]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd11;
    dataOut_0_int.man <= dataOut_0_abs_out[36:14] + (dataOut_0_abs_out[13] & (|dataOut_0_abs_out[12:0] | dataOut_0_abs_out[14]));
  end
  else if (dataOut_0_abs_out[36]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd12;
    dataOut_0_int.man <= dataOut_0_abs_out[35:13] + (dataOut_0_abs_out[12] & (|dataOut_0_abs_out[11:0] | dataOut_0_abs_out[13]));
  end
  else if (dataOut_0_abs_out[35]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd13;
    dataOut_0_int.man <= dataOut_0_abs_out[34:12] + (dataOut_0_abs_out[11] & (|dataOut_0_abs_out[10:0] | dataOut_0_abs_out[12]));
  end
  else if (dataOut_0_abs_out[34]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd14;
    dataOut_0_int.man <= dataOut_0_abs_out[33:11] + (dataOut_0_abs_out[10] & (|dataOut_0_abs_out[9:0] | dataOut_0_abs_out[11]));
  end
  else if (dataOut_0_abs_out[33]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd15;
    dataOut_0_int.man <= dataOut_0_abs_out[32:10] + (dataOut_0_abs_out[9] & (|dataOut_0_abs_out[8:0] | dataOut_0_abs_out[10]));
  end
  else if (dataOut_0_abs_out[32]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd16;
    dataOut_0_int.man <= dataOut_0_abs_out[31:9] + (dataOut_0_abs_out[8] & (|dataOut_0_abs_out[7:0] | dataOut_0_abs_out[9]));
  end
  else if (dataOut_0_abs_out[31]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd17;
    dataOut_0_int.man <= dataOut_0_abs_out[30:8] + (dataOut_0_abs_out[7] & (|dataOut_0_abs_out[6:0] | dataOut_0_abs_out[8]));
  end
  else if (dataOut_0_abs_out[30]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd18;
    dataOut_0_int.man <= dataOut_0_abs_out[29:7] + (dataOut_0_abs_out[6] & (|dataOut_0_abs_out[5:0] | dataOut_0_abs_out[7]));
  end
  else if (dataOut_0_abs_out[29]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd19;
    dataOut_0_int.man <= dataOut_0_abs_out[28:6] + (dataOut_0_abs_out[5] & (|dataOut_0_abs_out[4:0] | dataOut_0_abs_out[6]));
  end
  else if (dataOut_0_abs_out[28]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd20;
    dataOut_0_int.man <= dataOut_0_abs_out[27:5] + (dataOut_0_abs_out[4] & (|dataOut_0_abs_out[3:0] | dataOut_0_abs_out[5]));
  end
  else if (dataOut_0_abs_out[27]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd21;
    dataOut_0_int.man <= dataOut_0_abs_out[26:4] + (dataOut_0_abs_out[3] & (|dataOut_0_abs_out[2:0] | dataOut_0_abs_out[4]));
  end
  else if (dataOut_0_abs_out[26]) begin 
    dataOut_0_int.exp <= dataOut_0_exp[7:0] + 8'd2 - 8'd22;
    dataOut_0_int.man <= dataOut_0_abs_out[25:3] + (dataOut_0_abs_out[2] & (|dataOut_0_abs_out[1:0] | dataOut_0_abs_out[3]));
  end
end
always @(posedge clk) begin
  if (reset) begin
    dataOut_0_register <= 'd0;
  end
  else begin
    dataOut_0_register <= dataOut_0_int;
  end
end
assign dataOut_0 = dataOut_0_register;
endmodule

