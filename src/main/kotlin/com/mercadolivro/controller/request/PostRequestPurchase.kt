package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostRequestPurchase(

    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    val customerId: Long,

    @field:NotNull
    @JsonAlias("book_id")
    val bookIds: Set<Long>
) {
}
