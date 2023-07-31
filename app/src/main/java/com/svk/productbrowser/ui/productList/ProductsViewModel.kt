package com.svk.productbrowser.ui.productList

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svk.productbrowser.data.repository.ProductsRepository
import com.svk.productbrowser.domain.ProductListState
import com.svk.productbrowser.domain.ProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
): ViewModel() {

    companion object {
        val TAG: String = ProductsViewModel::class.java.simpleName
    }

    private val _searchText = MutableStateFlow("")
    private val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _productModels = MutableStateFlow<List<ProductModel>>(emptyList())
    @OptIn(FlowPreview::class)
    val productModels = searchText.debounce(1000L)
        .onEach { _isSearching.update {  true } }
        .map { query ->
            if(query.isBlank())
                ProductListState.Error("Browse Products")
            else
                withContext(Dispatchers.IO) {
                    productsRepository.fetchProducts(query)
                }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _productModels.value
        )

    fun onTextChange(query: String) {
        _searchText.value = URLEncoder.encode(query, "UTF-8")
    }
}