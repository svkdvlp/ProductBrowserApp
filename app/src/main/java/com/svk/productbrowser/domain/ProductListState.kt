package com.svk.productbrowser.domain

/**
 * ProductListState : sealed class for handling product listing states
 */
sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<ProductModel>) : ProductListState()
    data class Error(val error:String) : ProductListState()
}


/**
 * ProductListState : sealed class for handling product detail fetch states
 */
sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: ProductModel) : ProductDetailState()
    data class Error(val error:String) : ProductDetailState()
}