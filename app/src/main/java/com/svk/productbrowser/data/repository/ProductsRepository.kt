package com.svk.productbrowser.data.repository

import android.util.Log
import com.svk.productbrowser.R
import com.svk.productbrowser.data.local.ProductsDao
import com.svk.productbrowser.data.mapper.toProductEntity
import com.svk.productbrowser.data.mapper.toProductModel
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.di.ResourcesProvider
import com.svk.productbrowser.domain.ProductDetailState
import com.svk.productbrowser.domain.ProductListState
import com.svk.productbrowser.domain.ProductModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProductsRepository(
    private val api: ProductsApi,
    private val dao: ProductsDao,
    private val resourcesProvider: ResourcesProvider,
) {
    companion object{
        val TAG: String = ProductsRepository::class.java.simpleName
    }

    /**
     * fetchProducts : Fetch products from server or local DB by a input string
     * If network present it will search from network and save results in DB
     * If no network present it will search from local db
     */
    suspend fun fetchProducts(query: String): ProductListState {
        return try {
            val productModels = api.getProductsByQuery(query)
                .products.map {
                    dao.insertProduct(it.toProductEntity())
                    it.toProductModel()
                }

            if (productModels.isNotEmpty()) {
                ProductListState.Success(productModels)
            } else {
                ProductListState.Error(resourcesProvider
                    .getString(R.string.no_data_found))
            }
        } catch (e: Exception) {
            Log.w(TAG, "fetchPagedListData: ", e)
            searchFromLocalDB(query)
        }
    }

    /**
     * searchFromLocalDB : searches products from local db.
     * This method will be called if no network is there
     */
    private suspend fun searchFromLocalDB(query: String): ProductListState {
        Log.w(TAG, "searchFromLocalDB")
        return try{
            dao.searchProducts(query).map { productEntities->
                val resultProductModels = mutableListOf<ProductModel>()
                productEntities.forEach{
                    resultProductModels.add(it.toProductModel())
                }
                if(resultProductModels.isNotEmpty()){
                    ProductListState.Success(resultProductModels)
                }else{
                    ProductListState.Error(resourcesProvider
                        .getString(R.string.no_data_found))
                }
            }.first()
        } catch (e:Exception) {
            Log.w(TAG, "searchFromLocalDB: ", e)
            ProductListState.Error(e.message?:resourcesProvider
                .getString(R.string.local_search_gen_err))
        }
    }

    /**
     * getProductFromLocalDB : get a product detail by product id
     */
    fun getProductFromLocalDB(id: Int): ProductDetailState {
        Log.w(TAG, "searchFromLocalDB")
        return try{
            val product = dao.getProduct(id)
            if(product!=null){
                ProductDetailState.Success(product.toProductModel())
            }else
                ProductDetailState.Error(resourcesProvider
                    .getString(R.string.no_data_found))
        } catch (e:Exception) {
            Log.w(TAG, "searchFromLocalDB: ", e)
            ProductDetailState.Error(e.message?:resourcesProvider
                .getString(R.string.local_get_product_gen_err))
        }
    }

}