package ids

import chisel3._

class SystolicArray extends Module {
  // N is row, M is column
  val io = IO(new Bundle() {
    val in_l = Input(Vec(8, UInt(16.W))) //make 1D input for left access
    val weight = Input(Vec(8, UInt(16.W))) //make 1D input for upper access
    val out = Output(Vec(4,UInt(32.W))) //output return for down side output(real output)
    val weight_select = Input (Bool())
  })

  val block_pe = Array.fill(2,2)(Module(new BlockPE()))

  //make weight input for weight stationary
  for (row <- 0 until 2) {
    for (col <- 0 until 2) {
      block_pe(row)(col).bio.station := io.weight_select
    }
  }

  //connection of weight
  block_pe(0)(0).bio.in_up_weight(0) := io.weight(0)
  block_pe(0)(0).bio.in_up_weight(1) := io.weight(1)
  block_pe(0)(0).bio.in_up_weight(2) := io.weight(2)
  block_pe(0)(0).bio.in_up_weight(3) := io.weight(3)

  block_pe(0)(1).bio.in_up_weight(0) := io.weight(4)
  block_pe(0)(1).bio.in_up_weight(1) := io.weight(5)
  block_pe(0)(1).bio.in_up_weight(2) := io.weight(6)
  block_pe(0)(1).bio.in_up_weight(3) := io.weight(7)
  //end

  //weight passing
  for(t<-0 until 4){
    block_pe(1)(0).bio.in_up_weight(t) := block_pe(0)(0).bio.out_down_weight(t)
    block_pe(1)(1).bio.in_up_weight(t) := block_pe(0)(1).bio.out_down_weight(t)
  }

  //input from left side
  block_pe(0)(0).bio.in_left(0) := io.in_l(0)
  block_pe(0)(0).bio.in_left(1) := io.in_l(1)
  block_pe(0)(0).bio.in_left(2) := io.in_l(2)
  block_pe(0)(0).bio.in_left(3) := io.in_l(3)

  block_pe(1)(0).bio.in_left(0) := io.in_l(4)
  block_pe(1)(0).bio.in_left(1) := io.in_l(5)
  block_pe(1)(0).bio.in_left(2) := io.in_l(6)
  block_pe(1)(0).bio.in_left(3) := io.in_l(7)
  //end

  //left to right
  for(t<-0 until 4){
    block_pe(0)(1).bio.in_left(t) := block_pe(0)(0).bio.out_right(t)
    block_pe(1)(1).bio.in_left(t) := block_pe(1)(0).bio.out_right(t)
  }

  //result passing from up to down (frist line & last line)
  io.out(0) := block_pe(1)(0).bio.out_down(0)
  io.out(1) := block_pe(1)(0).bio.out_down(1)
  io.out(2) := block_pe(1)(1).bio.out_down(0)
  io.out(3) := block_pe(1)(1).bio.out_down(1)

  //PE upperside initialize to zero
  block_pe(0)(0).bio.in_up(0) := 0.U
  block_pe(0)(0).bio.in_up(1) := 0.U

  block_pe(0)(1).bio.in_up(0) := 0.U
  block_pe(0)(1).bio.in_up(1) := 0.U

  //result passing from up to down
  for(t<-0 until 4){
    block_pe(1)(0).bio.in_up := block_pe(0)(0).bio.out_down
    block_pe(1)(1).bio.in_up := block_pe(0)(1).bio.out_down
  }

}