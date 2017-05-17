package com.simplifide.generate.test2.verilator

import java.io.PrintWriter

/**
  * Created by andy on 4/27/17.
  */
class TestCWrapper(val name:String) {



  val code = s"""
#include "V${name}.h"
#include "verilated.h"
#include <iostream>
#include "verilated_vcd_c.h"

V${name}* top = new V${name};
vluint64_t main_time = 0;       // Current simulation time

int main(int argc, char **argv, char **env) {
  Verilated::commandArgs(argc, argv);

  //long time = 0;

  Verilated::traceEverOn(true);
  VerilatedVcdC* tfp = new VerilatedVcdC;
  top->trace (tfp, 99);
  tfp->open ("simx.vcd");


  while (!Verilated::gotFinish()) {


    if (main_time % 10 == 1) {
      top->clk = 0;
      tfp->dump(main_time);
    }
    else if (main_time % 10 == 6) {
      top->clk = 1;
    }
    if (main_time > 100) {
      top->reset = 0;
    }
    else {
      top->reset = 1;
    }
     top->eval();
     main_time++;
  }
  tfp->close();
     delete top;
     exit(0);
  }
"""

  def writeCode(location:String) = {
    new PrintWriter(location) { write(code); close }
  }

}

object TestCWrapper {

}

