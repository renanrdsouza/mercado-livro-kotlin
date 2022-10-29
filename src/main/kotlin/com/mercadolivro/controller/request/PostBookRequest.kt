package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import javax.validation.constraints.Email

data class PostBookRequest(

    @field:Email(message = "Name must be filled.")
    var name: String,

    @field:Email(message = "Price must be filled.")
    var price: BigDecimal,

    @JsonAlias("customer_id") // como customer_id é diferente de customerId, essa anotação faz um parse para indicar que os dois são a mesma coisa para o spring
    var customerId: Long
) {

}
