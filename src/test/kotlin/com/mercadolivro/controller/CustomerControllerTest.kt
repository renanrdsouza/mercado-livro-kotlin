package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.TestUtils.TestUtils
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserCustomDetails
import org.aspectj.lang.annotation.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username = "1", password = "pwd", roles = arrayOf("ADMIN"))
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customers`() {
        val customer1 = customerRepository.save(TestUtils.buildCustomer())
        val customer2 = customerRepository.save(TestUtils.buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].status").value(customer1.status))
            .andExpect(jsonPath("$[1].id").value(customer2.id))
            .andExpect(jsonPath("$[1].email").value(customer2.email))
            .andExpect(jsonPath("$[1].name").value(customer2.name))
            .andExpect(jsonPath("$[1].status").value(customer2.status))
    }

    @Test
    fun `should filter all customers by name when get all`() {
        val customer1 = customerRepository.save(TestUtils.buildCustomer(name = "Gustavo"))
        customerRepository.save(TestUtils.buildCustomer(name = "Daniel"))

        mockMvc.perform(get("/customers?name=Gus"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].status").value(customer1.status))

    }

    @Test
    fun `should create a customer`() {
        val request = PostCustomerRequest(name = "FakeName", email = "${Random.nextInt()}@fakeemail.com", password = "123456")

        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)

        val customers = customerRepository.findAll().toList()

        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.name, customers[0].email)
    }

    @Test
    fun `should get user by id when user has the same id`() {
        val customer = customerRepository.save(TestUtils.buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}").with(user(UserCustomDetails(customer))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.status").value(customer.status))
    }

    @Test
    fun `should return forbidden user by id when user has the same id`() {
        val customer = customerRepository.save(TestUtils.buildCustomer())

        mockMvc.perform(get("/customers/0").with(user(UserCustomDetails(customer))))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.httpCode").value(403))
            .andExpect(jsonPath("$.message").value("Access denied"))
            .andExpect(jsonPath("$.internalCode").value("ML-000"))
    }
}