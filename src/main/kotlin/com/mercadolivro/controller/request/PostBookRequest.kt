package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

data class PostBookRequest(

    var name: String,
    var price: BigDecimal,

    @JsonAlias("customer_id") // como customer_id � diferente de customerId, essa anota��o faz um parse para indicar que os dois s�o a mesma coisa para o spring
    var customerId: Long
) {

}
