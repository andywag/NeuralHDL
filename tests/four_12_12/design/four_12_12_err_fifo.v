//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       four_12_12_err_fifo
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module four_12_12_err_fifo(
  input                         clk,
  input                         reset,
  input                         stage_3_error_rdy,
  input float_24_8              zctrl,
  input                         zctrl_fst,
  input                         zctrl_vld,
  output float_24_8             stage_3_error,
  output                        stage_3_error_fst,
  output                        stage_3_error_vld,
  output                        zctrl_rdy);

// Parameters 



// Wires 

  wire                  [6:0]   fifo_read_address_e  ;  // <7,0>
  zctrl_typ_128                 internal;  // <1,0>


// Registers 

  reg                   [7:0]   fifo_depth        ;  // <8,0>
  reg                   [7:0]   fifo_e_depth      ;  // <8,0>
  reg                   [6:0]   fifo_read_address  ;  // <7,0>
  reg                   [6:0]   fifo_write_address  ;  // <7,0>


// Other



always @* begin
  if (((zctrl_rdy & zctrl_vld) & (stage_3_error_rdy & stage_3_error_vld))) begin
    fifo_e_depth <= fifo_depth;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    fifo_e_depth <= fifo_depth[7:0] + 8'd1;
  end
  else if ((stage_3_error_rdy & stage_3_error_vld)) begin 
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
    if ((zctrl_rdy & zctrl_vld)) begin
      fifo_write_address <= fifo_write_address[6:0] + 7'd1;
    end
  end
end
assign fifo_read_address_e = fifo_read_address[6:0] - 7'd1;
always @(posedge clk) begin
  if (reset) begin
    fifo_read_address <= 7'd1;
  end
  else if ((stage_3_error_rdy & stage_3_error_vld)) begin 
    fifo_read_address <= fifo_read_address[6:0] + 7'd1;
  end
