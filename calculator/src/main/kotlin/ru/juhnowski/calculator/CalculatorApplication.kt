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
    internal fun init(storageService: StorageService): CommandLineRunner {
        return { args ->
            storageService.deleteAll()
            storageService.init()
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CalculatorApplication::class.java, *args)
        }
    }
}