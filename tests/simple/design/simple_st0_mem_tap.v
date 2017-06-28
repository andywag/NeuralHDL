//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       simple_st0_mem_tap
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module simple_st0_mem_tap(
  input                         clk,
  input                         reset,
  input tap_int_192_4           tap_int,
  input                 [191:0] tap_int_wr_data,
  output                [191:0] tap_int_rd_data);

// Parameters 



// Wires 

  mem_int_0_192_4               mem_int_0;  // <1,0>
  mem_int_1_192_4               mem_int_1;  // <1,0>
  mem_int_2_192_4               mem_int_2;  // <1,0>
  mem_int_3_192_4               mem_int_3;  // <1,0>
  mem_int_4_192_4               mem_int_4;  // <1,0>
  mem_int_5_192_4               mem_int_5;  // <1,0>
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

  reg                   [3:0]   inter_base        ;  // <4,0>
  reg                   [3:0]   inter_count_0     ;  // <4,0>
  reg                   [3:0]   inter_count_1     ;  // <4,0>


// Other



////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_0
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_0 (
    .clk(clk),
    .m(mem_int_0),
    .m_rd_data(read_0),
    .m_wr_data(write_0),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_1
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_1 (
    .clk(clk),
    .m(mem_int_1),
    .m_rd_data(read_1),
    .m_wr_data(write_1),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_2
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_2 (
    .clk(clk),
    .m(mem_int_2),
    .m_rd_data(read_2),
    .m_wr_data(write_2),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_3
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_3 (
    .clk(clk),
    .m(mem_int_3),
    .m_rd_data(read_3),
    .m_wr_data(write_3),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_4
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_4 (
    .clk(clk),
    .m(mem_int_4),
    .m_rd_data(read_4),
    .m_wr_data(write_4),
    .reset(reset));

////////////////////////////////////////////////////////////////////////////////
// simple_st0_mem_tap_5
////////////////////////////////////////////////////////////////////////////////

memory_32_4 simple_st0_mem_tap_5 (
    .clk(clk),
    .m(mem_int_5),
    .m_rd_data(read_5),
    .m_wr_data(write_5),
    .reset(reset));

always @(posedge clk) begin
  if (reset) begin
    inter_count_0 <= 4'd0;
  end
  else if (tap_int.inter) begin 
    if ((tap_int.inter_first | (inter_count_0 == 'd5))) begin
      inter_count_0 <= 4'd0;
    end
    else begin
      inter_count_0 <= inter_count_0[3:0] + 4'd1;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    inter_count_1 <= 4'd0;
  end
  else if ((tap_int.inter_first | (inter_count_0 == 'd5))) begin 
    if (tap_int.inter_first) begin
      inter_count_1 <= 4'd0;
    end
    else begin
      inter_count_1 <= inter_count_1[3:0] + 4'd1;
    end
  end
end
assign write_sub[0] = ((tap_int.sub_addr == 'd0) & tap_int.sub_vld);
assign write_0 = write_sub[0] ? tap_int.sub_data : tap_int_wr_data[31:0];
assign mem_int_0.rd_address = tap_int.rd_address;
assign mem_int_0.rd_vld = tap_int.rd_vld;
assign mem_int_0.wr_address = tap_int.wr_address;
assign mem_int_0.wr_vld = (write_sub[0] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[31:0] = read_0;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_0.hex",simple_st0_mem_tap_0.memory_32_4_memory);
end

assign write_sub[1] = ((tap_int.sub_addr == 'd1) & tap_int.sub_vld);
assign write_1 = write_sub[1] ? tap_int.sub_data : tap_int_wr_data[63:32];
assign mem_int_1.rd_address = tap_int.rd_address;
assign mem_int_1.rd_vld = tap_int.rd_vld;
assign mem_int_1.wr_address = tap_int.wr_address;
assign mem_int_1.wr_vld = (write_sub[1] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[63:32] = read_1;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_1.hex",simple_st0_mem_tap_1.memory_32_4_memory);
end

assign write_sub[2] = ((tap_int.sub_addr == 'd2) & tap_int.sub_vld);
assign write_2 = write_sub[2] ? tap_int.sub_data : tap_int_wr_data[95:64];
assign mem_int_2.rd_address = tap_int.rd_address;
assign mem_int_2.rd_vld = tap_int.rd_vld;
assign mem_int_2.wr_address = tap_int.wr_address;
assign mem_int_2.wr_vld = (write_sub[2] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[95:64] = read_2;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_2.hex",simple_st0_mem_tap_2.memory_32_4_memory);
end

assign write_sub[3] = ((tap_int.sub_addr == 'd3) & tap_int.sub_vld);
assign write_3 = write_sub[3] ? tap_int.sub_data : tap_int_wr_data[127:96];
assign mem_int_3.rd_address = tap_int.rd_address;
assign mem_int_3.rd_vld = tap_int.rd_vld;
assign mem_int_3.wr_address = tap_int.wr_address;
assign mem_int_3.wr_vld = (write_sub[3] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[127:96] = read_3;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_3.hex",simple_st0_mem_tap_3.memory_32_4_memory);
end

assign write_sub[4] = ((tap_int.sub_addr == 'd4) & tap_int.sub_vld);
assign write_4 = write_sub[4] ? tap_int.sub_data : tap_int_wr_data[159:128];
assign mem_int_4.rd_address = tap_int.rd_address;
assign mem_int_4.rd_vld = tap_int.rd_vld;
assign mem_int_4.wr_address = tap_int.wr_address;
assign mem_int_4.wr_vld = (write_sub[4] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[159:128] = read_4;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_4.hex",simple_st0_mem_tap_4.memory_32_4_memory);
end

assign write_sub[5] = ((tap_int.sub_addr == 'd5) & tap_int.sub_vld);
assign write_5 = write_sub[5] ? tap_int.sub_data : tap_int_wr_data[191:160];
assign mem_int_5.rd_address = tap_int.rd_address;
assign mem_int_5.rd_vld = tap_int.rd_vld;
assign mem_int_5.wr_address = tap_int.wr_address;
assign mem_int_5.wr_vld = (write_sub[5] | (tap_int.wr_vld & ~tap_int.sub_vld));
assign tap_int_rd_data[191:160] = read_5;

// Optional Memory Load for Memory /home/andy/projects/NeuralHDL/tests/simple/data/init_taps
initial begin
  $readmemh("/home/andy/projects/NeuralHDL/tests/simple/data/init_taps_5.hex",simple_st0_mem_tap_5.memory_32_4_memory);
end

endmodule

