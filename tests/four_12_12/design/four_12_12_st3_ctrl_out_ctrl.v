//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st3_ctrl_out_ctrl
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st3_ctrl_out_ctrl(
  input                         active,
  input                         active_normal,
  input                         active_pre,
  input                         active_start_d,
  input                 [4:0]   bias_address,
  input                         bias_enable,
  input                 [31:0]  bias_int_rd_data,
  input                 [4:0]   bias_wr_address,
  input                         clk,
  input                 [31:0]  data_int_rd_data,
  input                 [8:0]   data_read_addr,
  input                         data_valid,
  input                 [31:0]  data_value,
  input                 [8:0]   data_write_addr,
  input                         err_finish_i,
  input                         err_finish_new,
  input                 [3:0]   error_count,
  input                 [3:0]   error_phase,
  input                 [3:0]   error_phase_read,
  input                 [31:0]  error_sub_address,
  input                 [4:0]   error_tap_length,
  input                         error_tap_update_out,
  input                         error_update_first,
  input                         error_update_latch,
  input                         error_valid,
  input                 [31:0]  error_value,
  input float_24_8              four_12_12_st3_st_data_out,
  input float_24_8              four_12_12_st3_st_data_out_bias,
  input float_24_8              four_12_12_st3_st_data_out_pre,
  input                 [383:0]  four_12_12_st3_st_tap_out,
  input                         read_finish,
  input                         reset,
  input                         stage_3_data_out_pre_rdy,
  input                         stage_3_data_out_rdy,
  input                         stage_3_error_out_rdy,
  input                 [4:0]   tap_address,
  input                         tap_enable,
  input                 [383:0]  tap_int_rd_data,
  input                         zerror_int_rdy,
  output bias_int_32_4          bias_int,
  output                [31:0]  bias_int_wr_data,
  output data_int_32_9          data_int,
  output                [31:0]  data_int_wr_data,
  output                        first,
  output float_24_8             four_12_12_st3_st_bias,
  output float_24_8             four_12_12_st3_st_data,
  output float_24_8             stage_3_data_out,
  output                        stage_3_data_out_fst,
  output float_24_8             stage_3_data_out_pre,
  output                        stage_3_data_out_pre_fst,
  output                        stage_3_data_out_pre_vld,
  output                        stage_3_data_out_vld,
  output float_24_8             stage_3_error_out,
  output                        stage_3_error_out_fst,
  output                        stage_3_error_out_vld,
  output                        stage_error_back,
  output                        stage_error_first,
  output                        stage_error_mode,
  output tap_int_384_5          tap_int,
  output                [383:0]  tap_int_wr_data,
  output taps_typ_12            taps,
  output                        update_error_first,
  output float_24_8             zerror_int,
  output                        zerror_int_fst,
  output                        zerror_int_vld);

// Parameters 



// Wires 

  wire                          errorUpdateRemove  ;  // <1,0>
  wire                          error_tap_write   ;  // <1,0>
  wire                  [4:0]   rd_address_wire   ;  // <5,0>
  wire                          wr_address_vld    ;  // <1,0>


// Registers 

  reg                           active_start_d_r1  ;  // <1,0>
  reg                           active_start_d_r2  ;  // <1,0>
  reg                           errorUpdateRemove_r1  ;  // <1,0>
  reg                           errorUpdateRemove_r10  ;  // <1,0>
  reg                           errorUpdateRemove_r11  ;  // <1,0>
  reg                           errorUpdateRemove_r12  ;  // <1,0>
  reg                           errorUpdateRemove_r13  ;  // <1,0>
  reg                           errorUpdateRemove_r14  ;  // <1,0>
  reg                           errorUpdateRemove_r15  ;  // <1,0>
  reg                           errorUpdateRemove_r16  ;  // <1,0>
  reg                           errorUpdateRemove_r2  ;  // <1,0>
  reg                           errorUpdateRemove_r3  ;  // <1,0>
  reg                           errorUpdateRemove_r4  ;  // <1,0>
  reg                           errorUpdateRemove_r5  ;  // <1,0>
  reg                           errorUpdateRemove_r6  ;  // <1,0>
  reg                           errorUpdateRemove_r7  ;  // <1,0>
  reg                           errorUpdateRemove_r8  ;  // <1,0>
  reg                           errorUpdateRemove_r9  ;  // <1,0>
  reg                           error_tap_update_out_r1  ;  // <1,0>
  reg                           error_tap_update_out_r10  ;  // <1,0>
  reg                           error_tap_update_out_r2  ;  // <1,0>
  reg                           error_tap_update_out_r3  ;  // <1,0>
  reg                           error_tap_update_out_r4  ;  // <1,0>
  reg                           error_tap_update_out_r5  ;  // <1,0>
  reg                           error_tap_update_out_r6  ;  // <1,0>
  reg                           error_tap_update_out_r7  ;  // <1,0>
  reg                           error_tap_update_out_r8  ;  // <1,0>
  reg                           error_tap_update_out_r9  ;  // <1,0>
  reg                   [4:0]   rd_address_wire_r1  ;  // <5,0>
  reg                   [4:0]   rd_address_wire_r2  ;  // <5,0>
  reg                   [4:0]   rd_address_wire_r3  ;  // <5,0>
  reg                   [4:0]   rd_address_wire_r4  ;  // <5,0>
  reg                   [4:0]   rd_address_wire_r5  ;  // <5,0>
  reg                           wr_address_vld_r1  ;  // <1,0>
  reg                           wr_address_vld_r2  ;  // <1,0>
  reg                           wr_address_vld_r3  ;  // <1,0>
  reg                           wr_address_vld_r4  ;  // <1,0>
  reg                           wr_address_vld_r5  ;  // <1,0>
  reg                           wr_address_vld_r6  ;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    active_start_d_r1 <= 'd0;
    active_start_d_r2 <= 'd0;
  end
  else begin
    active_start_d_r1 <= active_start_d;
    active_start_d_r2 <= active_start_d_r1;
  end
