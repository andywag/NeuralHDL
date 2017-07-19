//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st2_ctrl_data_fifo
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st2_ctrl_data_fifo(
  input                         clk,
  input                         error_finish_tap,
  input                         error_tap_update_out,
  input                         error_update_first,
  input                         error_update_latch,
  input                         error_update_mode,
  input                 [5:0]   load_depth,
  input                 [3:0]   load_length,
  input                         reset,
  input float_24_8              stage_2_data,
  input                         stage_2_data_fst,
  input                         stage_2_data_vld,
  input                         state_length,
  output                        active,
  output                        active_normal,
  output                        active_pre,
  output                        active_start_d,
  output                [4:0]   bias_address,
  output                [4:0]   bias_wr_address,
  output                [9:0]   data_read_addr,
  output                        data_valid,
  output                [31:0]  data_value,
  output                [9:0]   data_write_addr,
  output                        err_finish_i,
  output                        err_finish_new,
  output                        four_12_12_st2_ctrl_data_fifo_data_ready,
  output                        load_finish,
  output                        read_finish,
  output                        stage_2_data_rdy,
  output                        state_finish,
  output                [4:0]   tap_address);

// Parameters 



// Wires 

  wire                          data_active       ;  // <1,0>
  wire                          data_start        ;  // <1,0>
  wire                          fifo_empty        ;  // <1,0>
  wire                          gate_valid        ;  // <1,0>
  wire                          gate_valid_d      ;  // <1,0>
  wire                          gate_valid_e      ;  // <1,0>
  wire                          load_input_done   ;  // <1,0>
  wire                          output_valid      ;  // <1,0>
  wire                          temp              ;  // <1,0>
  wire                          update_counter    ;  // <1,0>


// Registers 

  reg                           data_active_r1    ;  // <1,0>
  reg                           data_active_r10   ;  // <1,0>
  reg                           data_active_r11   ;  // <1,0>
  reg                           data_active_r12   ;  // <1,0>
  reg                           data_active_r13   ;  // <1,0>
  reg                           data_active_r14   ;  // <1,0>
  reg                           data_active_r15   ;  // <1,0>
  reg                           data_active_r16   ;  // <1,0>
  reg                           data_active_r17   ;  // <1,0>
  reg                           data_active_r18   ;  // <1,0>
  reg                           data_active_r2    ;  // <1,0>
  reg                           data_active_r3    ;  // <1,0>
  reg                           data_active_r4    ;  // <1,0>
  reg                           data_active_r5    ;  // <1,0>
  reg                           data_active_r6    ;  // <1,0>
  reg                           data_active_r7    ;  // <1,0>
  reg                           data_active_r8    ;  // <1,0>
  reg                           data_active_r9    ;  // <1,0>
  reg                           data_start_r1     ;  // <1,0>
  reg                           data_start_r2     ;  // <1,0>
  reg                           data_start_r3     ;  // <1,0>
  reg                   [4:0]   dtap_address      ;  // <5,0>
  reg                           err_finish        ;  // <1,0>
  reg                           err_finish_r1     ;  // <1,0>
  reg                           err_finish_r2     ;  // <1,0>
  reg                           err_finish_r3     ;  // <1,0>
  reg                           err_finish_r4     ;  // <1,0>
  reg                   [3:0]   error_update_count  ;  // <4,0>
  reg                           error_update_mode_r1  ;  // <1,0>
  reg                           error_update_mode_r2  ;  // <1,0>
  reg                           error_update_mode_r3  ;  // <1,0>
  reg                           fifo_empty_reg    ;  // <1,0>
  reg                   [5:0]   fifo_input_depth  ;  // <6,0>
  reg                   [5:0]   load_depth_count  ;  // <6,0>
  reg                   [3:0]   load_width_count  ;  // <4,0>
  reg                           output_valid_r1   ;  // <1,0>
  reg                           output_valid_r10  ;  // <1,0>
  reg                           output_valid_r11  ;  // <1,0>
  reg                           output_valid_r12  ;  // <1,0>
  reg                           output_valid_r13  ;  // <1,0>
  reg                           output_valid_r14  ;  // <1,0>
  reg                           output_valid_r15  ;  // <1,0>
  reg                           output_valid_r16  ;  // <1,0>
  reg                           output_valid_r17  ;  // <1,0>
  reg                           output_valid_r18  ;  // <1,0>
  reg                           output_valid_r2   ;  // <1,0>
  reg                           output_valid_r3   ;  // <1,0>
  reg                           output_valid_r4   ;  // <1,0>
  reg                           output_valid_r5   ;  // <1,0>
  reg                           output_valid_r6   ;  // <1,0>
  reg                           output_valid_r7   ;  // <1,0>
  reg                           output_valid_r8   ;  // <1,0>
  reg                           output_valid_r9   ;  // <1,0>
  reg                   [5:0]   read_depth_count  ;  // <6,0>
  reg                   [5:0]   read_error_count  ;  // <6,0>
  reg                           read_state_count  ;  // <1,0>
  reg                   [3:0]   read_width_count  ;  // <4,0>
  reg                   [4:0]   tap_address_r1    ;  // <5,0>
  reg                   [4:0]   tap_address_r2    ;  // <5,0>
  reg                   [4:0]   tap_address_r3    ;  // <5,0>
  reg                   [4:0]   tap_address_r4    ;  // <5,0>
  reg                   [4:0]   tap_address_r5    ;  // <5,0>


