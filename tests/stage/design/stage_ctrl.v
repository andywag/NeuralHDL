//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage_ctrl
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage_ctrl(
  input                 [31:0]  bias_int_rd_data,
  input                 [2:0]   bias_length,
  input                         clk,
  input                 [31:0]  data_int_rd_data,
  input                 [3:0]   load_depth,
  input                 [2:0]   load_length,
  input                         reset,
  input float_24_8              st_data,
  input                         st_data_fst,
  input                         st_data_out_pre_rdy,
  input                         st_data_out_rdy,
  input                         st_data_vld,
  input float_24_8              st_error,
  input                         st_error_fst,
  input                         st_error_vld,
  input float_24_8              stage_st_data_out,
  input float_24_8              stage_st_data_out_pre,
  input                         state_length,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  input                 [191:0] tap_int_rd_data,
  output                [2:0]   bias_address,
  output bias_int_32_3          bias_int,
  output                [31:0]  bias_int_wr_data,
  output data_int_32_6          data_int,
  output                [31:0]  data_int_wr_data,
  output                        first,
  output float_24_8             st_data_out,
  output                        st_data_out_fst,
  output float_24_8             st_data_out_pre,
  output                        st_data_out_pre_fst,
  output                        st_data_out_pre_vld,
  output                        st_data_out_vld,
  output                        st_data_rdy,
  output                        st_error_rdy,
  output float_24_8             stage_st_bias,
  output float_24_8             stage_st_data,
  output                [3:0]   tap_address,
  output                        tap_in_rdy,
  output tap_int_192_4          tap_int,
  output                [191:0] tap_int_wr_data,
  output taps_typ_6             taps);

// Parameters 



// Wires 

  wire                          bias_enable       ;  // <1,0>
  wire                          bias_start        ;  // <1,0>
  wire                          data_active       ;  // <1,0>
  wire                          data_start        ;  // <1,0>
  wire                          fifo_empty        ;  // <1,0>
  wire                          load_finish       ;  // <1,0>
  wire                          load_input_done   ;  // <1,0>
  wire                  [2:0]   read_data_address ;  // <3,0>
  wire                          state_address     ;  // <1,0>
  wire                          state_done        ;  // <1,0>


// Registers 

  reg                           data_active_r1    ;  // <1,0>
  reg                           data_active_r10   ;  // <1,0>
  reg                           data_active_r11   ;  // <1,0>
  reg                           data_active_r12   ;  // <1,0>
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
  reg                   [11:0]  errorCount        ;  // <12,0>
  reg                           fifo_empty_reg    ;  // <1,0>
  reg                   [3:0]   fifo_input_depth  ;  // <4,0>
  reg                   [3:0]   load_data_write   ;  // <4,0>
  reg                   [2:0]   load_input_count  ;  // <3,0>
  reg                   [3:0]   read_data_depth   ;  // <4,0>


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

// Fifo Controls - Used to Gate Inputs
assign fifo_empty = (read_data_depth == load_data_write);
always @(posedge clk) begin
  if (reset) begin
    fifo_empty_reg <= 'd0;
  end
  else if (state_done) begin 
    fifo_empty_reg <= fifo_empty;
  end
