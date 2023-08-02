package ids
import chisel3._

class BlockPE extends Module{
  val bio = IO(new Bundle() {
    val in_up_weight = Input(Vec(4, UInt(16.W))) //input weight
    val in_up = Input(Vec(2, UInt(16.W))) //input from up (mac result)
    val in_left = Input(Vec(4, UInt(16.W))) //input from left
    val out_down_weight = Output(Vec(4,UInt(32.W))) //output wight down one
    val out_right = Output(Vec(4,UInt(32.W))) //output right
    val out_down = Output(Vec(2,UInt(32.W))) //output down
    val station = Input(Bool())
  })
  val pe = Array.fill(2,2)(Module(new ProcessingEngine()))

  val out_right_reg = RegInit(VecInit(Seq.fill(4)(0.U(16.W))))
  val out_down_reg = RegInit(VecInit(Seq.fill(2)(0.U(32.W))))

  for (row <- 0 until 2) {
    for (col <- 0 until 2) {
      pe(row)(col).io.station := bio.station
    }
  }

  pe(0)(0).io.in_up_weight(0) := bio.in_up_weight(0)
  pe(0)(0).io.in_up_weight(1) := bio.in_up_weight(1)
  pe(0)(1).io.in_up_weight(0) := bio.in_up_weight(2)
  pe(0)(1).io.in_up_weight(1) := bio.in_up_weight(3)

  //weight passing for internal
  for (col <- 0 until 2) {
    pe(1)(col).io.in_up_weight(0) := pe(0)(col).io.out_down_weight(0)
    pe(1)(col).io.in_up_weight(1) := pe(0)(col).io.out_down_weight(1)
  }

  //input from left side
  pe(0)(0).io.in_left(0) := bio.in_left(0)
  pe(0)(0).io.in_left(1) := bio.in_left(1)
  pe(1)(0).io.in_left(0) := bio.in_left(2)
  pe(1)(0).io.in_left(1) := bio.in_left(3)


  //left to right for internal
  pe(0)(1).io.in_left(0) := pe(0)(0).io.out_right(0)
  pe(0)(1).io.in_left(1) := pe(0)(0).io.out_right(1)
  pe(1)(1).io.in_left(0) := pe(1)(0).io.out_right(0)
  pe(1)(1).io.in_left(1) := pe(1)(0).io.out_right(1)

  //result passing from up to down
  pe(1)(0).io.in_up := pe(0)(0).io.out_down
  pe(1)(1).io.in_up := pe(0)(1).io.out_down

  //result passing from up to down (frist line & last line) + registered for out_down
  pe(0)(0).io.in_up := bio.in_up(0)
  pe(0)(1).io.in_up := bio.in_up(1)
  out_down_reg(0) := pe(1)(0).io.out_down
  out_down_reg(1) := pe(1)(1).io.out_down

  bio.out_down(0) := out_down_reg(0)
  bio.out_down(1) := out_down_reg(1)


  //for weight passing to outside
  bio.out_down_weight(0) :=  pe(1)(0).io.out_down_weight(0)
  bio.out_down_weight(1) := pe(1)(0).io.out_down_weight(1)
  bio.out_down_weight(2) := pe(1)(1).io.out_down_weight(0)
  bio.out_down_weight(3) := pe(1)(1).io.out_down_weight(1)

  //for registered output(right)
  for (t <- 0 until 2) {
    out_right_reg(t) := pe(0)(1).io.out_right(t)
    out_right_reg(t + 2) := pe(1)(1).io.out_right(t)
  }

  //for BLOCK PE out_right
  for (t <- 0 until 4) {
    bio.out_right(t) := out_right_reg(t)
  }

}

