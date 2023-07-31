package com.svk.productbrowser.domain

sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<ProductModel>) : ProductListState()
    data class Error(val error:String) : ProductListState()
}