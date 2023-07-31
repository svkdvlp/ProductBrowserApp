package com.svk.productbrowser.data.repository

import android.util.Log
import com.svk.productbrowser.data.mapper.toProductModel
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.domain.ProductListState

class ProductsRepository(
    private val api: ProductsApi,
) {
    companion object{
        const val TAG = "ProductsRepository"
    }

    suspend fun fetchProducts(query: String): ProductListState {
        return try{
            val productModels = api.getProductsByQuery(query)
                .products.map {
                    it.toProductModel()
                }

            if(productModels.isNotEmpty()){
                ProductListState.Success(productModels)
            }else{
                ProductListState.Error("No data")
            }
        }catch (e:Exception) {
            Log.w(TAG, "fetchPagedListData: ", e)
            ProductListState.Error(e.message?:"Some error")
        }
    }

}