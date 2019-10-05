package ru.juhnowski.calculator

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class CalculatorController {

    val counter = AtomicLong()

    @GetMapping("/alive")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")


    @GetMapping("/calc")
    fun calc(@RequestParam(value = "expr",defaultValue = "null") expr: String): Result {
        //TODO: добавить таймаут

        val result = calculate(expr);
        return Result(expr, result)
    }

}

data class Greeting(val id: Long, val content: String)


data class Result(val expr:String, val result:String)

fun calculate(expr:String):String{
    val result = eval
    return result;
}

