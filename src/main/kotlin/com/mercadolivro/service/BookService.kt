package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun getAll(): List<BookModel> {
        return bookRepository.findAll()
    }

    fun findActives(): List<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO)
    }

    fun findBy(id: Long): BookModel {
        return bookRepository.findById(id).orElseThrow()
    }

    fun deleteBy(id: Long) {
        val book = findBy(id)

        book.status = BookStatus.DELETADO

        bookRepository.save(book)
    }
}
