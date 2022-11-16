package com.mercadolivro.service

import com.mercadolivro.TestUtils.TestUtils
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.repository.CustomerRepository
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    private var customerRepository: CustomerRepository = mockk()
    private var bookService: BookService = mockk()
    private var bCrypt: BCryptPasswordEncoder = mockk()
    private var customerService = spyk(
        CustomerService(
            customerRepository,
            bookService,
            bCrypt
        )
    )

    @Test
    fun `should return all customers`() {
        val fakeCustomers = listOf(TestUtils.buildCustomer(), TestUtils.buildCustomer())

        every { customerRepository.findAll() } returns fakeCustomers

        val customers = customerService.getAll(null)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `should return customer when name is informed`() {
        val name = Math.random().toString()
        val fakeCustomer = listOf(
            TestUtils.buildCustomer()
        )

        every { customerRepository.findByNameContaining(name) } returns fakeCustomer

        val customers = customerService.getAll(name)

        assertEquals(fakeCustomer, customers)
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
        verify(exactly = 0) { customerRepository.findAll() }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = "password"
        val customer = TestUtils.buildCustomer(
            password = initialPassword
        )
        val fakePassword = Math.random().toString()
        val fakeCustomerEncryptedPassword = customer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncryptedPassword) } returns customer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(customer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncryptedPassword) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id: Long = Math.random().toLong()
        val fakeCustomer = TestUtils.buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findby(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw not found when find by id`() {
        val id = Math.random().toLong()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> {
            customerService.findby(id)
        }

        assertEquals("Customer $id not exists.", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Math.random().toLong()
        val fakeCustomer = TestUtils.buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should throw not found exception when update customer`() {
        val id = Math.random().toLong()
        val fakeCustomer = TestUtils.buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException> {
            customerService.update(fakeCustomer)
        }

        assertEquals("Customer $id not exists.", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should delete a customer`() {
        val id = Math.random().toLong()
        val fakeCustomer = TestUtils.buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findby(id) } returns fakeCustomer
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just Runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findby(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete a customer`() {
        val id = Math.random().toLong()

        every { customerService.findby(id) } throws NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)

        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer $id not exists.", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerService.findby(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = "test@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should return false when email unavailable`() {
        val email = "test@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
}