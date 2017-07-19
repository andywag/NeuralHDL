//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_st0_fifo
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_st0_fifo(
  input                         clk,
  input                         reset,
  input                         stage_0_error_out_rdy,
  input float_24_8              zerror_int,
  input                         zerror_int_fst,
  input                         zerror_int_vld,
  output float_24_8             stage_0_error_out,
  output                        stage_0_error_out_fst,
  output                        stage_0_error_out_vld,
  output                        zerror_int_rdy);

// Parameters 



// Wires 

  wire                  [6:0]   fifo_read_address_e  ;  // <7,0>
  zerror_int_typ_128            internal;  // <1,0>


// Registers 

  reg                   [7:0]   fifo_depth        ;  // <8,0>
  reg                   [7:0]   fifo_e_depth      ;  // <8,0>
  reg                   [6:0]   fifo_read_address  ;  // <7,0>
  reg                   [6:0]   fifo_write_address  ;  // <7,0>


// Other



always @* begin
  if (((zerror_int_rdy & zerror_int_vld) & (stage_0_error_out_rdy & stage_0_error_out_vld))) begin
    fifo_e_depth <= fifo_depth;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    fifo_e_depth <= fifo_depth[7:0] + 8'd1;
  end
  else if ((stage_0_error_out_rdy & stage_0_error_out_vld)) begin 
    fifo_e_depth <= fifo_depth[7:0] - 8'd1;
  end
  else begin
    fifo_e_depth <= fifo_depth;
  end
end
always @(posedge clk) begin
  if (reset) begin
    fifo_depth <= 8'd0;
  end
  else begin
    fifo_depth <= fifo_e_depth;
  end
end
always @(posedge clk) begin
  if (reset) begin
    fifo_write_address <= 7'd0;
  end
  else begin
    if ((zerror_int_rdy & zerror_int_vld)) begin
      fifo_write_address <= fifo_write_address[6:0] + 7'd1;
    end
  end
