package com.svk.productbrowser.data.repository

import android.content.Context
import com.svk.productbrowser.data.local.ProductEntity
import com.svk.productbrowser.data.local.ProductsDao
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.data.remote.models.Product
import com.svk.productbrowser.data.remote.models.ProductsResponse
import com.svk.productbrowser.di.ResourcesProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import java.util.concurrent.Flow


internal class ProductsRepositoryTest {

    @RelaxedMockK
    lateinit var api: ProductsApi

    @RelaxedMockK
    lateinit var dao: ProductsDao

    @RelaxedMockK
    lateinit var context: Context

    lateinit var resourcesProvider: ResourcesProvider

    lateinit var productsRepository: ProductsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        resourcesProvider = ResourcesProvider(context)
        productsRepository = ProductsRepository(api, dao, resourcesProvider)
        print(" Test: $ProductsRepository.TAG")
    }


    @Test
    fun `test fetchProducts not found case`()  {
        val expectedResponse = ProductsResponse(0, listOf(), 0, 0)

        every { resourcesProvider.getString(any()) } returns ""

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

        every { resourcesProvider.getString(any()) } returns ""

        coEvery { api.getProductsByQuery(any()) } returns expectedResponse

        runBlocking { productsRepository.fetchProducts("car") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts exception case`()  {
        coEvery { api.getProductsByQuery(any()) } throws (UnknownHostException())

        every { resourcesProvider.getString(any()) } returns ""

        runBlocking { productsRepository.fetchProducts("carx") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts from local case success`()  {
        coEvery { api.getProductsByQuery(any()) } throws (UnknownHostException())

        every { resourcesProvider.getString(any()) } returns ""

        coEvery { dao.searchProducts(any()) } returns flow {
            val pe1 = ProductEntity(0, "","","",1, 0)
            val pe2 = ProductEntity(1, "","","",1, 0)
            val res = listOf(pe1, pe2)
            emit(res)
        }

        runBlocking { productsRepository.fetchProducts("carx") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts from local case no products`()  {
        coEvery { api.getProductsByQuery(any()) } throws (UnknownHostException())

        every { resourcesProvider.getString(any()) } returns ""

        coEvery { dao.searchProducts(any()) } returns flow {
            emit(listOf())
        }

        runBlocking { productsRepository.fetchProducts("carx") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProducts from local case exception`()  {
        coEvery { api.getProductsByQuery(any()) } throws (UnknownHostException())

        every { resourcesProvider.getString(any()) } returns ""

        coEvery { dao.searchProducts(any()) } throws (Exception())

        runBlocking { productsRepository.fetchProducts("carx") }

        coVerify  { api.getProductsByQuery(any()) }
    }

    @Test
    fun `test fetchProductDetail success`()  {

        coEvery { dao.getProduct(any())} returns
                ProductEntity(0, "","","",1, 0)

        runBlocking { productsRepository.getProductFromLocalDB(0) }

        coVerify  { dao.getProduct(any()) }
    }

    @Test
    fun `test fetchProductDetail not found`()  {

        coEvery { dao.getProduct(any())} returns null

        runBlocking { productsRepository.getProductFromLocalDB(0) }

        coVerify  { dao.getProduct(any()) }
    }

    @Test
    fun `test fetchProductDetail exception`()  {

        coEvery { dao.getProduct(any())} throws  (Exception())

        runBlocking { productsRepository.getProductFromLocalDB(0) }

        coVerify  { dao.getProduct(any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}