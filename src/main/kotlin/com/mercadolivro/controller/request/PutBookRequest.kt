package com.mercadolivro.controller.request

import java.math.BigDecimal

data class PutBookRequest(

    val name: String?,
    val price: BigDecimal?
)
