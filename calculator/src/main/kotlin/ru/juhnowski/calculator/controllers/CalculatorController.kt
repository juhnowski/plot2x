package ru.juhnowski.calculator.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.juhnowski.calculator.calculation.Calculate
import ru.juhnowski.calculator.storage.StorageFileNotFoundException
import ru.juhnowski.calculator.storage.StorageService
import java.util.concurrent.atomic.AtomicLong



@RestController
class CalculatorController @Autowired
constructor(private val storageService: StorageService) {

    val counter = AtomicLong()

    @GetMapping("/alive")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")


    @GetMapping("/calc")
    fun calc(@RequestParam(value = "expr",defaultValue = "") expr: String): String {
        val  calc = Calculate();
        return calc.calculate(expr);
    }


    @GetMapping("/{filename:.+}")
    @ResponseBody
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {

        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body(file)
    }

    @GetMapping("/")
    fun calc(): String {
        return """
            <!DOCTYPE HTML>
            <html>
             <head>
              <meta charset="utf-8">
              <title>Calculate</title>
             </head>
             <body>
            
             <form action="/calc_test">
               <p><b>f(x)=</b>
               <input type="text" size="40" name="expr">
              </p>
              <p><input type="submit"></p>
             </form>
            
             </body>
            </html>
        """.trimIndent()
    }

    @GetMapping("/calc_test")
    fun calc_test(@RequestParam(value = "expr",defaultValue = "") expr: String): String {
        val  calc = Calculate();
        calc.calculate(expr)
        var formulaUrl: String;
        var plotUrl:String;

        if (expr=="") {
            formulaUrl = "/formula_minimal.png";
            plotUrl = "/sample_minimal.png";
        } else {
            val hash = expr.hashCode();
            formulaUrl = "/formula_${hash}.png";
            plotUrl = "/plot_${hash}.png";
        }

        val pod1Title = calc.query.pod!![0].title;
        val pod2Title = calc.query.pod!![1].title;

        return """
            <!DOCTYPE HTML>
            <html>
             <head>
              <meta charset="utf-8">
              <title>Calculate</title>
             </head>
             <body>
                <table>
                    <tr>
                        <td>
                            <h1>$pod1Title</h1>
                            <img src='${formulaUrl}'>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h1>$pod2Title</h1>
                            <img src='${plotUrl}'>
                        </td> 
                    </tr>
                </table>
             <form action="/">
              <p><input type="submit" value="Ok"></p>
             </form>
            
             </body>
            </html>
        """.trimIndent()
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}

data class Greeting(val id: Long, val content: String)




