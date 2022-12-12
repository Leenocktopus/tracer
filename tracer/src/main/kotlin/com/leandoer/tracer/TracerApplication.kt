package com.leandoer.tracer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class TracerApplication

fun main(args: Array<String>) {
    runApplication<TracerApplication>(*args)
}