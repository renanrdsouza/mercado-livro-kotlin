package com.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest(

    @field:NotEmpty(message = "Name must be filled.")
    var name: String,

    @field:Email(message = "Should be a valid email.")
    var email: String
)