end
always @(posedge clk) begin
  if (reset) begin
    fifo_input_depth <= 4'd0;
  end
  else begin
    if ((load_input_done & 'd0)) begin
      fifo_input_depth <= fifo_input_depth;
    end
    else if (load_input_done) begin 
      fifo_input_depth <= fifo_input_depth[3:0] + 4'd1;
    end
    else if ('d0) begin 
      fifo_input_depth <= fifo_input_depth[3:0] - 4'd1;
    end
  end
end

// Control for Input Data
assign load_input_done = (load_input_count == load_length);

// Data Input Burst Counter
always @(posedge clk) begin
  if (reset) begin
    load_input_count <= 3'd0;
  end
  else begin
    if (load_input_done) begin
      load_input_count <= 3'd0;
    end
    else if ((st_data_rdy & st_data_vld)) begin 
      load_input_count <= load_input_count[2:0] + 3'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    load_data_write <= 4'd0;
  end
  else begin
    if ((load_input_count == load_length)) begin
      load_data_write <= load_data_write[3:0] + 4'd1;
    end
  end
end

// Data Input Memory Control
assign st_data_rdy = (fifo_input_depth <= load_depth);
assign data_int_wr_data = st_data;
assign data_int.wr_address = {load_data_write,load_input_count};
assign data_int.wr_vld = (st_data_rdy & st_data_vld);

// Data Output Memory Control
assign data_start = ((load_finish | load_input_done) & (fifo_input_depth >= 'd0));
assign data_active = ((fifo_input_depth > 'd0) & ~(fifo_empty_reg | fifo_empty));
always @(posedge clk) begin
  if (reset) begin
    read_data_depth <= 4'd0;
  end
  else begin
    if (state_done) begin
      read_data_depth <= read_data_depth[3:0] + 4'd1;
    end
  end
end
assign data_int.rd_address = {read_data_depth,read_data_address};
assign data_int.rd_vld = (data_active | data_active_r6);

// Tap Input Memmory Control
always @(posedge clk) begin
  if (reset) begin
    errorCount <= 12'd0;
  end
  else if (st_error_vld) begin 
    errorCount <= errorCount[11:0] + 12'd1;
  end
end
assign tap_int.wr_address = 4'd12;
assign tap_int.wr_vld = st_error_vld;
assign tap_int.sub_vld = st_error_vld;
assign tap_int.sub_addr = errorCount;
assign tap_int.sub_data = st_error;

// Tap Output Memory Control
assign tap_int.rd_address = tap_address;
assign tap_int.rd_vld = (data_active | data_active_r6);

// Bias Output Memory Control
assign bias_int.rd_address = bias_address;
assign bias_int.rd_vld = bias_enable;

// Output Driving Control
assign first = data_start_r3;
assign stage_st_data = data_int_rd_data;
assign stage_st_bias = bias_int_rd_data;
assign taps.v0 = tap_int_rd_data[31:0];
assign taps.v1 = tap_int_rd_data[63:32];
assign taps.v2 = tap_int_rd_data[95:64];
assign taps.v3 = tap_int_rd_data[127:96];
assign taps.v4 = tap_int_rd_data[159:128];
assign taps.v5 = tap_int_rd_data[191:160];

// Final Output Control
assign st_data_out = stage_st_data_out;
assign st_data_out_vld = data_active_r12;
assign st_data_out_pre = stage_st_data_out_pre;
assign st_data_out_pre_vld = data_active_r10;

// Counter Controls
assign load_finish = (read_data_address == load_length);
assign state_done = ((state_address == state_length) & load_finish);
assign bias_start = (load_finish & (state_address == 'd0));
assign bias_enable = ((read_data_address <= bias_length) & (read_data_address > 'd0));

// Internal Counter for which state the operation is in
always @(posedge clk) begin
  if (reset) begin
    state_address <= 'd0;
  end
  else begin
    if (load_finish) begin
      state_address <= state_address + 'd1;
    end
  end
end

// Data Address
always @(posedge clk) begin
  if (reset) begin
    read_data_address <= 3'd0;
  end
  else begin
    if ((data_start | load_finish)) begin
      read_data_address <= 3'd0;
    end
    else if ((data_active | data_active_r6)) begin 
      read_data_address <= read_data_address[2:0] + 3'd1;
    end
  end
end

// Tap Address
always @(posedge clk) begin
  if (reset) begin
    tap_address <= 4'd0;
  end
  else begin
    if (state_done) begin
      tap_address <= 4'd0;
    end
    else if ((data_active | data_active_r6)) begin 
      tap_address <= tap_address[3:0] + 4'd1;
    end
  end
end

// Bias Address
always @(posedge clk) begin
  if (reset) begin
    bias_address <= 3'd0;
  end
  else begin
    if (bias_start) begin
      bias_address <= 3'd0;
    end
    else if (bias_enable) begin 
      bias_address <= bias_address[2:0] + 3'd1;
    end
  end
end
endmodule