end
always @(posedge clk) begin
  if (reset) begin
    errorUpdateRemove_r1 <= 'd0;
    errorUpdateRemove_r2 <= 'd0;
    errorUpdateRemove_r3 <= 'd0;
    errorUpdateRemove_r4 <= 'd0;
    errorUpdateRemove_r5 <= 'd0;
    errorUpdateRemove_r6 <= 'd0;
    errorUpdateRemove_r7 <= 'd0;
    errorUpdateRemove_r8 <= 'd0;
    errorUpdateRemove_r9 <= 'd0;
    errorUpdateRemove_r10 <= 'd0;
    errorUpdateRemove_r11 <= 'd0;
    errorUpdateRemove_r12 <= 'd0;
    errorUpdateRemove_r13 <= 'd0;
    errorUpdateRemove_r14 <= 'd0;
    errorUpdateRemove_r15 <= 'd0;
    errorUpdateRemove_r16 <= 'd0;
  end
  else begin
    errorUpdateRemove_r1 <= errorUpdateRemove;
    errorUpdateRemove_r2 <= errorUpdateRemove_r1;
    errorUpdateRemove_r3 <= errorUpdateRemove_r2;
    errorUpdateRemove_r4 <= errorUpdateRemove_r3;
    errorUpdateRemove_r5 <= errorUpdateRemove_r4;
    errorUpdateRemove_r6 <= errorUpdateRemove_r5;
    errorUpdateRemove_r7 <= errorUpdateRemove_r6;
    errorUpdateRemove_r8 <= errorUpdateRemove_r7;
    errorUpdateRemove_r9 <= errorUpdateRemove_r8;
    errorUpdateRemove_r10 <= errorUpdateRemove_r9;
    errorUpdateRemove_r11 <= errorUpdateRemove_r10;
    errorUpdateRemove_r12 <= errorUpdateRemove_r11;
    errorUpdateRemove_r13 <= errorUpdateRemove_r12;
    errorUpdateRemove_r14 <= errorUpdateRemove_r13;
    errorUpdateRemove_r15 <= errorUpdateRemove_r14;
    errorUpdateRemove_r16 <= errorUpdateRemove_r15;
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_tap_update_out_r1 <= 'd0;
    error_tap_update_out_r2 <= 'd0;
    error_tap_update_out_r3 <= 'd0;
    error_tap_update_out_r4 <= 'd0;
    error_tap_update_out_r5 <= 'd0;
    error_tap_update_out_r6 <= 'd0;
    error_tap_update_out_r7 <= 'd0;
    error_tap_update_out_r8 <= 'd0;
    error_tap_update_out_r9 <= 'd0;
    error_tap_update_out_r10 <= 'd0;
  end
  else begin
    error_tap_update_out_r1 <= error_tap_update_out;
    error_tap_update_out_r2 <= error_tap_update_out_r1;
    error_tap_update_out_r3 <= error_tap_update_out_r2;
    error_tap_update_out_r4 <= error_tap_update_out_r3;
    error_tap_update_out_r5 <= error_tap_update_out_r4;
    error_tap_update_out_r6 <= error_tap_update_out_r5;
    error_tap_update_out_r7 <= error_tap_update_out_r6;
    error_tap_update_out_r8 <= error_tap_update_out_r7;
    error_tap_update_out_r9 <= error_tap_update_out_r8;
    error_tap_update_out_r10 <= error_tap_update_out_r9;
  end
