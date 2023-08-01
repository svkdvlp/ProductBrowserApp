package com.svk.productbrowser.data.repository

import android.util.Log
import com.svk.productbrowser.data.local.ProductsDao
import com.svk.productbrowser.data.mapper.toProductEntity
import com.svk.productbrowser.data.mapper.toProductModel
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.domain.ProductDetailState
import com.svk.productbrowser.domain.ProductListState

class ProductsRepository(
    private val api: ProductsApi,
    private val dao: ProductsDao,
) {
    companion object{
        const val TAG = "ProductsRepository"
    }

    suspend fun fetchProducts(query: String): ProductListState {
        return try{
            val productModels = api.getProductsByQuery(query)
                .products.map {
                    dao.insertProduct(it.toProductEntity())
                    it.toProductModel()
                }

            if(productModels.isNotEmpty()){
                ProductListState.Success(productModels)
            }else{
                ProductListState.Error("No data found")
            }
        } catch (e:Exception) {
            Log.w(TAG, "fetchPagedListData: ", e)
            searchFromLocalDB(query)
        }
    }

    private fun searchFromLocalDB(query: String): ProductListState {
        Log.w(TAG, "searchFromLocalDB")
        return try{
            val productModels = dao.searchProducts(query).map{ pe->
                pe.toProductModel()
            }
            if(productModels.isNotEmpty()){
                ProductListState.Success(productModels)
            }else{
                ProductListState.Error("No data found")
            }
        } catch (e:Exception) {
            Log.w(TAG, "searchFromLocalDB: ", e)
            ProductListState.Error(e.message?:"searchFromLocalDB: Error")
        }
    }

    fun getProductFromLocalDB(id: Int): ProductDetailState {
        Log.w(TAG, "searchFromLocalDB")
        return try{
            val product = dao.getProduct(id)
            if(product!=null){
                ProductDetailState.Success(product.toProductModel())
            }else
                ProductDetailState.Error("No data found")
        } catch (e:Exception) {
            Log.w(TAG, "searchFromLocalDB: ", e)
            ProductDetailState.Error(e.message?:"getProductFromLocalDB: Error")
        }
    }

}