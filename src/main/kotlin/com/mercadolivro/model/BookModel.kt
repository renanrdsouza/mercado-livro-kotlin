package com.mercadolivro.model

import com.mercadolivro.enums.BookStatus
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "book")
class BookModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val name: String,

    @Column
    val price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel? = null
) {

    @Column
    @Enumerated(EnumType.STRING) // fala para o spring que esse valor é um ENUM
    var status: BookStatus? = null
        set(value) {
            if (field == BookStatus.DELETADO || field == BookStatus.CANCELADO)
                throw Exception("Não é possível cancelar um livro com status $field")

            field = value
        }

    constructor(
        id: Long? = null,
        name: String,
        price: BigDecimal,
        customer: CustomerModel? = null,
        status: BookStatus?
    ): this(id, name, price, customer) {
        this.status = status
    }
}