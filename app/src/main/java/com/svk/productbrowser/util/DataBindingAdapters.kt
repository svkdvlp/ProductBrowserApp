package com.svk.productbrowser.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.svk.productbrowser.domain.ProductDetailState
import com.svk.productbrowser.domain.ProductListState
import kotlinx.coroutines.flow.StateFlow

/**
 * Data Binder for load image from network.
 * Used in ProductsDetail fragment layout file
 */
@BindingAdapter("android:src")
fun ImageView.setImageUri(imageUri: String?) {
    if (imageUri == null) {
        this.setImageURI(null)
    } else {
        this.load(imageUri)
    }
}

/**
 * Data Binder to show productTitle
 * Used in ProductsDetail fragment layout file
 */
@BindingAdapter("productTitle")
fun TextView.productTitle(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = responseState.product.title
}

/**
 * Data Binder to show productDescription
 * Used in ProductsDetail fragment layout file
 */
@BindingAdapter("productDesc")
fun TextView.productDesc(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = responseState.product.description
}

/**
 * Data Binder to show product price
 * Used in ProductsDetail fragment layout file
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("productPrice")
fun TextView.productPrice(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Error)
        text = responseState.error
    else if(responseState is ProductDetailState.Success)
        text = "Price: ${responseState.product.price}"
}

/**
 * Data Binder to show product image
 * Used in ProductsDetail fragment layout file
 */
@BindingAdapter("productImageUrl")
fun ImageView.productImageUrl(detail:StateFlow<ProductDetailState>) {
    val responseState = detail.value
    if(responseState is ProductDetailState.Success)
        this.load(responseState.product.thumbnail)
    else
        this.setImageURI(null)
}

/**
 * Data Binder to show search loading indicator
 * Used in ProductsList fragment layout file
 */
@BindingAdapter("searchIndicator")
fun ProgressBar.searchIndicator(isSearching:StateFlow<Boolean>) {
    val responseState = isSearching.value
    visibility= if(responseState)
        View.VISIBLE
    else
        View.GONE
}

/**
 * Data Binder to show error message
 * Used in ProductsList fragment layout file
 */
@BindingAdapter("showError")
fun TextView.showError(result:StateFlow<ProductListState>) {
    val responseState = result.value
    text = if(responseState is ProductListState.Error)
        responseState.error
    else
        ""
    visibility= if(responseState is ProductListState.Error)
        View.VISIBLE
    else
        View.GONE
}
