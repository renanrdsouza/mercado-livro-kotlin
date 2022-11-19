package com.mercadolivro.controller.response

data class PageResponse<T>(
    var itens: List<T>,
    var currentPage: Int,
    var totalItens: Long,
    var totalPages: Int
)