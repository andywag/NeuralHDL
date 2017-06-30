//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       full_st0_ctrl_error_fifo
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module full_st0_ctrl_error_fifo(
  input                         clk,
  input                 [3:0]   error_tap_length,
  input                         input_stage,
  input                 [2:0]   load_length,
  input                         read_finish,
  input                         reset,
  input float_24_8              stage_0_error,
  input                         stage_0_error_fst,
  input                         stage_0_error_vld,
  input                         state_finish,
  output                [3:0]   error_count,
  output                        error_finish_tap,
  output                [1:0]   error_phase,
  output                [1:0]   error_phase_read,
  output                [31:0]  error_sub_address,
  output                        error_tap_update_out,
  output                        error_update_first,
  output                        error_update_latch,
  output                        error_update_mode,
  output                        error_valid,
  output                [31:0]  error_value,
  output                        stage_0_error_rdy,
  output                        stage_error_first,
  output                        stage_error_mode);

// Parameters 



// Wires 

  wire                          error_fifo_full   ;  // <1,0>
  wire                          error_finish      ;  // <1,0>
  wire                          error_tap_update  ;  // <1,0>
  wire                          error_update_first_internal;  // <1,0>
  wire                          error_update_last_internal;  // <1,0>
  wire                          real_error_finish ;  // <1,0>
  wire                          wr_address_vld    ;  // <1,0>


// Registers 

  reg                   [2:0]   error_fifo_depth  ;  // <3,0>
  reg                           error_update_first_internal_r1;  // <1,0>
  reg                           error_update_first_internal_r2;  // <1,0>
  reg                           error_update_first_internal_r3;  // <1,0>
  reg                           error_update_first_internal_r4;  // <1,0>
  reg                           error_update_last_internal_r1;  // <1,0>
  reg                           error_update_last_internal_r2;  // <1,0>
  reg                           error_update_last_internal_r3;  // <1,0>
  reg                           error_update_last_internal_r4;  // <1,0>
  reg                           error_update_latch;  // <1,0>
  reg                           error_update_latch_r1;  // <1,0>
  reg                           error_update_latch_r2;  // <1,0>
  reg                           error_update_latch_r3;  // <1,0>
  reg                           error_update_latch_r4;  // <1,0>
  reg                           error_update_latch_r5;  // <1,0>
  reg                           error_update_latch_r6;  // <1,0>
  reg                   [3:0]   error_write_count ;  // <4,0>
  reg                           wr_address_vld_r1 ;  // <1,0>
  reg                           wr_address_vld_r2 ;  // <1,0>
  reg                           wr_address_vld_r3 ;  // <1,0>
  reg                           wr_address_vld_r4 ;  // <1,0>
  reg                           wr_address_vld_r5 ;  // <1,0>
  reg                           wr_address_vld_r6 ;  // <1,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    error_update_first_internal_r1 <= 'd0;
    error_update_first_internal_r2 <= 'd0;
    error_update_first_internal_r3 <= 'd0;
    error_update_first_internal_r4 <= 'd0;
  end
  else begin
    error_update_first_internal_r1 <= error_update_first_internal;
    error_update_first_internal_r2 <= error_update_first_internal_r1;
    error_update_first_internal_r3 <= error_update_first_internal_r2;
    error_update_first_internal_r4 <= error_update_first_internal_r3;
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_last_internal_r1 <= 'd0;
    error_update_last_internal_r2 <= 'd0;
    error_update_last_internal_r3 <= 'd0;
    error_update_last_internal_r4 <= 'd0;
  end
  else begin
    error_update_last_internal_r1 <= error_update_last_internal;
    error_update_last_internal_r2 <= error_update_last_internal_r1;
    error_update_last_internal_r3 <= error_update_last_internal_r2;
    error_update_last_internal_r4 <= error_update_last_internal_r3;
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_latch_r1 <= 'd0;
    error_update_latch_r2 <= 'd0;
    error_update_latch_r3 <= 'd0;
    error_update_latch_r4 <= 'd0;
    error_update_latch_r5 <= 'd0;
    error_update_latch_r6 <= 'd0;
  end
  else begin
    error_update_latch_r1 <= error_update_latch;
    error_update_latch_r2 <= error_update_latch_r1;
    error_update_latch_r3 <= error_update_latch_r2;
    error_update_latch_r4 <= error_update_latch_r3;
    error_update_latch_r5 <= error_update_latch_r4;
    error_update_latch_r6 <= error_update_latch_r5;
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

