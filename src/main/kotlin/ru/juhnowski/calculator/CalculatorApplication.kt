package ru.juhnowski.calculator


import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

import ru.juhnowski.calculator.storage.StorageProperties
import ru.juhnowski.calculator.storage.StorageService

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class CalculatorApplication {

    @Bean
    internal fun init(storageService: StorageService) = CommandLineRunner {
            storageService.deleteAll()
            storageService.init()

    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CalculatorApplication::class.java, *args)
        }
    }
}