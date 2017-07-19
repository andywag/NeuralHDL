//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st3_mem_tap
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st3_mem_tap(
  input                         clk,
  input                         reset,
  input tap_int_384_5           tap_int,
  input                 [383:0]  tap_int_wr_data,
  output                [383:0]  tap_int_rd_data);

// Parameters 



// Wires 

  mem_int_0_384_5               mem_int_0;  // <1,0>
  mem_int_1_384_5               mem_int_1;  // <1,0>
  mem_int_10_384_5              mem_int_10;  // <1,0>
  mem_int_11_384_5              mem_int_11;  // <1,0>
  mem_int_2_384_5               mem_int_2;  // <1,0>
  mem_int_3_384_5               mem_int_3;  // <1,0>
  mem_int_4_384_5               mem_int_4;  // <1,0>
  mem_int_5_384_5               mem_int_5;  // <1,0>
  mem_int_6_384_5               mem_int_6;  // <1,0>
  mem_int_7_384_5               mem_int_7;  // <1,0>
  mem_int_8_384_5               mem_int_8;  // <1,0>
  mem_int_9_384_5               mem_int_9;  // <1,0>
  wire                  [4:0]   rd_offset         ;  // <5,0>
  wire                  [4:0]   rd_offset0_0      ;  // <5,0>
  wire                  [4:0]   rd_offset0_1      ;  // <5,0>
  wire                  [4:0]   rd_offset0_10     ;  // <5,0>
  wire                  [4:0]   rd_offset0_11     ;  // <5,0>
  wire                  [4:0]   rd_offset0_2      ;  // <5,0>
  wire                  [4:0]   rd_offset0_3      ;  // <5,0>
  wire                  [4:0]   rd_offset0_4      ;  // <5,0>
  wire                  [4:0]   rd_offset0_5      ;  // <5,0>
  wire                  [4:0]   rd_offset0_6      ;  // <5,0>
  wire                  [4:0]   rd_offset0_7      ;  // <5,0>
  wire                  [4:0]   rd_offset0_8      ;  // <5,0>
  wire                  [4:0]   rd_offset0_9      ;  // <5,0>
  wire                  [4:0]   rd_offset1_0      ;  // <5,0>
  wire                  [4:0]   rd_offset1_1      ;  // <5,0>
  wire                  [4:0]   rd_offset1_10     ;  // <5,0>
  wire                  [4:0]   rd_offset1_11     ;  // <5,0>
  wire                  [4:0]   rd_offset1_2      ;  // <5,0>
  wire                  [4:0]   rd_offset1_3      ;  // <5,0>
  wire                  [4:0]   rd_offset1_4      ;  // <5,0>
  wire                  [4:0]   rd_offset1_5      ;  // <5,0>
  wire                  [4:0]   rd_offset1_6      ;  // <5,0>
  wire                  [4:0]   rd_offset1_7      ;  // <5,0>
  wire                  [4:0]   rd_offset1_8      ;  // <5,0>
  wire                  [4:0]   rd_offset1_9      ;  // <5,0>
  wire                  [4:0]   rd_offset_w0      ;  // <5,0>
  wire                  [4:0]   rd_offset_w1      ;  // <5,0>
  wire                  [4:0]   rd_offset_w10     ;  // <5,0>
  wire                  [4:0]   rd_offset_w11     ;  // <5,0>
  wire                  [4:0]   rd_offset_w2      ;  // <5,0>
  wire                  [4:0]   rd_offset_w3      ;  // <5,0>
  wire                  [4:0]   rd_offset_w4      ;  // <5,0>
  wire                  [4:0]   rd_offset_w5      ;  // <5,0>
  wire                  [4:0]   rd_offset_w6      ;  // <5,0>
  wire                  [4:0]   rd_offset_w7      ;  // <5,0>
  wire                  [4:0]   rd_offset_w8      ;  // <5,0>
  wire                  [4:0]   rd_offset_w9      ;  // <5,0>
  wire                  [31:0]  read_0            ;  // <32,0>
  wire                  [31:0]  read_1            ;  // <32,0>
  wire                  [31:0]  read_10           ;  // <32,0>
  wire                  [31:0]  read_11           ;  // <32,0>
  wire                  [31:0]  read_2            ;  // <32,0>
  wire                  [31:0]  read_3            ;  // <32,0>
  wire                  [31:0]  read_4            ;  // <32,0>
  wire                  [31:0]  read_5            ;  // <32,0>
  wire                  [31:0]  read_6            ;  // <32,0>
  wire                  [31:0]  read_7            ;  // <32,0>
  wire                  [31:0]  read_8            ;  // <32,0>
  wire                  [31:0]  read_9            ;  // <32,0>
  wire                  [31:0]  write_0           ;  // <32,0>
  wire                  [31:0]  write_1           ;  // <32,0>
  wire                  [31:0]  write_10          ;  // <32,0>
  wire                  [31:0]  write_11          ;  // <32,0>
  wire                  [31:0]  write_2           ;  // <32,0>
  wire                  [31:0]  write_3           ;  // <32,0>
  wire                  [31:0]  write_4           ;  // <32,0>
  wire                  [31:0]  write_5           ;  // <32,0>
  wire                  [31:0]  write_6           ;  // <32,0>
  wire                  [31:0]  write_7           ;  // <32,0>
  wire                  [31:0]  write_8           ;  // <32,0>
  wire                  [31:0]  write_9           ;  // <32,0>
  wire                  [11:0]  write_sub         ;  // <12,1>


