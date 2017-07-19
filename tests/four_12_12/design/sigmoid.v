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
  input                         bypass,
  input                         clk,
  input float_24_8              data_in,
  input                         reset,
  output float_24_8             data_out);

// Parameters 



// Wires 

  float_24_8                    sigmoid_int;  // <1,0>


// Registers 



// Other




// Exponent LUT
always @* begin
  if ((data_in.exp > 'd129)) begin
    sigmoid_int.exp <= data_in.sgn ? 8'd0 : 8'd127;
  end
  else if ((data_in.exp == 'd129)) begin 
    if (data_in.sgn) begin
      if (~data_in.man[22]) begin
        sigmoid_int.exp <= 8'd121;
      end
      else if (~data_in.man[21]) begin 
        sigmoid_int.exp <= 8'd120;
      end
      else if (~data_in.man[20]) begin 
        sigmoid_int.exp <= 8'd119;
      end
      else if (~data_in.man[19]) begin 
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
  else if ((data_in.exp == 'd128)) begin 
    sigmoid_int.exp <= data_in.sgn ? data_in.man[22] ? 8'd122 : 8'd123 : 8'd126;
  end
  else if ((data_in.exp == 'd127)) begin 
    sigmoid_int.exp <= data_in.sgn ? 8'd124 : 8'd126;
  end
  else if ((data_in.exp < 'd127)) begin 
    sigmoid_int.exp <= data_in.sgn ? 8'd125 : 8'd126;
  end
end

// Mantissa LUT
always @* begin
  if ((data_in.exp > 'd129)) begin
    sigmoid_int.man <= 23'd0;
  end
  else if ((data_in.exp == 'd129)) begin 
    sigmoid_int.man <= data_in.sgn ? 23'd0 : {4'he,data_in.man[22:4]};
  end
  else if ((data_in.exp == 'd128)) begin 
    sigmoid_int.man <= data_in.sgn ? {~data_in.man[22:0],1'h0} : {3'h6,data_in.man[22:3]};
  end
  else if ((data_in.exp == 'd127)) begin 
    sigmoid_int.man <= data_in.sgn ? ~data_in.man[22:0] : {2'h2,data_in.man[22:2]};
  end
  else if ((data_in.exp == 'd126)) begin 
    sigmoid_int.man <= data_in.sgn ? {1'h0,~data_in.man[22:1]} : {2'h1,data_in.man[22:2]};
  end
  else if ((data_in.exp == 'd125)) begin 
    sigmoid_int.man <= data_in.sgn ? {2'h2,~data_in.man[22:2]} : {3'h1,data_in.man[22:3]};
  end
  else if ((data_in.exp == 'd124)) begin 
    sigmoid_int.man <= data_in.sgn ? {3'h6,~data_in.man[22:3]} : {4'h1,data_in.man[22:4]};
  end
  else begin
    sigmoid_int.man <= data_in.sgn ? {4'he,~data_in.man[22:4]} : {5'h1,data_in.man[22:5]};
  end
end
assign sigmoid_int.sgn = 'd0;
always @(posedge clk) begin
  if (reset) begin
    data_out <= 'd0;
  end
  else begin
    if (bypass) begin
      data_out <= data_in;
    end
    else begin
      data_out <= sigmoid_int;
    end
  end
end
endmodule

