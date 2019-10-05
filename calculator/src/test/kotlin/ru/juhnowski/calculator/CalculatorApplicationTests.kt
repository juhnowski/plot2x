package ru.juhnowski.calculator

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.juhnowski.calculator.calculation.stub
import ru.juhnowski.calculator.plotter.Plot




@SpringBootTest
class CalculatorApplicationTests {

	@Test
	fun contextLoads() {
	}


	@Test
	fun xml() {
		val res = stub()
		assert(res.toString().length >0);

	}

	@Test
	fun testPlot(){
		val plot = Plot.plot(null).series(null, Plot.data().xy(1.0, 2.0).xy(3.0, 4.0), null)// setting data

		plot.save("upload-dir/sample_minimal", "png")
	}



}
