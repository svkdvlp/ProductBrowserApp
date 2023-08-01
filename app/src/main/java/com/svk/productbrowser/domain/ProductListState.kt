package com.svk.productbrowser.domain

sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<ProductModel>) : ProductListState()
    data class Error(val error:String) : ProductListState()
}

sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: ProductModel) : ProductDetailState()
    data class Error(val error:String) : ProductDetailState()
}