package com.mercadolivro.TestUtils

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class TestUtils {

    companion object {
        fun buildCustomer(id: Long? = null,
                          name: String = "customer_name",
                          email: String = "email@email.com",
                          password: String = "password"
        ) : CustomerModel = CustomerModel(
            id,
            name,
            email,
            status = CustomerStatus.ATIVO,
            password,
            roles = setOf(Role.CUSTOMER)
        )

        fun buildPurchase(
            id: Long? = null,
            customerModel: CustomerModel = buildCustomer(),
            books: MutableList<BookModel> = mutableListOf<BookModel>(),
            nfe: String? = UUID.randomUUID().toString(),
            price: BigDecimal = BigDecimal.TEN
        ) = PurchaseModel(
            id = id,
            customer = customerModel,
            books = books,
            nfe = nfe,
            price = price
        )
    }

}