end
always @(posedge clk) begin
  if (reset) begin
    stage_3_error_vld <= 'd0;
  end
  else begin
    stage_3_error_vld <= (fifo_e_depth > 'd0);
  end
end
always @(posedge clk) begin
  if (reset) begin
    zctrl_rdy <= 'd0;
  end
  else begin
    zctrl_rdy <= (fifo_e_depth < 'd127);
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v0 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd0)) begin
      internal.v0 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v1 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd1)) begin
      internal.v1 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v2 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd2)) begin
      internal.v2 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v3 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd3)) begin
      internal.v3 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v4 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd4)) begin
      internal.v4 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v5 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd5)) begin
      internal.v5 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v6 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd6)) begin
      internal.v6 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v7 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd7)) begin
      internal.v7 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v8 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd8)) begin
      internal.v8 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v9 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd9)) begin
      internal.v9 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v10 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd10)) begin
      internal.v10 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v11 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd11)) begin
      internal.v11 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v12 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd12)) begin
      internal.v12 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v13 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd13)) begin
      internal.v13 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v14 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd14)) begin
      internal.v14 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v15 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd15)) begin
      internal.v15 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v16 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd16)) begin
      internal.v16 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v17 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd17)) begin
      internal.v17 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v18 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd18)) begin
      internal.v18 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v19 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd19)) begin
      internal.v19 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v20 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd20)) begin
      internal.v20 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v21 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd21)) begin
      internal.v21 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v22 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd22)) begin
      internal.v22 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v23 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd23)) begin
      internal.v23 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v24 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd24)) begin
      internal.v24 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v25 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd25)) begin
      internal.v25 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v26 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd26)) begin
      internal.v26 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v27 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd27)) begin
      internal.v27 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v28 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd28)) begin
      internal.v28 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v29 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd29)) begin
      internal.v29 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v30 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd30)) begin
      internal.v30 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v31 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd31)) begin
      internal.v31 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v32 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd32)) begin
      internal.v32 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v33 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd33)) begin
      internal.v33 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v34 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd34)) begin
      internal.v34 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v35 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd35)) begin
      internal.v35 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v36 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd36)) begin
      internal.v36 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v37 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd37)) begin
      internal.v37 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v38 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd38)) begin
      internal.v38 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v39 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd39)) begin
      internal.v39 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v40 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd40)) begin
      internal.v40 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v41 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd41)) begin
      internal.v41 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v42 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd42)) begin
      internal.v42 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v43 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd43)) begin
      internal.v43 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v44 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd44)) begin
      internal.v44 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v45 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd45)) begin
      internal.v45 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v46 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd46)) begin
      internal.v46 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v47 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd47)) begin
      internal.v47 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v48 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd48)) begin
      internal.v48 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v49 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd49)) begin
      internal.v49 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v50 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd50)) begin
      internal.v50 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v51 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd51)) begin
      internal.v51 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v52 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd52)) begin
      internal.v52 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v53 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd53)) begin
      internal.v53 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v54 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd54)) begin
      internal.v54 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v55 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd55)) begin
      internal.v55 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v56 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd56)) begin
      internal.v56 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v57 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd57)) begin
      internal.v57 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v58 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd58)) begin
      internal.v58 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v59 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd59)) begin
      internal.v59 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v60 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd60)) begin
      internal.v60 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v61 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd61)) begin
      internal.v61 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v62 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd62)) begin
      internal.v62 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v63 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd63)) begin
      internal.v63 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v64 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd64)) begin
      internal.v64 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v65 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd65)) begin
      internal.v65 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v66 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd66)) begin
      internal.v66 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v67 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd67)) begin
      internal.v67 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v68 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd68)) begin
      internal.v68 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v69 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd69)) begin
      internal.v69 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v70 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd70)) begin
      internal.v70 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v71 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd71)) begin
      internal.v71 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v72 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd72)) begin
      internal.v72 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v73 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd73)) begin
      internal.v73 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v74 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd74)) begin
      internal.v74 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v75 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd75)) begin
      internal.v75 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v76 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd76)) begin
      internal.v76 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v77 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd77)) begin
      internal.v77 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v78 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd78)) begin
      internal.v78 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v79 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd79)) begin
      internal.v79 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v80 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd80)) begin
      internal.v80 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v81 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd81)) begin
      internal.v81 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v82 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd82)) begin
      internal.v82 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v83 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd83)) begin
      internal.v83 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v84 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd84)) begin
      internal.v84 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v85 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd85)) begin
      internal.v85 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v86 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd86)) begin
      internal.v86 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v87 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd87)) begin
      internal.v87 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v88 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd88)) begin
      internal.v88 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v89 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd89)) begin
      internal.v89 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v90 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd90)) begin
      internal.v90 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v91 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd91)) begin
      internal.v91 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v92 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd92)) begin
      internal.v92 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v93 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd93)) begin
      internal.v93 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v94 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd94)) begin
      internal.v94 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v95 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd95)) begin
      internal.v95 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v96 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd96)) begin
      internal.v96 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v97 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd97)) begin
      internal.v97 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v98 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd98)) begin
      internal.v98 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v99 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd99)) begin
      internal.v99 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v100 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd100)) begin
      internal.v100 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v101 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd101)) begin
      internal.v101 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v102 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd102)) begin
      internal.v102 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v103 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd103)) begin
      internal.v103 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v104 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd104)) begin
      internal.v104 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v105 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd105)) begin
      internal.v105 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v106 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd106)) begin
      internal.v106 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v107 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd107)) begin
      internal.v107 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v108 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd108)) begin
      internal.v108 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v109 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd109)) begin
      internal.v109 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v110 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd110)) begin
      internal.v110 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v111 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd111)) begin
      internal.v111 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v112 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd112)) begin
      internal.v112 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v113 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd113)) begin
      internal.v113 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v114 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd114)) begin
      internal.v114 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v115 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd115)) begin
      internal.v115 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v116 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd116)) begin
      internal.v116 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v117 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd117)) begin
      internal.v117 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v118 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd118)) begin
      internal.v118 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v119 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd119)) begin
      internal.v119 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v120 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd120)) begin
      internal.v120 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v121 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd121)) begin
      internal.v121 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v122 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd122)) begin
      internal.v122 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v123 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd123)) begin
      internal.v123 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v124 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd124)) begin
      internal.v124 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v125 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd125)) begin
      internal.v125 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v126 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd126)) begin
      internal.v126 <= zctrl;
    end
  end
