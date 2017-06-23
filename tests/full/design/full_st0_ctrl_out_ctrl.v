//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_st0_ctrl_out_ctrl
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_st0_ctrl_out_ctrl(
  input                         active,
  input                         active_normal,
  input                         active_pre,
  input                         active_start_d,
  input                 [3:0]   bias_address,
  input                 [31:0]  bias_int_rd_data,
  input                 [3:0]   bias_wr_address,
  input                         clk,
  input                 [31:0]  data_int_rd_data,
  input                 [5:0]   data_read_addr,
  input                         data_valid,
  input                 [31:0]  data_value,
  input                 [5:0]   data_write_addr,
  input                 [3:0]   error_count,
  input                 [1:0]   error_phase,
  input                 [1:0]   error_phase_read,
  input                 [31:0]  error_sub_address,
  input                 [3:0]   error_tap_length,
  input                         error_update_first,
  input                         error_update_latch,
  input                         error_valid,
  input                 [31:0]  error_value,
  input float_24_8              full_st0_st_data_out,
  input float_24_8              full_st0_st_data_out_bias,
  input float_24_8              full_st0_st_data_out_pre,
  input                 [191:0] full_st0_st_tap_out,
  input                         read_finish,
  input                         reset,
  input                         stage_0_data_out_pre_rdy,
  input                         stage_0_data_out_rdy,
  input                 [3:0]   tap_address,
  input                 [191:0] tap_int_rd_data,
  output bias_int_32_4          bias_int,
  output                [31:0]  bias_int_wr_data,
  output data_int_32_6          data_int,
  output                [31:0]  data_int_wr_data,
  output                        first,
  output float_24_8             full_st0_st_bias,
  output float_24_8             full_st0_st_data,
  output float_24_8             stage_0_data_out,
  output                        stage_0_data_out_fst,
  output float_24_8             stage_0_data_out_pre,
  output                        stage_0_data_out_pre_fst,
  output                        stage_0_data_out_pre_vld,
  output                        stage_0_data_out_vld,
  output                        stage_error_first,
  output                        stage_error_mode,
  output tap_int_192_4          tap_int,
  output                [191:0] tap_int_wr_data,
  output taps_typ_6             taps);

// Parameters 



// Wires 

  wire                          enable_bias_feedback;  // <1,0>
  wire                          enable_feedback   ;  // <1,0>
  wire                  [3:0]   rd_address_wire   ;  // <4,0>
  wire                          wr_address_vld    ;  // <1,0>


// Registers 

  reg                   [3:0]   rd_address_wire_r1;  // <4,0>
  reg                   [3:0]   rd_address_wire_r2;  // <4,0>
  reg                   [3:0]   rd_address_wire_r3;  // <4,0>
  reg                   [3:0]   rd_address_wire_r4;  // <4,0>
  reg                   [3:0]   rd_address_wire_r5;  // <4,0>
  reg                           wr_address_vld_r1 ;  // <1,0>
  reg                           wr_address_vld_r2 ;  // <1,0>
  reg                           wr_address_vld_r3 ;  // <1,0>
  reg                           wr_address_vld_r4 ;  // <1,0>
  reg                           wr_address_vld_r5 ;  // <1,0>
  reg                           wr_address_vld_r6 ;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    rd_address_wire_r1 <= 4'd0;
    rd_address_wire_r2 <= 4'd0;
    rd_address_wire_r3 <= 4'd0;
    rd_address_wire_r4 <= 4'd0;
    rd_address_wire_r5 <= 4'd0;
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
assign enable_feedback = 'd1;
assign enable_bias_feedback = 'd1;

// Data Input Memory Control
assign data_int_wr_data = data_value;
assign data_int.wr_address = data_write_addr;
assign data_int.wr_vld = data_valid;

// Data Output Memory Control
assign data_int.rd_address = data_read_addr;
assign data_int.rd_vld = active_normal;

// Tap Output Memory Control
assign tap_int.rd_address = error_update_first ? 4'd12 + {1'd0,error_phase_read} : tap_address;
assign tap_int.rd_vld = active_normal;

// Tap Input Update Control
assign rd_address_wire = tap_int.rd_address;
assign wr_address_vld = (error_update_latch & ~error_update_first);

// Tap Input Memmory Control
assign tap_int.wr_address = wr_address_vld_r5 ? rd_address_wire_r5 : 4'd12 + {1'd0,error_phase};
assign tap_int.wr_vld = (error_valid | (enable_feedback & wr_address_vld_r5));
assign tap_int.sub_vld = wr_address_vld_r5 ? 'd0 : error_valid;
assign tap_int.sub_addr = error_sub_address;
assign tap_int.sub_data = error_value;
assign tap_int_wr_data = full_st0_st_tap_out;

// Output Driving Control
assign first = active_start_d;
assign full_st0_st_data = data_int_rd_data;
assign full_st0_st_bias[31:0] = bias_int_rd_data;
assign taps.v0 = tap_int_rd_data[31:0];
assign taps.v1 = tap_int_rd_data[63:32];
assign taps.v2 = tap_int_rd_data[95:64];
assign taps.v3 = tap_int_rd_data[127:96];
assign taps.v4 = tap_int_rd_data[159:128];
assign taps.v5 = tap_int_rd_data[191:160];

// Final Output Control
assign stage_0_data_out = full_st0_st_data_out;
assign stage_0_data_out_vld = active;
assign stage_0_data_out_pre = full_st0_st_data_out_pre;
assign stage_0_data_out_pre_vld = active_pre;

// Bias Output Memory Control
assign bias_int.rd_address = tap_address;
assign bias_int.rd_vld = tap_int.rd_vld;
assign bias_int.wr_address = bias_wr_address;
assign bias_int.wr_vld = (enable_bias_feedback & wr_address_vld_r4);
assign bias_int_wr_data = full_st0_st_data_out_bias;
endmodule

