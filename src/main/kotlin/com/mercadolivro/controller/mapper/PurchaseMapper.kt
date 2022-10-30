package com.mercadolivro.controller.mapper

import com.mercadolivro.controller.request.PostRequestPurchase
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PurchaseMapper(
   private val bookService: BookService,
   private val customerService: CustomerService
) {

    fun toModel(request: PostRequestPurchase): PurchaseModel {
        val customer = customerService.findby(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)

        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}