// Tap Input Memmory Control

// Create the Tap Update Control which gates the error feedback
assign wr_address_vld = (error_update_latch & ~error_update_first);
assign stage_0_error_rdy = ((~wr_address_vld_r4 & ~error_fifo_full) & ~error_update_latch_r6);

// Finish Conditions
assign error_finish = ((error_count == error_tap_length) & (stage_0_error_rdy & stage_0_error_vld));
assign error_finish_tap = ((state_finish & error_update_latch) & error_tap_update);

// Condition to Update Error Mode
always @(posedge clk) begin
  if (reset) begin
    error_tap_update <= 'd0;
  end
  else begin
    if (input_stage) begin
      error_tap_update <= 'd1;
    end
    else if (error_update_last_internal) begin 
      error_tap_update <= ~error_tap_update;
    end
  end
end
assign error_tap_update_out = (error_tap_update & ~input_stage);
assign real_error_finish = (error_finish & error_tap_update);

// Input Control and Tap Addressiong
always @(posedge clk) begin
  if (reset) begin
    error_count <= 4'd0;
  end
  else if ((stage_0_error_rdy & stage_0_error_vld)) begin 
    if (error_finish) begin
      error_count <= 4'd0;
    end
    else begin
      error_count <= error_count[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_write_count <= 4'd0;
  end
  else if ((stage_0_error_rdy & stage_0_error_vld)) begin 
    if ((error_write_count == 'd5)) begin
      error_write_count <= 4'd0;
    end
    else begin
      error_write_count <= error_write_count[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_phase <= 2'd0;
  end
  else if (((error_write_count == 'd5) & (stage_0_error_rdy & stage_0_error_vld))) begin 
    if ((error_phase == 'd3)) begin
      error_phase <= 2'd0;
    end
    else begin
      error_phase <= error_phase[1:0] + 2'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_sub_address <= 32'd0;
  end
  else if ((stage_0_error_rdy & stage_0_error_vld)) begin 
    if ((error_sub_address == 'd5)) begin
      error_sub_address <= 32'd0;
    end
    else begin
      error_sub_address <= error_sub_address[31:0] + 32'd1;
    end
  end
end

// Error Input Operations
always @(posedge clk) begin
  if (reset) begin
    error_fifo_depth <= 3'd0;
  end
  else begin
    if (((error_update_last_internal & error_tap_update) & error_finish)) begin
      error_fifo_depth <= error_fifo_depth;
    end
    else if (error_finish) begin 
      error_fifo_depth <= error_fifo_depth[2:0] + 3'd1;
    end
    else if ((error_update_last_internal & error_tap_update)) begin 
      error_fifo_depth <= error_fifo_depth[2:0] - 3'd1;
    end
  end
end
assign error_fifo_full = (error_fifo_depth == 'd2);

// Error Control Signals
assign error_update_mode = (error_fifo_depth > 'd0);
always @(posedge clk) begin
  if (reset) begin
    error_phase_read <= 2'd0;
  end
  else if ((error_update_first_internal & error_tap_update)) begin 
    if ((error_phase_read == 'd3)) begin
      error_phase_read <= 2'd0;
    end
    else begin
      error_phase_read <= error_phase_read[1:0] + 2'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_latch <= 'd0;
  end
  else if (state_finish) begin 
    error_update_latch <= error_update_mode;
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_first_internal <= 'd0;
  end
  else begin
    error_update_first_internal <= state_finish ? (error_update_mode & read_finish) : (error_update_latch & read_finish);
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_last_internal <= 'd0;
  end
  else begin
    error_update_last_internal <= ((error_fifo_depth > 'd0) & state_finish);
  end
end
assign error_update_first = (error_update_first_internal & error_update_latch);
assign error_value = stage_0_error;
assign error_valid = (stage_0_error_vld & stage_0_error_rdy);

// Error Mode Driving Controls
assign stage_error_mode = error_update_latch_r2;
assign stage_error_first = (error_update_first_internal_r2 & error_update_latch_r2);
endmodule