// Other



always @(posedge clk) begin
  if (reset) begin
    data_active_r1 <= 'd0;
    data_active_r2 <= 'd0;
    data_active_r3 <= 'd0;
    data_active_r4 <= 'd0;
    data_active_r5 <= 'd0;
    data_active_r6 <= 'd0;
    data_active_r7 <= 'd0;
    data_active_r8 <= 'd0;
    data_active_r9 <= 'd0;
    data_active_r10 <= 'd0;
    data_active_r11 <= 'd0;
    data_active_r12 <= 'd0;
    data_active_r13 <= 'd0;
    data_active_r14 <= 'd0;
    data_active_r15 <= 'd0;
    data_active_r16 <= 'd0;
    data_active_r17 <= 'd0;
    data_active_r18 <= 'd0;
  end
  else begin
    data_active_r1 <= data_active;
    data_active_r2 <= data_active_r1;
    data_active_r3 <= data_active_r2;
    data_active_r4 <= data_active_r3;
    data_active_r5 <= data_active_r4;
    data_active_r6 <= data_active_r5;
    data_active_r7 <= data_active_r6;
    data_active_r8 <= data_active_r7;
    data_active_r9 <= data_active_r8;
    data_active_r10 <= data_active_r9;
    data_active_r11 <= data_active_r10;
    data_active_r12 <= data_active_r11;
    data_active_r13 <= data_active_r12;
    data_active_r14 <= data_active_r13;
    data_active_r15 <= data_active_r14;
    data_active_r16 <= data_active_r15;
    data_active_r17 <= data_active_r16;
    data_active_r18 <= data_active_r17;
  end
end
always @(posedge clk) begin
  if (reset) begin
    data_start_r1 <= 'd0;
    data_start_r2 <= 'd0;
    data_start_r3 <= 'd0;
  end
  else begin
    data_start_r1 <= data_start;
    data_start_r2 <= data_start_r1;
    data_start_r3 <= data_start_r2;
  end
end
always @(posedge clk) begin
  if (reset) begin
    err_finish_r1 <= 'd0;
    err_finish_r2 <= 'd0;
    err_finish_r3 <= 'd0;
    err_finish_r4 <= 'd0;
  end
  else begin
    err_finish_r1 <= err_finish;
    err_finish_r2 <= err_finish_r1;
    err_finish_r3 <= err_finish_r2;
    err_finish_r4 <= err_finish_r3;
  end