end
always @(posedge clk) begin
  if (reset) begin
    internal.v127 <= 'd0;
  end
  else if ((zctrl_rdy & zctrl_vld)) begin 
    if ((fifo_write_address == 'd127)) begin
      internal.v127 <= zctrl;
    end
  end
end
always @* begin
  case(fifo_read_address_e)
    'd0 : stage_3_error <= internal.v0;
    'd1 : stage_3_error <= internal.v1;
    'd2 : stage_3_error <= internal.v2;
    'd3 : stage_3_error <= internal.v3;
    'd4 : stage_3_error <= internal.v4;
    'd5 : stage_3_error <= internal.v5;
    'd6 : stage_3_error <= internal.v6;
    'd7 : stage_3_error <= internal.v7;
    'd8 : stage_3_error <= internal.v8;
    'd9 : stage_3_error <= internal.v9;
    'd10 : stage_3_error <= internal.v10;
    'd11 : stage_3_error <= internal.v11;
    'd12 : stage_3_error <= internal.v12;
    'd13 : stage_3_error <= internal.v13;
    'd14 : stage_3_error <= internal.v14;
    'd15 : stage_3_error <= internal.v15;
    'd16 : stage_3_error <= internal.v16;
    'd17 : stage_3_error <= internal.v17;
    'd18 : stage_3_error <= internal.v18;
    'd19 : stage_3_error <= internal.v19;
    'd20 : stage_3_error <= internal.v20;
    'd21 : stage_3_error <= internal.v21;
    'd22 : stage_3_error <= internal.v22;
    'd23 : stage_3_error <= internal.v23;
    'd24 : stage_3_error <= internal.v24;
    'd25 : stage_3_error <= internal.v25;
    'd26 : stage_3_error <= internal.v26;
    'd27 : stage_3_error <= internal.v27;
    'd28 : stage_3_error <= internal.v28;
    'd29 : stage_3_error <= internal.v29;
    'd30 : stage_3_error <= internal.v30;
    'd31 : stage_3_error <= internal.v31;
    'd32 : stage_3_error <= internal.v32;
    'd33 : stage_3_error <= internal.v33;
    'd34 : stage_3_error <= internal.v34;
    'd35 : stage_3_error <= internal.v35;
    'd36 : stage_3_error <= internal.v36;
    'd37 : stage_3_error <= internal.v37;
    'd38 : stage_3_error <= internal.v38;
    'd39 : stage_3_error <= internal.v39;
    'd40 : stage_3_error <= internal.v40;
    'd41 : stage_3_error <= internal.v41;
    'd42 : stage_3_error <= internal.v42;
    'd43 : stage_3_error <= internal.v43;
    'd44 : stage_3_error <= internal.v44;
    'd45 : stage_3_error <= internal.v45;
    'd46 : stage_3_error <= internal.v46;
    'd47 : stage_3_error <= internal.v47;
    'd48 : stage_3_error <= internal.v48;
    'd49 : stage_3_error <= internal.v49;
    'd50 : stage_3_error <= internal.v50;
    'd51 : stage_3_error <= internal.v51;
    'd52 : stage_3_error <= internal.v52;
    'd53 : stage_3_error <= internal.v53;
    'd54 : stage_3_error <= internal.v54;
    'd55 : stage_3_error <= internal.v55;
    'd56 : stage_3_error <= internal.v56;
    'd57 : stage_3_error <= internal.v57;
    'd58 : stage_3_error <= internal.v58;
    'd59 : stage_3_error <= internal.v59;
    'd60 : stage_3_error <= internal.v60;
    'd61 : stage_3_error <= internal.v61;
    'd62 : stage_3_error <= internal.v62;
    'd63 : stage_3_error <= internal.v63;
    'd64 : stage_3_error <= internal.v64;
    'd65 : stage_3_error <= internal.v65;
    'd66 : stage_3_error <= internal.v66;
    'd67 : stage_3_error <= internal.v67;
    'd68 : stage_3_error <= internal.v68;
    'd69 : stage_3_error <= internal.v69;
    'd70 : stage_3_error <= internal.v70;
    'd71 : stage_3_error <= internal.v71;
    'd72 : stage_3_error <= internal.v72;
    'd73 : stage_3_error <= internal.v73;
    'd74 : stage_3_error <= internal.v74;
    'd75 : stage_3_error <= internal.v75;
    'd76 : stage_3_error <= internal.v76;
    'd77 : stage_3_error <= internal.v77;
    'd78 : stage_3_error <= internal.v78;
    'd79 : stage_3_error <= internal.v79;
    'd80 : stage_3_error <= internal.v80;
    'd81 : stage_3_error <= internal.v81;
    'd82 : stage_3_error <= internal.v82;
    'd83 : stage_3_error <= internal.v83;
    'd84 : stage_3_error <= internal.v84;
    'd85 : stage_3_error <= internal.v85;
    'd86 : stage_3_error <= internal.v86;
    'd87 : stage_3_error <= internal.v87;
    'd88 : stage_3_error <= internal.v88;
    'd89 : stage_3_error <= internal.v89;
    'd90 : stage_3_error <= internal.v90;
    'd91 : stage_3_error <= internal.v91;
    'd92 : stage_3_error <= internal.v92;
    'd93 : stage_3_error <= internal.v93;
    'd94 : stage_3_error <= internal.v94;
    'd95 : stage_3_error <= internal.v95;
    'd96 : stage_3_error <= internal.v96;
    'd97 : stage_3_error <= internal.v97;
    'd98 : stage_3_error <= internal.v98;
    'd99 : stage_3_error <= internal.v99;
    'd100 : stage_3_error <= internal.v100;
    'd101 : stage_3_error <= internal.v101;
    'd102 : stage_3_error <= internal.v102;
    'd103 : stage_3_error <= internal.v103;
    'd104 : stage_3_error <= internal.v104;
    'd105 : stage_3_error <= internal.v105;
    'd106 : stage_3_error <= internal.v106;
    'd107 : stage_3_error <= internal.v107;
    'd108 : stage_3_error <= internal.v108;
    'd109 : stage_3_error <= internal.v109;
    'd110 : stage_3_error <= internal.v110;
    'd111 : stage_3_error <= internal.v111;
    'd112 : stage_3_error <= internal.v112;
    'd113 : stage_3_error <= internal.v113;
    'd114 : stage_3_error <= internal.v114;
    'd115 : stage_3_error <= internal.v115;
    'd116 : stage_3_error <= internal.v116;
    'd117 : stage_3_error <= internal.v117;
    'd118 : stage_3_error <= internal.v118;
    'd119 : stage_3_error <= internal.v119;
    'd120 : stage_3_error <= internal.v120;
    'd121 : stage_3_error <= internal.v121;
    'd122 : stage_3_error <= internal.v122;
    'd123 : stage_3_error <= internal.v123;
    'd124 : stage_3_error <= internal.v124;
    'd125 : stage_3_error <= internal.v125;
    'd126 : stage_3_error <= internal.v126;
    'd127 : stage_3_error <= internal.v127;
  endcase
end
endmodule

