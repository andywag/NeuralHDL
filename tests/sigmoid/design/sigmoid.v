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
  input float_24_8              data,
  input                         reset,
  output float_24_8             out);

// Parameters 



// Wires 

  float_24_8                    sigmoid_int;  // <1,0>


// Registers 



// Other




// Exponent LUT
always @* begin
  if ((data.exp > 'd129)) begin
    sigmoid_int.exp <= data.sgn ? 8'd0 : 8'd127;
  end
  else if ((data.exp == 'd129)) begin 
    if (data.sgn) begin
      if (~data.man[22]) begin
        sigmoid_int.exp <= 8'd121;
      end
      else if (~data.man[21]) begin 
        sigmoid_int.exp <= 8'd120;
      end
      else if (~data.man[20]) begin 
        sigmoid_int.exp <= 8'd119;
      end
      else if (~data.man[19]) begin 
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
  else if ((data.exp == 'd128)) begin 
    sigmoid_int.exp <= data.sgn ? data.man[22] ? 8'd122 : 8'd123 : 8'd126;
  end
  else if ((data.exp == 'd127)) begin 
    sigmoid_int.exp <= data.sgn ? 8'd124 : 8'd126;
  end
  else if ((data.exp < 'd127)) begin 
    sigmoid_int.exp <= data.sgn ? 8'd125 : 8'd126;
  end
end

// Mantissa LUT
always @* begin
  if ((data.exp > 'd129)) begin
    sigmoid_int.man <= 23'd0;
  end
  else if ((data.exp == 'd129)) begin 
    sigmoid_int.man <= data.sgn ? 23'd0 : {4'he,data.man[22:4]};
  end
  else if ((data.exp == 'd128)) begin 
    sigmoid_int.man <= data.sgn ? {~data.man[22:0],1'h0} : {3'h6,data.man[22:3]};
  end
  else if ((data.exp == 'd127)) begin 
    sigmoid_int.man <= data.sgn ? ~data.man[22:0] : {2'h2,data.man[22:2]};
  end
  else if ((data.exp == 'd126)) begin 
    sigmoid_int.man <= data.sgn ? {1'h0,~data.man[22:1]} : {2'h1,data.man[22:2]};
  end
  else if ((data.exp == 'd125)) begin 
    sigmoid_int.man <= data.sgn ? {2'h2,~data.man[22:2]} : {3'h1,data.man[22:3]};
  end
  else if ((data.exp == 'd124)) begin 
    sigmoid_int.man <= data.sgn ? {3'h6,~data.man[22:3]} : {4'h1,data.man[22:4]};
  end
  else begin
    sigmoid_int.man <= data.sgn ? {4'he,~data.man[22:4]} : {5'h1,data.man[22:5]};
  end
end
assign sigmoid_int.sgn = 'd0;
always @(posedge clk) begin
  if (reset) begin
    out <= 'd0;
  end
  else begin
    out <= sigmoid_int;
  end
end
endmodule

