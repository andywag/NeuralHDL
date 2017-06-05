//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       testNeural_stage
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module testNeural_stage(
  input                         clk,
  input                         reset);

// Parameters 



// Wires 

  wire                  [31:0]  d_index           ;  // <32,0>
  wire                  [31:0]  d_index2          ;  // <32,0>
  wire                  [31:0]  d_index3          ;  // <32,0>


// Registers 

  reg                   [31:0]  counter           ;  // <32,0>
  reg                           error_first       ;  // <1,0>
  reg                           error_mode        ;  // <1,0>
  reg                           first             ;  // <1,0>
  reg                   [31:0]  neural_stage_bias_mem[0:65536];  // <32,0>
  reg                   [31:0]  neural_stage_data_mem[0:65536];  // <32,0>
  reg                   [31:0]  rout1_fptr        ;  // <32,0>
  reg                   [31:0]  rout2_fptr        ;  // <32,0>
  reg                   [31:0]  rout_fptr         ;  // <32,0>
  reg                   [31:0]  taps_v0_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v10_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v11_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v12_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v13_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v14_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v15_mem[0:16];  // <32,0>
  reg                   [31:0]  taps_v1_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v2_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v3_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v4_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v5_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v6_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v7_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v8_mem[0:16] ;  // <32,0>
  reg                   [31:0]  taps_v9_mem[0:16] ;  // <32,0>


// Other

  float_24_8                    neural_stage_bias;  // <1,0>
  float_24_8                    neural_stage_data;  // <1,0>
  float_24_8                    neural_stage_data_out;  // <1,0>
  float_24_8                    neural_stage_data_out_bias;  // <1,0>
  float_24_8                    neural_stage_data_out_pre;  // <1,0>
  taps_typ_16                   taps;  // <1,0>


////////////////////////////////////////////////////////////////////////////////
// neural_stage
////////////////////////////////////////////////////////////////////////////////

neural_stage neural_stage (
    .clk(clk),
    .error_first(error_first),
    .error_mode(error_mode),
    .first(first),
    .neural_stage_bias(neural_stage_bias),
    .neural_stage_data(neural_stage_data),
    .neural_stage_data_out(neural_stage_data_out),
    .neural_stage_data_out_bias(neural_stage_data_out_bias),
    .neural_stage_data_out_pre(neural_stage_data_out_pre),
    .reset(reset),
    .taps(taps));


// Stop the test when the data runs out
always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    if ((counter == 'd65536)) begin
      $finish;
    end
  end
end

// Create a valid pulse
assign first = (counter[3:0] == 'd0);

// Delay the bias to line up the data
assign d_index2 = counter[31:0] - 32'd18;
assign d_index3[3:0] = counter[3:0];

// Load neural_stage_data
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/data.hex",neural_stage_data_mem);
end

assign neural_stage_data = counter[31] ? 'd0 : neural_stage_data_mem[counter];

// Load taps.v0
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_0.hex",taps_v0_mem);
end

assign taps.v0 = d_index3[31] ? 'd0 : taps_v0_mem[d_index3];

// Load taps.v1
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_1.hex",taps_v1_mem);
end

assign taps.v1 = d_index3[31] ? 'd0 : taps_v1_mem[d_index3];

// Load taps.v2
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_2.hex",taps_v2_mem);
end

assign taps.v2 = d_index3[31] ? 'd0 : taps_v2_mem[d_index3];

// Load taps.v3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_3.hex",taps_v3_mem);
end

assign taps.v3 = d_index3[31] ? 'd0 : taps_v3_mem[d_index3];

// Load taps.v4
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_4.hex",taps_v4_mem);
end

assign taps.v4 = d_index3[31] ? 'd0 : taps_v4_mem[d_index3];

// Load taps.v5
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_5.hex",taps_v5_mem);
end

assign taps.v5 = d_index3[31] ? 'd0 : taps_v5_mem[d_index3];

// Load taps.v6
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_6.hex",taps_v6_mem);
end

assign taps.v6 = d_index3[31] ? 'd0 : taps_v6_mem[d_index3];

// Load taps.v7
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_7.hex",taps_v7_mem);
end

assign taps.v7 = d_index3[31] ? 'd0 : taps_v7_mem[d_index3];

// Load taps.v8
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_8.hex",taps_v8_mem);
end

assign taps.v8 = d_index3[31] ? 'd0 : taps_v8_mem[d_index3];

// Load taps.v9
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_9.hex",taps_v9_mem);
end

assign taps.v9 = d_index3[31] ? 'd0 : taps_v9_mem[d_index3];

// Load taps.v10
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_10.hex",taps_v10_mem);
end

assign taps.v10 = d_index3[31] ? 'd0 : taps_v10_mem[d_index3];

// Load taps.v11
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_11.hex",taps_v11_mem);
end

assign taps.v11 = d_index3[31] ? 'd0 : taps_v11_mem[d_index3];

// Load taps.v12
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_12.hex",taps_v12_mem);
end

assign taps.v12 = d_index3[31] ? 'd0 : taps_v12_mem[d_index3];

// Load taps.v13
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_13.hex",taps_v13_mem);
end

assign taps.v13 = d_index3[31] ? 'd0 : taps_v13_mem[d_index3];

// Load taps.v14
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_14.hex",taps_v14_mem);
end

assign taps.v14 = d_index3[31] ? 'd0 : taps_v14_mem[d_index3];

// Load taps.v15
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/taps_15.hex",taps_v15_mem);
end

assign taps.v15 = d_index3[31] ? 'd0 : taps_v15_mem[d_index3];

// Load neural_stage_bias
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/neural_stage/data/bias.hex",neural_stage_bias_mem);
end

assign neural_stage_bias = d_index2[31] ? 'd0 : neural_stage_bias_mem[d_index2];

// Store Store neural_stage_data_out_pre
initial begin
  rout_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/neural_stage/data/rout.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout_fptr,"%h ",neural_stage_data_out_pre);
  end
end

// Store Store neural_stage_data_out_bias
initial begin
  rout1_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/neural_stage/data/rout1.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout1_fptr,"%h ",neural_stage_data_out_bias);
  end
end

// Store Store neural_stage_data_out
initial begin
  rout2_fptr = $fopen("/home/andy/projects/NeuralHDL/tests/neural_stage/data/rout2.hex","w");
end

always @(posedge clk) begin
  if (reset) begin
    
  end
  else begin
    $fdisplay(rout2_fptr,"%h ",neural_stage_data_out);
  end
end

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

