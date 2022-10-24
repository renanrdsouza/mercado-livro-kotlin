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

    @Column
    @Enumerated(EnumType.STRING) // fala para o spring que esse valor é um ENUM
    var status: BookStatus? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel? = null
)