end
assign fifo_read_address_e = fifo_read_address[6:0] - 7'd1;
always @(posedge clk) begin
  if (reset) begin
    fifo_read_address <= 7'd1;
  end
  else if ((stage_0_error_out_rdy & stage_0_error_out_vld)) begin 
    fifo_read_address <= fifo_read_address[6:0] + 7'd1;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_0_error_out_vld <= 'd0;
  end
  else begin
    stage_0_error_out_vld <= (fifo_e_depth > 'd0);
  end
end
always @(posedge clk) begin
  if (reset) begin
    zerror_int_rdy <= 'd0;
  end
  else begin
    zerror_int_rdy <= (fifo_e_depth < 'd127);
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v0 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd0)) begin
      internal.v0 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v1 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd1)) begin
      internal.v1 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v2 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd2)) begin
      internal.v2 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v3 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd3)) begin
      internal.v3 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v4 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd4)) begin
      internal.v4 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v5 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd5)) begin
      internal.v5 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v6 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd6)) begin
      internal.v6 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v7 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd7)) begin
      internal.v7 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v8 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd8)) begin
      internal.v8 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v9 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd9)) begin
      internal.v9 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v10 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd10)) begin
      internal.v10 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v11 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd11)) begin
      internal.v11 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v12 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd12)) begin
      internal.v12 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v13 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd13)) begin
      internal.v13 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v14 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd14)) begin
      internal.v14 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v15 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd15)) begin
      internal.v15 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v16 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd16)) begin
      internal.v16 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v17 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd17)) begin
      internal.v17 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v18 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd18)) begin
      internal.v18 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v19 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd19)) begin
      internal.v19 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v20 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd20)) begin
      internal.v20 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v21 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd21)) begin
      internal.v21 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v22 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd22)) begin
      internal.v22 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v23 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd23)) begin
      internal.v23 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v24 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd24)) begin
      internal.v24 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v25 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd25)) begin
      internal.v25 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v26 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd26)) begin
      internal.v26 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v27 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd27)) begin
      internal.v27 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v28 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd28)) begin
      internal.v28 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v29 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd29)) begin
      internal.v29 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v30 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd30)) begin
      internal.v30 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v31 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd31)) begin
      internal.v31 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v32 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd32)) begin
      internal.v32 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v33 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd33)) begin
      internal.v33 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v34 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd34)) begin
      internal.v34 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v35 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd35)) begin
      internal.v35 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v36 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd36)) begin
      internal.v36 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v37 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd37)) begin
      internal.v37 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v38 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd38)) begin
      internal.v38 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v39 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd39)) begin
      internal.v39 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v40 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd40)) begin
      internal.v40 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v41 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd41)) begin
      internal.v41 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v42 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd42)) begin
      internal.v42 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v43 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd43)) begin
      internal.v43 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v44 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd44)) begin
      internal.v44 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v45 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd45)) begin
      internal.v45 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v46 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd46)) begin
      internal.v46 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v47 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd47)) begin
      internal.v47 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v48 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd48)) begin
      internal.v48 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v49 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd49)) begin
      internal.v49 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v50 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd50)) begin
      internal.v50 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v51 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd51)) begin
      internal.v51 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v52 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd52)) begin
      internal.v52 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v53 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd53)) begin
      internal.v53 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v54 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd54)) begin
      internal.v54 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v55 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd55)) begin
      internal.v55 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v56 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd56)) begin
      internal.v56 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v57 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd57)) begin
      internal.v57 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v58 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd58)) begin
      internal.v58 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v59 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd59)) begin
      internal.v59 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v60 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd60)) begin
      internal.v60 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v61 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd61)) begin
      internal.v61 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v62 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd62)) begin
      internal.v62 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v63 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd63)) begin
      internal.v63 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v64 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd64)) begin
      internal.v64 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v65 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd65)) begin
      internal.v65 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v66 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd66)) begin
      internal.v66 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v67 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd67)) begin
      internal.v67 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v68 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd68)) begin
      internal.v68 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v69 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd69)) begin
      internal.v69 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v70 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd70)) begin
      internal.v70 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v71 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd71)) begin
      internal.v71 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v72 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd72)) begin
      internal.v72 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v73 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd73)) begin
      internal.v73 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v74 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd74)) begin
      internal.v74 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v75 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd75)) begin
      internal.v75 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v76 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd76)) begin
      internal.v76 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v77 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd77)) begin
      internal.v77 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v78 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd78)) begin
      internal.v78 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v79 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd79)) begin
      internal.v79 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v80 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd80)) begin
      internal.v80 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v81 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd81)) begin
      internal.v81 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v82 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd82)) begin
      internal.v82 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v83 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd83)) begin
      internal.v83 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v84 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd84)) begin
      internal.v84 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v85 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd85)) begin
      internal.v85 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v86 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd86)) begin
      internal.v86 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v87 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd87)) begin
      internal.v87 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v88 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd88)) begin
      internal.v88 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v89 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd89)) begin
      internal.v89 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v90 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd90)) begin
      internal.v90 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v91 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd91)) begin
      internal.v91 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v92 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd92)) begin
      internal.v92 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v93 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd93)) begin
      internal.v93 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v94 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd94)) begin
      internal.v94 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v95 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd95)) begin
      internal.v95 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v96 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd96)) begin
      internal.v96 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v97 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd97)) begin
      internal.v97 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v98 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd98)) begin
      internal.v98 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v99 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd99)) begin
      internal.v99 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v100 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd100)) begin
      internal.v100 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v101 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd101)) begin
      internal.v101 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v102 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd102)) begin
      internal.v102 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v103 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd103)) begin
      internal.v103 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v104 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd104)) begin
      internal.v104 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v105 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd105)) begin
      internal.v105 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v106 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd106)) begin
      internal.v106 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v107 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd107)) begin
      internal.v107 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v108 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd108)) begin
      internal.v108 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v109 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd109)) begin
      internal.v109 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v110 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd110)) begin
      internal.v110 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v111 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd111)) begin
      internal.v111 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v112 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd112)) begin
      internal.v112 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v113 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd113)) begin
      internal.v113 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v114 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd114)) begin
      internal.v114 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v115 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd115)) begin
      internal.v115 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v116 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd116)) begin
      internal.v116 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v117 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd117)) begin
      internal.v117 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v118 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd118)) begin
      internal.v118 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v119 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd119)) begin
      internal.v119 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v120 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd120)) begin
      internal.v120 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v121 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd121)) begin
      internal.v121 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v122 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd122)) begin
      internal.v122 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v123 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd123)) begin
      internal.v123 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v124 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd124)) begin
      internal.v124 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v125 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd125)) begin
      internal.v125 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v126 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd126)) begin
      internal.v126 <= zerror_int;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v127 <= 'd0;
  end
  else if ((zerror_int_rdy & zerror_int_vld)) begin 
    if ((fifo_write_address == 'd127)) begin
      internal.v127 <= zerror_int;
    end
  end
end
always @* begin
  case(fifo_read_address_e)
    'd0 : stage_0_error_out <= internal.v0;
    'd1 : stage_0_error_out <= internal.v1;
    'd2 : stage_0_error_out <= internal.v2;
    'd3 : stage_0_error_out <= internal.v3;
    'd4 : stage_0_error_out <= internal.v4;
    'd5 : stage_0_error_out <= internal.v5;
    'd6 : stage_0_error_out <= internal.v6;
    'd7 : stage_0_error_out <= internal.v7;
    'd8 : stage_0_error_out <= internal.v8;
    'd9 : stage_0_error_out <= internal.v9;
    'd10 : stage_0_error_out <= internal.v10;
    'd11 : stage_0_error_out <= internal.v11;
    'd12 : stage_0_error_out <= internal.v12;
    'd13 : stage_0_error_out <= internal.v13;
    'd14 : stage_0_error_out <= internal.v14;
    'd15 : stage_0_error_out <= internal.v15;
    'd16 : stage_0_error_out <= internal.v16;
    'd17 : stage_0_error_out <= internal.v17;
    'd18 : stage_0_error_out <= internal.v18;
    'd19 : stage_0_error_out <= internal.v19;
    'd20 : stage_0_error_out <= internal.v20;
    'd21 : stage_0_error_out <= internal.v21;
    'd22 : stage_0_error_out <= internal.v22;
    'd23 : stage_0_error_out <= internal.v23;
    'd24 : stage_0_error_out <= internal.v24;
    'd25 : stage_0_error_out <= internal.v25;
    'd26 : stage_0_error_out <= internal.v26;
    'd27 : stage_0_error_out <= internal.v27;
    'd28 : stage_0_error_out <= internal.v28;
    'd29 : stage_0_error_out <= internal.v29;
    'd30 : stage_0_error_out <= internal.v30;
    'd31 : stage_0_error_out <= internal.v31;
    'd32 : stage_0_error_out <= internal.v32;
    'd33 : stage_0_error_out <= internal.v33;
    'd34 : stage_0_error_out <= internal.v34;
    'd35 : stage_0_error_out <= internal.v35;
    'd36 : stage_0_error_out <= internal.v36;
    'd37 : stage_0_error_out <= internal.v37;
    'd38 : stage_0_error_out <= internal.v38;
    'd39 : stage_0_error_out <= internal.v39;
    'd40 : stage_0_error_out <= internal.v40;
    'd41 : stage_0_error_out <= internal.v41;
    'd42 : stage_0_error_out <= internal.v42;
    'd43 : stage_0_error_out <= internal.v43;
    'd44 : stage_0_error_out <= internal.v44;
    'd45 : stage_0_error_out <= internal.v45;
    'd46 : stage_0_error_out <= internal.v46;
    'd47 : stage_0_error_out <= internal.v47;
    'd48 : stage_0_error_out <= internal.v48;
    'd49 : stage_0_error_out <= internal.v49;
    'd50 : stage_0_error_out <= internal.v50;
    'd51 : stage_0_error_out <= internal.v51;
    'd52 : stage_0_error_out <= internal.v52;
    'd53 : stage_0_error_out <= internal.v53;
    'd54 : stage_0_error_out <= internal.v54;
    'd55 : stage_0_error_out <= internal.v55;
    'd56 : stage_0_error_out <= internal.v56;
    'd57 : stage_0_error_out <= internal.v57;
    'd58 : stage_0_error_out <= internal.v58;
    'd59 : stage_0_error_out <= internal.v59;
    'd60 : stage_0_error_out <= internal.v60;
    'd61 : stage_0_error_out <= internal.v61;
    'd62 : stage_0_error_out <= internal.v62;
    'd63 : stage_0_error_out <= internal.v63;
    'd64 : stage_0_error_out <= internal.v64;
    'd65 : stage_0_error_out <= internal.v65;
    'd66 : stage_0_error_out <= internal.v66;
    'd67 : stage_0_error_out <= internal.v67;
    'd68 : stage_0_error_out <= internal.v68;
    'd69 : stage_0_error_out <= internal.v69;
    'd70 : stage_0_error_out <= internal.v70;
    'd71 : stage_0_error_out <= internal.v71;
    'd72 : stage_0_error_out <= internal.v72;
    'd73 : stage_0_error_out <= internal.v73;
    'd74 : stage_0_error_out <= internal.v74;
    'd75 : stage_0_error_out <= internal.v75;
    'd76 : stage_0_error_out <= internal.v76;
    'd77 : stage_0_error_out <= internal.v77;
    'd78 : stage_0_error_out <= internal.v78;
    'd79 : stage_0_error_out <= internal.v79;
    'd80 : stage_0_error_out <= internal.v80;
    'd81 : stage_0_error_out <= internal.v81;
    'd82 : stage_0_error_out <= internal.v82;
    'd83 : stage_0_error_out <= internal.v83;
    'd84 : stage_0_error_out <= internal.v84;
    'd85 : stage_0_error_out <= internal.v85;
    'd86 : stage_0_error_out <= internal.v86;
    'd87 : stage_0_error_out <= internal.v87;
    'd88 : stage_0_error_out <= internal.v88;
    'd89 : stage_0_error_out <= internal.v89;
    'd90 : stage_0_error_out <= internal.v90;
    'd91 : stage_0_error_out <= internal.v91;
    'd92 : stage_0_error_out <= internal.v92;
    'd93 : stage_0_error_out <= internal.v93;
    'd94 : stage_0_error_out <= internal.v94;
    'd95 : stage_0_error_out <= internal.v95;
    'd96 : stage_0_error_out <= internal.v96;
    'd97 : stage_0_error_out <= internal.v97;
    'd98 : stage_0_error_out <= internal.v98;
    'd99 : stage_0_error_out <= internal.v99;
    'd100 : stage_0_error_out <= internal.v100;
    'd101 : stage_0_error_out <= internal.v101;
    'd102 : stage_0_error_out <= internal.v102;
    'd103 : stage_0_error_out <= internal.v103;
    'd104 : stage_0_error_out <= internal.v104;
    'd105 : stage_0_error_out <= internal.v105;
    'd106 : stage_0_error_out <= internal.v106;
    'd107 : stage_0_error_out <= internal.v107;
    'd108 : stage_0_error_out <= internal.v108;
    'd109 : stage_0_error_out <= internal.v109;
    'd110 : stage_0_error_out <= internal.v110;
    'd111 : stage_0_error_out <= internal.v111;
    'd112 : stage_0_error_out <= internal.v112;
    'd113 : stage_0_error_out <= internal.v113;
    'd114 : stage_0_error_out <= internal.v114;
    'd115 : stage_0_error_out <= internal.v115;
    'd116 : stage_0_error_out <= internal.v116;
    'd117 : stage_0_error_out <= internal.v117;
    'd118 : stage_0_error_out <= internal.v118;
    'd119 : stage_0_error_out <= internal.v119;
    'd120 : stage_0_error_out <= internal.v120;
    'd121 : stage_0_error_out <= internal.v121;
    'd122 : stage_0_error_out <= internal.v122;
    'd123 : stage_0_error_out <= internal.v123;
    'd124 : stage_0_error_out <= internal.v124;
    'd125 : stage_0_error_out <= internal.v125;
    'd126 : stage_0_error_out <= internal.v126;
    'd127 : stage_0_error_out <= internal.v127;
  endcase
end
endmodule

