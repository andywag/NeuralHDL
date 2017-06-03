//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       stage_mem_tap
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module stage_mem_tap(
  input                         clk,
  input                         reset,
  input tap_int_192_4           tap_int,
  input                 [191:0] tap_int_wr_data,
  output                [191:0] tap_int_rd_data);

// Parameters 



// Wires 

  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  read_1            ;  // <32,0>
  wire                  [31:0]  read_2            ;  // <32,0>
  wire                  [31:0]  read_3            ;  // <32,0>
  wire                  [31:0]  read_4            ;  // <32,0>
  wire                  [31:0]  read_5            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>
  wire                  [31:0]  write_1           ;  // <32,0>
  wire                  [31:0]  write_2           ;  // <32,0>
  wire                  [31:0]  write_3           ;  // <32,0>
  wire                  [31:0]  write_4           ;  // <32,0>
  wire                  [31:0]  write_5           ;  // <32,0>
  wire                  [5:0]   write_sub         ;  // <6,1>


// Registers 



// Other



////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_0
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_0 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_1
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_1 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_1),
    .m_wr_data(write_1),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_2
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_2 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_2),
    .m_wr_data(write_2),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_3
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_3 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_3),
    .m_wr_data(write_3),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_4
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_4 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_4),
    .m_wr_data(write_4),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// stage_mem_tap_5
////////////////////////////////////////////////////////////////////////////////

memory_32_4 stage_mem_tap_5 (
    .clk(clk),
    .m(tap_int),
    .m_rd_data(read_5),
    .m_wr_data(write_5),
    .reset(reset));

assign write_sub[0] = (tap_int.sub_addr == 'd0);
assign write_0 = write_sub[0] ? tap_int.sub_data : tap_int_wr_data[31:0];
assign tap_int_rd_data[31:0] = read_0;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_0.hex",stage_mem_tap_0.memory_32_4_memory);
end

assign write_sub[1] = (tap_int.sub_addr == 'd1);
assign write_1 = write_sub[1] ? tap_int.sub_data : tap_int_wr_data[63:32];
assign tap_int_rd_data[63:32] = read_1;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_1.hex",stage_mem_tap_1.memory_32_4_memory);
end

assign write_sub[2] = (tap_int.sub_addr == 'd2);
assign write_2 = write_sub[2] ? tap_int.sub_data : tap_int_wr_data[95:64];
assign tap_int_rd_data[95:64] = read_2;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_2.hex",stage_mem_tap_2.memory_32_4_memory);
end

assign write_sub[3] = (tap_int.sub_addr == 'd3);
assign write_3 = write_sub[3] ? tap_int.sub_data : tap_int_wr_data[127:96];
assign tap_int_rd_data[127:96] = read_3;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_3.hex",stage_mem_tap_3.memory_32_4_memory);
end

assign write_sub[4] = (tap_int.sub_addr == 'd4);
assign write_4 = write_sub[4] ? tap_int.sub_data : tap_int_wr_data[159:128];
assign tap_int_rd_data[159:128] = read_4;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_4.hex",stage_mem_tap_4.memory_32_4_memory);
end

assign write_sub[5] = (tap_int.sub_addr == 'd5);
assign write_5 = write_sub[5] ? tap_int.sub_data : tap_int_wr_data[191:160];
assign tap_int_rd_data[191:160] = read_5;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/stage/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/stage/data/init_taps_5.hex",stage_mem_tap_5.memory_32_4_memory);
end

endmodule

