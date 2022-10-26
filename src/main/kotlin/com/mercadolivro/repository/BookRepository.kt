package com.mercadolivro.repository

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<BookModel, Long> {

    fun findByStatus(ativo: BookStatus): List<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}