package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PostBookRequest(

    @field:NotEmpty(message = "Name must be filled.")
    var name: String,

    @field:NotNull(message = "Price must be filled.")
    var price: BigDecimal,

    @JsonAlias("customer_id") // como customer_id é diferente de customerId, essa anotação faz um parse para indicar que os dois são a mesma coisa para o spring
    var customerId: Long
) {

}
