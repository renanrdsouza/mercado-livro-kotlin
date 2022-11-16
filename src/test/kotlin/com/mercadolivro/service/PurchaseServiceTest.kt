package com.mercadolivro.service

import com.mercadolivro.TestUtils.TestUtils
import com.mercadolivro.event.PurchaseEvent
import com.mercadolivro.repository.PurchaseRepository
import io.mockk.*

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    private val purchaseRepository: PurchaseRepository = mockk()
    private val applicationEventPublisher: ApplicationEventPublisher = mockk()
    private var purchaseService = PurchaseService(
        purchaseRepository,
        applicationEventPublisher
    )

    private val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should create purchase and publish event`() {
        val purchase = TestUtils.buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase
        every { applicationEventPublisher.publishEvent(any()) } just Runs

        purchaseService.create(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(purchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`() {
        val purchase = TestUtils.buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        purchaseService.update(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase) }
    }
}