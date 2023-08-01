package com.svk.productbrowser.data.repository

import com.svk.productbrowser.data.local.ProductsDao
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.data.remote.models.Product
import com.svk.productbrowser.data.remote.models.ProductsResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException


internal class ProductsRepositoryTest {


    @RelaxedMockK
    lateinit var api: ProductsApi

    @RelaxedMockK
    lateinit var dao: ProductsDao

    lateinit var productsRepository: ProductsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        productsRepository = ProductsRepository(api, dao)
    }


    @Test
    fun `test fetchProducts not found case`()  {
        val expectedResponse = ProductsResponse(0, listOf(), 0, 0)
        coEvery { api.getProductsByQuery(any()) } returns expectedResponse

        runBlocking { productsRepository.fetchProducts("abc") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts found case`()  {
        val product1 = Product("", "", "",0.0,1,
        listOf(),2123,1.0,1,"","")
        val product2 = Product("", "", "",0.0,2,
            listOf(),2123,1.0,1,"","")
        val listProducts = listOf<Product>(product1, product2)

        val expectedResponse = ProductsResponse(0, listProducts, 0, 0)
        coEvery { api.getProductsByQuery(any()) } returns expectedResponse

        runBlocking { productsRepository.fetchProducts("car") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts exception case`()  {
        coEvery { api.getProductsByQuery(any()) } throws (UnknownHostException())

        runBlocking { productsRepository.fetchProducts("carx") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}