end
always @(posedge clk) begin
  if (reset) begin
    error_update_mode_r1 <= 'd0;
    error_update_mode_r2 <= 'd0;
    error_update_mode_r3 <= 'd0;
  end
  else begin
    error_update_mode_r1 <= error_update_mode;
    error_update_mode_r2 <= error_update_mode_r1;
    error_update_mode_r3 <= error_update_mode_r2;
  end
end
always @(posedge clk) begin
  if (reset) begin
    output_valid_r1 <= 'd0;
    output_valid_r2 <= 'd0;
    output_valid_r3 <= 'd0;
    output_valid_r4 <= 'd0;
    output_valid_r5 <= 'd0;
    output_valid_r6 <= 'd0;
    output_valid_r7 <= 'd0;
    output_valid_r8 <= 'd0;
    output_valid_r9 <= 'd0;
    output_valid_r10 <= 'd0;
    output_valid_r11 <= 'd0;
    output_valid_r12 <= 'd0;
    output_valid_r13 <= 'd0;
    output_valid_r14 <= 'd0;
    output_valid_r15 <= 'd0;
    output_valid_r16 <= 'd0;
    output_valid_r17 <= 'd0;
    output_valid_r18 <= 'd0;
  end
  else begin
    output_valid_r1 <= output_valid;
    output_valid_r2 <= output_valid_r1;
    output_valid_r3 <= output_valid_r2;
    output_valid_r4 <= output_valid_r3;
    output_valid_r5 <= output_valid_r4;
    output_valid_r6 <= output_valid_r5;
    output_valid_r7 <= output_valid_r6;
    output_valid_r8 <= output_valid_r7;
    output_valid_r9 <= output_valid_r8;
    output_valid_r10 <= output_valid_r9;
    output_valid_r11 <= output_valid_r10;
    output_valid_r12 <= output_valid_r11;
    output_valid_r13 <= output_valid_r12;
    output_valid_r14 <= output_valid_r13;
    output_valid_r15 <= output_valid_r14;
    output_valid_r16 <= output_valid_r15;
    output_valid_r17 <= output_valid_r16;
    output_valid_r18 <= output_valid_r17;
  end
end
always @(posedge clk) begin
  if (reset) begin
    tap_address_r1 <= 5'd0;
    tap_address_r2 <= 5'd0;
    tap_address_r3 <= 5'd0;
    tap_address_r4 <= 5'd0;
    tap_address_r5 <= 5'd0;
  end
  else begin
    tap_address_r1 <= tap_address;
    tap_address_r2 <= tap_address_r1;
    tap_address_r3 <= tap_address_r2;
    tap_address_r4 <= tap_address_r3;
    tap_address_r5 <= tap_address_r4;
  end
end

// Ready Valid Input Interface
assign stage_2_data_rdy = four_12_12_st2_ctrl_data_fifo_data_ready;