end
always @(posedge clk) begin
  if (reset) begin
    rd_address_wire_r1 <= 5'd0;
    rd_address_wire_r2 <= 5'd0;
    rd_address_wire_r3 <= 5'd0;
    rd_address_wire_r4 <= 5'd0;
    rd_address_wire_r5 <= 5'd0;
  end
  else begin
    rd_address_wire_r1 <= rd_address_wire;
    rd_address_wire_r2 <= rd_address_wire_r1;
    rd_address_wire_r3 <= rd_address_wire_r2;
    rd_address_wire_r4 <= rd_address_wire_r3;
    rd_address_wire_r5 <= rd_address_wire_r4;
  end
end
always @(posedge clk) begin
  if (reset) begin
    wr_address_vld_r1 <= 'd0;
    wr_address_vld_r2 <= 'd0;
    wr_address_vld_r3 <= 'd0;
    wr_address_vld_r4 <= 'd0;
    wr_address_vld_r5 <= 'd0;
    wr_address_vld_r6 <= 'd0;
  end
  else begin
    wr_address_vld_r1 <= wr_address_vld;
    wr_address_vld_r2 <= wr_address_vld_r1;
    wr_address_vld_r3 <= wr_address_vld_r2;
    wr_address_vld_r4 <= wr_address_vld_r3;
    wr_address_vld_r5 <= wr_address_vld_r4;
    wr_address_vld_r6 <= wr_address_vld_r5;
  end
end

// Data Input Memory Control
assign data_int_wr_data = data_value;
assign data_int.wr_address = data_write_addr;
assign data_int.wr_vld = data_valid;

// Data Output Memory Control
assign data_int.rd_address = data_read_addr;
assign data_int.rd_vld = active_normal;

// Tap Output Memory Control
assign tap_int.rd_address = error_update_first ? 5'd12 + {0'd0,error_phase_read} : tap_address;
assign tap_int.rd_vld = active_normal;

// Tap Input Update Control
assign rd_address_wire = tap_int.rd_address;
assign wr_address_vld = (error_update_latch & ~error_update_first);

// Tap Input Memmory Control
assign tap_int.wr_address = wr_address_vld_r5 ? rd_address_wire_r5 : 5'd12 + {0'd0,error_phase};
assign error_tap_write = (~error_tap_update_out_r5 & (tap_enable & wr_address_vld_r5));
assign tap_int.wr_vld = (error_valid | error_tap_write);
assign tap_int.sub_vld = wr_address_vld_r5 ? 'd0 : error_valid;
assign tap_int.sub_addr = error_sub_address;
assign tap_int.sub_data = error_value;
assign tap_int.inter = error_tap_update_out;
assign tap_int.inter_first = error_update_first;
assign tap_int_wr_data = four_12_12_st3_st_tap_out;

// Output Driving Control
assign stage_error_back = error_tap_update_out;
assign first = active_start_d;
assign four_12_12_st3_st_data = data_int_rd_data;
assign four_12_12_st3_st_bias[31:0] = bias_int_rd_data;
assign taps.v0 = tap_int_rd_data[31:0];
assign taps.v1 = tap_int_rd_data[63:32];
assign taps.v2 = tap_int_rd_data[95:64];
assign taps.v3 = tap_int_rd_data[127:96];
assign taps.v4 = tap_int_rd_data[159:128];
assign taps.v5 = tap_int_rd_data[191:160];
assign taps.v6 = tap_int_rd_data[223:192];
assign taps.v7 = tap_int_rd_data[255:224];
assign taps.v8 = tap_int_rd_data[287:256];
assign taps.v9 = tap_int_rd_data[319:288];
assign taps.v10 = tap_int_rd_data[351:320];
assign taps.v11 = tap_int_rd_data[383:352];

// Final Output Control
assign stage_3_data_out = four_12_12_st3_st_data_out;
assign stage_3_data_out_vld = active;
assign stage_3_data_out_pre = four_12_12_st3_st_data_out_pre;
assign stage_3_data_out_pre_vld = active_pre;

// Bias Output Memory Control
assign bias_int.rd_address = tap_address;
assign bias_int.rd_vld = tap_int.rd_vld;
assign bias_int.wr_address = bias_wr_address;
assign bias_int.wr_vld = (bias_enable & wr_address_vld_r4);
assign bias_int_wr_data = four_12_12_st3_st_data_out_bias;
assign errorUpdateRemove = (error_tap_update_out & ~error_update_first);

// Error Output Control
assign zerror_int = four_12_12_st3_st_data_out_pre;
assign zerror_int_vld = errorUpdateRemove_r16;
assign update_error_first = err_finish_new;
endmodule

