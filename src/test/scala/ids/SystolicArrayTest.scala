package ids

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SystolicArrayTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Systolic Array Test"
  it should "Systolic array MAC, using weight stationary" in {
    val N=2 //row
    val M=2 //col
    // to make {2 x 2} x {2 x 2} x 2
    test(new SystolicArray()).withAnnotations(Seq(WriteVcdAnnotation)) {
      p =>
        /*
        val in_l = Input(Vec(8, UInt(16.W))) //make 1D input for left access
        val weight = Input(Vec(8, UInt(16.W))) //make 1D input for upper access
        val out = Output(Vec(8,UInt(32.W))) //output return for down side output(real output)
        val weight_select = Input (Bool())
        */
        //make weight stationary
        //1st weight input
        p.io.weight_select.poke(true.B)

        for (i<-0 until 8){
          p.io.in_l(i).poke(0.U)
        }
        p.io.weight(0).poke(3.U)
        p.io.weight(1).poke(3.U)
        p.io.weight(2).poke(3.U)
        p.io.weight(3).poke(4.U)

        p.io.weight(4).poke(3.U)
        p.io.weight(5).poke(1.U)
        p.io.weight(6).poke(1.U)
        p.io.weight(7).poke(2.U)

        p.clock.step()
        //1st weight input end

        //2nd weight input
        p.io.weight(0).poke(3.U)
        p.io.weight(1).poke(2.U)
        p.io.weight(2).poke(3.U)
        p.io.weight(3).poke(1.U)

        p.io.weight(4).poke(2.U)
        p.io.weight(5).poke(3.U)
        p.io.weight(6).poke(4.U)
        p.io.weight(7).poke(1.U)

        p.clock.step()
        //2nd weight input end

        //3rd weight input
        p.io.weight(0).poke(1.U)
        p.io.weight(1).poke(2.U)
        p.io.weight(2).poke(4.U)
        p.io.weight(3).poke(2.U)

        p.io.weight(4).poke(4.U)
        p.io.weight(5).poke(2.U)
        p.io.weight(6).poke(2.U)
        p.io.weight(7).poke(3.U)

        p.clock.step()
        //3rd weight input end

        //4th weight input
        p.io.weight(0).poke(2.U)
        p.io.weight(1).poke(1.U)
        p.io.weight(2).poke(4.U)
        p.io.weight(3).poke(4.U)

        p.io.weight(4).poke(4.U)
        p.io.weight(5).poke(2.U)
        p.io.weight(6).poke(3.U)
        p.io.weight(7).poke(4.U)

        //make for concurrent input(input 1st)
        p.io.in_l(0).poke(1.U) //concurrently input from left side
        p.io.in_l(1).poke(4.U) //concurrently input from left side
        p.io.in_l(2).poke(3.U) //concurrently input from left side
        p.io.in_l(3).poke(3.U) //concurrently input from left side)
        //input 1st end

        p.clock.step()

        p.io.weight_select.poke(false.B)

        //4th weight input end

        //input 2nd
        p.io.in_l(0).poke(1.U)
        p.io.in_l(1).poke(2.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(3.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(2.U)
        p.io.in_l(6).poke(2.U)
        p.io.in_l(7).poke(2.U)

        p.clock.step()
        //input 2nd end

        //input 3rd
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(1.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 3rd end

        //input 4th
        p.io.in_l(0).poke(4.U)
        p.io.in_l(1).poke(1.U)
        p.io.in_l(2).poke(4.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(4.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 4th end

        //input 5th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(1.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(2.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(1.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 5th end

        //input 6th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(1.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 6th end

        //input 7th
        p.io.in_l(0).poke(1.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(1.U)
        p.io.in_l(3).poke(4.U)

        p.io.in_l(4).poke(2.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(4.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 7th end

        //input 8th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(4.U)
        p.io.in_l(3).poke(3.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(1.U)
        p.io.in_l(6).poke(3.U)
        p.io.in_l(7).poke(4.U)

        p.clock.step()
        //input 8th end

        //input 9th
        p.io.in_l(0).poke(0.U) //input initialize for ready
        p.io.in_l(1).poke(0.U) //input initialize for ready
        p.io.in_l(2).poke(0.U) //input initialize for ready
        p.io.in_l(3).poke(0.U) //input initialize for ready

        p.io.in_l(4).poke(3.U)
        p.io.in_l(5).poke(2.U)
        p.io.in_l(6).poke(4.U)
        p.io.in_l(7).poke(4.U)

        p.clock.step()
        //input 9th end

        //weight change and input initalize for ready
        p.io.in_l(4).poke(0.U) //input initialize for ready
        p.io.in_l(5).poke(0.U) //input initialize for ready
        p.io.in_l(6).poke(0.U) //input initialize for ready
        p.io.in_l(7).poke(0.U) //input initialize for ready

        p.clock.step(cycles = 2)
        //weight change and input initalize for ready end

        //new weight input
        //1st weight input
        p.io.weight_select.poke(true.B)
        p.io.weight(0).poke(4.U)
        p.io.weight(1).poke(3.U)
        p.io.weight(2).poke(2.U)
        p.io.weight(3).poke(3.U)

        p.io.weight(4).poke(3.U)
        p.io.weight(5).poke(4.U)
        p.io.weight(6).poke(3.U)
        p.io.weight(7).poke(2.U)

        p.clock.step()
        //1st weight input end

        //2nd weight input
        p.io.weight(0).poke(2.U)
        p.io.weight(1).poke(1.U)
        p.io.weight(2).poke(1.U)
        p.io.weight(3).poke(2.U)

        p.io.weight(4).poke(1.U)
        p.io.weight(5).poke(4.U)
        p.io.weight(6).poke(1.U)
        p.io.weight(7).poke(2.U)

        p.clock.step()
        //2nd weight input end

        //3rd weight input
        p.io.weight(0).poke(2.U)
        p.io.weight(1).poke(4.U)
        p.io.weight(2).poke(2.U)
        p.io.weight(3).poke(2.U)

        p.io.weight(4).poke(4.U)
        p.io.weight(5).poke(2.U)
        p.io.weight(6).poke(4.U)
        p.io.weight(7).poke(3.U)

        p.clock.step()
        //3rd weight input end

        //4th weight input
        p.io.weight(0).poke(3.U)
        p.io.weight(1).poke(4.U)
        p.io.weight(2).poke(4.U)
        p.io.weight(3).poke(2.U)

        p.io.weight(4).poke(4.U)
        p.io.weight(5).poke(1.U)
        p.io.weight(6).poke(2.U)
        p.io.weight(7).poke(3.U)

        //make for concurrent input(input 1st)
        p.io.in_l(0).poke(1.U) //concurrently input from left side
        p.io.in_l(1).poke(4.U) //concurrently input from left side
        p.io.in_l(2).poke(3.U) //concurrently input from left side
        p.io.in_l(3).poke(3.U) //concurrently input from left side)
        //input 1st end

        p.clock.step()

        p.io.weight_select.poke(false.B)
        //4th weight input end

        //input 2nd
        p.io.in_l(0).poke(1.U)
        p.io.in_l(1).poke(2.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(3.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(2.U)
        p.io.in_l(6).poke(2.U)
        p.io.in_l(7).poke(2.U)

        p.clock.step()
        //input 2nd end

        //input 3rd
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(1.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 3rd end

        //input 4th
        p.io.in_l(0).poke(4.U)
        p.io.in_l(1).poke(1.U)
        p.io.in_l(2).poke(4.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(4.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 4th end

        //input 5th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(1.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(2.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(1.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 5th end

        //input 6th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(2.U)
        p.io.in_l(3).poke(1.U)

        p.io.in_l(4).poke(1.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(1.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 6th end

        //input 7th
        p.io.in_l(0).poke(1.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(1.U)
        p.io.in_l(3).poke(4.U)

        p.io.in_l(4).poke(2.U)
        p.io.in_l(5).poke(3.U)
        p.io.in_l(6).poke(4.U)
        p.io.in_l(7).poke(1.U)

        p.clock.step()
        //input 7th end

        //input 8th
        p.io.in_l(0).poke(3.U)
        p.io.in_l(1).poke(4.U)
        p.io.in_l(2).poke(4.U)
        p.io.in_l(3).poke(3.U)

        p.io.in_l(4).poke(4.U)
        p.io.in_l(5).poke(1.U)
        p.io.in_l(6).poke(3.U)
        p.io.in_l(7).poke(4.U)

        p.clock.step()
        //input 8th end

        //input 9th
        p.io.in_l(0).poke(0.U) //input initialize for result
        p.io.in_l(1).poke(0.U) //input initialize for result
        p.io.in_l(2).poke(0.U) //input initialize for result
        p.io.in_l(3).poke(0.U) //input initialize for result

        p.io.in_l(4).poke(3.U)
        p.io.in_l(5).poke(2.U)
        p.io.in_l(6).poke(4.U)
        p.io.in_l(7).poke(4.U)

        p.clock.step()
        //input 9th end

        //input initalize for result
        p.io.in_l(4).poke(0.U) //input initialize for result
        p.io.in_l(5).poke(0.U) //input initialize for result
        p.io.in_l(6).poke(0.U) //input initialize for result
        p.io.in_l(7).poke(0.U) //input initialize for result

        p.clock.step(cycles = 4)
      //input initalize for result

    }
  }
}
