package com.mercadolivro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController {

    @GetMapping
    fun helloWord(): String {
        return "Hello, world!"
    }

    @GetMapping("/2")
    fun customer2(): String {
        return "Customer 2"
    }
}