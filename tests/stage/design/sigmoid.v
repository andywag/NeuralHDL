//-----------------------------------------------------------------------------
// Company: 			                                                              
// Author:				Andy                                                          
// Date:                                                                       
// Module Name:       sigmoid
// Description:                                                                
//                                                                             
//-----------------------------------------------------------------------------

`ifndef TYPES
  `include "types.v"
  `define TYPES
`endif

    module sigmoid(
  input                         clk,
  input                         reset,
  input float_24_8              stage_st_data_out_pre,
  output float_24_8             stage_st_data_out);

// Parameters 



// Wires 

  float_24_8                    sigmoid_int;  // <1,0>


// Registers 



// Other




// Exponent LUT
always @* begin
  if ((stage_st_data_out_pre.exp > 'd129)) begin
    sigmoid_int.exp <= stage_st_data_out_pre.sgn ? 8'd0 : 8'd127;
  end
  else if ((stage_st_data_out_pre.exp == 'd129)) begin 
    if (stage_st_data_out_pre.sgn) begin
      if (~stage_st_data_out_pre.man[22]) begin
        sigmoid_int.exp <= 8'd121;
      end
      else if (~stage_st_data_out_pre.man[21]) begin 
        sigmoid_int.exp <= 8'd120;
      end
      else if (~stage_st_data_out_pre.man[20]) begin 
        sigmoid_int.exp <= 8'd119;
      end
      else if (~stage_st_data_out_pre.man[19]) begin 
        sigmoid_int.exp <= 8'd118;
      end
      else begin
        sigmoid_int.exp <= 8'd0;
      end
    end
    else begin
      sigmoid_int.exp <= 8'd126;
    end
  end
  else if ((stage_st_data_out_pre.exp == 'd128)) begin 
    sigmoid_int.exp <= stage_st_data_out_pre.sgn ? stage_st_data_out_pre.man[22] ? 8'd122 : 8'd123 : 8'd126;
  end
  else if ((stage_st_data_out_pre.exp == 'd127)) begin 
    sigmoid_int.exp <= stage_st_data_out_pre.sgn ? 8'd124 : 8'd126;
  end
  else if ((stage_st_data_out_pre.exp < 'd127)) begin 
    sigmoid_int.exp <= stage_st_data_out_pre.sgn ? 8'd125 : 8'd126;
  end
end

// Mantissa LUT
always @* begin
  if ((stage_st_data_out_pre.exp > 'd129)) begin
    sigmoid_int.man <= 23'd0;
  end
  else if ((stage_st_data_out_pre.exp == 'd129)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? 23'd0 : {4'he,stage_st_data_out_pre.man[22:4]};
  end
  else if ((stage_st_data_out_pre.exp == 'd128)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? {~stage_st_data_out_pre.man[22:0],1'h0} : {3'h6,stage_st_data_out_pre.man[22:3]};
  end
  else if ((stage_st_data_out_pre.exp == 'd127)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? ~stage_st_data_out_pre.man[22:0] : {2'h2,stage_st_data_out_pre.man[22:2]};
  end
  else if ((stage_st_data_out_pre.exp == 'd126)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? {1'h0,~stage_st_data_out_pre.man[22:1]} : {2'h1,stage_st_data_out_pre.man[22:2]};
  end
  else if ((stage_st_data_out_pre.exp == 'd125)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? {2'h2,~stage_st_data_out_pre.man[22:2]} : {3'h1,stage_st_data_out_pre.man[22:3]};
  end
  else if ((stage_st_data_out_pre.exp == 'd124)) begin 
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? {3'h6,~stage_st_data_out_pre.man[22:3]} : {4'h1,stage_st_data_out_pre.man[22:4]};
  end
  else begin
    sigmoid_int.man <= stage_st_data_out_pre.sgn ? {4'he,~stage_st_data_out_pre.man[22:4]} : {5'h1,stage_st_data_out_pre.man[22:5]};
  end
end
assign sigmoid_int.sgn = 'd0;
always @(posedge clk) begin
  if (reset) begin
    stage_st_data_out <= 'd0;
  end
  else begin
    stage_st_data_out <= sigmoid_int;
  end
end
endmodule