// Data Input Burst Counter
assign load_input_done = (load_width_count == load_length);
always @(posedge clk) begin
  if (reset) begin
    load_width_count <= 4'd0;
  end
  else begin
    if (load_input_done) begin
      load_width_count <= 4'd0;
    end
    else if ((stage_2_data_rdy & stage_2_data_vld)) begin 
      load_width_count <= load_width_count[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    load_depth_count <= 6'd0;
  end
  else if (load_input_done) begin 
    if ((load_depth_count == load_depth)) begin
      load_depth_count <= 6'd0;
    end
    else begin
      load_depth_count <= load_depth_count[5:0] + 6'd1;
    end
  end
end

// Control signals for the Read/Write Fifo Operation

// Fifo Controls - Used to Gate Inputs
assign fifo_empty = (read_depth_count == load_depth_count);
always @(posedge clk) begin
  if (reset) begin
    fifo_empty_reg <= 'd0;
  end
  else if (state_finish) begin 
    fifo_empty_reg <= fifo_empty;
  end
end
always @(posedge clk) begin
  if (reset) begin
    fifo_input_depth <= 6'd0;
  end
  else begin
    if ((load_input_done & error_finish_tap)) begin
      fifo_input_depth <= fifo_input_depth;
    end
    else if (load_input_done) begin 
      fifo_input_depth <= fifo_input_depth[5:0] + 6'd1;
    end
    else if (error_finish_tap) begin 
      fifo_input_depth <= fifo_input_depth[5:0] - 6'd1;
    end
  end
end

// Internal Counter for which state the operation is in
assign read_finish = (read_width_count == load_length);
assign state_finish = read_finish;
assign data_start = ((read_finish | ((((load_input_done & ~error_update_mode) & ~error_update_latch) & (read_width_count == 'd0)) & (fifo_input_depth <= 'd0))) & (fifo_input_depth >= 'd0));
assign data_active = ((((fifo_input_depth > 'd0) & ~(fifo_empty_reg | fifo_empty)) | error_update_mode) | error_update_latch);
assign temp = ((fifo_input_depth > 'd0) & ~(fifo_empty_reg | fifo_empty));
assign output_valid = (~error_update_latch & temp);
assign gate_valid_d = (~error_update_latch & (read_width_count >= load_length - 'd12 + 'd1));
assign gate_valid_e = ((error_update_latch & (read_width_count >= load_length - 'd12)) & (read_width_count < load_length));
assign gate_valid = 'd1;
assign update_counter = ((data_active | data_active_r12) & (~error_update_first | (~error_update_latch & temp)));
always @(posedge clk) begin
  if (reset) begin
    read_width_count <= 4'd0;
  end
  else begin
    if ((data_start | read_finish)) begin
      read_width_count <= 4'd0;
    end
    else if (update_counter) begin 
      read_width_count <= read_width_count[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    read_state_count <= 'd0;
  end
  else begin
    if (read_finish) begin
      read_state_count <= read_state_count + 'd1;
    end
  end
end
always @* err_finish <= (error_update_count == 'd11);
assign err_finish_new = ((error_update_count == 'd2) & error_tap_update_out);
always @(posedge clk) begin
  if (reset) begin
    error_update_count <= 4'd0;
  end
  else begin
    if ((data_start | err_finish)) begin
      error_update_count <= 4'd0;
    end
    else if (update_counter) begin 
      error_update_count <= error_update_count[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    read_depth_count <= 6'd0;
  end
  else if ((state_finish & output_valid)) begin 
    if ((read_depth_count == load_depth)) begin
      read_depth_count <= 6'd0;
    end
    else begin
      read_depth_count <= read_depth_count[5:0] + 6'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    read_error_count <= 6'd0;
  end
  else if (((state_finish & ~error_tap_update_out) & error_update_latch)) begin 
    if ((read_error_count == load_depth)) begin
      read_error_count <= 6'd0;
    end
    else begin
      read_error_count <= read_error_count[5:0] + 6'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    tap_address <= 5'd0;
  end
  else begin
    if (state_finish) begin
      tap_address <= 5'd0;
    end
    else if (update_counter) begin 
      tap_address <= tap_address[4:0] + 5'd1;
    end
  end
end
assign bias_address = error_update_latch ? tap_address : tap_address_r1;
assign bias_wr_address = tap_address_r4;

// Data Memory Interface and Input Control
assign active_start = data_start;
assign active_start_d = data_start_r3;
assign active_pre = output_valid_r16;
assign active = (output_valid_r18 & gate_valid);
assign active_normal = (data_active | data_active_r12);
assign data_write_addr = {load_depth_count,load_width_count};
assign data_read_addr = (error_update_latch & ~output_valid) ? {read_error_count,read_width_count} : {read_depth_count,read_width_count};
assign load_finish = read_finish;
assign four_12_12_st2_ctrl_data_fifo_data_ready = (fifo_input_depth != load_depth);

// Data Memory Interface
assign data_valid = (stage_2_data_rdy & stage_2_data_vld);
assign data_value = stage_2_data;
assign err_finish_i = (err_finish_r4 & error_tap_update_out);
endmodule

