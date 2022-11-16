package com.mercadolivro.event.listener

import com.mercadolivro.TestUtils.TestUtils
import com.mercadolivro.event.PurchaseEvent
import com.mercadolivro.service.PurchaseService
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {

    private var purchaseService: PurchaseService = mockk()
    private var generateNfeListener = GenerateNfeListener(purchaseService)

    @Test
    fun `should generate nfe`() {
        val purchase = TestUtils.buildPurchase(nfe = null)
        val fakeNfe = UUID.randomUUID()
        val purchaseExpected = purchase.copy(nfe = fakeNfe.toString())
        mockkStatic(UUID::class)

        every { UUID.randomUUID() } returns fakeNfe
        every { purchaseService.update(purchaseExpected) } just Runs

        generateNfeListener.listen(PurchaseEvent(this, purchase))

        verify(exactly = 1) { purchaseService.update(purchaseExpected) }
    }
}