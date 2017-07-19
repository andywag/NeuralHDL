//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st3_ctrl
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st3_ctrl(
  input                 [31:0]  bias_int_rd_data,
  input                         clk,
  input                 [31:0]  data_int_rd_data,
  input four_12_12_st3_ctrl_int_t four_12_12_st3_ctrl_int,
  input float_24_8              four_12_12_st3_st_data_out,
  input float_24_8              four_12_12_st3_st_data_out_bias,
  input float_24_8              four_12_12_st3_st_data_out_pre,
  input                 [383:0]  four_12_12_st3_st_tap_out,
  input                         reset,
  input float_24_8              stage_3_data,
  input                         stage_3_data_fst,
  input                         stage_3_data_vld,
  input float_24_8              stage_3_error,
  input                         stage_3_error_fst,
  input                         stage_3_error_vld,
  input float_24_8              tap_in,
  input                         tap_in_fst,
  input                         tap_in_vld,
  input                 [383:0]  tap_int_rd_data,
  output bias_int_32_4          bias_int,
  output                [31:0]  bias_int_wr_data,
  output data_int_32_9          data_int,
  output                [31:0]  data_int_wr_data,
  output                        first,
  output                        four_12_12_st3_ctrl_data_fifo_data_ready,
  output float_24_8             four_12_12_st3_st_bias,
  output float_24_8             four_12_12_st3_st_data,
  output                        load_finish,
  output float_24_8             stage_3_data_out,
  output                        stage_3_data_out_fst,
  output float_24_8             stage_3_data_out_pre,
  output                        stage_3_data_out_pre_fst,
  output                        stage_3_data_out_pre_vld,
  output                        stage_3_data_out_vld,
  output                        stage_3_data_rdy,
  output float_24_8             stage_3_error_out,
  output                        stage_3_error_out_fst,
  output                        stage_3_error_out_vld,
  output                        stage_3_error_rdy,
  output                        stage_error_back,
  output                        stage_error_first,
  output                        stage_error_mode,
  output                        tap_in_rdy,
  output tap_int_384_5          tap_int,
  output                [383:0]  tap_int_wr_data,
  output taps_typ_12            taps,
  output                        update_error_first,
  output float_24_8             zerror_int,
  output                        zerror_int_fst,
  output                        zerror_int_vld);

// Parameters 



// Wires 

  wire                          active            ;  // <1,0>
  wire                          active_normal     ;  // <1,0>
  wire                          active_pre        ;  // <1,0>
  wire                          active_start_d    ;  // <1,0>
  wire                  [4:0]   bias_address      ;  // <5,0>
  wire                          bias_enable       ;  // <1,0>
  wire                  [31:0]  bias_int_rd_data  ;  // <32,0>
  wire                  [4:0]   bias_wr_address   ;  // <5,0>
  wire                  [31:0]  data_int_rd_data  ;  // <32,0>
  wire                  [8:0]   data_read_addr    ;  // <9,0>
  wire                          data_valid        ;  // <1,0>
  wire                  [31:0]  data_value        ;  // <32,0>
  wire                  [8:0]   data_write_addr   ;  // <9,0>
  wire                          err_finish_i      ;  // <1,0>
  wire                          err_finish_new    ;  // <1,0>
  wire                  [3:0]   error_count       ;  // <4,0>
  wire                          error_finish_tap  ;  // <1,0>
  wire                  [3:0]   error_phase       ;  // <4,0>
  wire                  [3:0]   error_phase_read  ;  // <4,0>
  wire                  [31:0]  error_sub_address  ;  // <32,0>
  wire                  [4:0]   error_tap_length  ;  // <5,0>
  wire                          error_tap_update_out  ;  // <1,0>
  wire                          error_update_first  ;  // <1,0>
  wire                          error_update_latch  ;  // <1,0>
  wire                          error_update_mode  ;  // <1,0>
  wire                          error_valid       ;  // <1,0>
  wire                  [31:0]  error_value       ;  // <32,0>
  float_24_8                    four_12_12_st3_st_data_out;  // <1,0>
  float_24_8                    four_12_12_st3_st_data_out_bias;  // <1,0>
  float_24_8                    four_12_12_st3_st_data_out_pre;  // <1,0>
  wire                          input_stage       ;  // <1,0>
  wire                  [4:0]   load_depth        ;  // <5,0>
  wire                  [3:0]   load_length       ;  // <4,0>
  wire                          read_finish       ;  // <1,0>
  wire                          stage_3_data_out_pre_rdy  ;  // <1,0>
  wire                          stage_3_data_out_rdy  ;  // <1,0>
  wire                          stage_3_error_out_rdy  ;  // <1,0>
  wire                          state_finish      ;  // <1,0>
  wire                          state_length      ;  // <0,0>
  wire                  [4:0]   tap_address       ;  // <5,0>
  wire                          tap_enable        ;  // <1,0>
  wire                  [383:0]  tap_int_rd_data  ;  // <384,0>
  wire                          zerror_int_rdy    ;  // <1,0>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_ctrl_data_fifo
////////////////////////////////////////////////////////////////////////////////

