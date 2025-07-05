package com.hobom.hobominternal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class HoBomInternalApplication

fun main(args: Array<String>) {
    runApplication<HoBomInternalApplication>(*args)
}
