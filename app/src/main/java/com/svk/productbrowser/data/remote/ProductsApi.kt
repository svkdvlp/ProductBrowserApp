package com.svk.productbrowser.data.remote

import com.svk.productbrowser.data.remote.models.ProductsResponse
import com.svk.productbrowser.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API interface for network calls
 */
interface ProductsApi {

    @GET(Constants.SEARCH_URL)
    suspend fun getProductsByQuery(
        @Query(Constants.PARAM_SEARCH_KEY) searchQuery: String): ProductsResponse
}