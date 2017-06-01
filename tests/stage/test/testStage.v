//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testStage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testStage(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [2:0]   bias_address      ;  // <3,0>
  wire                          data_in_rdy       ;  // <1,0>
  wire                          data_out_fst      ;  // <1,0>
  wire                          data_out_pre_fst  ;  // <1,0>
  wire                          data_out_pre_vld  ;  // <1,0>
  wire                          data_out_vld      ;  // <1,0>
  wire                  [3:0]   tap_address       ;  // <4,0>
  wire                          tap_in_rdy        ;  // <1,0>


// Registers 

  reg                   [2:0]   bias_length       ;  // <3,0>
  reg                   [31:0]  counter           ;  // <32,0>
  reg                           data_in_fst       ;  // <1,0>
  reg                   [31:0]  data_in_mem[0:156];  // <32,0>
  reg                           data_in_vld       ;  // <1,0>
  reg                           data_out_pre_rdy  ;  // <1,0>
  reg                           data_out_rdy      ;  // <1,0>
  reg                   [3:0]   load_depth        ;  // <4,0>
  reg                   [2:0]   load_length       ;  // <3,0>
  reg                   [31:0]  rtl_out_fptr      ;  // <32,0>
  reg                   [31:0]  rtl_pre_fptr      ;  // <32,0>
  reg                           state_length      ;  // <1,0>
  reg                           tap_in_fst        ;  // <1,0>
  reg                           tap_in_vld        ;  // <1,0>


// Other

  float_24_8                    data_in;  // <1,0>
  float_24_8                    data_out;  // <1,0>
  float_24_8                    data_out_pre;  // <1,0>
  float_24_8                    tap_in;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// stage
////////////////////////////////////////////////////////////////////////////////

stage stage (
    .bias_address(bias_address),
    .bias_length(bias_length),
    .clk(clk),
    .data_in(data_in),
    .data_in_fst(data_in_fst),
    .data_in_rdy(data_in_rdy),
    .data_in_vld(data_in_vld),
    .data_out(data_out),
    .data_out_fst(data_out_fst),
    .data_out_pre(data_out_pre),
    .data_out_pre_fst(data_out_pre_fst),
    .data_out_pre_rdy(data_out_pre_rdy),
    .data_out_pre_vld(data_out_pre_vld),
    .data_out_rdy(data_out_rdy),
    .data_out_vld(data_out_vld),
    .load_depth(load_depth),
    .load_length(load_length),
    .reset(reset),
    .state_length(state_length),
    .tap_address(tap_address),
    .tap_in(tap_in),
    .tap_in_fst(tap_in_fst),
    .tap_in_rdy(tap_in_rdy),
    .tap_in_vld(tap_in_vld));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd864)) begin
      $finish;
    end
  end
end
assign data_in_vld = 'd1;

// Load data_in
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_data.hex",data_in_mem);
end

assign data_in = counter[31] ? 'd0 : data_in_mem[counter];

// Store Store data_out
initial begin
  rtl_out_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/stage/data/rtl_out.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (data_out_vld) begin 
    $fdisplay(rtl_out_fptr,"%h ",data_out);
  end
end

// Store Store data_out_pre
initial begin
  rtl_pre_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/stage/data/rtl_pre.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else if (data_out_pre_vld) begin 
    $fdisplay(rtl_pre_fptr,"%h ",data_out_pre);
  end
end
assign load_length = 3'd5;
assign load_depth = 4'd7;
assign state_length = 'd1;
assign bias_length = 3'd5;
assign load_depth = 4'd7;

// Counter to Index Test
always @(posedge clk) begin
  if (reset) begin
    counter <= 32'd0;
  end
  else begin
    counter <= counter[31:0] + 32'd1;
  end
end

// Initial Statement
initial begin
  
end


// DUT
endmodule

