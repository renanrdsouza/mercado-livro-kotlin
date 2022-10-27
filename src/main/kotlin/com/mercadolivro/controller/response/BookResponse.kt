package com.mercadolivro.controller.response

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.CustomerModel
import java.math.BigDecimal

data class BookResponse(
    val id: Long? = null,
    val name: String,
    val price: BigDecimal,
    val customer: CustomerModel? = null,
    val status: BookStatus? = null
)
