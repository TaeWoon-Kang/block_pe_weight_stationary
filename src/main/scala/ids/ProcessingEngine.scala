package ids

import chisel3._

class ProcessingEngine extends Module{
  val io = IO(new Bundle {
    val in_up_weight = Input(Vec(2, UInt(16.W))) //input weight
    val in_up = Input(UInt(32.W)) //input uppder mac result from up
    val in_left = Input(Vec(2, UInt(16.W))) //input from left
    val out_down_weight = Output(Vec(2,UInt(32.W))) //output wight down
    val out_right = Output(Vec(2,UInt(32.W))) //output right
    val out_down   = Output(UInt(32.W))
    val station = Input(Bool())
  })

  val weight1 = RegInit(UInt(16.W),0.U)
  val weight2 = RegInit(UInt(16.W),0.U)

  val input1 = WireInit(UInt(16.W),0.U)
  val input2 = WireInit(UInt(16.W),0.U)

  val result = WireInit(UInt(32.W),0.U)

  weight1 := Mux(io.station, io.in_up_weight(0), weight1)
  weight2 := Mux(io.station, io.in_up_weight(1), weight2)

  input1 := io.in_left(0)
  input2 := io.in_left(1)

  result := Mux(io.station, io.in_up + io.in_up_weight(0) * io.in_left(0) + io.in_up_weight(1) * io.in_left(1), io.in_up + weight1 * io.in_left(0) + weight2 * io.in_left(1))

  io.out_down_weight(0) := weight1
  io.out_down_weight(1) := weight2
  io.out_right(0) := input1
  io.out_right(1) := input2
  io.out_down := result
}
