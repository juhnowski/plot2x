package ru.juhnowski.calculator.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import ru.juhnowski.calculator.calculation.calculate
import ru.juhnowski.calculator.model.*
import ru.juhnowski.calculator.storage.StorageFileNotFoundException
import ru.juhnowski.calculator.storage.StorageService
import java.io.StringWriter
import java.util.concurrent.atomic.AtomicLong
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import java.awt.PageAttributes
import java.io.File
import java.io.IOException
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.Collectors.toList
import javax.xml.ws.Response
import kotlin.streams.toList


@RestController
class CalculatorController @Autowired
constructor(private val storageService: StorageService) {

    val counter = AtomicLong()

    @GetMapping("/alive")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")


    @GetMapping("/calc")
    fun calc(@RequestParam(value = "expr",defaultValue = "") expr: String): String {
        return calculate(expr)
    }


    @GetMapping("/{filename:.+}")
    @ResponseBody
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {

        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.filename + "\"").body(file)
    }


    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException): ResponseEntity<*> {
        return ResponseEntity.notFound().build<Any>()
    }

}

data class Greeting(val id: Long, val content: String)




