package ru.juhnowski.calculator

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.juhnowski.calculator.calculation.Evaluate
import ru.juhnowski.calculator.calculation.Evaluate.MAX_X
import ru.juhnowski.calculator.calculation.Evaluate.MIN_X
import ru.juhnowski.calculator.calculation.stub
import ru.juhnowski.calculator.plotter.Formula
import ru.juhnowski.calculator.plotter.Plot
import java.io.File


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
		val plot = Plot.plot(null);
		plot.series(null, plot.data().xy(1.0, 2.0).xy(3.0, 4.0), null)// setting data
		plot.save("upload-dir/sample_minimal", "png")
		val tmp = File("upload-dir/sample_minimal.png")
		assert(tmp.exists())
	}


	@Test
	fun testFormula(){
		val formula = Formula.formula(null).series(null, Formula.data().xy(1.0, 2.0).xy(3.0, 4.0), null)// setting data
		formula.formulaText = "f ( x ) = x + 1"
		formula.save("upload-dir/formula_minimal", "png")
		val tmp = File("upload-dir/formula_minimal.png")
		assert(tmp.exists());
	}

	@Test
	fun fx_const(){
		val expr = "1"
		val data = Evaluate.getData(expr);
		assert(data[MIN_X]==1.0)
		assert(data[MAX_X]==1.0)
	}

	@Test
	fun fx_x() {
		val expr = "x+1"
		val data = Evaluate.getData(expr);
		assert(data[MIN_X]==1.0)
		assert(data[MAX_X]==10.0)
	}

	@Test
	fun fx_x2() {
		val expr = "x^2"
		val data = Evaluate.getData(expr);
		assert(data[MIN_X]==0.0)
		assert(data[MAX_X]==81.0)
	}

	@Test
	fun fx_sqrt() {
		val expr = "sqrt(x)"
		val data = Evaluate.getData(expr);
		assert(data[MIN_X]==0.0)
		assert(data[MAX_X]==3.0)
	}


}