// Registers 

  reg                   [4:0]   inter_base        ;  // <5,0>
  reg                   [4:0]   inter_count_0     ;  // <5,0>
  reg                   [4:0]   inter_count_1     ;  // <5,0>


// Other



////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_0
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_0 (
    .clk(clk),
    .m(mem_int_0),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_1
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_1 (
    .clk(clk),
    .m(mem_int_1),
    .m_rd_data(read_1),
    .m_wr_data(write_1),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_2
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_2 (
    .clk(clk),
    .m(mem_int_2),
    .m_rd_data(read_2),
    .m_wr_data(write_2),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_3
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_3 (
    .clk(clk),
    .m(mem_int_3),
    .m_rd_data(read_3),
    .m_wr_data(write_3),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_4
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_4 (
    .clk(clk),
    .m(mem_int_4),
    .m_rd_data(read_4),
    .m_wr_data(write_4),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_5
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_5 (
    .clk(clk),
    .m(mem_int_5),
    .m_rd_data(read_5),
    .m_wr_data(write_5),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_6
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_6 (
    .clk(clk),
    .m(mem_int_6),
    .m_rd_data(read_6),
    .m_wr_data(write_6),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_7
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_7 (
    .clk(clk),
    .m(mem_int_7),
    .m_rd_data(read_7),
    .m_wr_data(write_7),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_8
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_8 (
    .clk(clk),
    .m(mem_int_8),
    .m_rd_data(read_8),
    .m_wr_data(write_8),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_9
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_9 (
    .clk(clk),
    .m(mem_int_9),
    .m_rd_data(read_9),
    .m_wr_data(write_9),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_10
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_10 (
    .clk(clk),
    .m(mem_int_10),
    .m_rd_data(read_10),
    .m_wr_data(write_10),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// four_12_12_st3_mem_tap_11
////////////////////////////////////////////////////////////////////////////////

memory_32_5 four_12_12_st3_mem_tap_11 (
    .clk(clk),
    .m(mem_int_11),
    .m_rd_data(read_11),
    .m_wr_data(write_11),
    .reset(reset));

always @(posedge clk) begin
  if (reset) begin
    inter_count_0 <= 5'd0;
  end
  else if (tap_int.inter) begin 
    if ((tap_int.inter_first | (inter_count_0 == 'd11))) begin
      inter_count_0 <= 5'd0;
    end
    else begin
      inter_count_0 <= inter_count_0[4:0] + 5'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    inter_count_1 <= 5'd0;
  end
  else if ((tap_int.inter_first | (inter_count_0 == 'd11))) begin 
    if (tap_int.inter_first) begin
      inter_count_1 <= 5'd0;
    end
    else begin
      inter_count_1 <= inter_count_1[4:0] + 5'd12;
    end
  end
end
assign rd_offset1_0 = inter_count_0[4:0] + 5'd0;
assign rd_offset0_0 = (rd_offset1_0 < 'd12) ? rd_offset1_0 : rd_offset1_0[4:0] - 5'd12;
assign rd_offset_w0 = rd_offset0_0[4:0] + inter_count_1[4:0];
assign rd_offset1_1 = inter_count_0[4:0] + 5'd1;
assign rd_offset0_1 = (rd_offset1_1 < 'd12) ? rd_offset1_1 : rd_offset1_1[4:0] - 5'd12;
assign rd_offset_w1 = rd_offset0_1[4:0] + inter_count_1[4:0];
assign rd_offset1_2 = inter_count_0[4:0] + 5'd2;
assign rd_offset0_2 = (rd_offset1_2 < 'd12) ? rd_offset1_2 : rd_offset1_2[4:0] - 5'd12;
assign rd_offset_w2 = rd_offset0_2[4:0] + inter_count_1[4:0];
assign rd_offset1_3 = inter_count_0[4:0] + 5'd3;
assign rd_offset0_3 = (rd_offset1_3 < 'd12) ? rd_offset1_3 : rd_offset1_3[4:0] - 5'd12;
assign rd_offset_w3 = rd_offset0_3[4:0] + inter_count_1[4:0];
assign rd_offset1_4 = inter_count_0[4:0] + 5'd4;
assign rd_offset0_4 = (rd_offset1_4 < 'd12) ? rd_offset1_4 : rd_offset1_4[4:0] - 5'd12;
assign rd_offset_w4 = rd_offset0_4[4:0] + inter_count_1[4:0];
assign rd_offset1_5 = inter_count_0[4:0] + 5'd5;
assign rd_offset0_5 = (rd_offset1_5 < 'd12) ? rd_offset1_5 : rd_offset1_5[4:0] - 5'd12;
assign rd_offset_w5 = rd_offset0_5[4:0] + inter_count_1[4:0];
assign rd_offset1_6 = inter_count_0[4:0] + 5'd6;
assign rd_offset0_6 = (rd_offset1_6 < 'd12) ? rd_offset1_6 : rd_offset1_6[4:0] - 5'd12;
assign rd_offset_w6 = rd_offset0_6[4:0] + inter_count_1[4:0];
assign rd_offset1_7 = inter_count_0[4:0] + 5'd7;
assign rd_offset0_7 = (rd_offset1_7 < 'd12) ? rd_offset1_7 : rd_offset1_7[4:0] - 5'd12;
assign rd_offset_w7 = rd_offset0_7[4:0] + inter_count_1[4:0];
assign rd_offset1_8 = inter_count_0[4:0] + 5'd8;
assign rd_offset0_8 = (rd_offset1_8 < 'd12) ? rd_offset1_8 : rd_offset1_8[4:0] - 5'd12;
assign rd_offset_w8 = rd_offset0_8[4:0] + inter_count_1[4:0];
assign rd_offset1_9 = inter_count_0[4:0] + 5'd9;
assign rd_offset0_9 = (rd_offset1_9 < 'd12) ? rd_offset1_9 : rd_offset1_9[4:0] - 5'd12;
assign rd_offset_w9 = rd_offset0_9[4:0] + inter_count_1[4:0];
assign rd_offset1_10 = inter_count_0[4:0] + 5'd10;
assign rd_offset0_10 = (rd_offset1_10 < 'd12) ? rd_offset1_10 : rd_offset1_10[4:0] - 5'd12;
assign rd_offset_w10 = rd_offset0_10[4:0] + inter_count_1[4:0];
assign rd_offset1_11 = inter_count_0[4:0] + 5'd11;
assign rd_offset0_11 = (rd_offset1_11 < 'd12) ? rd_offset1_11 : rd_offset1_11[4:0] - 5'd12;
assign rd_offset_w11 = rd_offset0_11[4:0] + inter_count_1[4:0];
assign write_sub[0] = ((tap_int.sub_addr == 'd0) & tap_int.sub_vld);
assign write_0 = write_sub[0] ? tap_int.sub_data : tap_int_wr_data[31:0];
assign mem_int_0.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w0 : tap_int.rd_address;
assign mem_int_0.rd_vld = tap_int.rd_vld;
assign mem_int_0.wr_address = tap_int.wr_address;
assign mem_int_0.wr_vld = (write_sub[0] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[31:0] = read_0;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_0.hex",four_12_12_st3_mem_tap_0.memory_32_5_memory);
end

assign write_sub[1] = ((tap_int.sub_addr == 'd1) & tap_int.sub_vld);
assign write_1 = write_sub[1] ? tap_int.sub_data : tap_int_wr_data[63:32];
assign mem_int_1.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w1 : tap_int.rd_address;
assign mem_int_1.rd_vld = tap_int.rd_vld;
assign mem_int_1.wr_address = tap_int.wr_address;
assign mem_int_1.wr_vld = (write_sub[1] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[63:32] = read_1;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_1.hex",four_12_12_st3_mem_tap_1.memory_32_5_memory);
end

assign write_sub[2] = ((tap_int.sub_addr == 'd2) & tap_int.sub_vld);
assign write_2 = write_sub[2] ? tap_int.sub_data : tap_int_wr_data[95:64];
assign mem_int_2.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w2 : tap_int.rd_address;
assign mem_int_2.rd_vld = tap_int.rd_vld;
assign mem_int_2.wr_address = tap_int.wr_address;
assign mem_int_2.wr_vld = (write_sub[2] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[95:64] = read_2;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_2.hex",four_12_12_st3_mem_tap_2.memory_32_5_memory);
end

assign write_sub[3] = ((tap_int.sub_addr == 'd3) & tap_int.sub_vld);
assign write_3 = write_sub[3] ? tap_int.sub_data : tap_int_wr_data[127:96];
assign mem_int_3.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w3 : tap_int.rd_address;
assign mem_int_3.rd_vld = tap_int.rd_vld;
assign mem_int_3.wr_address = tap_int.wr_address;
assign mem_int_3.wr_vld = (write_sub[3] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[127:96] = read_3;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_3.hex",four_12_12_st3_mem_tap_3.memory_32_5_memory);
end

assign write_sub[4] = ((tap_int.sub_addr == 'd4) & tap_int.sub_vld);
assign write_4 = write_sub[4] ? tap_int.sub_data : tap_int_wr_data[159:128];
assign mem_int_4.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w4 : tap_int.rd_address;
assign mem_int_4.rd_vld = tap_int.rd_vld;
assign mem_int_4.wr_address = tap_int.wr_address;
assign mem_int_4.wr_vld = (write_sub[4] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[159:128] = read_4;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_4.hex",four_12_12_st3_mem_tap_4.memory_32_5_memory);
end

assign write_sub[5] = ((tap_int.sub_addr == 'd5) & tap_int.sub_vld);
assign write_5 = write_sub[5] ? tap_int.sub_data : tap_int_wr_data[191:160];
assign mem_int_5.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w5 : tap_int.rd_address;
assign mem_int_5.rd_vld = tap_int.rd_vld;
assign mem_int_5.wr_address = tap_int.wr_address;
assign mem_int_5.wr_vld = (write_sub[5] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[191:160] = read_5;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_5.hex",four_12_12_st3_mem_tap_5.memory_32_5_memory);
end

assign write_sub[6] = ((tap_int.sub_addr == 'd6) & tap_int.sub_vld);
assign write_6 = write_sub[6] ? tap_int.sub_data : tap_int_wr_data[223:192];
assign mem_int_6.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w6 : tap_int.rd_address;
assign mem_int_6.rd_vld = tap_int.rd_vld;
assign mem_int_6.wr_address = tap_int.wr_address;
assign mem_int_6.wr_vld = (write_sub[6] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[223:192] = read_6;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_6.hex",four_12_12_st3_mem_tap_6.memory_32_5_memory);
end

assign write_sub[7] = ((tap_int.sub_addr == 'd7) & tap_int.sub_vld);
assign write_7 = write_sub[7] ? tap_int.sub_data : tap_int_wr_data[255:224];
assign mem_int_7.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w7 : tap_int.rd_address;
assign mem_int_7.rd_vld = tap_int.rd_vld;
assign mem_int_7.wr_address = tap_int.wr_address;
assign mem_int_7.wr_vld = (write_sub[7] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[255:224] = read_7;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_7.hex",four_12_12_st3_mem_tap_7.memory_32_5_memory);
end

assign write_sub[8] = ((tap_int.sub_addr == 'd8) & tap_int.sub_vld);
assign write_8 = write_sub[8] ? tap_int.sub_data : tap_int_wr_data[287:256];
assign mem_int_8.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w8 : tap_int.rd_address;
assign mem_int_8.rd_vld = tap_int.rd_vld;
assign mem_int_8.wr_address = tap_int.wr_address;
assign mem_int_8.wr_vld = (write_sub[8] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[287:256] = read_8;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_8.hex",four_12_12_st3_mem_tap_8.memory_32_5_memory);
end

assign write_sub[9] = ((tap_int.sub_addr == 'd9) & tap_int.sub_vld);
assign write_9 = write_sub[9] ? tap_int.sub_data : tap_int_wr_data[319:288];
assign mem_int_9.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w9 : tap_int.rd_address;
assign mem_int_9.rd_vld = tap_int.rd_vld;
assign mem_int_9.wr_address = tap_int.wr_address;
assign mem_int_9.wr_vld = (write_sub[9] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[319:288] = read_9;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_9.hex",four_12_12_st3_mem_tap_9.memory_32_5_memory);
end

assign write_sub[10] = ((tap_int.sub_addr == 'd10) & tap_int.sub_vld);
assign write_10 = write_sub[10] ? tap_int.sub_data : tap_int_wr_data[351:320];
assign mem_int_10.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w10 : tap_int.rd_address;
assign mem_int_10.rd_vld = tap_int.rd_vld;
assign mem_int_10.wr_address = tap_int.wr_address;
assign mem_int_10.wr_vld = (write_sub[10] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[351:320] = read_10;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_10.hex",four_12_12_st3_mem_tap_10.memory_32_5_memory);
end

assign write_sub[11] = ((tap_int.sub_addr == 'd11) & tap_int.sub_vld);
assign write_11 = write_sub[11] ? tap_int.sub_data : tap_int_wr_data[383:352];
assign mem_int_11.rd_address = (tap_int.inter & ~tap_int.inter_first) ? rd_offset_w11 : tap_int.rd_address;
assign mem_int_11.rd_vld = tap_int.rd_vld;
assign mem_int_11.wr_address = tap_int.wr_address;
assign mem_int_11.wr_vld = (write_sub[11] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[383:352] = read_11;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/four_12_12/data/init_taps3_11.hex",four_12_12_st3_mem_tap_11.memory_32_5_memory);
end

endmodule

