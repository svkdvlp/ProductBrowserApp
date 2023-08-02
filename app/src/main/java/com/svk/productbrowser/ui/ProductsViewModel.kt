package com.svk.productbrowser.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svk.productbrowser.R
import com.svk.productbrowser.data.repository.ProductsRepository
import com.svk.productbrowser.di.ResourcesProvider
import com.svk.productbrowser.domain.ProductDetailState
import com.svk.productbrowser.domain.ProductListState
import com.svk.productbrowser.ui.productDetail.ProductDetailFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val resourcesProvider: ResourcesProvider,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    companion object {
        val TAG: String = ProductsViewModel::class.java.simpleName
    }

    private val _searchText = MutableStateFlow("")
    private val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val productDetail = _productDetail.asStateFlow()

    private var _productModels = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val productModels = getSearchedProducts()

    init {
        /**
         * This code is used for fetch product detail
         */
        val args = ProductDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)
        viewModelScope.launch {
            getProductDetail(args.paramId)
        }
    }

    /**
     * onTextChange: on text change of input product search input
     */
    fun onTextChange(query: String) {
        _searchText.value = URLEncoder.encode(query, "UTF-8")
    }

    /**
     * getProductDetail: getProductDetail by product id
     */
    private suspend fun getProductDetail(id: Int) = withContext(Dispatchers.IO) {
        try {
            val result = productsRepository.getProductFromLocalDB(id)
            _productDetail.update { result }
        }catch (e: Exception){
            Log.e(TAG, "getProductDetail: ", e )
        }
    }

    /**
     *  getSearchedProducts: this method will return a flow of search result by given input string.
     *  It will debounce the input string for 1 second and provide the result
     *  fetchProducts() : If network exists it will return result from network else from local db
     */
    @OptIn(FlowPreview::class)
    private fun getSearchedProducts(): StateFlow<ProductListState> {
        return try {
            searchText.debounce(1000L)
                .onEach { _isSearching.update { true } }
                .map { query ->
                    if (query.isBlank())
                        ProductListState.Error(
                            resourcesProvider
                                .getString(R.string.browse_products)
                        )
                    else
                        productsRepository.fetchProducts(query)
                }
                .onEach { _isSearching.update { false } }
                .flowOn(Dispatchers.IO)
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    _productModels.value
                )
        }catch (e:Exception){
            Log.e(TAG, "getSearchedProducts: ", e)
            _productModels
        }
    }
}