four_12_12_st3_ctrl_data_fifo four_12_12_st3_ctrl_data_fifo (
    .active(active),
    .active_normal(active_normal),
    .active_pre(active_pre),
    .active_start_d(active_start_d),
    .bias_address(bias_address),
    .bias_wr_address(bias_wr_address),
    .clk(clk),
    .data_read_addr(data_read_addr),
    .data_valid(data_valid),
    .data_value(data_value),
    .data_write_addr(data_write_addr),
    .err_finish_i(err_finish_i),
    .err_finish_new(err_finish_new),
    .error_finish_tap(error_finish_tap),
    .error_tap_update_out(error_tap_update_out),
    .error_update_first(error_update_first),
    .error_update_latch(error_update_latch),
    .error_update_mode(error_update_mode),
    .four_12_12_st3_ctrl_data_fifo_data_ready(four_12_12_st3_ctrl_data_fifo_data_ready),
    .load_depth(load_depth),
    .load_finish(load_finish),
    .load_length(load_length),
    .read_finish(read_finish),
    .reset(reset),
    .stage_3_data(stage_3_data),
    .stage_3_data_fst(stage_3_data_fst),
    .stage_3_data_rdy(stage_3_data_rdy),
    .stage_3_data_vld(stage_3_data_vld),
    .state_finish(state_finish),
    .state_length(state_length),
    .tap_address(tap_address));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_ctrl_error_fifo
////////////////////////////////////////////////////////////////////////////////

four_12_12_st3_ctrl_error_fifo four_12_12_st3_ctrl_error_fifo (
    .clk(clk),
    .error_count(error_count),
    .error_finish_tap(error_finish_tap),
    .error_phase(error_phase),
    .error_phase_read(error_phase_read),
    .error_sub_address(error_sub_address),
    .error_tap_length(error_tap_length),
    .error_tap_update_out(error_tap_update_out),
    .error_update_first(error_update_first),
    .error_update_latch(error_update_latch),
    .error_update_mode(error_update_mode),
    .error_valid(error_valid),
    .error_value(error_value),
    .input_stage(input_stage),
    .load_length(load_length),
    .read_finish(read_finish),
    .reset(reset),
    .stage_3_error(stage_3_error),
    .stage_3_error_fst(stage_3_error_fst),
    .stage_3_error_rdy(stage_3_error_rdy),
    .stage_3_error_vld(stage_3_error_vld),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .state_finish(state_finish));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_ctrl_out_ctrl
////////////////////////////////////////////////////////////////////////////////

four_12_12_st3_ctrl_out_ctrl four_12_12_st3_ctrl_out_ctrl (
    .active(active),
    .active_normal(active_normal),
    .active_pre(active_pre),
    .active_start_d(active_start_d),
    .bias_address(bias_address),
    .bias_enable(bias_enable),
    .bias_int(bias_int),
    .bias_int_rd_data(bias_int_rd_data),
    .bias_int_wr_data(bias_int_wr_data),
    .bias_wr_address(bias_wr_address),
    .clk(clk),
    .data_int(data_int),
    .data_int_rd_data(data_int_rd_data),
    .data_int_wr_data(data_int_wr_data),
    .data_read_addr(data_read_addr),
    .data_valid(data_valid),
    .data_value(data_value),
    .data_write_addr(data_write_addr),
    .err_finish_i(err_finish_i),
    .err_finish_new(err_finish_new),
    .error_count(error_count),
    .error_phase(error_phase),
    .error_phase_read(error_phase_read),
    .error_sub_address(error_sub_address),
    .error_tap_length(error_tap_length),
    .error_tap_update_out(error_tap_update_out),
    .error_update_first(error_update_first),
    .error_update_latch(error_update_latch),
    .error_valid(error_valid),
    .error_value(error_value),
    .first(first),
    .four_12_12_st3_st_bias(four_12_12_st3_st_bias),
    .four_12_12_st3_st_data(four_12_12_st3_st_data),
    .four_12_12_st3_st_data_out(four_12_12_st3_st_data_out),
    .four_12_12_st3_st_data_out_bias(four_12_12_st3_st_data_out_bias),
    .four_12_12_st3_st_data_out_pre(four_12_12_st3_st_data_out_pre),
    .four_12_12_st3_st_tap_out(four_12_12_st3_st_tap_out),
    .read_finish(read_finish),
    .reset(reset),
    .stage_3_data_out(stage_3_data_out),
    .stage_3_data_out_fst(stage_3_data_out_fst),
    .stage_3_data_out_pre(stage_3_data_out_pre),
    .stage_3_data_out_pre_fst(stage_3_data_out_pre_fst),
    .stage_3_data_out_pre_rdy(stage_3_data_out_pre_rdy),
    .stage_3_data_out_pre_vld(stage_3_data_out_pre_vld),
    .stage_3_data_out_rdy(stage_3_data_out_rdy),
    .stage_3_data_out_vld(stage_3_data_out_vld),
    .stage_3_error_out(stage_3_error_out),
    .stage_3_error_out_fst(stage_3_error_out_fst),
    .stage_3_error_out_rdy(stage_3_error_out_rdy),
    .stage_3_error_out_vld(stage_3_error_out_vld),
    .stage_error_back(stage_error_back),
    .stage_error_first(stage_error_first),
    .stage_error_mode(stage_error_mode),
    .tap_address(tap_address),
    .tap_enable(tap_enable),
    .tap_int(tap_int),
    .tap_int_rd_data(tap_int_rd_data),
    .tap_int_wr_data(tap_int_wr_data),
    .taps(taps),
    .update_error_first(update_error_first),
    .zerror_int(zerror_int),
    .zerror_int_fst(zerror_int_fst),
    .zerror_int_rdy(zerror_int_rdy),
    .zerror_int_vld(zerror_int_vld));

assign load_length = four_12_12_st3_ctrl_int.load_length;
assign load_depth = four_12_12_st3_ctrl_int.load_depth;
assign state_length = four_12_12_st3_ctrl_int.state_length;
assign error_tap_length = four_12_12_st3_ctrl_int.error_length;
assign input_stage = four_12_12_st3_ctrl_int.input_stage;
assign tap_enable = four_12_12_st3_ctrl_int.tap_update_enable;
assign bias_enable = four_12_12_st3_ctrl_int.bias_update_enable;
endmodule

